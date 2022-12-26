package com.github.kohthecodemaster.controller;

import com.github.kohthecodemaster.pojo.AccountPojo;
import com.github.kohthecodemaster.pojo.CreditCardPojo;
import com.github.kohthecodemaster.pojo.TransactionPojo;
import com.github.kohthecodemaster.pojo.TransactionType;

import java.io.File;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainController {

    public static final File transactionJsonFile = new File("src/main/resources/stub/transactions.json");
    public static final File accountJsonFile = new File("src/main/resources/stub/accounts.json");
    public static final File creditCardJsonFile = new File("src/main/resources/stub/credit-cards.json");

    private Map<String, AccountPojo> accountMap;
    private Map<String, CreditCardPojo> creditCardsMap;
    private List<TransactionPojo> transactionPojoList;

    public void major() {

        init();
        testing();

    }

    private void testing() {

//        TestingHelper testingHelper = new TestingHelper(transactionJsonFile, accountJsonFile, creditCardJsonFile);
//        testingHelper.testTransactionPojoListFromJson();
//        testingHelper.testAccountPojoListFromJson();
//        testingHelper.testCreditCardPojoListFromJson();

        testTxnProcessing();

    }

    public void testTxnProcessing() {

        AccountPojo.summary(accountMap);

        transactionPojoList.forEach(this::processTxn);

        AccountPojo.summary(accountMap);

    }

    private void processTxn(TransactionPojo transactionPojo) {

//        System.out.println("Processing - Begin.");

//        System.out.println(transactionPojo);

        AccountPojo sourceAccountPojo = accountMap.get(transactionPojo.getSourceAccount());

        if (transactionPojo.getTransactionType() == TransactionType.Income) {
            //  Add Amount
            sourceAccountPojo.setBalance(
                    sourceAccountPojo.getBalance().add(transactionPojo.getAmount())
            );
        } else if (transactionPojo.getTransactionType() == TransactionType.Expense) {
            //  Deduct Amount
            sourceAccountPojo.setBalance(
                    sourceAccountPojo.getBalance().subtract(transactionPojo.getAmount())
            );
        } else if (transactionPojo.getTransactionType() == TransactionType.Transfer) {

            AccountPojo targetAccountPojo = accountMap.get(transactionPojo.getTargetAccount());
            BigDecimal amount = transactionPojo.getAmount();

            //  Transfer Amount from Source to Target Account based on Account & Credit Card scenario
            if (sourceAccountPojo != null &&
                    targetAccountPojo != null) {

                //  Source & Target Account Both are NOT A Credit Card
                AccountPojo.transferBalance(sourceAccountPojo, targetAccountPojo, amount);

            } else if (sourceAccountPojo == null &&
                    targetAccountPojo != null) {

                //  Source IS A Credit Card  AND  Target Account IS NOT A Credit Card
                CreditCardPojo sourceCreditCardPojo = creditCardsMap.get(transactionPojo.getSourceAccount());//  Last 4 Digits
                AccountPojo.transferBalance(sourceCreditCardPojo, targetAccountPojo, amount);

            } else if (sourceAccountPojo != null &&
                    targetAccountPojo == null) {

                //  Source Account IS NOT A Credit Card  AND  Target IS A Credit Card
                CreditCardPojo targetCreditCardPojo = creditCardsMap.get(transactionPojo.getTargetAccount());//  Last 4 Digits
                AccountPojo.transferBalance(sourceAccountPojo, targetCreditCardPojo, amount);

            } else {
                System.out.println("INVALID Scenario - Failed to Process Transaction:\n" + transactionPojo);
            }
        } else {
            System.out.println("INVALID Scenario - Failed to Process Transaction:\n" + transactionPojo);
        }

//        System.out.println("Processing - Completed.");

    }

    private void init() {

        initializeAccountsMap();
        initializeCreditCardsMap();
        initializeTransactionList();

    }

    private void initializeAccountsMap() {

        accountMap = new HashMap<>();
        List<AccountPojo> accountPojoList = AccountPojo.loadAccountPojoListFromJson(accountJsonFile);
        accountPojoList.forEach(accountPojo -> accountMap.put(accountPojo.getName(), accountPojo));

    }

    private void initializeCreditCardsMap() {

        creditCardsMap = new HashMap<>();
        List<CreditCardPojo> creditCardPojoList = CreditCardPojo.loadCreditCardPojoListFromJson(creditCardJsonFile);
        creditCardPojoList.forEach(creditCardPojo -> creditCardsMap.put(creditCardPojo.getLast4Digits(), creditCardPojo));

    }

    private void initializeTransactionList() {
        this.transactionPojoList = TransactionPojo.loadTransactionPojoListFromJson(transactionJsonFile);
    }

}
