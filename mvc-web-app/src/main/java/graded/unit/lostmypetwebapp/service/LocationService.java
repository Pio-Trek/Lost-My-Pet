package graded.unit.lostmypetwebapp.service;

import graded.unit.lostmypetwebapp.dao.LocationDao;
import graded.unit.lostmypetwebapp.model.locations.Location;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service layer which manages the data of the {@link Location} domain object
 *
 * @author Piotr Przechodzki
 * @since 26/03/2018
 */
@Service
public class LocationService {

    private final LocationDao locationDao;

    public LocationService(LocationDao locationDao) {
        this.locationDao = locationDao;
    }

    /**
     * Fetch a list of locations.
     *
     * @return List of all {@link Location} objects.
     */
    public List<Location> getLocations() {
        return locationDao.fetchLocations();
    }
}
