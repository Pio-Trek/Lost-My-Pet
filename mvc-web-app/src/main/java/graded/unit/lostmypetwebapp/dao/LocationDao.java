package graded.unit.lostmypetwebapp.dao;

import graded.unit.lostmypetwebapp.model.locations.Location;

import java.util.List;

/**
 * Data Access Object interface which manages the data of the {@link Location} domain
 * object from the consume Service.
 *
 * @author Piotr Przechodzki
 * @since 26/03/2018
 */
public interface LocationDao {

    /**
     * Fetch a list of locations.
     *
     * @return List of all {@link Location} objects.
     */
    List<Location> fetchLocations();

}