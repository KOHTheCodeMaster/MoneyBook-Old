package com.github.kohthecodemaster.pojo;

import com.github.kohthecodemaster.utils.JsonController;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class CardSwipePojo {

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
    BigDecimal mdr;         //  MDR is inclusive of GST

    /**
     * Converting Card Swipe Pojo List to Transaction Pojo List
     * It adds additional MDR Entries in the form of Transaction Pojo
     */
    public static List<TransactionPojo> processCardSwipePojoList(List<CardSwipePojo> cardSwipeList) {

        List<TransactionPojo> newTransactionPojoList = new ArrayList<>();
        int id = 0;
        String swipeCategory = "Swipe - PayTM";
        String swipeTarget = "PayTM Business Wallet";
        String mdrCategory = "MDR";
        String mdrTarget = "MDR - PayTM";

        for (CardSwipePojo cardSwipePojo : cardSwipeList) {

            TransactionPojo transactionPojo = new TransactionPojo(++id, cardSwipePojo.getDate(),
                    cardSwipePojo.getLast4Digits(), swipeTarget, swipeCategory, cardSwipePojo.getAmount(),
                    cardSwipePojo.getCardHolderName() + " - " + cardSwipePojo.getCardName() +
                    " - xx-" + cardSwipePojo.getLast4Digits()
            );
            newTransactionPojoList.add(transactionPojo);

            //  Additional MDR Transaction Pojo if card swipe pojo has MDR value > 0
            if (cardSwipePojo.getMdr().compareTo(BigDecimal.ZERO) != 0) {

                transactionPojo = new TransactionPojo(++id, cardSwipePojo.getDate(),
                        swipeTarget, mdrTarget, mdrCategory, cardSwipePojo.getMdr(),
                        cardSwipePojo.getCardHolderName() + " - " + cardSwipePojo.getCardName() +
                        " - xx-" + cardSwipePojo.getLast4Digits()
                );
                newTransactionPojoList.add(transactionPojo);
            }

        }

        return newTransactionPojoList;

    }

    public static List<CardSwipePojo> loadCardSwipePojoListFromJson(File jsonFile) {

        Type type = new TypeToken<List<CardSwipePojo>>() {
        }.getType();

        @SuppressWarnings("unchecked")
        List<CardSwipePojo> cardSwipePojoList = (List<CardSwipePojo>) JsonController.parseJsonFileToList(jsonFile, type);
        return cardSwipePojoList;

    }

    @Override
    public String toString() {
        return "CardSwipePojo{" +
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
