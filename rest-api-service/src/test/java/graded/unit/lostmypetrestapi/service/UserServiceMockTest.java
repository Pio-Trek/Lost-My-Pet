package graded.unit.lostmypetrestapi.service;

import graded.unit.lostmypetrestapi.entity.locations.Location;
import graded.unit.lostmypetrestapi.entity.users.Role;
import graded.unit.lostmypetrestapi.entity.users.User;
import graded.unit.lostmypetrestapi.entity.users.UserRole;
import graded.unit.lostmypetrestapi.repository.UserRepository;
import graded.unit.lostmypetrestapi.service.impl.UserServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/*
 * MOCK UNIT TEST FOR MEMBER USER SERVICE LAYER
 */
public class UserServiceMockTest {

    @Mock
    private UserRepository repository;

    @InjectMocks
    private UserServiceImpl service;

    //create John object for tests
    private User member = new User(getUUID(), "john@gmail.com", "secret", "John", "Brown", new Location("Burton"));

    //create Mary object for tests
    private User admin = new User(getUUID(), "mary@gmail.com", "very-secret", "Mary", null, new Location("Oxenfurt"));

    @Before
    public void setUp() {
        //create mock
        MockitoAnnotations.initMocks(this);

        //assign user role to objects
        member.setRoles(userRoles(Role.ROLE_USER));
        admin.setRoles(userRoles(Role.ROLE_ADMIN));
    }

    @Test
    public void testGetAllUsers() {
        //given
        given(repository.findAll()).willReturn(Arrays.asList(member, admin));

        //calling method under the test
        List<User> allUsers = service.getAllUsers();

        //assert respond has 2 objects
        assertThat(allUsers).hasSize(2);

        //assert
        assertMemberFields(allUsers.get(0));
        assertAdminFields(allUsers.get(1));

        //verify that repository was called
        verify(repository, times(1)).findAll();
    }

    @Test
    public void testGetUserByConfirmationToken(){
        String token = UUID.randomUUID().toString().replace("-", "");

        //given
        given(repository.findByConfirmationToken(token)).willReturn(Optional.ofNullable(member));

        //calling method under the test
        Optional<User> optMember = service.getUserByConfirmationToken(token);

        //assert
        assertThat(optMember.isPresent()).isTrue();

        //assert
        assertMemberFields(optMember.orElseGet(null));

        //verify that repository was called
        verify(repository, times(1)).findByConfirmationToken(token);
    }

    @Test
    public void testGetUserById() {
        //given
        given(repository.findById(member.getId())).willReturn(Optional.of(member));

        //calling method under the test
        Optional<User> optMember = service.getUserById(member.getId());

        //assert
        assertThat(optMember.isPresent()).isTrue();

        //assert
        assertMemberFields(optMember.orElseGet(null));

        //verify that repository was called
        verify(repository, times(1)).findById(member.getId());
    }


    @Test
    public void testGetUserByEmail() {
        //given
        given(repository.findByEmail(admin.getEmail())).willReturn(Optional.of(admin));

        //calling method under the test
        Optional<User> optMember = service.getUserByEmail(admin.getEmail());

        //assert
        assertThat(optMember.isPresent()).isTrue();

        //assert
        assertAdminFields(optMember.orElseGet(null));

        //verify that repository was called
        verify(repository, times(1)).findByEmail(admin.getEmail());
    }

    @Test
    public void testAddMember() {
        //given
        given(repository.save(member)).willReturn(member);

        //calling method under the test
        ResponseEntity<User> response = service.addMember(member);

        //assert that HTTP code is 200 and body is not null
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isNotNull();

        //assert
        assertMemberFields(response.getBody());

        //verify that repository was called
        verify(repository, times(1)).save(member);
    }

    @Test
    public void testAddAdmin() {
        //given
        given(repository.save(admin)).willReturn(admin);

        //calling method under the test
        ResponseEntity<User> response = service.addAdmin(admin);

        //assert that HTTP code is 200 and body is not null
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isNotNull();

        //assert
        assertAdminFields(response.getBody());

        //verify that repository was called
        verify(repository, times(1)).save(admin);
    }

    @Test
    public void testUpdateMember() {
        //given
        given(repository.findById(member.getId())).willReturn(Optional.ofNullable(member));

        //update Mary with new details
        member.setLastName("Brown");
        member.setEmail("john.b@email.com");

        //given
        given(repository.save(member)).willReturn(member);

        //calling method under the test
        ResponseEntity<User> response = service.updateMember(member);

        //assert that HTTP code is 200 and body is not null
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isNotNull();

        //assert
        assertMemberFields(response.getBody());

        //verify that repository was called
        verify(repository, times(1)).save(member);
    }

    @Test
    public void testUpdateAdmin() {
        //update Mary with new details
        admin.setFirstName("Evans");
        admin.setEmail("mary.evans@email.com");

        //given
        given(repository.save(admin)).willReturn(admin);

        //calling method under the test
        ResponseEntity<User> response = service.updateAdmin(admin);

        //assert that HTTP code is 200 and body is not null
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isNotNull();

        //assert
        assertAdminFields(response.getBody());

        //verify that repository was called
        verify(repository, times(1)).save(admin);
    }

    @Test
    public void testDeleteMember() {
        //calling method under the test
        ResponseEntity<?> response = service.deleteUser(member.getId());

        //assert that HTTP code is 200 and body is not null
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isNotNull();

        //verify that repository was called
        verify(repository, times(1)).deleteById(member.getId());
    }


    //generate UUID
    private String getUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }


    //verify that John object have same fields
    private void assertMemberFields(User response) {
        assertThat(response.getLastName()).isEqualTo(member.getLastName());
        assertThat(response.getRoles().iterator().next().getRole()).isEqualTo(Role.ROLE_USER.name());
        assertThat(response.getLocation()).isEqualTo(member.getLocation());
        compareUserFields(response, member);
    }

    //verify that Mary object have same fields
    private void assertAdminFields(User response) {
        assertThat(response.getRoles().iterator().next().getId()).isEqualTo(Role.ROLE_ADMIN.getId());
        assertThat(response.getLastName()).isNull();
        assertThat(response.getLocation()).isNotNull();
        compareUserFields(response, admin);
    }

    private void compareUserFields(User response, User user) {
        assertThat(response.getId().length()).isEqualTo(32);
        assertThat(response.getFirstName()).isEqualTo(user.getFirstName());
        assertThat(response.getEmail()).isEqualTo(user.getEmail());
        assertThat(response.getPassword()).isEqualTo(user.getPassword());
    }

    private Set<UserRole> userRoles(Role role) {
        UserRole userRole = new UserRole(role.name());
        userRole.setId(role.getId());
        return Stream.of(userRole)
                .collect(Collectors.toCollection(HashSet::new));
    }

}