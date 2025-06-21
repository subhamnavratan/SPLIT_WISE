package com.ZETA.KN.controller;

import com.ZETA.KN.model.AddAmount;
import com.ZETA.KN.model.MemberDetail;
import com.ZETA.KN.service.MembersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/members-db")
public class MembersController {

    @Autowired
    private MembersService membersService;

    // ✅ GET members of a group
    @GetMapping("/{groupName}")
    public ResponseEntity<List<MemberDetail>> getMembers(@PathVariable String groupName) {
        List<MemberDetail> members = membersService.getMembersByGroup(groupName);
        if (members == null || members.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(members);
    }

    // ✅ POST: Add a new member to MEMBERS + GROUP collections
    @PostMapping("/{groupName}/add")
    public ResponseEntity<String> addMember(@PathVariable String groupName, @RequestBody MemberDetail memberDetail) {
        try {
            membersService.addMember(groupName, memberDetail);
            return ResponseEntity.ok("Member added to group: " + groupName);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error adding member: " + e.getMessage());
        }
    }

    // ✅ DELETE: Remove a member by phone
    @DeleteMapping("/{groupName}/delete/{phone}")
    public ResponseEntity<String> deleteMember(@PathVariable String groupName, @PathVariable Long phone) {
        boolean deleted = membersService.deleteMember(groupName, phone);
        if (deleted) {
            return ResponseEntity.ok("Member deleted from group: " + groupName);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // ✅ POST: Add a payment to a member (via AddAmount)
    @PostMapping("/payment/{groupName}/{phone}")
    public ResponseEntity<String> addPayment(@PathVariable String groupName,
                                             @PathVariable Long phone,
                                             @RequestBody AddAmount addAmount) {
        boolean success = membersService.addPayment(groupName, phone, addAmount);
        if (success) {
            return ResponseEntity.ok("Payment added successfully.");
        } else {
            return ResponseEntity.badRequest().body("Payment failed.");
        }
    }
}







