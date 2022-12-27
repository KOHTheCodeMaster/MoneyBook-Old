package com.github.kohthecodemaster.pojo;

import com.github.kohthecodemaster.utils.JsonController;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class AccountPojo {

    @SerializedName("Id")
    Integer id;
    @SerializedName("Name")
    String name;
    @SerializedName("Balance")
    BigDecimal balance;
    List<TransactionPojo> transactionPojoList = new ArrayList<>();

    public static List<AccountPojo> loadAccountPojoListFromJson(File jsonFile) {

        Type type = new TypeToken<List<AccountPojo>>() {
        }.getType();

        @SuppressWarnings("unchecked")
        List<AccountPojo> accountPojoList = (List<AccountPojo>) JsonController.parseJsonFileToList(jsonFile, type);
        return accountPojoList;

    }

    public static void transferBalance(AccountPojo sourceAccountPojo, AccountPojo targetAccountPojo, TransactionPojo transactionPojo) {

        BigDecimal sourceBalance = sourceAccountPojo.getBalance();
        BigDecimal targetBalance = targetAccountPojo.getBalance();

        targetBalance = targetBalance.add(transactionPojo.getAmount());          //  Add Amount to Target Balance
        sourceBalance = sourceBalance.subtract(transactionPojo.getAmount());     //  Deduct Amount from Source Balance

        //  Update target & source balance
        targetAccountPojo.setBalance(targetBalance);
        sourceAccountPojo.setBalance(sourceBalance);

        //  Using Copy Constructor to avoid conflict of updating closing balance field for
        //  sourceAccountPojo's transactionPojoList
        sourceAccountPojo.logTransaction(transactionPojo);
        targetAccountPojo.logTransaction(new TransactionPojo(transactionPojo));


    }

    public static void transferBalance(CreditCardPojo creditCardPojo, AccountPojo targetAccountPojo, TransactionPojo transactionPojo) {

        BigDecimal sourceBalance = creditCardPojo.getBalance();
        BigDecimal targetBalance = targetAccountPojo.getBalance();

        targetBalance = targetBalance.add(transactionPojo.getAmount());          //  Add Amount to Target Balance
        sourceBalance = sourceBalance.subtract(transactionPojo.getAmount());     //  Deduct Amount from Source Balance

        //  Update target & source balance
        targetAccountPojo.setBalance(targetBalance);
        creditCardPojo.setBalance(sourceBalance);

        creditCardPojo.logTransaction(transactionPojo);
        targetAccountPojo.logTransaction(new TransactionPojo(transactionPojo));

    }

    public static void transferBalance(AccountPojo sourceAccountPojo, CreditCardPojo creditCardPojo, TransactionPojo transactionPojo) {

        BigDecimal sourceBalance = sourceAccountPojo.getBalance();
        BigDecimal targetBalance = creditCardPojo.getBalance();

        sourceBalance = sourceBalance.subtract(transactionPojo.getAmount());     //  Deduct Amount from Source Balance
        targetBalance = targetBalance.add(transactionPojo.getAmount());          //  Add Amount to Target Balance

        //  Update target & source balance
        sourceAccountPojo.setBalance(sourceBalance);
        creditCardPojo.setBalance(targetBalance);

        sourceAccountPojo.logTransaction(transactionPojo);
        creditCardPojo.logTransaction(new TransactionPojo(transactionPojo));

    }

    public void logTransaction(TransactionPojo transactionPojo) {
        transactionPojo.setClosingBalance(balance);
        transactionPojoList.add(transactionPojo);
    }

    @Override
    public String toString() {
        return "Id: " + id +
                "Account Name: " + name +
                "Balance: " + balance +
                "Transaction Pojo List: " + transactionPojoList;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public List<TransactionPojo> getTransactionPojoList() {
        return transactionPojoList;
    }
}
