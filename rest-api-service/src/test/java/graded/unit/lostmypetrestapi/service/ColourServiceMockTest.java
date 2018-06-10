package graded.unit.lostmypetrestapi.service;

import graded.unit.lostmypetrestapi.entity.pets.Colour;
import graded.unit.lostmypetrestapi.entity.pets.PetColour;
import graded.unit.lostmypetrestapi.repository.ColourRepository;
import graded.unit.lostmypetrestapi.service.impl.ColourServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/*
 * MOCK UNIT TEST FOR COLOUR SERVICE LAYER
 */
public class ColourServiceMockTest {

    @Mock
    private ColourRepository repository;

    @InjectMocks
    private ColourServiceImpl service;

    //create pet colours objects for tests
    private Colour black = new Colour(PetColour.Black);
    private Colour brown = new Colour(PetColour.Brown);

    @Before
    public void setUp() {
        //create mock
        MockitoAnnotations.initMocks(this);

        //set IDs for test objects
        black.setId(1);
        brown.setId(2);
    }

    @Test
    public void testGetAllColours() {

        Set<Colour> colours  = new HashSet<>();
        colours.add(black);
        colours.add(brown);

        //given
        given(repository.findAll()).willReturn(Arrays.asList(black, brown));

        //calling method under the test
        List<Colour> allColours = service.getAllColours();

        //assert respond has 2 objects
        assertThat(allColours).hasSize(2);

        //assert
        assertBlackColourFields(allColours.get(0)); //black
        assertBrownColourFields(allColours.get(1)); //brown

        //verify that repository was called
        verify(repository, times(1)).findAll();
    }

    @Test
    public void testGetColourByName() {
        //given
        given(repository.findByColour(black.getColour())).willReturn(Optional.ofNullable(black));

        //calling method under the test
        Optional<Colour> optColour = service.getColourByName(PetColour.Black);

        //assert
        assertThat(optColour.isPresent()).isTrue();

        //assert
        assertBlackColourFields(optColour.orElseGet(null));

        //verify that repository was called
        verify(repository, times(1)).findByColour(PetColour.Black);
    }

    private void assertBlackColourFields(Colour response) {
        compareColourFields(response, black);
    }

    private void assertBrownColourFields(Colour response) {
        compareColourFields(response, brown);
    }

    private void compareColourFields(Colour response, Colour colour) {
        assertThat(response.getId()).isEqualTo(colour.getId());
        assertThat(response.getId()).isInstanceOf(Integer.class);
        assertThat(response.getColour()).isEqualTo(colour.getColour());
    }
}