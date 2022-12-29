package com.github.kohthecodemaster.utils;

import com.github.kohthecodemaster.pojo.AccountPojo;
import com.github.kohthecodemaster.pojo.CardSwipePojo;
import com.github.kohthecodemaster.pojo.CreditCardPojo;
import com.github.kohthecodemaster.pojo.TransactionPojo;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class TestingHelper {

    public static void testMainTransactionPojoListFromJson(File transactionJsonFile) {

        List<TransactionPojo> transactionPojoList = TransactionPojo.loadTransactionPojoListFromJson(transactionJsonFile);

        transactionPojoList.forEach(System.out::println);
        System.out.println("Size: " + transactionPojoList.size());

    }

    public static void testAccountPojoListFromJson(File accountJsonFile) {

        List<AccountPojo> accountPojoList = AccountPojo.loadAccountPojoListFromJson(accountJsonFile);

        accountPojoList.forEach(System.out::println);
        System.out.println("Size: " + accountPojoList.size());

    }

    public static void testCreditCardPojoListFromJson(File creditCardJsonFile) {

        List<CreditCardPojo> creditCardPojoList = CreditCardPojo.loadCreditCardPojoListFromJson(creditCardJsonFile);

        creditCardPojoList.forEach(System.out::println);
        System.out.println("Size: " + creditCardPojoList.size());

    }

    public static void testCardSwipePojoListFromJson(File cardSwipeJsonFile) {

        List<CardSwipePojo> cardSwipePojoList = CardSwipePojo.loadCardSwipePojoListFromJson(cardSwipeJsonFile);

        cardSwipePojoList.forEach(System.out::println);
        System.out.println("Size: " + cardSwipePojoList.size());

    }

    public static void testCardNameMismatch(Map<String, CreditCardPojo> creditCardsMap, File cardSwipeJsonFile) {

        AtomicInteger counter = new AtomicInteger();

        List<CardSwipePojo> cardSwipePojoList = CardSwipePojo.loadCardSwipePojoListFromJson(cardSwipeJsonFile);

//        cardSwipePojoList.forEach(System.out::println);

        cardSwipePojoList.forEach(cardSwipePojo -> {

            CreditCardPojo creditCardPojo = creditCardsMap.get(cardSwipePojo.getLast4Digits());
            String cardName = creditCardPojo != null ? creditCardPojo.getCardName() : cardSwipePojo.getCardName();

            if (!creditCardPojo.getCardName().equals(cardSwipePojo.getCardName())) {
                System.out.println("Card Name Mismatch: \n" +
                        creditCardPojo.getCardName() + " ----> " +
                        cardSwipePojo.getCardName() + " | " +
                        cardSwipePojo.getId());
                counter.getAndIncrement();
                cardSwipePojo.setCardName(cardName);
            }

//            System.out.println(cardSwipePojo);

        });

        System.out.println("\nCard Name Mismatch Count: " + counter);
        System.out.println("Card Swipe Pojo List Size: " + cardSwipePojoList.size());

    }

    public static void checkDuplicateCardEntries(File creditCardJsonFile) {

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
