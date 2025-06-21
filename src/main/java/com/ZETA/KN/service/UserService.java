package com.ZETA.KN.service;

import com.ZETA.KN.model.User;
import com.ZETA.KN.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public  UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<User> findPhone(Long phone) {
        return userRepository.findByPhone(phone);
    }

    public User save(User user) {
        return userRepository.save(user);
    }


}
