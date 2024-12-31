package com.paralex.erp.controllers;

import com.paralex.erp.entities.News;
import com.paralex.erp.enums.NewsSection;
import com.paralex.erp.services.NewsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/news")
public class NewsController {

    private final NewsService newsService;

    @Autowired
    public NewsController(NewsService newsService) {
        this.newsService = newsService;
    }


    @Operation(
            summary = "Post a news item",
            description = "Allows the user to post a news item to a specific section. " +
                    "Available sections: 'CRIMINAL', 'FINANCE', 'GOVERNMENT', 'ETC'."
    )
    @ApiResponse(
            responseCode = "201",
            description = "News posted successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = News.class))
    )
    @ApiResponse(
            responseCode = "400",
            description = "Invalid input, please check the news details",
            content = @Content(mediaType = "application/json")
    )
    @PostMapping("/post")
    public ResponseEntity<News> postNews(@RequestBody News news) {
        News postedNews = newsService.postNews(news);
        return new ResponseEntity<>(postedNews, HttpStatus.CREATED);
    }

    @GetMapping("/get-all")
    public ResponseEntity<List<News>> getAllNews() {
        List<News> newsList = newsService.getAllNews();
        return ResponseEntity.ok(newsList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<News> getNewsById(@PathVariable String id) {
        News news = newsService.getNewsById(id);
        return ResponseEntity.ok(news);
    }

    @GetMapping("/get-by-section")
    public ResponseEntity<List<News>> getNewsBySection(@RequestParam("section") NewsSection section) {
        List<News> newsList = newsService.getNewsBySection(section);
        return ResponseEntity.ok(newsList);
    }
}
