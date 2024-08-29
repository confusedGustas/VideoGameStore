package com.gustas.videogamestore.dao.Publisher;

import com.gustas.videogamestore.domain.Publisher;
import java.util.Optional;

public interface PublisherDao {

    Publisher save(Publisher publisher);
    Optional<Publisher> findByPublisherName(String publisherName);

}
