package com.db.awmd.challenge.service;

import com.db.awmd.challenge.domain.Account;
import com.db.awmd.challenge.dto.MoneyTransferRequest;
import com.db.awmd.challenge.dto.MoneyTransferResponse;
import com.db.awmd.challenge.exception.MoneyTransferException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.NumberFormat;

/**
 * Created by USER on 24-02-2018.
 */

@Service
@Slf4j
public class MoneyTransferServiceImpl implements MoneyTransferService {

    @Autowired
    private AccountsService accountsService;

    @Autowired
    private NotificationService notificationService;

    private static Object sharedLock = new Object();


    /**
     * This method validate the source and destination account & amount to transfer . And on successful
     * validation , make the money transfer as expected and update the corresponding balances in the account
     * @param request
     * @return
     */
    @Override
    public MoneyTransferResponse transferMoney(MoneyTransferRequest request) {
        validateAccounts(request);
        synchronized (sharedLock) {
            validateTransferAmount(request);
            return transferAmount(request);
        }
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
    private MoneyTransferResponse transferAmount(MoneyTransferRequest request) {
        Account fromAccount = accountsService.getAccount(request.getAccountFrom());
        Account toAccount = accountsService.getAccount(request.getAccountFrom());
        BigDecimal transferAmt = request.getAmountToTransfer();
        try {
            fromAccount.debit(transferAmt);
            toAccount.credit(transferAmt);
            notifyMoneyTransfer(fromAccount, toAccount, transferAmt);
        } catch (Exception e) {

            log.error("Exception occurs while fund transfer", e);
            throw new MoneyTransferException(e.getMessage());
        }

        return new MoneyTransferResponse("Successful");
    }

    /**
     * This method notify both account holder regarding the transactions.
     *
     * @param fromAccount
     * @param toAccount
     * @param transferAmt
     */
    private void notifyMoneyTransfer(Account fromAccount, Account toAccount, BigDecimal transferAmt) {
        notificationService.notifyAboutTransfer(fromAccount, String.join(" ", "Amount of", currencyFormat(transferAmt), "debited to the account-id:", fromAccount.getAccountId()));
        notificationService.notifyAboutTransfer(toAccount, String.join(" ", "Amount of", currencyFormat(transferAmt), "credited to the account-id:", toAccount.getAccountId()));
    }

    /**
     * This method uses locale to choose your currency symbol,
     * and return decorated string value.
     *
     * @param n amount transfer
     * @return amount with symbol
     */
    public static String currencyFormat(BigDecimal n) {
        return NumberFormat.getCurrencyInstance().format(n);
    }


}
