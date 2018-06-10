package graded.unit.lostmypetrestapi.service;

import graded.unit.lostmypetrestapi.entity.pets.CatBreed;
import graded.unit.lostmypetrestapi.repository.CatBreedRepository;
import graded.unit.lostmypetrestapi.service.impl.CatBreedServiceImpl;
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
public class CatBreedServiceMockTest {

    @Mock
    private CatBreedRepository repository;

    @InjectMocks
    private CatBreedServiceImpl service;

    //create cat breed objects for tests
    private CatBreed abyssinian = new CatBreed("Abyssinian");
    private CatBreed bengal = new CatBreed("Bengal");

    @Before
    public void setUp() {
        //create mock
        MockitoAnnotations.initMocks(this);

        //set IDs for test objects
        abyssinian.setId(3);
        bengal.setId(4);
    }

    @Test
    public void testGetAllCatBreeds() {
        //given
        given(repository.findAll()).willReturn(Arrays.asList(abyssinian, bengal));

        //calling method under the test
        List<CatBreed> allBreeds = service.getAllCatBreeds();

        //assert respond has 2 objects
        assertThat(allBreeds).hasSize(2);

        //assert
        assertAbyssinianBreedFields(allBreeds.get(0)); //abyssinian
        assertBengalBreedFields(allBreeds.get(1)); //bengal

        //verify that repository was called
        verify(repository, times(1)).findAll();
    }

    @Test
    public void testGetCatBreedByName() {
        //given
        given(repository.findByName(abyssinian.getName())).willReturn(Optional.ofNullable(abyssinian));

        //calling method under the test
        Optional<CatBreed> optCatBreed = service.getCatBreedByName(abyssinian.getName());

        //assert
        assertThat(optCatBreed.isPresent()).isTrue();

        //assert
        assertAbyssinianBreedFields(optCatBreed.orElseGet(null));

        //verify that repository was called
        verify(repository, times(1)).findByName(abyssinian.getName());
    }

    private void assertAbyssinianBreedFields(CatBreed response) {
        compareCatBreedFields(response, abyssinian);
    }

    private void assertBengalBreedFields(CatBreed response) {
        compareCatBreedFields(response, bengal);
    }

    private void compareCatBreedFields(CatBreed response, CatBreed catBreed) {
        assertThat(response.getId()).isEqualTo(catBreed.getId());
        assertThat(response.getId()).isInstanceOf(Integer.class);
        assertThat(response.getName()).isEqualTo(catBreed.getName());
    }
}