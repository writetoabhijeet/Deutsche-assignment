package com.db.awmd.challenge.service;

import com.db.awmd.challenge.domain.Account;
import com.db.awmd.challenge.dto.MoneyTransferRequest;
import com.db.awmd.challenge.dto.MoneyTransferResponse;
import com.db.awmd.challenge.exception.MoneyTransferException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * Created by USER on 24-02-2018.
 */

@Service
@Slf4j
public class MoneyTransferServiceImpl implements MoneyTransferService {

    @Autowired
    private AccountsService accountsService;


    /**
     * This method validate the source and destination account & amount to transfer . And on successful
     * validation , make the money transfer as expected and update the corresponding balances in the account
     * @param request
     * @return
     */
    @Override
    public MoneyTransferResponse transferMoney(MoneyTransferRequest request) {
        validateAccounts(request);
        validateTransferAmount(request);
        return transferAmount(request);
    }

    /**
     * This method validate the source and destination accounts
     * @param request
     */
    private void validateAccounts(MoneyTransferRequest request) {
        if (request.getAccountFrom().equals(request.getAccountTo())){
            throw new MoneyTransferException(
                    "From & To account cannot be same");
        }

        checkIfAccountExists(request.getAccountFrom());
        checkIfAccountExists(request.getAccountTo());
    }

    /**
     * This method validate if the account is exists
     * @param account
     */
    private void checkIfAccountExists(String account) {
        if(accountsService.getAccount(account) == null){
            throw new MoneyTransferException("Account :"+account+" doesn't exists");
        }
    }

    /**
     * This method valide if source account has sufficient balance to transfer
     * @param request
     */
    private void validateTransferAmount(MoneyTransferRequest request) {
        BigDecimal availableBalance = accountsService.getAccount(request.getAccountFrom()).getBalance();
        if(availableBalance.compareTo(request.getAmountToTransfer())<0){
            throw new MoneyTransferException("Insufficient balance in the account, cannot transfer more than"+availableBalance);
        }else {
            log.info("Sufficient Balance : {} available for transfer",availableBalance.toString());
        }
    }

    /**
     * Transfer money from 'From ' account to 'To'
     * @param request
     * @return
     */
    private synchronized MoneyTransferResponse transferAmount(MoneyTransferRequest request) {
        Account fromAccount = accountsService.getAccount(request.getAccountFrom());
        Account toAccount = accountsService.getAccount(request.getAccountFrom());
        BigDecimal transferAmt = request.getAmountToTransfer();
        fromAccount.debit(transferAmt);
        toAccount.credit(transferAmt);
        return new MoneyTransferResponse("Successful");
    }


}
