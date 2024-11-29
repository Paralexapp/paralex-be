package com.paralex.erp.repositories;

import com.paralex.erp.dtos.SumOfWalletTransactionDto;
import com.paralex.erp.entities.WalletTransactionEntity;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface WalletTransactionRepository extends JpaRepository<WalletTransactionEntity, String> {
    @Query(value = """
                SELECT SUM(wte.amount) as sum
                    FROM WalletTransactionEntity wte
                    WHERE wte.type = :type AND
                    wte.creatorId = :creatorId
            """)
    SumOfWalletTransactionDto sumAmountByCreatorIdAndType(
            @Param("type") @NotNull String type,
            @Param("creatorId") @NotNull String creatorId);
}
