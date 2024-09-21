package com.gustas.videogamestore.service.Game;

import com.gustas.videogamestore.constant.SortingConstant;
import com.gustas.videogamestore.dao.Game.GameDao;
import com.gustas.videogamestore.dao.Genre.GenreDao;
import com.gustas.videogamestore.dao.Platform.PlatformDao;
import com.gustas.videogamestore.dao.Publisher.PublisherDao;
import com.gustas.videogamestore.dao.Region.RegionDao;
import com.gustas.videogamestore.domain.Game;
import com.gustas.videogamestore.domain.GameSearchCriteria;
import com.gustas.videogamestore.domain.Genre;
import com.gustas.videogamestore.domain.Platform;
import com.gustas.videogamestore.domain.Publisher;
import com.gustas.videogamestore.domain.Region;
import com.gustas.videogamestore.domain.SortOrder;
import com.gustas.videogamestore.dto.request.SaveGameRequestDto;
import com.gustas.videogamestore.dto.response.GameResponseDto;
import com.gustas.videogamestore.dto.response.PaginatedResponseDto;
import com.gustas.videogamestore.mapper.GameMapper;
import com.gustas.videogamestore.service.Session.SessionService;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@AllArgsConstructor
@Service
public class GameServiceImpl implements GameService {

    private GameDao gameDao;
    private GenreDao genreDao;
    private PlatformDao platformDao;
    private RegionDao regionDao;
    private PublisherDao publisherDao;
    private SessionService sessionService;

    @Override
    public PaginatedResponseDto getGames(GameSearchCriteria gameSearchCriteria) {
        Pageable pageable = createPageable(gameSearchCriteria);
        Specification<Game> specification = createBookSpecification(gameSearchCriteria);

        Page<Game> gamePage = gameDao.findAll(specification, pageable);

        List<GameResponseDto> dtoList = GameMapper.toDto(gamePage.getContent());

        return new PaginatedResponseDto(
                dtoList, gamePage.getNumber(), gamePage.getTotalPages(), gamePage.getTotalElements());
    }

    @Override
    public void deleteGame(Long gameId) {
        gameDao.deleteGame(gameId);
    }

    @Override
    public GameResponseDto saveGame(SaveGameRequestDto saveGameRequestDto) {
        Game game = createGameEntity(saveGameRequestDto);

        return GameMapper.toDto(gameDao.saveGame(game));
    }

    private Specification<Game> createBookSpecification(GameSearchCriteria gameSearchCriteria) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            addNamePredicate(gameSearchCriteria, root, criteriaBuilder, predicates);

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
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
        int pageLimit = Optional.ofNullable(gameSearchCriteria.getLimit()).orElse(Integer.MAX_VALUE);
        Sort sortOrder = determineSortOrder(gameSearchCriteria);

        return PageRequest.of(pageOffset, pageLimit, sortOrder);
    }

    private Sort determineSortOrder(GameSearchCriteria gameSearchCriteria) {
        String sortColumn = Optional.ofNullable(gameSearchCriteria.getSortColumn()).orElse("id");

        if (!SortingConstant.VALID_SORT_COLUMNS.contains(sortColumn)) {
            throw new IllegalArgumentException("Invalid sort column: " + sortColumn);
        }

        SortOrder sortOrder = gameSearchCriteria.getSortOrder();
        return Sort.by(sortOrder == SortOrder.DESC ? Sort.Order.desc(sortColumn) : Sort.Order.asc(sortColumn));
    }

    private Game createGameEntity(SaveGameRequestDto saveGameRequestDto) {
        Game game = validateRequest(saveGameRequestDto);
        game.setUser(sessionService.getUserFromSessionId());

        return game;
    }

    private Game validateRequest(SaveGameRequestDto saveGameRequestDto) {
        Genre genre = findOrCreate(saveGameRequestDto.getGenre(),
                genreDao::findByGenreName, Genre::new, genreDao::save);

        Platform platform = findOrCreate(saveGameRequestDto.getPlatform(),
                platformDao::findByPlatformName, Platform::new, platformDao::save);

        Region region = findOrCreate(saveGameRequestDto.getRegion(),
                regionDao::findByRegionName, Region::new, regionDao::save);

        Publisher publisher = findOrCreate(saveGameRequestDto.getPublisher(),
                publisherDao::findByPublisherName, Publisher::new, publisherDao::save);

        return GameMapper.toEntity(saveGameRequestDto, genre, platform, region, publisher);
    }

    private <T> T findOrCreate(String name,
                               Function<String,
                               Optional<T>> findFunction,
                               Function<String, T> createFunction,
                               Function<T, T> saveFunction) {
        return findFunction.apply(name).orElseGet(() -> saveFunction.apply(createFunction.apply(name)));
    }

}
