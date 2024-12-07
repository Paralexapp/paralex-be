package com.paralex.erp.dtos;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection ="bill_transaction_initiator")
@Data
@RequiredArgsConstructor
public class TransactionInitiatorData {
    @Id
    private String id;
    private String recipient;
    private String email;
}
