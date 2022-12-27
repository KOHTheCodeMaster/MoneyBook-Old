package com.github.kohthecodemaster.pojo;

import com.github.kohthecodemaster.utils.JsonController;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.List;

public class TransactionPojo {

    @SerializedName("Id")
    Integer transactionId;
    @SerializedName("Date")
    String date;
    @SerializedName("Source Account")
    String sourceAccount;
    @SerializedName("Target Account")
    String targetAccount;
    @SerializedName("Category")
    String category;
    @SerializedName("Amount")
    BigDecimal amount;
    @SerializedName("Note")
    String note;
    @SerializedName("Credit Card Description")
    String creditCardDescription;
    @SerializedName("Closing Balance")
    BigDecimal closingBalance;

    public TransactionPojo(TransactionPojo transactionPojo) {
        this.transactionId = transactionPojo.getTransactionId();
        this.date = transactionPojo.getDate();
        this.sourceAccount = transactionPojo.getSourceAccount();
        this.targetAccount = transactionPojo.getTargetAccount();
        this.category = transactionPojo.getCategory();
        this.amount = transactionPojo.getAmount();
        this.note = transactionPojo.getNote();
        this.creditCardDescription = transactionPojo.getCreditCardDescription();
        this.closingBalance = transactionPojo.getClosingBalance();
    }

    public static List<TransactionPojo> loadTransactionPojoListFromJson(File jsonFile) {

        Type type = new TypeToken<List<TransactionPojo>>() {
        }.getType();

        @SuppressWarnings("unchecked")
        List<TransactionPojo> transactionPojoList = (List<TransactionPojo>) JsonController.parseJsonFileToList(jsonFile, type);
        return transactionPojoList;

    }

    @Override
    public String toString() {
        return "Transaction Id: " + transactionId +
                "\nDate: " + date +
                "\nSource Account: " + sourceAccount +
                "\nTarget Account: " + targetAccount +
                "\nCategory: " + category +
                "\nAmount: " + amount +
                "\nNote: " + note +
                "\nCredit Card Description: " + creditCardDescription +
                "\nClosing Balance: " + closingBalance +
                "\n";
    }

    public Integer getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Integer transactionId) {
        this.transactionId = transactionId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getSourceAccount() {
        return sourceAccount;
    }

    public void setSourceAccount(String sourceAccount) {
        this.sourceAccount = sourceAccount;
    }

    public String getTargetAccount() {
        return targetAccount;
    }

    public void setTargetAccount(String targetAccount) {
        this.targetAccount = targetAccount;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getCreditCardDescription() {
        return creditCardDescription;
    }

    public void setCreditCardDescription(String creditCardDescription) {
        this.creditCardDescription = creditCardDescription;
    }

    public BigDecimal getClosingBalance() {
        return closingBalance;
    }

    public void setClosingBalance(BigDecimal closingBalance) {
        this.closingBalance = closingBalance;
    }
}
