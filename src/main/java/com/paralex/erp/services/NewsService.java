package com.paralex.erp.services;

import com.paralex.erp.entities.News;
import com.paralex.erp.enums.NewsSection;
import com.paralex.erp.repositories.NewsRepository;
import kotlin.collections.ArrayDeque;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class NewsService {

    private final NewsRepository newsRepository;
    private final NotificationService notificationService; // Assuming notification logic exists

    @Autowired
    public NewsService(NewsRepository newsRepository, NotificationService notificationService) {
        this.newsRepository = newsRepository;
        this.notificationService = notificationService;
    }

//    public News postNews(News news) {
//        // Set the published date to the current time
//        news.setPublishedDate(LocalDateTime.now());
//
//        // Save the news in the database
//        News savedNews = newsRepository.save(news);
//
//        // Notify all users
//        notificationService.notifyUsers("New News Posted", "A new news article has been published: " + news.getTitle());
//
//        return savedNews;
//    }

    public News postNews(News news) {
        // Set the published date to the current time
        news.setPublishedDate(LocalDateTime.now());

        // Save the news in the database
        News savedNews = newsRepository.save(news);

        // Create and save a global notification
        notificationService.createNotification(
                "New News Posted",
                "A new news article has been published: " + news.getTitle(),
                null // Null for global notifications
        );

        // Optionally send the notification to connected users in real-time (e.g., WebSocket)
        notificationService.broadcastNotification(
                "New News Posted",
                "A new news article has been published: " + news.getTitle()
        );

        return savedNews;
    }


    public List<News> getAllNews() {
        return newsRepository.findAll();
    }

    public News getNewsById(String id) {
        return newsRepository.findById(id).orElseThrow(() -> new RuntimeException("News not found with id: " + id));
    }

    public List<News> getNewsBySection(NewsSection section) {
        List<News> newsList = newsRepository.findNewsBySection(section);
        if (newsList.isEmpty()) {
//            throw new RuntimeException("No news found in section: " + section);
            return new ArrayList<>();
        }
        return newsList;
    }
}
