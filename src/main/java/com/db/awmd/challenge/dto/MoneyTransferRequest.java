package com.db.awmd.challenge.dto;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * CCreated by USER on 24-02-2018.
 */
public class MoneyTransferRequest {

    @NotNull
    @NotEmpty
    @Getter
    @Setter
    private String accountFrom ;

    @NotNull
    @NotEmpty
    @Getter
    @Setter
    private String accountTo;

    @NotNull
    @Min(value = 0, message = "transfer amount must be positive")
    @Getter
    @Setter
    private BigDecimal amountToTransfer;

}
