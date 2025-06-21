package com.ZETA.KN.service;

import com.ZETA.KN.model.MemberDetail;
import com.ZETA.KN.model.Members;
import com.ZETA.KN.model.TransactionDetail;
import com.ZETA.KN.repository.MembersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CalculationService {

    @Autowired
    private MembersRepository membersRepository;

    public int calculateTotal(String groupName) {
        Members group = membersRepository.findByGroupName(groupName)
                .orElseThrow(() -> new RuntimeException("Group not found"));

        return group.getMembers().stream()
                .flatMap(member -> member.getDetail().stream())
                .mapToInt(TransactionDetail::getAmount)
                .sum();
    }

    public double calculateAverage(String groupName) {
        Members group = membersRepository.findByGroupName(groupName)
                .orElseThrow(() -> new RuntimeException("Group not found"));

        List<MemberDetail> members = group.getMembers();
        if (members.isEmpty()) return 0.0;

        int totalAmount = members.stream()
                .flatMap(member -> member.getDetail().stream())
                .mapToInt(TransactionDetail::getAmount)
                .sum();

        return (double) totalAmount / members.size();
    }
    public List<MemberDetail> getMemberDetailsWithNetAmount(String groupName) {
        Members group = membersRepository.findByGroupName(groupName)
                .orElseThrow(() -> new RuntimeException("Group not found"));

        return group.getMembers().stream()
                .map(member -> {
                    int netAmount = member.getDetail().stream()
                            .mapToInt(TransactionDetail::getAmount)
                            .sum();
                    return new MemberDetail(member.getName(), netAmount, member.getDetail());
                })
                .collect(Collectors.toList());
    }

}


