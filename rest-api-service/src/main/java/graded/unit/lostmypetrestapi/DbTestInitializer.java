package graded.unit.lostmypetrestapi;

import graded.unit.lostmypetrestapi.controller.*;
import graded.unit.lostmypetrestapi.exception.CustomException;
import javassist.NotFoundException;
import org.springframework.boot.CommandLineRunner;

/**
 * Database initializer helper class that saves sample fake data only for the test purpose.
 * !!! Should be off in production version !!!
 *
 * @author Piotr Przechodzki
 * @since 26/03/2018
 */
//@Component // uncomment for test mode
public class DbTestInitializer implements CommandLineRunner {

    private final LostPostController lostPostController;
    private final FoundPostController foundPostController;
    private final ReportController reportController;
    private final MessageController messageController;
    private final UserController userController;

    public DbTestInitializer(LostPostController lostPostController, FoundPostController foundPostController, ReportController reportController, MessageController messageController, UserController userController) {
        this.lostPostController = lostPostController;
        this.foundPostController = foundPostController;
        this.reportController = reportController;
        this.messageController = messageController;
        this.userController = userController;
    }

    @Override
    public void run(String... args) throws NotFoundException, CustomException {

/*        User member1 = new User("test-id-user1", "user1@email.com", "{noop}password123", "Test1", "User1", new Location("West Vinewood"));

        User member2 = new User("test-id-user2", "user2@email.com", "{noop}password123", "Test2", "User2", new Location("West Vinewood"));

        User member3 = new User("test-id-user3", "user3@email.com", "{noop}password123", "Test3", "User3", new Location("Chamberlain Hills"));


        Dog dog1 = new Dog("Max", PetGender.Male, 2, false, true, null, "Lorem ipsum dolor sit amet, consectetur adipiscing elit.", setColours(), new DogBreed("Chihuahua"));

        Cat cat1 = new Cat("Mickie", PetGender.Male, 3, true, false, null, "Test Text", setColours(), new CatBreed("Other - Mongrel / Crossbreed"));

        Lost dogLostPost1 = new Lost(new Location("West Vinewood"), member1, dog1, new Date());
        Lost catLostPost1 = new Lost(new Location("Oreton"), member1, cat1, new Date());


        lostPostController.insertLostPost(dogLostPost1);
        lostPostController.insertLostPost(catLostPost1);

        lostPostController.setLostPostEnabled(dogLostPost1.getId());
        lostPostController.setLostPostEnabled(catLostPost1.getId());


        Dog dog2 = new Dog(PetGender.Female, true, true, null, "Lorem ipsum dolor sit amet, consectetur adipiscing elit.", setColours(), new DogBreed("Rottweiler"));

        Cat cat2 = new Cat(PetGender.Male, false, false, null, "Test Text", setColours(), new CatBreed("Burmese"));

        Dog dog3 = new Dog(PetGender.Unknown, false, true, null, "Lorem ipsum dolorem...", setColours(), new DogBreed("Lhasa Apso"));

        Found dogFoundPost1 = new Found(new Location("East Tenpenny"), member2, dog2, new Date());
        Found catFoundPost1 = new Found(new Location("West Vinewood"), member2, cat2, new Date());
        Found dogFoundPost2 = new Found(new Location("West Vinewood"), member3, dog3, new Date());

        foundPostController.insertFoundPost(dogFoundPost1);
        foundPostController.insertFoundPost(catFoundPost1);
        foundPostController.insertFoundPost(dogFoundPost2);

        foundPostController.setFoundPostEnabled(dogFoundPost1.getId());
        foundPostController.setFoundPostEnabled(catFoundPost1.getId());


        Report report = new Report(member1, dogFoundPost1, "This announcement is a spam!");
        reportController.insertNotification(report);

    }
    */

    }

/*
    private Set<Colour> setColours() {
        Colour c1 = new Colour(PetColour.White);
        Colour c2 = new Colour(PetColour.Grey);
        return new HashSet<>(Arrays.asList(c1, c2));
    }*/


}
