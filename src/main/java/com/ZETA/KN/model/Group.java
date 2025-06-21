package com.ZETA.KN.model;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "GROUP")
public class Group {
    @Id
    private String id;

    private String groupName;
    private List<Long> memberPhones = new ArrayList<>();

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public List<Long> getMemberPhones() {
        return memberPhones;
    }

    public void setMemberPhones(List<Long> memberPhones) {
        this.memberPhones = memberPhones;
    }
}

