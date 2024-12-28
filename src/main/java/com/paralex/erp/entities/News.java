package com.paralex.erp.entities;

import com.paralex.erp.enums.NewsSection;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Setter
@Getter
@Document(collection = "news")
public class News {

    @Id
    private String id;
    private NewsSection section; // Criminal, Finance, Government, etc.
    private String title;
    private String content;
    private String publishedBy;
    private LocalDateTime publishedDate;
    private String imageUrl; // Optional: URL for an image associated with the news

}
