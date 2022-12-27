package com.github.kohthecodemaster.controller;

import com.github.kohthecodemaster.pojo.AccountPojo;
import com.github.kohthecodemaster.pojo.CardSwipeTransactionPojo;
import com.github.kohthecodemaster.pojo.CreditCardPojo;
import com.github.kohthecodemaster.pojo.TransactionPojo;
import com.github.kohthecodemaster.utils.TestingHelper;

import java.io.File;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainController {

    public static final File transactionJsonFile = new File("src/main/resources/stub/transactions.json");
    public static final File accountJsonFile = new File("src/main/resources/stub/accounts.json");
    public static final File creditCardJsonFile = new File("src/main/resources/stub/credit-cards.json");
    public static final File cardSwipeTransactionJsonFile = new File("src/main/resources/stub/card-swipe-transaction.json");

    private Map<String, AccountPojo> accountMap;
    private Map<String, CreditCardPojo> creditCardsMap;
    private List<TransactionPojo> transactionPojoList;
    private List<CardSwipeTransactionPojo> cardSwipeTransactionPojoList;

    public void major() {

        init();
        testing();

    }

    private void testing() {

        testStubFromJson();
//        testTxnProcessing();

    }

    private void testStubFromJson() {

        TestingHelper testingHelper = new TestingHelper(transactionJsonFile, accountJsonFile, creditCardJsonFile, cardSwipeTransactionJsonFile);
//        testingHelper.testTransactionPojoListFromJson();
//        testingHelper.testAccountPojoListFromJson();
//        testingHelper.testCreditCardPojoListFromJson();
        testingHelper.testCardSwipePojoListFromJson(creditCardsMap);

    }

    public void testTxnProcessing() {

        transactionPojoList.forEach(this::processTxn);
        summary();

    }

    private void summary() {

        BigDecimal totalAccounts = BigDecimal.ZERO;
        BigDecimal totalCards = BigDecimal.ZERO;

        System.out.println("\nAccounts Summary:");
        for (Map.Entry<String, AccountPojo> entry : accountMap.entrySet()) {
            String strAccountName = entry.getKey();
            AccountPojo accountPojo = entry.getValue();
            if (accountPojo.getBalance().compareTo(BigDecimal.ZERO) != 0) {
                System.out.println(strAccountName + " -> " + accountPojo.getBalance());
                totalAccounts = totalAccounts.add(accountPojo.getBalance());
            }
        }

        System.out.println("\nCredit Cards Summary:");
        for (Map.Entry<String, CreditCardPojo> entry : creditCardsMap.entrySet()) {
            String last4Digits = entry.getKey();
            CreditCardPojo creditCardPojo = entry.getValue();
            if (creditCardPojo.getBalance().compareTo(BigDecimal.ZERO) != 0) {
                System.out.println(last4Digits + " -> " + creditCardPojo.getBalance());
                totalCards = totalCards.add(creditCardPojo.getBalance());
            }
        }

//        System.out.println("\n======\n");
//        AccountPojo accountPojo = accountMap.get("BillPe Kotak");
//        accountPojo.getTransactionPojoList().forEach(System.out::println);

        System.out.println("\nAcc. Total: " + totalAccounts + " | Cards Total: " + totalCards + " | Diff: " + totalAccounts.add(totalCards));

    }

    private void processTxn(TransactionPojo transactionPojo) {

//        System.out.println("Processing - Begin.");

        try {

            AccountPojo sourceAccountPojo = accountMap.get(transactionPojo.getSourceAccount());
            AccountPojo targetAccountPojo = accountMap.get(transactionPojo.getTargetAccount());

            //  Transfer Amount from Source to Target Account based on Account & Credit Card scenario
            if (sourceAccountPojo != null &&
                    targetAccountPojo != null) {

                //  Source & Target Account Both are NOT A Credit Card
                AccountPojo.transferBalance(sourceAccountPojo, targetAccountPojo, transactionPojo);

            } else if (sourceAccountPojo == null &&
                    targetAccountPojo != null) {

                //  Source IS A Credit Card  AND  Target Account IS NOT A Credit Card
                CreditCardPojo sourceCreditCardPojo = creditCardsMap.get(transactionPojo.getSourceAccount());//  Last 4 Digits
                AccountPojo.transferBalance(sourceCreditCardPojo, targetAccountPojo, transactionPojo);

            } else if (sourceAccountPojo != null &&
                    targetAccountPojo == null) {

                //  Source Account IS NOT A Credit Card  AND  Target IS A Credit Card
                CreditCardPojo targetCreditCardPojo = creditCardsMap.get(transactionPojo.getTargetAccount());//  Last 4 Digits
                AccountPojo.transferBalance(sourceAccountPojo, targetCreditCardPojo, transactionPojo);

            } else {
                System.out.println("INVALID Scenario - Failed to Process Transaction:\n" + transactionPojo);
            }

        } catch (Exception e) {
            System.out.println("Failed to Process - Transaction Pojo: " + transactionPojo);
            e.printStackTrace();
            throw new RuntimeException(e);
        }

//        System.out.println("Processing - Completed.");

    }

    private void init() {

        initializeAccountsMap();
        initializeCreditCardsMap();
        initializeTransactionList();
        initializeCardSwipeTransactionPojoList();

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

    private void initializeCardSwipeTransactionPojoList() {
        this.cardSwipeTransactionPojoList = CardSwipeTransactionPojo.loadCardSwipeTransactionPojoListFromJson(cardSwipeTransactionJsonFile);
    }

}
