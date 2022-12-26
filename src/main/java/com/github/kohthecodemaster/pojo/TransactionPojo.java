package com.github.kohthecodemaster.pojo;

import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;

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
    @SerializedName("Transaction Type")
    TransactionType transactionType;
    @SerializedName("Amount")
    BigDecimal amount;
    @SerializedName("Note")
    String note;

    @Override
    public String toString() {
        return "TransactionPojo{" +
                "transactionId=" + transactionId +
                ", date='" + date + '\'' +
                ", sourceAccount='" + sourceAccount + '\'' +
                ", targetAccount='" + targetAccount + '\'' +
                ", category='" + category + '\'' +
                ", transactionType=" + transactionType +
                ", amount=" + amount +
                ", note='" + note + '\'' +
                '}';
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

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
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
}
