package com.nnk.springboot.services;

import com.nnk.springboot.domain.User;
import com.nnk.springboot.repositories.UserRepository;
import jakarta.validation.constraints.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public List<User> findAll(){ return userRepository.findAll(); }

    public User save(User user){ return userRepository.save(user); }

    public Optional<User> findById(Integer id){ return userRepository.findById(id); }

    public void deleteById(Integer id){ userRepository.deleteById(id); }
}
