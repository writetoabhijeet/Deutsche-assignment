package com.db.awmd.challenge.web;

import com.db.awmd.challenge.dto.MoneyTransferRequest;
import com.db.awmd.challenge.dto.MoneyTransferResponse;
import com.db.awmd.challenge.exception.MoneyTransferException;
import com.db.awmd.challenge.service.AccountsService;
import com.db.awmd.challenge.service.MoneyTransferService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * Created by USER on 24-02-2018.
 */

@RestController
@RequestMapping("/v1/accounts")
@Slf4j
public class FundTransferController {

    private final MoneyTransferService moneyTransferService;

    @Autowired
    public FundTransferController(MoneyTransferService moneyTransferService) {
        this.moneyTransferService=moneyTransferService;
    }

    @PostMapping(path = "/transfer" , consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MoneyTransferResponse> transferMoney(@RequestBody @Valid MoneyTransferRequest request){
        log.info("Received request to transfer amount {}, from account id {}, to account id {}", request.getAmountToTransfer(),request.getAccountFrom(),request.getAccountTo());
        MoneyTransferResponse response=null;
        try {
            response=  moneyTransferService.transferMoney(request);
        }catch (MoneyTransferException mte){
            return new ResponseEntity<MoneyTransferResponse>(response, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<MoneyTransferResponse>(response, HttpStatus.OK);


    }
}