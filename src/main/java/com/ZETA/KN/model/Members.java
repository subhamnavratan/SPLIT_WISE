package com.ZETA.KN.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;

@Document(collection="MEMBERS")
public class Members {
    @Id
    private String id;
    private String groupName;
    private List<MemberDetail> members;

    public Members() {}

    public Members(String groupName, List<MemberDetail> members) {
        this.groupName = groupName;
        this.members = members;
    }

    public String getId() { return id; }

    public String getGroupName() { return groupName; }
    public void setGroupName(String groupName) { this.groupName = groupName; }

    public List<MemberDetail> getMembers() { return members; }
    public void setMembers(List<MemberDetail> members) { this.members = members; }
}



