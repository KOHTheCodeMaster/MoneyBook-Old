package com.github.kohthecodemaster.utils;

import com.github.kohthecodemaster.pojo.AccountPojo;
import com.github.kohthecodemaster.pojo.CardSwipeTransactionPojo;
import com.github.kohthecodemaster.pojo.CreditCardPojo;
import com.github.kohthecodemaster.pojo.TransactionPojo;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestingHelper {

    public static int counter;
    public final File transactionJsonFile;
    public final File accountJsonFile;
    public final File creditCardJsonFile;
    public final File cardSwipeTransactionJsonFile;

    public TestingHelper(File transactionJsonFile, File accountJsonFile, File creditCardJsonFile, File cardSwipeTransactionJsonFile) {
        this.transactionJsonFile = transactionJsonFile;
        this.accountJsonFile = accountJsonFile;
        this.creditCardJsonFile = creditCardJsonFile;
        this.cardSwipeTransactionJsonFile = cardSwipeTransactionJsonFile;
    }

    public void testTransactionPojoListFromJson() {

        List<TransactionPojo> transactionPojoList = TransactionPojo.loadTransactionPojoListFromJson(transactionJsonFile);

        transactionPojoList.forEach(System.out::println);
        System.out.println("Size: " + transactionPojoList.size());

    }

    public void testAccountPojoListFromJson() {

        List<AccountPojo> accountPojoList = AccountPojo.loadAccountPojoListFromJson(accountJsonFile);

        accountPojoList.forEach(System.out::println);
        System.out.println("Size: " + accountPojoList.size());

    }

    public void testCreditCardPojoListFromJson() {

        List<CreditCardPojo> creditCardPojoList = CreditCardPojo.loadCreditCardPojoListFromJson(creditCardJsonFile);

        creditCardPojoList.forEach(System.out::println);
        System.out.println("Size: " + creditCardPojoList.size());

    }

    public void testCardSwipePojoListFromJson(Map<String, CreditCardPojo> creditCardsMap) {

        List<CardSwipeTransactionPojo> cardSwipeTransactionPojoList = CardSwipeTransactionPojo.loadCardSwipeTransactionPojoListFromJson(cardSwipeTransactionJsonFile);

//        cardSwipeTransactionPojoList.forEach(System.out::println);

        cardSwipeTransactionPojoList.forEach(cardSwipeTransactionPojo -> {

            CreditCardPojo creditCardPojo = creditCardsMap.get(cardSwipeTransactionPojo.getLast4Digits());
            String cardName = creditCardPojo != null ? creditCardPojo.getCardName() : cardSwipeTransactionPojo.getCardName();

            if (!creditCardPojo.getCardName().equals(cardSwipeTransactionPojo.getCardName())) {
                System.out.println("Card Name Mismatch: \n" +
                        creditCardPojo.getCardName() + " ----> " +
                        cardSwipeTransactionPojo.getCardName() + " | " +
                        cardSwipeTransactionPojo.getId());
                counter++;
                cardSwipeTransactionPojo.setCardName(cardName);
            }

            System.out.println(cardSwipeTransactionPojo);

        });

        System.out.println("\nCard Name Mismatch Count: " + counter);
        System.out.println("Card Swipe Transaction Pojo List Size: " + cardSwipeTransactionPojoList.size());

    }

    public void checkDuplicateCardEntries() {

        List<CreditCardPojo> creditCardPojoList = CreditCardPojo.loadCreditCardPojoListFromJson(creditCardJsonFile);
        Map<String, Integer> map = new HashMap<>();
        int duplicateCount = 0;

        for (CreditCardPojo creditCardPojo : creditCardPojoList) {
            Integer frequency = map.putIfAbsent(creditCardPojo.getLast4Digits(), 1);
            if (frequency != null && frequency > 1) {
                System.out.println("Duplicate Card Entry: " + creditCardPojo);
                duplicateCount++;
            }
        }

        if (duplicateCount != 0) System.out.println(duplicateCount + " - Duplicate Card Entries Exists.");
        else System.out.println("NO Duplicate Card Entries Found.");

    }

}
