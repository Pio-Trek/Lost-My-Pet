package graded.unit.lostmypetrestapi.service.impl;

import graded.unit.lostmypetrestapi.entity.users.Role;
import graded.unit.lostmypetrestapi.entity.users.User;
import graded.unit.lostmypetrestapi.entity.users.UserRole;
import graded.unit.lostmypetrestapi.exception.CustomResponse;
import graded.unit.lostmypetrestapi.repository.UserRepository;
import graded.unit.lostmypetrestapi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Implementation of {@link UserService} interface
 *
 * @author Piotr Przechodzki
 * @since 26/03/2018
 */
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository repository;

    @Autowired
    public UserServiceImpl(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<User> getAllUsers() {
        return repository.findAll();
    }


    @Override
    public Optional<User> getUserById(String id) {
        return repository.findById(id);
    }

    @Override
    public Optional<User> getUserByEmail(String email) {
        return repository.findByEmail(email);
    }

    @Override
    public Optional<User> getUserByConfirmationToken(String confirmationToken) {
        return repository.findByConfirmationToken(confirmationToken);
    }

    @Override
    public ResponseEntity<User> addMember(User member) {
        // Generate random ID number based on UUID
        member.setId(UUID.randomUUID().toString().replace("-", ""));

        // Assign by default Member to User Role
        member.setRoles(userRoles(Role.ROLE_USER));
        return new ResponseEntity<>(repository.save(member), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<User> addAdmin(User admin) {
        // Generate random ID number based on UUID
        admin.setId(UUID.randomUUID().toString().replace("-", ""));

        // Assign by default Administrator to Admin Role
        admin.setRoles(userRoles(Role.ROLE_ADMIN));
        return new ResponseEntity<>(repository.save(admin), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<User> updateMember(User member) {
        member.setRoles(userRoles(Role.ROLE_USER));
        return new ResponseEntity<>(repository.save(member), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<User> updateAdmin(User admin) {
        admin.setRoles(userRoles(Role.ROLE_ADMIN));
        return new ResponseEntity<>(repository.save(admin), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<CustomResponse> deleteUser(String id) {
        this.repository.deleteById(id);
        return new ResponseEntity<>(
                new CustomResponse(new Date(), HttpStatus.OK.value(),
                        "User with ID: '" + id + "' deleted"), HttpStatus.OK);
    }


    private Set<UserRole> userRoles(Role role) {
        UserRole userRole = new UserRole(role.name());
        userRole.setId(role.getId());
        return Stream.of(userRole)
                .collect(Collectors.toCollection(HashSet::new));
    }

}
