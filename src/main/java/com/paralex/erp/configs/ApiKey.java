package com.paralex.erp.configs;

import com.paralex.erp.entities.BaseCollection;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Data
public class ApiKey extends BaseCollection {
    @Id
    private String id;
    private String testKey;
    private String prodKey;
    private Boolean isEnabled;
    @Indexed(unique = true)
    private String institutionName;
}
