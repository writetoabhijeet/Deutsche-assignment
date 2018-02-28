package com.db.awmd.challenge.service;

import com.db.awmd.challenge.domain.Account;
import com.db.awmd.challenge.dto.MoneyTransferRequest;
import com.db.awmd.challenge.dto.MoneyTransferResponse;
import com.db.awmd.challenge.exception.MoneyTransferException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;

import static org.junit.Assert.*;

/**
 * Created by USER on 28-02-2018.
 */

@RunWith(SpringRunner.class)
@SpringBootTest
public class MoneyTransferServiceImplTest {

    @Autowired
    private MoneyTransferService moneyService;

    @Autowired
    private AccountsService accountsService;


    @Test(expected = MoneyTransferException.class )
    public void testTransferMoneyForSameSourceAndDestinationAcc() throws Exception {
        MoneyTransferRequest request = buildReqObject("ID-101","ID-101",50l);
        moneyService.transferMoney(request);
    }

    @Test(expected = MoneyTransferException.class )
    public void testTransferMoneyForInvalidAcc() throws Exception {
        Account account = new Account("Id-050");
        account.setBalance(new BigDecimal(1000));
        this.accountsService.createAccount(account);
        MoneyTransferRequest request = buildReqObject("Id-050","ID-101",50l);
        moneyService.transferMoney(request);
    }

    @Test(expected = MoneyTransferException.class )
    public void testTransferMoneyForInsufficientBalance() throws Exception {
        Account account = new Account("Id-300");
        account.setBalance(new BigDecimal(1000));
        this.accountsService.createAccount(account);

        Account account1 = new Account("Id-301");
        account1.setBalance(new BigDecimal(5000));
        this.accountsService.createAccount(account1);
        MoneyTransferRequest request = buildReqObject("Id-300","Id-301",5000);
        moneyService.transferMoney(request);
    }

    @Test
    public void testTransferMoneyForSucessfulScenario() throws Exception {
        Account account = new Account("Id-400");
        account.setBalance(new BigDecimal(1000));
        this.accountsService.createAccount(account);

        Account account1 = new Account("Id-401");
        account1.setBalance(new BigDecimal(5000));
        this.accountsService.createAccount(account1);
        MoneyTransferRequest request = buildReqObject("Id-400","Id-401",500);
        MoneyTransferResponse response= moneyService.transferMoney(request);

    }



    private MoneyTransferRequest buildReqObject(String fromAcc, String toAcc, long amount) {
        MoneyTransferRequest request = new MoneyTransferRequest();
        request.setAccountFrom(fromAcc);
        request.setAccountTo(toAcc);
        request.setAmountToTransfer(BigDecimal.valueOf(amount));
        return request;
    }
}