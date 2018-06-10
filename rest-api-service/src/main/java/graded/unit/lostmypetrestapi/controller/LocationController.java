package graded.unit.lostmypetrestapi.controller;

import graded.unit.lostmypetrestapi.entity.locations.Location;
import graded.unit.lostmypetrestapi.service.LocationService;
import org.jsondoc.core.annotation.Api;
import org.jsondoc.core.annotation.ApiMethod;
import org.jsondoc.core.pojo.ApiStage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controller layer class for {@link Location} which exposes resources,
 * read operation and business logic for the {@link LocationService}.
 *
 * @author Piotr Przechodzki
 * @since 26/03/2018
 */
@RestController
@RequestMapping("/api/locations")
@Api(
        name = "Locations API",
        description = "Provides a method that fetch locations.",
        stage = ApiStage.GA)
public class LocationController {

    // URI address for testing purpose
    static String URI = "/api/locations/";

    private final LocationService locationService;

    @Autowired
    public LocationController(LocationService locationService) {
        this.locationService = locationService;
    }

    /**
     * HTTP GET request method.
     * Fetch all locations from the database.
     *
     * @return List of all {@link Location} objects.
     */
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiMethod(description = "Fetch a list of locations.")
    public List<Location> fetchAllLocations() {
        return locationService.getAllLocations();
    }
}
