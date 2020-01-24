package com.easybank.security;

import com.easybank.Const;
import com.easybank.model.User;
import com.easybank.repository.RoleRepository;
import com.easybank.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class DataInit implements ApplicationListener<ContextRefreshedEvent> {

    final UserRepository userRepository;

    final RoleRepository roleRepository;

    final PasswordEncoder passwordEncoder;

    @Autowired
    public DataInit(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent arg0) {
        List<User> users = userRepository.findAll();
        if (users.isEmpty()) {
            createUser("Manager", "manager", passwordEncoder.encode("manager"), Const.ROLE_MANAGER);
            createUser("Client", "client", passwordEncoder.encode("client"), Const.ROLE_CLIENT);
        }
    }

    public void createUser(String name, String email, String password, String roleName) {
        com.easybank.model.Role role = new com.easybank.model.Role(roleName);
        this.roleRepository.save(role);
        User user = new User(name, email, password, Collections.singletonList(role));
        userRepository.save(user);
    }

}

