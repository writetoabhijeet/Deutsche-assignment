package com.db.awmd.challenge.dto;

import lombok.Data;

/**
 * Created by USER on 24-02-2018.
 */

@Data
public class MoneyTransferResponse {

    private String transferStatus;

    public MoneyTransferResponse(String transferStatus) {
        this.transferStatus = transferStatus;
    }
}
