package graded.unit.lostmypetwebapp.dao.impl;

import graded.unit.lostmypetwebapp.client.WebClient;
import graded.unit.lostmypetwebapp.dao.LostPostDao;
import graded.unit.lostmypetwebapp.model.posts.Lost;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Implementation of {@link LostPostDao} interface
 *
 * @author Piotr Przechodzki
 * @since 26/03/2018
 */
@Repository
public class LostPostDaoImpl implements LostPostDao {

    private final String LOST_SERVICE_URL = "/announcements/lost/";

    private final WebClient client;

    public LostPostDaoImpl(WebClient client) {
        this.client = client;
    }

    @Override
    public ResponseEntity<List<Lost>> fetchAllLostPosts() {
        return client.getRestTemplate()
                .exchange(
                        LOST_SERVICE_URL,
                        HttpMethod.GET, null,
                        new ParameterizedTypeReference<List<Lost>>() {
                        });
    }

    @Override
    public ResponseEntity<List<Lost>> fetchAllLostPostsByMemberId(String memberId) {
        return client.getRestTemplate()
                .exchange(
                        LOST_SERVICE_URL + "/member/" + memberId,
                        HttpMethod.GET, null,
                        new ParameterizedTypeReference<List<Lost>>() {
                        });
    }

    @Override
    public ResponseEntity<List<Lost>> fetchLostDogsPosts() {
        return client.getRestTemplate()
                .exchange(
                        LOST_SERVICE_URL + "/dogs",
                        HttpMethod.GET, null,
                        new ParameterizedTypeReference<List<Lost>>() {
                        });
    }

    @Override
    public ResponseEntity<List<Lost>> fetchLostCatsPosts() {
        return client.getRestTemplate()
                .exchange(
                        LOST_SERVICE_URL + "/cats",
                        HttpMethod.GET, null,
                        new ParameterizedTypeReference<List<Lost>>() {
                        });
    }

    @Override
    public ResponseEntity<Lost> fetchLostPostById(Long id) {
        return client.getRestTemplate()
                .getForEntity(LOST_SERVICE_URL + id, Lost.class);
    }

    @Override
    public void setLostPostEnabled(Long id) {
        this.client.getRestTemplate()
                .exchange(LOST_SERVICE_URL + "enabled/" + id,
                        HttpMethod.GET, null, String.class);
    }

    @Override
    public void insertOrUpdateLostPost(Lost lost) {
        HttpMethod httpMethod = lost.getId() == null ? HttpMethod.POST : HttpMethod.PUT;
        HttpEntity<Lost> request = new HttpEntity<>(lost);
        client.getRestTemplate()
                .exchange(LOST_SERVICE_URL, httpMethod, request, Lost.class);
    }

    @Override
    public void removeLostPost(Long id) {
        this.client.getRestTemplate()
                .exchange(LOST_SERVICE_URL + id, HttpMethod.DELETE, null, String.class);
    }
}
