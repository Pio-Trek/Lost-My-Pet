package graded.unit.lostmypetrestapi.service;

import graded.unit.lostmypetrestapi.entity.locations.Location;
import graded.unit.lostmypetrestapi.entity.pets.Cat;
import graded.unit.lostmypetrestapi.entity.pets.Dog;
import graded.unit.lostmypetrestapi.entity.pets.PetGender;
import graded.unit.lostmypetrestapi.entity.posts.Lost;
import graded.unit.lostmypetrestapi.entity.users.User;
import graded.unit.lostmypetrestapi.repository.LostPostRepository;
import graded.unit.lostmypetrestapi.service.impl.LostPostServiceImpl;
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
 * MOCK UNIT TEST FOR LOST PET ANNOUNCEMENT SERVICE LAYER
 */
public class LostPostServiceMockTest {

    @Mock
    private LostPostRepository repository;

    @InjectMocks
    private LostPostServiceImpl service;

    private User member = new User("member-id", "password", "member@email.com", "Test", "Member", new Location("Oxenfurt"));

    //create Lost Announcement with Dog object for tests
    private Dog dog = new Dog("Max", PetGender.Male, 9, true, false, null, "Test lost dog description", null, null);
    private Lost lostDog = new Lost(new Location("Oxenfurt"), member, dog, new Date());

    //create Lost Announcement with Cat object for tests
    private Cat cat = new Cat("Misty", PetGender.Female, 3, true, false, null, "Test lost cat description", null, null);
    private Lost lostCat = new Lost(new Location("East Tenpenny"), member, cat, new Date());

    @Before
    public void setUp() {
        //create mock
        MockitoAnnotations.initMocks(this);

        //set IDs for test objects
        lostDog.setId(1L);
        lostCat.setId(2L);

        //set 'added date' for test objects
        lostDog.setAddedDate(new Date());
        lostCat.setAddedDate(new Date());
    }

    @Test
    public void testGetAllLostPosts() {
        //give
        given(repository.findAll()).willReturn(Arrays.asList(lostDog, lostCat));

        //calling method under the test
        List<Lost> allLostPosts = service.getAllLostPosts();

        //assert respond has 2 objects
        assertThat(allLostPosts).hasSize(2);

        //assert
        assertLostDogFields(allLostPosts.get(0)); //dog
        assertLostCatFields(allLostPosts.get(1)); //cat

        //verify that repository was called
        verify(repository, times(1)).findAll();
    }

    @Test
    public void testGetLostPostMemberId() {
        //given
        given(repository.findAllByMemberId("test-id-user1")).willReturn(Arrays.asList(lostDog, lostCat));

        //calling method under the test
        List<Lost> allLostPostsByMemberId = service.getAllLostPostsByMemberId("test-id-user1");

        //assert
        assertThat(allLostPostsByMemberId).hasSize(2);

        //assert
        assertLostDogFields(allLostPostsByMemberId.get(0)); //dog
        assertLostCatFields(allLostPostsByMemberId.get(1)); //cat

        //verify that repository was called
        verify(repository, times(1)).findAllByMemberId("test-id-user1");
    }


    @Test
    public void testGetLostPostById() {
        //given
        given(repository.findById(lostDog.getId())).willReturn(Optional.of(lostDog));

        //calling method under the test
        Optional<Lost> optFound = service.getLostPostById(lostDog.getId());

        //assert is present
        assertThat(optFound.isPresent()).isTrue();

        //assert fields
        assertLostDogFields(optFound.orElseGet(null));

        //verify that repository was called
        verify(repository, times(1)).findById(lostDog.getId());
    }

    @Test
    public void testAddLostPost() {
        //given
        given(repository.save(lostCat)).willReturn(lostCat);

        //calling method under the test
        ResponseEntity<Lost> response = service.addLostPost(lostCat);

        //assert that HTTP code is 200 and body is not null
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isNotNull();

        //assert
        assertLostCatFields(response.getBody());

        //verify that repository was called
        verify(repository, times(1)).save(lostCat);
    }

    @Test
    public void testUpdateLostPost() {
        //given
        given(repository.findById(lostDog.getId())).willReturn(Optional.ofNullable(lostDog));

        //update Lost Dog Announcement with new details
        lostDog.setLocation(new Location("Chamberlain Hills"));

        //given
        given(repository.save(lostDog)).willReturn(lostDog);

        //calling method under the test
        ResponseEntity<Lost> response = service.updateLostPost(lostDog, lostDog.getAddedDate());

        //assert that HTTP code is 200 and body is not null
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isNotNull();

        //assert
        assertLostDogFields(response.getBody());

        //verify that repository was called
        verify(repository, times(1)).save(lostDog);

    }

    @Test
    public void testDeleteLostPost() {
        //calling method under the test
        ResponseEntity<?> response = service.deleteLostPost(lostCat.getId());

        //assert that HTTP code is 200 and body is not null
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().toString()).contains("Lost announcement deleted");

        //verify that repository was called
        verify(repository, times(1)).deleteById(lostCat.getId());
    }

    @Test
    public void testCountByEnabledFalse() {
        //given
        given(repository.countByEnabledFalse()).willReturn(1);

        //calling method under the test
        int countByEnabledFalse = service.countByEnabledFalse();

        //assert that HTTP code is 200 and body is not null
        assertThat(countByEnabledFalse).isEqualTo(1);

        //verify that repository was called
        verify(repository, times(1)).countByEnabledFalse();
    }


    private void assertLostDogFields(Lost response) {
        assertThat(response.getPet()).isInstanceOf(Dog.class);
        compareLostPetFields(response, lostDog);
    }

    private void assertLostCatFields(Lost response) {
        assertThat(response.getPet()).isInstanceOf(Cat.class);
        compareLostPetFields(response, lostCat);
    }

    private void compareLostPetFields(Lost response, Lost pet) {
        assertThat(response.getId()).isEqualTo(pet.getId());
        assertThat(response.getAddedDate()).isToday();
        assertThat(response.getLocation().getName()).isEqualTo(pet.getLocation().getName());
        assertThat(response.getPet()).isNotNull();
        assertThat(response.getPet().getName()).isEqualTo(pet.getPet().getName());
        assertThat(response.getPet().getGender()).isEqualTo(pet.getPet().getGender());
        assertThat(response.getPet().getAge()).isEqualTo(pet.getPet().getAge());
        assertThat(response.getPet().getChipped()).isTrue();
        assertThat(response.getPet().getCollar()).isFalse();
        assertThat(response.getPet().getDescription()).isEqualTo(pet.getPet().getDescription());
        assertThat(response.getPet().getColours()).isNull();
        assertThat(response.getLostDate()).isToday();
    }
}