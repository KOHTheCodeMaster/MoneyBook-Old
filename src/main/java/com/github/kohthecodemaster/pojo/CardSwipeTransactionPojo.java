package com.github.kohthecodemaster.pojo;

import com.github.kohthecodemaster.utils.JsonController;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.List;

public class CardSwipeTransactionPojo {

    @SerializedName("Id")
    Integer id;
    @SerializedName("Date")
    String date;
    @SerializedName("Last 4 Digits")
    String last4Digits;
    @SerializedName("Card Holder Name")
    String cardHolderName;
    @SerializedName("Card Name")
    String cardName;
    @SerializedName("Amount")
    BigDecimal amount;
    @SerializedName("MDR")
    BigDecimal mdr; //  MDR is inclusive of GST

    public static List<CardSwipeTransactionPojo> loadCardSwipeTransactionPojoListFromJson(File jsonFile) {

        Type type = new TypeToken<List<CardSwipeTransactionPojo>>() {
        }.getType();

        @SuppressWarnings("unchecked")
        List<CardSwipeTransactionPojo> cardSwipeTransactionPojoList = (List<CardSwipeTransactionPojo>) JsonController.parseJsonFileToList(jsonFile, type);
        return cardSwipeTransactionPojoList;

    }

    public static void updateTransactionPojoList(List<TransactionPojo> transactionPojoList) {

    }

    @Override
    public String toString() {
        return "CardSwipeTransactionPojo{" +
                "id=" + id +
                ", date='" + date + '\'' +
                ", last4Digits='" + last4Digits + '\'' +
                ", cardHolderName='" + cardHolderName + '\'' +
                ", cardName='" + cardName + '\'' +
                ", amount=" + amount +
                ", mdr=" + mdr +
                '}';
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
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

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getMdr() {
        return mdr;
    }

    public void setMdr(BigDecimal mdr) {
        this.mdr = mdr;
    }
}
