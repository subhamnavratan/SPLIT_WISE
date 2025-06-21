package com.ZETA.KN.service;

import com.ZETA.KN.model.*;
import com.ZETA.KN.repository.GroupRepository;
import com.ZETA.KN.repository.MembersRepository;
import com.ZETA.KN.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class GroupService {

    private final GroupRepository groupRepository;
    private final UserRepository userRepository;
    private final MembersRepository membersRepository;

    @Autowired
    public GroupService(GroupRepository groupRepository,
                        UserRepository userRepository,
                        MembersRepository membersRepository) {
        this.groupRepository = groupRepository;
        this.userRepository = userRepository;
        this.membersRepository = membersRepository;
    }

    // ✅ Create a new group and add creator to MEMBERS collection
    public Group createGroup(Group group) {
        // Check for duplicate group name
        if (groupRepository.findByGroupName(group.getGroupName()).isPresent()) {
            throw new RuntimeException("Group with this name already exists.");
        }

        // Ensure group has at least one phone number (creator)
        if (group.getMemberPhones() == null || group.getMemberPhones().isEmpty()) {
            throw new RuntimeException("At least one member (creator) must be specified.");
        }

        // Save group
        Group savedGroup = groupRepository.save(group);

        // Get creator phone
        Long creatorPhone = group.getMemberPhones().get(0);

        // Fetch user details
        User user = userRepository.findByPhone(creatorPhone)
                .orElseThrow(() -> new RuntimeException("Creator phone not registered as user."));

        // Prepare creator member entry
        MemberDetail creator = new MemberDetail();
        creator.setName(user.getName());
        creator.setPhone(user.getPhone());
        creator.setNetAmount(0);
        creator.setDetail(new ArrayList<>());

        // Create MEMBERS document
        Members members = new Members(group.getGroupName(), new ArrayList<>(List.of(creator)));
        membersRepository.save(members);

        return savedGroup;
    }

    // ✅ Get all groups a user belongs to
    public List<Group> getGroupsByPhone(Long phone) {
        return groupRepository.findByMemberPhonesContaining(phone);
    }

    // ✅ Add a member to group + MEMBERS collection
    public Group addMemberByPhone(String groupName, Long phone) {
        // Fetch user
        User user = userRepository.findByPhone(phone)
                .orElseThrow(() -> new RuntimeException("User not found. Please sign up first."));

        // Fetch group
        Group group = groupRepository.findByGroupName(groupName)
                .orElseThrow(() -> new RuntimeException("Group not found."));

        // Add phone to group if not already present
        if (!group.getMemberPhones().contains(phone)) {
            group.getMemberPhones().add(phone);
            groupRepository.save(group);
        }

        // Fetch or create MEMBERS document
        Members membersDoc = membersRepository.findByGroupName(groupName)
                .orElse(new Members(groupName, new ArrayList<>()));

        // Add member if not already in MEMBERS doc
        boolean alreadyExists = membersDoc.getMembers().stream()
                .anyMatch(m -> m.getPhone().equals(phone));

        if (!alreadyExists) {
            MemberDetail newMember = new MemberDetail(
                    phone,
                    user.getName(),
                    0,
                    new ArrayList<>()
            );
            membersDoc.getMembers().add(newMember);
            membersRepository.save(membersDoc);
        }

        return group;
    }

    // ✅ Remove a member from group and MEMBERS collection
    public Group removeMemberByPhone(String groupName, Long phone) {
        // Fetch group
        Group group = groupRepository.findByGroupName(groupName)
                .orElseThrow(() -> new RuntimeException("Group not found."));

        // Remove from group memberPhones list
        group.getMemberPhones().remove(phone);
        groupRepository.save(group);

        // Remove from MEMBERS collection
        Members membersDoc = membersRepository.findByGroupName(groupName)
                .orElseThrow(() -> new RuntimeException("Members document not found."));
        membersDoc.getMembers().removeIf(m -> m.getPhone().equals(phone));
        membersRepository.save(membersDoc);

        return group;
    }
    public void deleteGroup(String groupName) {
        // 1. Delete from GROUP collection
        Group group = groupRepository.findByGroupName(groupName)
                .orElseThrow(() -> new RuntimeException("Group not found"));
        groupRepository.delete(group);

        // 2. Delete from MEMBERS collection
        Members membersDoc = membersRepository.findByGroupName(groupName)
                .orElse(null);
        if (membersDoc != null) {
            membersRepository.delete(membersDoc);
        }
    }

}


