package graded.unit.lostmypetrestapi.service;

import graded.unit.lostmypetrestapi.entity.locations.Location;
import graded.unit.lostmypetrestapi.entity.pets.Cat;
import graded.unit.lostmypetrestapi.entity.pets.Dog;
import graded.unit.lostmypetrestapi.entity.pets.PetGender;
import graded.unit.lostmypetrestapi.entity.posts.Found;
import graded.unit.lostmypetrestapi.repository.FoundPostRepository;
import graded.unit.lostmypetrestapi.service.impl.FoundPostServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/*
 * MOCK UNIT TEST FOR FOUND PET ANNOUNCEMENT SERVICE LAYER
 */
public class FoundPostServiceMockTest {

    @Mock
    private FoundPostRepository repository;

    @InjectMocks
    private FoundPostServiceImpl service;

    //create Found Announcement with Dog object for tests
    private Dog dog = new Dog(null, PetGender.Male, 0, null, null, null, "Test found dog description", null, null);
    private Found foundDog = new Found(new Location("Oxenfurt"), null, dog, new Date());

    //create Found Announcement with Cat object for tests
    private Cat cat = new Cat(null, PetGender.Female, 0, null, null, null, "Test found cat description", null, null);
    private Found foundCat = new Found(new Location("East Tenpenny"), null, cat, new Date());


    @Before
    public void setUp() {
        //create mock
        MockitoAnnotations.initMocks(this);

        //set IDs for test objects
        foundDog.setId(1L);
        foundCat.setId(2L);

        //set 'added date' for test objects
        foundDog.setAddedDate(new Date());
        foundCat.setAddedDate(new Date());
    }

    @Test
    public void testGetAllFoundPosts() {
        //give
        given(repository.findAll()).willReturn(Arrays.asList(foundDog, foundCat));

        //calling method under the test
        List<Found> allFoundPosts = service.getAllFoundPosts();

        //assert respond has 2 objects
        assertThat(allFoundPosts).hasSize(2);

        //assert
        assertFoundDogFields(allFoundPosts.get(0)); //dog
        assertFoundCatFields(allFoundPosts.get(1)); //cat

        //verify that repository was called
        verify(repository, times(1)).findAll();
    }

    @Test
    public void testGetFoundPostMemberId() {
        //given
        given(repository.findAllByMemberId("test-id-user2")).willReturn(Arrays.asList(foundDog, foundCat));

        //calling method under the test
        List<Found> allFoundPostsByMemberId = service.getAllFoundPostsByMemberId("test-id-user2");

        //assert
        assertThat(allFoundPostsByMemberId).hasSize(2);

        //assert
        assertFoundDogFields(allFoundPostsByMemberId.get(0)); //dog
        assertFoundCatFields(allFoundPostsByMemberId.get(1)); //cat

        //verify that repository was called
        verify(repository, times(1)).findAllByMemberId("test-id-user2");
    }

    @Test
    public void testGetFoundPostById() {
        //given
        given(repository.findById(foundDog.getId())).willReturn(Optional.of(foundDog));

        //calling method under the test
        Optional<Found> optFound = service.getFoundPostById(foundDog.getId());

        //assert
        assertThat(optFound.isPresent()).isTrue();

        //assert
        assertFoundDogFields(optFound.orElseGet(null));

        //verify that repository was called
        verify(repository, times(1)).findById(foundDog.getId());
    }

    @Test
    public void testAddFoundPost() {
        //given
        given(repository.save(foundCat)).willReturn(foundCat);

        //calling method under the test
        ResponseEntity<Found> response = service.addFoundPost(foundCat);

        //assert that HTTP code is 200 and body is not null
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isNotNull();

        //assert
        assertFoundCatFields(response.getBody());

        verify(repository, times(1)).save(foundCat);
    }

    @Test
    public void testUpdateFoundPost() {
        //given
        given(repository.findById(foundDog.getId())).willReturn(Optional.ofNullable(foundDog));

        //update Found Dog Announcement with new details
        foundDog.setLocation(new Location("Chamberlain Hills"));

        //given
        given(repository.save(foundDog)).willReturn(foundDog);

        //calling method under the test
        ResponseEntity<Found> response = service.updateFoundPost(foundDog, foundDog.getAddedDate());

        //assert that HTTP code is 200 and body is not null
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isNotNull();

        //assert
        assertFoundDogFields(response.getBody());

        //verify that repository was called
        verify(repository, times(1)).save(foundDog);
    }

    @Test
    public void testDeleteFoundPost() {
        //calling method under the test
        ResponseEntity<?> response = service.deleteFoundPost(foundCat.getId());

        //assert that HTTP code is 200 and body is not null
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().toString()).contains("Found announcement deleted");

        //verify that repository was called
        verify(repository, times(1)).deleteById(foundCat.getId());
    }

    @Test
    public void testCountByEnabledFalse() {
        //given
        given(repository.countByEnabledFalse()).willReturn(2);

        //calling method under the test
        int countByEnabledFalse = service.countByEnabledFalse();

        //assert that HTTP code is 200 and body is not null
        assertThat(countByEnabledFalse).isEqualTo(2);

        //verify that repository was called
        verify(repository, times(1)).countByEnabledFalse();
    }

    private void assertFoundDogFields(Found response) {
        assertThat(response.getPet()).isInstanceOf(Dog.class);
        compareFoundPetFields(response, foundDog);
    }

    private void assertFoundCatFields(Found response) {
        assertThat(response.getPet()).isInstanceOf(Cat.class);
        compareFoundPetFields(response, foundCat);
    }

    private void compareFoundPetFields(Found response, Found pet) {
        assertThat(response.getId()).isEqualTo(pet.getId());
        assertThat(response.getAddedDate()).isToday();
        assertThat(response.getLocation().getName()).isEqualTo(pet.getLocation().getName());
        assertThat(response.getPet()).isNotNull();
        assertThat(response.getPet().getName()).isNull();
        assertThat(response.getPet().getGender()).isEqualTo(pet.getPet().getGender());
        assertThat(response.getPet().getAge()).isEqualTo(pet.getPet().getAge());
        assertThat(response.getPet().getChipped()).isNull();
        assertThat(response.getPet().getCollar()).isNull();
        assertThat(response.getPet().getDescription()).isEqualTo(pet.getPet().getDescription());
        assertThat(response.getPet().getColours()).isNull();
        assertThat(response.getFoundDate()).isToday();
    }

}