package com.github.kohthecodemaster.pojo;

import com.github.kohthecodemaster.utils.JsonController;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.List;

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

    public void addAmount(BigDecimal amount) {
        balance = balance.add(amount);
    }

    @Override
    public String toString() {
        return "AccountPojo{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", balance=" + balance +
                '}';
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
