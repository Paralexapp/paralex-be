package com.paralex.erp.ledger.documents;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class PageableLedgerDTO {
    private String walletId;
    private String type;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
}
