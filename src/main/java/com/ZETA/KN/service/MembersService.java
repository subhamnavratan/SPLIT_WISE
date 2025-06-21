package com.ZETA.KN.service;

import com.ZETA.KN.model.*;
import com.ZETA.KN.repository.GroupRepository;
import com.ZETA.KN.repository.MembersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class MembersService {

    @Autowired
    private MembersRepository membersRepository;

    @Autowired
    private GroupRepository groupRepository;

    // ✅ Get all members of a group from MEMBERS collection
    public List<MemberDetail> getMembersByGroup(String groupName) {
        Optional<Members> optionalMembers = membersRepository.findByGroupName(groupName);
        return optionalMembers.map(Members::getMembers).orElse(List.of());
    }

    // ✅ Add a new member to both MEMBERS and GROUP collections
    public void addMember(String groupName, MemberDetail memberDetail) {
        // Step 1: Add to MEMBERS collection
        Optional<Members> optionalMembers = membersRepository.findByGroupName(groupName);
        if (optionalMembers.isPresent()) {
            Members members = optionalMembers.get();
            members.getMembers().add(memberDetail);
            membersRepository.save(members);
        } else {
            Members newMembers = new Members(groupName, List.of(memberDetail));
            membersRepository.save(newMembers);
        }

        // Step 2: Add to GROUP's memberPhones list
        Optional<Group> optionalGroup = groupRepository.findByGroupName(groupName);
        if (optionalGroup.isPresent()) {
            Group group = optionalGroup.get();
            if (!group.getMemberPhones().contains(memberDetail.getPhone())) {
                group.getMemberPhones().add(memberDetail.getPhone());
                groupRepository.save(group);
            }
        } else {
            throw new RuntimeException("Group not found while syncing memberPhones list");
        }
    }

    // ✅ Delete a member from MEMBERS collection and keep GROUP data untouched
    public boolean deleteMember(String groupName, Long phone) {
        Optional<Members> optionalMembers = membersRepository.findByGroupName(groupName);
        if (optionalMembers.isPresent()) {
            Members members = optionalMembers.get();
            List<MemberDetail> updatedList = members.getMembers().stream()
                    .filter(m -> !m.getPhone().equals(phone))
                    .toList();
            members.setMembers(updatedList);
            membersRepository.save(members);
            return true;
        }
        return false;
    }

    // ✅ Controller helper method: add payment with validation
    public boolean addPayment(String groupName, Long phone, AddAmount addAmount) {
        try {
            TransactionDetail transaction = new TransactionDetail(
                    addAmount.getAmount(),
                    addAmount.getDescription()
            );
            addPaymentToMember(groupName, phone, transaction);
            return true;
        } catch (Exception e) {
            System.err.println("Payment failed: " + e.getMessage());
            return false;
        }
    }

    // ✅ Core method to update a member’s net amount and transaction history
    public Members addPaymentToMember(String groupName, Long phone, TransactionDetail transaction) {
        // Step 1: Check if group exists
        Group group = groupRepository.findByGroupName(groupName)
                .orElseThrow(() -> new RuntimeException("Group not found"));

        // Step 2: Validate phone is part of GROUP
        if (!group.getMemberPhones().contains(phone)) {
            throw new RuntimeException("Phone " + phone + " is not part of group " + groupName);
        }

        // Step 3: Fetch MEMBERS document
        Members membersDoc = membersRepository.findByGroupName(groupName)
                .orElseThrow(() -> new RuntimeException("Members document not found"));

        // Step 4: Find matching member
        MemberDetail member = membersDoc.getMembers().stream()
                .filter(m -> m.getPhone().equals(phone))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Member not found in this group."));

        // Step 5: Ensure transaction list exists
        if (member.getDetail() == null) {
            member.setDetail(new ArrayList<>());
        }

        // Step 6: Add transaction and update netAmount
        member.getDetail().add(transaction);
        member.setNetAmount(member.getNetAmount() + transaction.getAmount());

        // Step 7: Save updated MEMBERS doc
        return membersRepository.save(membersDoc);
    }
}



