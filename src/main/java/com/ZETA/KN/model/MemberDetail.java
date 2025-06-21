package com.ZETA.KN.model;

import java.util.ArrayList;
import java.util.List;

public class MemberDetail {
    private Long phone;
    private String name;
    private Integer netAmount;
    private List<TransactionDetail> detail;

    public MemberDetail() {
        this.detail = new ArrayList<>();
        this.netAmount = 0;
    }

    public MemberDetail(Long phone, String name, Integer netAmount, List<TransactionDetail> detail) {
        this.phone = phone;
        this.name = name;
        this.netAmount = netAmount;
        this.detail = detail;
    }

    public MemberDetail(String name, Integer netAmount, List<TransactionDetail> detail) {
        this.name = name;
        this.netAmount = netAmount;
        this.detail = detail;
    }

    public Long getPhone() { return phone; }
    public void setPhone(Long phone) { this.phone = phone; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Integer getNetAmount() { return netAmount; }
    public void setNetAmount(Integer netAmount) { this.netAmount = netAmount; }

    public List<TransactionDetail> getDetail() { return detail; }
    public void setDetail(List<TransactionDetail> detail) { this.detail = detail; }
}




