package com.gustas.videogamestore.dao.Publisher;

import com.gustas.videogamestore.domain.Publisher;
import com.gustas.videogamestore.repository.PublisherRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
@AllArgsConstructor
public class PublisherDaoImpl implements PublisherDao {

    private PublisherRepository publisherRepository;

    @Override
    public Publisher save(Publisher publisher) {
        return publisherRepository.save(publisher);
    }

    @Override
    public Optional<Publisher> findByPublisherName(String publisherName) {
        return publisherRepository.findByPublisher(publisherName);
    }

}
