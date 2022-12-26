package com.github.kohthecodemaster.controller;

import com.github.kohthecodemaster.pojo.AccountPojo;
import com.github.kohthecodemaster.pojo.CreditCardPojo;
import com.github.kohthecodemaster.pojo.TransactionPojo;
import com.github.kohthecodemaster.utils.TestingHelper;

import java.io.File;
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

        TestingHelper testingHelper = new TestingHelper(transactionJsonFile, accountJsonFile, creditCardJsonFile);

//        testingHelper.testTransactionPojoListFromJson();
//        testingHelper.testAccountPojoListFromJson();
//        testingHelper.testCreditCardPojoListFromJson();
        testingHelper.testTxnProcessing();

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
