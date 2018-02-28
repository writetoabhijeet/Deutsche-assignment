package com.db.awmd.challenge.web;

import com.db.awmd.challenge.domain.Account;
import com.db.awmd.challenge.dto.MoneyTransferRequest;
import com.db.awmd.challenge.dto.MoneyTransferResponse;
import com.db.awmd.challenge.exception.DuplicateAccountIdException;
import com.db.awmd.challenge.exception.MoneyTransferException;
import com.db.awmd.challenge.service.AccountsService;
import com.db.awmd.challenge.service.MoneyTransferService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/v1/accounts")
@Slf4j
public class AccountsController {

  private final AccountsService accountsService;

  private final MoneyTransferService moneyTransferService;

  @Autowired
  public AccountsController(AccountsService accountsService, MoneyTransferService moneyTransferService) {
    this.accountsService = accountsService;
    this.moneyTransferService = moneyTransferService;
  }

  @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Object> createAccount(@RequestBody @Valid Account account) {
    log.info("Creating account {}", account);

    try {
    this.accountsService.createAccount(account);
    } catch (DuplicateAccountIdException daie) {
      return new ResponseEntity<Object>(daie.getMessage(), HttpStatus.BAD_REQUEST);
    }

    return new ResponseEntity<Object>(HttpStatus.CREATED);
  }

  @GetMapping(path = "/{accountId}")
  public Account getAccount(@PathVariable String accountId) {
    log.info("Retrieving account for id {}", accountId);
    return this.accountsService.getAccount(accountId);
  }

  @PostMapping(path = "/transfer", consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<MoneyTransferResponse> transferMoney(@RequestBody @Valid MoneyTransferRequest request) {
    log.info("Received request to transfer amount {}, from account id {}, to account id {}", request.getAmountToTransfer(), request.getAccountFrom(), request.getAccountTo());
    MoneyTransferResponse response = null;
    try {
      response = moneyTransferService.transferMoney(request);
    } catch (MoneyTransferException mte) {
      return new ResponseEntity<MoneyTransferResponse>(response, HttpStatus.BAD_REQUEST);
    }
    return new ResponseEntity<MoneyTransferResponse>(response, HttpStatus.OK);


  }

}
