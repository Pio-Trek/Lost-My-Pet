package graded.unit.lostmypetrestapi.service;

import graded.unit.lostmypetrestapi.entity.pets.DogBreed;
import graded.unit.lostmypetrestapi.repository.DogBreedRepository;
import graded.unit.lostmypetrestapi.service.impl.DogBreedServiceImpl;
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

/*
 * MOCK UNIT TEST FOR DOG BREED SERVICE LAYER
 */
public class DogBreedServiceMockTest {

    @Mock
    private DogBreedRepository repository;

    @InjectMocks
    private DogBreedServiceImpl service;

    //create dog breed objects for tests
    private DogBreed beagle = new DogBreed("Beagle");
    private DogBreed labrador = new DogBreed("Labrador");

    @Before
    public void setUp() {
        //create mock
        MockitoAnnotations.initMocks(this);

        //set IDs for test objects
        beagle.setId(3);
        labrador.setId(19);
    }

    @Test
    public void testGetAllDogBreeds() {
        //given
        given(repository.findAll()).willReturn(Arrays.asList(beagle, labrador));

        //calling method under the test
        List<DogBreed> allBreeds = service.getAllDogBreeds();

        //assert respond has 2 objects
        assertThat(allBreeds).hasSize(2);

        //assert
        assertBeagleBreedFields(allBreeds.get(0)); //beagle
        assertLabradorBreedFields(allBreeds.get(1)); //labrador

        //verify that repository was called
        verify(repository, times(1)).findAll();
    }

    @Test
    public void testGetDogBreedByName() {
        //given
        given(repository.findByName(labrador.getName())).willReturn(Optional.ofNullable(labrador));

        //calling method under the test
        Optional<DogBreed> optDogBreed = service.getDogBreedByName(labrador.getName());

        //assert
        assertThat(optDogBreed.isPresent()).isTrue();

        //assert
        assertLabradorBreedFields(optDogBreed.orElseGet(null));

        //verify that repository was called
        verify(repository, times(1)).findByName(labrador.getName());
    }

    private void assertBeagleBreedFields(DogBreed response) {
        compareDogBreedFields(response, beagle);
    }

    private void assertLabradorBreedFields(DogBreed response) {
        compareDogBreedFields(response, labrador);
    }

    private void compareDogBreedFields(DogBreed response, DogBreed dogBreed) {
        assertThat(response.getId()).isEqualTo(dogBreed.getId());
        assertThat(response.getId()).isInstanceOf(Integer.class);
        assertThat(response.getName()).isEqualTo(dogBreed.getName());
    }
}