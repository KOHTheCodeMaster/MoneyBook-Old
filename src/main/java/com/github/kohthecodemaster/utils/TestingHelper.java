package com.github.kohthecodemaster.utils;

import com.github.kohthecodemaster.pojo.AccountPojo;
import com.github.kohthecodemaster.pojo.CreditCardPojo;
import com.github.kohthecodemaster.pojo.TransactionPojo;

import java.io.File;
import java.util.List;

public class TestingHelper {

    public final File transactionJsonFile;
    public final File accountJsonFile;
    public final File creditCardJsonFile;

    public TestingHelper(File transactionJsonFile, File accountJsonFile, File creditCardJsonFile) {
        this.transactionJsonFile = transactionJsonFile;
        this.accountJsonFile = accountJsonFile;
        this.creditCardJsonFile = creditCardJsonFile;
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


}
