package com.paralex.erp.repositories;

import com.paralex.erp.entities.News;
import com.paralex.erp.enums.NewsSection;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NewsRepository extends MongoRepository<News, String> {
    List<News> findNewsBySection(NewsSection section);
}
