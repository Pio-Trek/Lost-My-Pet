package graded.unit.lostmypetwebapp.dao.impl;

import graded.unit.lostmypetwebapp.client.WebClient;
import graded.unit.lostmypetwebapp.dao.FoundPostDao;
import graded.unit.lostmypetwebapp.model.posts.Found;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Implementation of {@link FoundPostDao} interface
 *
 * @author Piotr Przechodzki
 * @since 26/03/2018
 */
@Repository
public class FoundPostDaoImpl implements FoundPostDao {

    private final String FOUND_SERVICE_URL = "/announcements/found/";

    private final WebClient client;

    public FoundPostDaoImpl(WebClient client) {
        this.client = client;
    }

    @Override
    public ResponseEntity<List<Found>> fetchAllFoundPosts() {
        return client.getRestTemplate()
                .exchange(
                        FOUND_SERVICE_URL,
                        HttpMethod.GET, null,
                        new ParameterizedTypeReference<List<Found>>() {
                        });
    }

    @Override
    public ResponseEntity<List<Found>> fetchAllFoundPostsByMemberId(String memberId) {
        return client.getRestTemplate()
                .exchange(
                        FOUND_SERVICE_URL + "/member/" + memberId,
                        HttpMethod.GET, null,
                        new ParameterizedTypeReference<List<Found>>() {
                        });
    }

    @Override
    public ResponseEntity<List<Found>> fetchFoundDogsPosts() {
        return client.getRestTemplate()
                .exchange(
                        FOUND_SERVICE_URL + "/dogs",
                        HttpMethod.GET, null,
                        new ParameterizedTypeReference<List<Found>>() {
                        });
    }

    @Override
    public ResponseEntity<List<Found>> fetchFoundCatsPosts() {
        return client.getRestTemplate()
                .exchange(
                        FOUND_SERVICE_URL + "/cats",
                        HttpMethod.GET, null,
                        new ParameterizedTypeReference<List<Found>>() {
                        });
    }

    @Override
    public ResponseEntity<Found> fetchFoundPostById(Long id) {
        return client.getRestTemplate()
                .getForEntity(FOUND_SERVICE_URL + id, Found.class);
    }

    @Override
    public void setFoundPostEnabled(Long id) {
        this.client.getRestTemplate()
                .exchange(FOUND_SERVICE_URL + "enabled/" + id,
                        HttpMethod.GET, null, String.class);
    }

    @Override
    public void insertOrUpdateFoundPost(Found found) {
        HttpMethod httpMethod = found.getId() == null ? HttpMethod.POST : HttpMethod.PUT;
        HttpEntity<Found> request = new HttpEntity<>(found);
        client.getRestTemplate()
                .exchange(FOUND_SERVICE_URL, httpMethod, request, Found.class);
    }

    @Override
    public void removeFoundPost(Long id) {
        this.client.getRestTemplate()
                .exchange(FOUND_SERVICE_URL + id, HttpMethod.DELETE, null, String.class);
    }
}
