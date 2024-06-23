package com.project.demo.logic.entity.rol;

import com.project.demo.logic.entity.user.User;
import com.project.demo.logic.entity.user.UserRepository;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

@Component
public class AdminSeeder implements ApplicationListener<ContextRefreshedEvent> {
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;


    public AdminSeeder(
            RoleRepository roleRepository,
            UserRepository  userRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        this.loadRoles();
        this.createSuperAdministrator();
        this.createUser();
    }

    private void loadRoles() {
        RoleEnum[] roleNames = new RoleEnum[] { RoleEnum.USER, RoleEnum.SUPER_ADMIN_ROLE};
        Map<RoleEnum, String> roleDescriptionMap = Map.of(
                RoleEnum.USER, "Default user role",
                RoleEnum.SUPER_ADMIN_ROLE, "Super Administrator role"
        );

        Arrays.stream(roleNames).forEach((roleName) -> {
            Optional<Role> optionalRole = roleRepository.findByName(roleName);

            optionalRole.ifPresentOrElse(System.out::println, () -> {
                Role roleToCreate = new Role();

                roleToCreate.setName(roleName);
                roleToCreate.setDescription(roleDescriptionMap.get(roleName));

                roleRepository.save(roleToCreate);
            });
        });
    }

    private void createSuperAdministrator() {
        User superAdmin = new User();
        superAdmin.setName("Super");
        superAdmin.setLastname("Admin");
        superAdmin.setEmail("super.admin@gmail.com");
        superAdmin.setPassword("superadmin123");

        Optional<Role> optionalRole = roleRepository.findByName(RoleEnum.SUPER_ADMIN_ROLE);
        Optional<User> optionalUser = userRepository.findByEmail(superAdmin.getEmail());

        if (optionalRole.isEmpty() || optionalUser.isPresent()) {
            return;
        }

        var user = new User();
        user.setName(superAdmin.getName());
        user.setLastname(superAdmin.getLastname());
        user.setEmail(superAdmin.getEmail());
        user.setPassword(passwordEncoder.encode(superAdmin.getPassword()));
        user.setRole(optionalRole.get());

        userRepository.save(user);
    }

    private void createUser() {
        User newUser = new User();
        newUser.setName("User");
        newUser.setLastname("Regular");
        newUser.setEmail("user@gmail.com");
        newUser.setPassword("user123");

        Optional<Role> optionalRole = roleRepository.findByName(RoleEnum.USER);
        Optional<User> optionalUser = userRepository.findByEmail(newUser.getEmail());

        if (optionalRole.isEmpty() || optionalUser.isPresent()) {
            return;
        }

        var user = new User();
        user.setName(newUser.getName());
        user.setLastname(newUser.getLastname());
        user.setEmail(newUser.getEmail());
        user.setPassword(passwordEncoder.encode(newUser.getPassword()));
        user.setRole(optionalRole.get());

        userRepository.save(user);
    }

}
