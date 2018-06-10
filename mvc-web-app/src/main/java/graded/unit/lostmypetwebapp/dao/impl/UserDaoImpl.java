package graded.unit.lostmypetwebapp.dao.impl;

import graded.unit.lostmypetwebapp.client.WebClient;
import graded.unit.lostmypetwebapp.dao.UserDao;
import graded.unit.lostmypetwebapp.model.users.User;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Implementation of {@link UserDao} interface
 *
 * @author Piotr Przechodzki
 * @since 26/03/2018
 */
@Repository
public class UserDaoImpl implements UserDao {

    private final String SERVICE_URL = "/users";

    private final WebClient client;

    public UserDaoImpl(WebClient client) {
        this.client = client;
    }

    @Override
    public ResponseEntity<List<User>> fetchAllUsers() {
        return client.getRestTemplate()
                .exchange(
                        SERVICE_URL,
                        HttpMethod.GET, null,
                        new ParameterizedTypeReference<List<User>>() {
                        });
    }

    @Override
    public ResponseEntity<User> fetchUserByEmail(String email) {
        return client.getRestTemplate()
                .getForEntity(SERVICE_URL + "?email=" + email, User.class);
    }

    @Override
    public ResponseEntity<User> findByConfirmationToken(String confirmationToken) {
        return client.getRestTemplate()
                .getForEntity(SERVICE_URL + "?token=" + confirmationToken, User.class);
    }

    @Override
    public ResponseEntity<Boolean> findIsUserExistsWithEmail(String email) {
        return client.getRestTemplate()
                .getForEntity(SERVICE_URL + "/exists/" + email, Boolean.class);
    }

    @Override
    public void setMemberResetPasswordByEmail(String email) {
        client.getRestTemplate().exchange(
                SERVICE_URL + "/member?reset=" + email, HttpMethod.PATCH, null, User.class);
    }


    @Override
    public ResponseEntity<User> insertOrUpdateMember(User member) {
        HttpMethod httpMethod = member.getId() == null ? HttpMethod.POST : HttpMethod.PUT;
        HttpEntity<User> request = new HttpEntity<>(member);
        return client.getRestTemplate().exchange
                (SERVICE_URL + "/member", httpMethod, request, User.class);
    }

    @Override
    public ResponseEntity<User> insertOrUpdateAdmin(User admin) {
        HttpMethod httpMethod = admin.getId() == null ? HttpMethod.POST : HttpMethod.PUT;
        HttpEntity<User> request = new HttpEntity<>(admin);
        return client.getRestTemplate().exchange
                (SERVICE_URL + "/admin", httpMethod, request, User.class);
    }

    @Override
    public void removeUser(String id) {
        this.client.getRestTemplate().exchange
                (SERVICE_URL + "/" + id, HttpMethod.DELETE, null, String.class);
    }
}
