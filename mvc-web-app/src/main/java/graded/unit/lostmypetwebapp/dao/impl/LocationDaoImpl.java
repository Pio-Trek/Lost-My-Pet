package graded.unit.lostmypetwebapp.dao.impl;

import graded.unit.lostmypetwebapp.client.WebClient;
import graded.unit.lostmypetwebapp.dao.LocationDao;
import graded.unit.lostmypetwebapp.model.locations.Location;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Implementation of {@link LocationDao} interface
 *
 * @author Piotr Przechodzki
 * @since 26/03/2018
 */
@Repository
public class LocationDaoImpl implements LocationDao {

    private final WebClient client;

    public LocationDaoImpl(WebClient client) {
        this.client = client;
    }

    @Override
    public List<Location> fetchLocations() {
        return client.getRestTemplate()
                .exchange(
                        "/locations/",
                        HttpMethod.GET, null,
                        new ParameterizedTypeReference<List<Location>>() {
                        }).getBody();
    }

}
