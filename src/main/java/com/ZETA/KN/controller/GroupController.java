package com.ZETA.KN.controller;

import com.ZETA.KN.model.Group;
import com.ZETA.KN.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/groups")
public class GroupController {

    private final GroupService groupService;

    @Autowired
    public GroupController(GroupService groupService) {
        this.groupService = groupService;
    }

    // Create a group
    @PostMapping
    public ResponseEntity<Group> createGroup(@RequestBody Group group) {
        Group saved = groupService.createGroup(group);
        return ResponseEntity.ok(saved);
    }
    @DeleteMapping("/delete/{groupName}")
    public ResponseEntity<String> deleteGroup(@PathVariable String groupName) {
        try {
            groupService.deleteGroup(groupName);
            return ResponseEntity.ok("Group deleted successfully.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error deleting group: " + e.getMessage());
        }
    }
    // Get groups that include a given phone number
    @GetMapping("/phone/{phone}")
    public ResponseEntity<List<Group>> getGroupsByPhone(@PathVariable Long phone) {
        List<Group> groups = groupService.getGroupsByPhone(phone);
        return ResponseEntity.ok(groups);
    }

    @PostMapping("/add/{groupName}/{phone}")
    public ResponseEntity<Group> addMemberByPhone(@PathVariable String groupName, @PathVariable Long phone) {
        Group updated = groupService.addMemberByPhone(groupName, phone);
        return ResponseEntity.ok(updated);
    }


    @DeleteMapping("/remove/{groupName}/{phone}")
    public ResponseEntity<Group> removeMemberByPhone(@PathVariable String groupName, @PathVariable Long phone) {
        Group updated = groupService.removeMemberByPhone(groupName, phone);
        return ResponseEntity.ok(updated);
    }

}

