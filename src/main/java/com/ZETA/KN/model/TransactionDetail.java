package com.ZETA.KN.model;

public class TransactionDetail {
    private Integer amount;
    private String description;

    public TransactionDetail() {}

    public TransactionDetail(Integer amount, String description) {
        this.amount = amount;
        this.description = description;
    }

    public Integer getAmount() { return amount; }
    public void setAmount(Integer amount) { this.amount = amount; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}


