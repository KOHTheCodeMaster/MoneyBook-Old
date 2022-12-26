package com.github.kohthecodemaster.pojo;

import com.github.kohthecodemaster.utils.JsonController;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class AccountPojo {

    @SerializedName("Id")
    Integer id;
    @SerializedName("Name")
    String name;
    @SerializedName("Balance")
    BigDecimal balance;

    public static List<AccountPojo> loadAccountPojoListFromJson(File jsonFile) {

        Type type = new TypeToken<List<AccountPojo>>() {
        }.getType();

        @SuppressWarnings("unchecked")
        List<AccountPojo> accountPojoList = (List<AccountPojo>) JsonController.parseJsonFileToList(jsonFile, type);
        return accountPojoList;

    }

    public static void transferBalance(AccountPojo sourceAccountPojo, AccountPojo targetAccountPojo, BigDecimal amount) {

        BigDecimal sourceBalance = sourceAccountPojo.getBalance();
        BigDecimal targetBalance = targetAccountPojo.getBalance();

        targetBalance = targetBalance.add(amount);          //  Add Amount to Target Balance
        sourceBalance = sourceBalance.subtract(amount);     //  Deduct Amount from Source Balance

        //  Update target & source balance
        targetAccountPojo.setBalance(targetBalance);
        sourceAccountPojo.setBalance(sourceBalance);

    }

    public static void transferBalance(CreditCardPojo creditCardPojo, AccountPojo targetAccountPojo, BigDecimal amount) {

        BigDecimal sourceBalance = creditCardPojo.getBalance();
        BigDecimal targetBalance = targetAccountPojo.getBalance();

        targetBalance = targetBalance.add(amount);          //  Add Amount to Target Balance
        sourceBalance = sourceBalance.subtract(amount);     //  Deduct Amount from Source Balance

        //  Update target & source balance
        targetAccountPojo.setBalance(targetBalance);
        creditCardPojo.setBalance(sourceBalance);

    }

    public static void transferBalance(AccountPojo sourceAccountPojo, CreditCardPojo creditCardPojo, BigDecimal amount) {

        BigDecimal sourceBalance = creditCardPojo.getBalance();
        BigDecimal targetBalance = sourceAccountPojo.getBalance();

        targetBalance = targetBalance.add(amount);          //  Add Amount to Target Balance
        sourceBalance = sourceBalance.subtract(amount);     //  Deduct Amount from Source Balance

        //  Update target & source balance
        sourceAccountPojo.setBalance(targetBalance);
        creditCardPojo.setBalance(sourceBalance);

    }

    public static void summary(Map<String, AccountPojo> accountMap) {

        System.out.println("\nAccounts Summary:");
        accountMap.forEach((strAccountName, accountPojo) -> System.out.println(strAccountName + " -> " + accountPojo.getBalance()));

    }

    @Override
    public String toString() {
        return "Id: " + id +
                "Account Name: " + name +
                "Balance: " + balance;
    }

    public Integer getAccountId() {
        return id;
    }

    public void setAccountId(Integer id) {
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
}
