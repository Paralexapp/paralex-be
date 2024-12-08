package com.paralex.erp.ledger.documents;

import com.paralex.erp.entities.BaseCollection;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "events")
@Data
@RequiredArgsConstructor
public class Events extends BaseCollection {
    @Id
    private String id;
    private org.bson.Document event;
}
