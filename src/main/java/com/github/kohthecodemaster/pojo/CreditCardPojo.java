package com.github.kohthecodemaster.pojo;

import com.github.kohthecodemaster.utils.JsonController;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.List;

public class CreditCardPojo {

    @SerializedName("Id")
    Integer id;
    @SerializedName("Last 4 Digits")
    String last4Digits;
    @SerializedName("Card Holder Name")
    String cardHolderName;
    @SerializedName("Card Name")
    String cardName;
    @SerializedName("Max Limit")
    BigDecimal maxLimit;
    @SerializedName("Balance")
    BigDecimal balance;

    public static List<CreditCardPojo> loadCreditCardPojoListFromJson(File jsonFile) {

        Type type = new TypeToken<List<CreditCardPojo>>() {
        }.getType();

        @SuppressWarnings("unchecked")
        List<CreditCardPojo> creditCardPojoList = (List<CreditCardPojo>) JsonController.parseJsonFileToList(jsonFile, type);
        return creditCardPojoList;

    }

    @Override
    public String toString() {
        return "CreditCardPojo{" +
                "id=" + id +
                ", last4Digits='" + last4Digits + '\'' +
                ", cardHolderName='" + cardHolderName + '\'' +
                ", cardName='" + cardName + '\'' +
                ", maxLimit=" + maxLimit +
                ", balance=" + balance +
                '}';
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLast4Digits() {
        return last4Digits;
    }

    public void setLast4Digits(String last4Digits) {
        this.last4Digits = last4Digits;
    }

    public String getCardHolderName() {
        return cardHolderName;
    }

    public void setCardHolderName(String cardHolderName) {
        this.cardHolderName = cardHolderName;
    }

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public BigDecimal getMaxLimit() {
        return maxLimit;
    }

    public void setMaxLimit(BigDecimal maxLimit) {
        this.maxLimit = maxLimit;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}
