package com.db.awmd.challenge.service;

import com.db.awmd.challenge.dto.MoneyTransferRequest;
import com.db.awmd.challenge.dto.MoneyTransferResponse;

/**
 * Created by USER on 24-02-2018.
 */
public interface MoneyTransferService {

    MoneyTransferResponse transferMoney(MoneyTransferRequest request);
}
