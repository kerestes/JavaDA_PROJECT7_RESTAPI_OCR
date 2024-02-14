package com.nnk.springboot.integration;

import com.nnk.springboot.domain.CustomUserDetails;
import com.nnk.springboot.domain.User;
import com.nnk.springboot.services.CustomUserDetailsService;
import com.nnk.springboot.services.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.Optional;

@SpringBootTest
@AutoConfigureMockMvc(addFilters=false)
@ActiveProfiles("test")

public class UserServiceTest {
    @Autowired
    private UserService userService;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Test
    @Sql("/banco_scripts/user.sql")
    @Order(1)
    public void userIT() {

        // Find All Test

        List<User> users = userService.findAll();

        User user1 = users.get(0);
        User user2 = users.get(1);

        if (user1.getId() != 1) {
            user1 = users.get(1);
            user2 = users.get(0);
        }

        Assertions.assertEquals("Administrator", user1.getFullname());
        Assertions.assertEquals("User", user2.getFullname());
        Assertions.assertEquals("ADMIN", user1.getRole());
        Assertions.assertEquals("USER", user2.getRole());

        Assertions.assertTrue(users.size() == 2);

        //Save Test

        User user = new User();
        user.setFullname("Test Test");
        user.setUsername("test");
        user.setPassword(new BCryptPasswordEncoder().encode("abcd1234"));
        user.setRole("USER");

        User userResponse = userService.save(user);
        Assertions.assertNotNull(userResponse.getId());
        Assertions.assertEquals(user.getRole(), userResponse.getRole());
        Assertions.assertEquals(user.getFullname(), userResponse.getFullname());

        //FindById Test

        Optional<User> optionalUser = userService.findById(3);

        Assertions.assertTrue(optionalUser.isPresent());
        Assertions.assertEquals("test", optionalUser.get().getUsername());
        Assertions.assertNotNull(optionalUser.get().getPassword());
        Assertions.assertNotEquals("abcd1234", optionalUser.get().getPassword());

        // Delete Test

        userService.deleteById(3);

        optionalUser = userService.findById(3);

        Assertions.assertTrue(optionalUser.isEmpty());
    }

    @Test
    @Order(2)
    public void customUserDetailsServiceErrorTest(){
        Exception exception = Assertions.assertThrows(UsernameNotFoundException.class, () -> {
           customUserDetailsService.loadUserByUsername(" ");
        });

        Assertions.assertEquals("User not found", exception.getMessage());
    }

    @Test
    @Order(3)
    public void customUserDetailsServiceTest(){
        UserDetails customUserDetails = customUserDetailsService.loadUserByUsername("user");

        Assertions.assertNotNull(customUserDetails);
        Assertions.assertEquals("$2a$10$6wCyKv.v/Zj/4dS5B.Ew6.2oFKk6vvTANd43UdYTgmHFSf82pOV6i", customUserDetails.getPassword());
    }
}
