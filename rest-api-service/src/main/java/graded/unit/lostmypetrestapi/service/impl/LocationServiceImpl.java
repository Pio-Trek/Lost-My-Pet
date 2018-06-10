package graded.unit.lostmypetrestapi.service.impl;

import graded.unit.lostmypetrestapi.entity.locations.Location;
import graded.unit.lostmypetrestapi.repository.LocationRepository;
import graded.unit.lostmypetrestapi.service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Implementation of {@link LocationService} interface
 *
 * @author Piotr Przechodzki
 * @since 26/03/2018
 */
@Service
public class LocationServiceImpl implements LocationService {

    private final LocationRepository repository;

    @Autowired
    public LocationServiceImpl(LocationRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Location> getAllLocations() {
        return repository.findAll();
    }

    @Override
    public Optional<Location> getLocationByName(String location) {
        return repository.findByName(location);
    }
}
