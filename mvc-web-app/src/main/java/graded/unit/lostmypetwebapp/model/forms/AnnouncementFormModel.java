package graded.unit.lostmypetwebapp.model.forms;

import graded.unit.lostmypetwebapp.model.pets.CatBreed;
import graded.unit.lostmypetwebapp.model.pets.DogBreed;
import graded.unit.lostmypetwebapp.model.pets.PetGender;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Past;
import java.util.Date;

/**
 * This is the helper class used to create a model from the announcement form (add or update post).
 * This model class contains all the properties from the announcement and pet object, becouse it's not
 * possible to create a html form that will include and post many models.
 */
public class AnnouncementFormModel {

    /**
     * This is an announcement id number.
     */
    private Long id;

    /**
     * This is the date when the pet was missing or found.
     */
    @Past
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private Date eventDate;

    /**
     * This is a location where the pet was lost or found.
     */
    @NotEmpty(message = "Please, select location")
    private String location;

    /**
     * The is the pet type (dog or cat) field.
     */
    @NotEmpty(message = "Please, select a pet type")
    private String petType;

    /**
     * This is the pet id number field.
     */
    private Long petId;

    /**
     * This is the pet name field.
     */
    @Length(max = 15)
    private String petName;

    /**
     * This is the pet gender field.
     */
    private PetGender petGender;

    /**
     * This is the pet age field.
     */
    private Integer petAge;

    /**
     * This is string array with pet colours.
     */
    private String[] petColours;

    /**
     * This is the pet chipped field.
     */
    private Boolean chipped;

    /**
     * This is the pet collar field.
     */
    private Boolean collar;

    /**
     * This is the new pet image uploaded.
     */
    private byte[] newImage;

    /**
     * This is th old or existing pet image.
     * It is used when member is updating the announcement.
     */
    private byte[] oldImage;

    /**
     * This is the announcement description field.
     */
    @Length(max = 255)
    private String description;

    /**
     * This is the cat breed field.
     */
    private CatBreed catBreed;

    /**
     * This is the dog breed field.
     */
    private DogBreed dogBreed;

    // Constructors

    public AnnouncementFormModel() {
    }

    public AnnouncementFormModel(Long id, @Past Date eventDate, @NotEmpty(message = "Please, select location") String location, @NotEmpty(message = "Please, select a pet type") String petType, Long petId, @Length(max = 15) String petName, PetGender petGender, Integer petAge, String[] petColours, Boolean chipped, Boolean collar, byte[] oldImage, @Length(max = 255) String description, CatBreed catBreed, DogBreed dogBreed) {
        this.id = id;
        this.eventDate = eventDate;
        this.location = location;
        this.petType = petType;
        this.petId = petId;
        this.petName = petName;
        this.petGender = petGender;
        this.petAge = petAge;
        this.petColours = petColours;
        this.chipped = chipped;
        this.collar = collar;
        this.oldImage = oldImage;
        this.description = description;
        this.catBreed = catBreed;
        this.dogBreed = dogBreed;
    }

    // Getters and setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getEventDate() {
        return eventDate;
    }

    public void setEventDate(Date eventDate) {
        this.eventDate = eventDate;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPetType() {
        return petType;
    }

    public void setPetType(String petType) {
        this.petType = petType;
    }

    public Long getPetId() {
        return petId;
    }

    public void setPetId(Long petId) {
        this.petId = petId;
    }

    public String getPetName() {
        return petName;
    }

    public void setPetName(String petName) {
        this.petName = petName;
    }

    public PetGender getPetGender() {
        return petGender;
    }

    public void setPetGender(PetGender petGender) {
        this.petGender = petGender;
    }

    public Integer getPetAge() {
        return petAge;
    }

    public void setPetAge(Integer petAge) {
        this.petAge = petAge;
    }

    public String[] getPetColours() {
        return petColours;
    }

    public void setPetColours(String[] petColours) {
        this.petColours = petColours;
    }

    public Boolean getChipped() {
        return chipped;
    }

    public void setChipped(Boolean chipped) {
        this.chipped = chipped;
    }

    public Boolean getCollar() {
        return collar;
    }

    public void setCollar(Boolean collar) {
        this.collar = collar;
    }

    public byte[] getNewImage() {
        return newImage;
    }

    public void setNewImage(byte[] newImage) {
        this.newImage = newImage;
    }

    public byte[] getOldImage() {
        return oldImage;
    }

    public void setOldImage(byte[] oldImage) {
        this.oldImage = oldImage;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public CatBreed getCatBreed() {
        return catBreed;
    }

    public void setCatBreed(CatBreed catBreed) {
        this.catBreed = catBreed;
    }

    public DogBreed getDogBreed() {
        return dogBreed;
    }

    public void setDogBreed(DogBreed dogBreed) {
        this.dogBreed = dogBreed;
    }

}
