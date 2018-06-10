package graded.unit.lostmypetwebapp.util;

import graded.unit.lostmypetwebapp.model.forms.AnnouncementFormModel;
import graded.unit.lostmypetwebapp.model.locations.Location;
import graded.unit.lostmypetwebapp.model.pets.*;
import graded.unit.lostmypetwebapp.model.posts.Found;
import graded.unit.lostmypetwebapp.model.posts.Lost;
import graded.unit.lostmypetwebapp.model.users.User;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * This is a helper class used to generate {@link AnnouncementFormModel} object when member what to add or update
 * the post and system need to create the announcement model form.
 * It is responsible for converting the {@link Found} and {@link Lost} object to {@link AnnouncementFormModel} object
 * and vice versa.
 */
public class PostProcessUtil {

    private static byte[] oldPetImage;
    private static Dog dog = new Dog();
    private static Cat cat = new Cat();

    /**
     * This method will create the {@link AnnouncementFormModel} object from the parameters.
     *
     * @param pet       This is the {@link Pet} object form the announcement.
     * @param postId    This is an id number of the announcement.
     * @param eventDate This is the date of lost or found the pet.
     * @param location  This is the location where pet was lost or found.
     * @return @link AnnouncementFormModel} object.
     */
    public static AnnouncementFormModel getFormModel(Pet pet, Long postId, Date eventDate, Location location) {

        String petType = null;
        if (pet instanceof Dog) {
            petType = "dog";
            dog = (Dog) pet;
        }
        if (pet instanceof Cat) {
            petType = "cat";
            cat = (Cat) pet;
        }

        Set<Colour> colourSet = pet.getColours();

        String[] petColours = new String[colourSet.size()];

        int i = 0;
        for (Colour colour : colourSet) {
            petColours[i++] = colour.getColour();
        }

        oldPetImage = pet.getImage();

        return new AnnouncementFormModel(
                postId,
                eventDate,
                location.getName(),
                petType,
                pet.getId(),
                pet.getName(),
                pet.getGender(),
                pet.getAge(),
                petColours,
                pet.getChipped(),
                pet.getCollar(),
                oldPetImage,
                pet.getDescription(),
                cat.getBreed(),
                dog.getBreed());
    }


    /**
     * This method is used to set the pet image in the announcement form model.
     *
     * @param formModel This is the {@link AnnouncementFormModel} object to update.
     * @param file      This is the file with a pet image.
     * @throws IOException When an I/O exception of some sort has occurred.
     */
    public static void setImageFile(AnnouncementFormModel formModel, MultipartFile file) throws IOException {
        if (!file.isEmpty()) {
            formModel.setNewImage(file.getBytes());
        } else if (ArrayUtils.isNotEmpty(formModel.getOldImage())) {
            formModel.setNewImage(oldPetImage);
        } else {
            formModel.setNewImage(null);
        }
    }

    /**
     * This method will create the {@link Lost} object from the {@link AnnouncementFormModel}.
     *
     * @param model  This is the {@link AnnouncementFormModel} object to convert.
     * @param member This is the member which is the owner of the announcement.
     * @return {@link Lost} object.
     */
    public static Lost setLostAnnouncement(AnnouncementFormModel model, User member) {
        Lost lost = new Lost();

        String petType = model.getPetType();

        if (petType.equals("dog")) {
            lost.setPet(getDog(model));
        }
        if (petType.equals("cat")) {
            lost.setPet(getCat(model));
        }

        lost.setId(model.getId());
        lost.setLocation(new Location(model.getLocation()));
        lost.setLostDate(model.getEventDate());
        lost.setMember(member);

        return lost;
    }

    /**
     * This method will create the {@link Found} object from the {@link AnnouncementFormModel}.
     *
     * @param model  This is the {@link AnnouncementFormModel} object to convert.
     * @param member This is the member which is the owner of the announcement.
     * @return {@link Found} object.
     */
    public static Found setFoundAnnouncement(AnnouncementFormModel model, User member) {
        Found found = new Found();

        String petType = model.getPetType();

        if (petType.equals("dog")) {
            found.setPet(getDog(model));
        }
        if (petType.equals("cat")) {
            found.setPet(getCat(model));
        }

        found.setId(model.getId());
        found.setLocation(new Location(model.getLocation()));
        found.setFoundDate(model.getEventDate());
        found.setMember(member);

        return found;
    }

    /**
     * This method will create the {@link Dog} object from the {@link AnnouncementFormModel}.
     */
    private static Dog getDog(AnnouncementFormModel model) {
        return new Dog(model.getPetId(), model.getPetName(), checkPetGender(model), model.getPetAge(), model.getChipped(), model.getCollar(), model.getNewImage(), model.getDescription(), getPetColours(model), model.getDogBreed());
    }

    /**
     * This method will create the {@link Cat} object from the {@link AnnouncementFormModel}.
     */
    private static Cat getCat(AnnouncementFormModel model) {
        return new Cat(model.getPetId(), model.getPetName(), checkPetGender(model), model.getPetAge(), model.getChipped(), model.getCollar(), model.getNewImage(), model.getDescription(), getPetColours(model), model.getCatBreed());
    }

    /**
     * This method will create the {@link Colour} object used for pet model.
     */
    private static Set<Colour> getPetColours(AnnouncementFormModel model) {
        Set<Colour> colours = new HashSet<>();

        for (String c : model.getPetColours()) {
            colours.add(new Colour(c));
        }
        return colours;
    }

    private static PetGender checkPetGender(AnnouncementFormModel model) {
        if (model.getPetGender() == null) {
            model.setPetGender(PetGender.Unknown);
        }

        return model.getPetGender();
    }
}
