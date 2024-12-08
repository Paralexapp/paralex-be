package com.paralex.erp.ledger.documents;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.paralex.erp.entities.BaseCollection;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

@Document(collection = "ledger")
@Data
@RequiredArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Ledger extends BaseCollection {
    @Id
    private String id;
    private String type;
    private BigDecimal previousBalance;
    private BigDecimal newBalance;
    private String walletId;
    private String currency;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String description;
    @Indexed(unique = true)
    private String transactionRef;
    private String status;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String channel;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String accountNumber;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String accountBank;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String channelPaymentId;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String channelPaymentRef;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String channelPaymentStatus;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String channelPaymentCode;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private BigDecimal amount;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String senderAccount;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String senderName;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String senderBank;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String receiverAccount;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String receiverName;
    private String receiverEmailAddress;
    private String senderEmailAddress;

}
