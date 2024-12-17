package com.gustas.videogamestore.service.game;

import com.gustas.videogamestore.constant.SortingConstant;
import com.gustas.videogamestore.dao.game.GameDao;
import com.gustas.videogamestore.dao.genre.GenreDao;
import com.gustas.videogamestore.dao.image.ImageDao;
import com.gustas.videogamestore.dao.platform.PlatformDao;
import com.gustas.videogamestore.dao.publisher.PublisherDao;
import com.gustas.videogamestore.dao.region.RegionDao;
import com.gustas.videogamestore.domain.Game;
import com.gustas.videogamestore.domain.GameSearchCriteria;
import com.gustas.videogamestore.domain.Genre;
import com.gustas.videogamestore.domain.Image;
import com.gustas.videogamestore.domain.Platform;
import com.gustas.videogamestore.domain.Publisher;
import com.gustas.videogamestore.domain.Rating;
import com.gustas.videogamestore.domain.Region;
import com.gustas.videogamestore.domain.SortOrder;
import com.gustas.videogamestore.dto.request.GameRequestDto;
import com.gustas.videogamestore.dto.response.GameResponseDto;
import com.gustas.videogamestore.dto.response.PaginatedResponseDto;
import com.gustas.videogamestore.mapper.GameMapper;
import com.gustas.videogamestore.service.image.ImageService;
import com.gustas.videogamestore.service.session.SessionService;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.UnaryOperator;

@AllArgsConstructor
@Service
public class GameServiceImpl implements GameService {

    private GameDao gameDao;
    private GenreDao genreDao;
    private PlatformDao platformDao;
    private RegionDao regionDao;
    private PublisherDao publisherDao;
    private SessionService sessionService;
    private ImageService imageService;
    private ImageDao imageDoa;
    private GameMapper gameMapper;

    @Override
    public PaginatedResponseDto getGames(GameSearchCriteria gameSearchCriteria) {
        Pageable pageable = createPageable(gameSearchCriteria);
        Specification<Game> specification = createGameSpecification(gameSearchCriteria);

        Page<Game> gamePage = gameDao.findAll(specification, pageable);

        List<GameResponseDto> dtoList = gameMapper.toDto(gamePage.getContent());

        return new PaginatedResponseDto(
                dtoList, gamePage.getNumber(), gamePage.getTotalPages(), gamePage.getTotalElements());
    }

    @Override
    @Transactional
    public void deleteGame(Long gameId) {
        gameDao.deleteGame(gameId);
    }

    @Override
    @Transactional
    public GameResponseDto getGame(Long gameId) {
        return gameMapper.toDto(gameDao.getGame(gameId));
    }

    @Override
    @Transactional
    public void updateGame(Long gameId, GameRequestDto gameRequestDto, MultipartFile file) throws IOException {
        Game game = gameDao.getGame(gameId);

        if (game == null) {
            throw new IllegalArgumentException("Game with id " + gameId + " not found");
        }

        Image image = game.getImage();
        image.setImageData(file.getBytes());

        imageDoa.save(image);
        gameDao.saveGame(updateInstance(gameRequestDto, game));
    }

    @Override
    @Transactional
    public GameResponseDto saveGame(GameRequestDto gameRequestDto, MultipartFile file) throws IOException {
        Game game = createGameEntity(gameRequestDto, file);

        return gameMapper.toDto(gameDao.saveGame(game));
    }

    private Game updateInstance(GameRequestDto gameRequestDto, Game game) {
        game.setName(gameRequestDto.getName());
        game.setStock(gameRequestDto.getStock());
        game.setPrice(gameRequestDto.getPrice());
        game.setDescription(gameRequestDto.getDescription());
        game.setReleaseYear(gameRequestDto.getReleaseYear());
        game.setGenre(getOrCreateGenre(gameRequestDto));
        game.setActivationPlatform(getOrCreatePlatform(gameRequestDto));
        game.setActivationRegion(getOrCreateRegion(gameRequestDto));
        game.setPublisher(getOrCreatePublisher(gameRequestDto));

        return game;
    }

