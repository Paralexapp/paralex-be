package com.paralex.erp.dtos;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "pay-stack-events")
@Data
@RequiredArgsConstructor
public class PayStackEvent {
    @Id
    private String id;
    private String type;
    private org.bson.Document event;
}
