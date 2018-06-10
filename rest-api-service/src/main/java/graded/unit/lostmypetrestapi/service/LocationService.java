package graded.unit.lostmypetrestapi.service;

import graded.unit.lostmypetrestapi.entity.locations.Location;

import java.util.List;
import java.util.Optional;

/**
 * Service layer which manages the data of the {@link Location} domain object
 *
 * @author Piotr Przechodzki
 * @since 26/03/2018
 */
public interface LocationService {

    /**
     * Fetch all locations from the database
     *
     * @return List of all {@link Location} objects
     */
    List<Location> getAllLocations();

    /**
     * Fetch a single location by name
     *
     * @param location name string
     * @return Optional value of {@link Location} object
     */
    Optional<Location> getLocationByName(String location);
}
