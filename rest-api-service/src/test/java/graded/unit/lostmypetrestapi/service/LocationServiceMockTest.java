package graded.unit.lostmypetrestapi.service;

import graded.unit.lostmypetrestapi.entity.locations.Location;
import graded.unit.lostmypetrestapi.repository.LocationRepository;
import graded.unit.lostmypetrestapi.service.impl.LocationServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class LocationServiceMockTest {

    @Mock
    private LocationRepository repository;

    @InjectMocks
    private LocationServiceImpl service;

    private Location vinewood = new Location("West Vinewood");
    private Location chamberlain = new Location("Chamberlain Hills");

    @Before
    public void setUp() {
        //create mock
        MockitoAnnotations.initMocks(this);

        //set IDs for test objects
        vinewood.setId(1);
        chamberlain.setId(2);
    }

    @Test
    public void testGetAllLocations() {
        given(repository.findAll()).willReturn(Arrays.asList(vinewood, chamberlain));

        //calling method under the test
        List<Location> allLocations = service.getAllLocations();

        //assert respond has 2 objects
        assertThat(allLocations).hasSize(2);

        //assert
        assertVinewoodLocationFields(allLocations.get(0)); //vinewood
        assertChamberlainLocationFields(allLocations.get(1)); //chamberlain

        //verify that repository was called
        verify(repository, times(1)).findAll();
    }

    @Test
    public void testGetLocationByName() {
        //given
        given(repository.findByName(chamberlain.getName())).willReturn(Optional.ofNullable(chamberlain));

        //calling method under the test
        Optional<Location> optLocation = service.getLocationByName(chamberlain.getName());

        //assert
        assertThat(optLocation.isPresent()).isTrue();

        //assert
        assertChamberlainLocationFields(optLocation.orElseGet(null));

        //verify that repository was called
        verify(repository, times(1)).findByName(chamberlain.getName());
    }

    private void assertVinewoodLocationFields(Location response) {
        compareLocationFields(response, vinewood);
    }

    private void assertChamberlainLocationFields(Location response) {
        compareLocationFields(response, chamberlain);
    }

    private void compareLocationFields(Location response, Location location) {
        assertThat(response.getId()).isEqualTo(location.getId());
        assertThat(response.getId()).isInstanceOf(Integer.class);
        assertThat(response.getName()).isEqualTo(location.getName());

    }
}