    private Specification<Game> createGameSpecification(GameSearchCriteria gameSearchCriteria) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            addNamePredicate(gameSearchCriteria, root, criteriaBuilder, predicates);
            addRatingPredicate(gameSearchCriteria, root, criteriaBuilder, predicates);
            predicates.add(criteriaBuilder.isTrue(root.get("user").get("enabled")));

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    private void addRatingPredicate(GameSearchCriteria gameSearchCriteria, Root<Game> root, CriteriaBuilder criteriaBuilder, List<Predicate> predicates) {
        if (gameSearchCriteria.getRating() != null) {
            Subquery<Integer> ratingSubquery = criteriaBuilder.createQuery().subquery(Integer.class);
            Root<Rating> ratingRoot = ratingSubquery.from(Rating.class);
            Expression<Double> avgRating = criteriaBuilder.avg(ratingRoot.get("rating"));
            Expression<Integer> roundedAvgRating = criteriaBuilder.function("ROUND", Integer.class, avgRating);

            ratingSubquery.select(roundedAvgRating)
                    .where(criteriaBuilder.equal(ratingRoot.get("game"), root));

            predicates.add(criteriaBuilder.equal(ratingSubquery, gameSearchCriteria.getRating()));
        }
    }

    private void addNamePredicate(GameSearchCriteria gameSearchCriteria, Root<Game> root, CriteriaBuilder criteriaBuilder, List<Predicate> predicates) {
        if (gameSearchCriteria.getSearch() != null && !gameSearchCriteria.getSearch().isEmpty()) {
            String[] words = gameSearchCriteria.getSearch().toLowerCase().split("\\s+");
            List<Predicate> wordPredicates = new ArrayList<>();

            for (String word : words) {
                wordPredicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + word + "%"));
            }

            predicates.add(criteriaBuilder.and(wordPredicates.toArray(new Predicate[0])));
        }
    }

    private Pageable createPageable(GameSearchCriteria gameSearchCriteria) {
        int pageOffset = Optional.ofNullable(gameSearchCriteria.getOffset()).orElse(0);
        int pageLimit = Optional.ofNullable(gameSearchCriteria.getLimit()).orElse(10);
        Sort sortOrder = determineSortOrder(gameSearchCriteria);

        return PageRequest.of(pageOffset, Math.min(pageLimit, 20), sortOrder);
    }

    private Sort determineSortOrder(GameSearchCriteria gameSearchCriteria) {
        String sortColumn = Optional.ofNullable(gameSearchCriteria.getSortColumn()).orElse("id");

        if (!SortingConstant.VALID_SORT_COLUMNS.contains(sortColumn)) {
            throw new IllegalArgumentException("Invalid sort column: " + sortColumn);
        }

        SortOrder sortOrder = gameSearchCriteria.getSortOrder();
        return Sort.by(sortOrder == SortOrder.DESC ? Sort.Order.desc(sortColumn) : Sort.Order.asc(sortColumn));
    }

    private Game createGameEntity(GameRequestDto gameRequestDto, MultipartFile file) throws IOException {
        Game game = validateRequest(gameRequestDto);
        game.setUser(sessionService.getUserFromSessionId());
        game.setImage(imageService.saveImage(file));

        return game;
    }

    private Game validateRequest(GameRequestDto gameRequestDto) {
        Genre genre = getOrCreateGenre(gameRequestDto);
        Platform platform = getOrCreatePlatform(gameRequestDto);
        Region region = getOrCreateRegion(gameRequestDto);
        Publisher publisher = getOrCreatePublisher(gameRequestDto);

        return gameMapper.toEntity(gameRequestDto, genre, platform, region, publisher);
    }

    private Publisher getOrCreatePublisher(GameRequestDto gameRequestDto) {
        return findOrCreate(gameRequestDto.getPublisher(),
                publisherDao::findByPublisherName, Publisher::new, publisherDao::save);
    }

    private Region getOrCreateRegion(GameRequestDto gameRequestDto) {
        return findOrCreate(gameRequestDto.getRegion(),
                regionDao::findByRegionName, Region::new, regionDao::save);
    }

    private Platform getOrCreatePlatform(GameRequestDto gameRequestDto) {
        return findOrCreate(gameRequestDto.getPlatform(),
                platformDao::findByPlatformName, Platform::new, platformDao::save);
    }

    private Genre getOrCreateGenre(GameRequestDto gameRequestDto) {
        return findOrCreate(gameRequestDto.getGenre(),
                genreDao::findByGenreName, Genre::new, genreDao::save);
    }

    private <T> T findOrCreate(String name,
                               Function<String, Optional<T>> findFunction,
                               Function<String, T> createFunction,
                               UnaryOperator<T> saveFunction) {
        return findFunction.apply(name).orElseGet(() -> saveFunction.apply(createFunction.apply(name)));
    }

}
