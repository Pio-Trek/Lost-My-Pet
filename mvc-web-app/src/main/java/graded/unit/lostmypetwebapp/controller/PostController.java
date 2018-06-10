package graded.unit.lostmypetwebapp.controller;

import graded.unit.lostmypetwebapp.model.forms.AnnouncementFormModel;
import graded.unit.lostmypetwebapp.model.posts.Found;
import graded.unit.lostmypetwebapp.model.posts.Lost;
import graded.unit.lostmypetwebapp.service.*;
import graded.unit.lostmypetwebapp.util.PostProcessUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.io.IOException;

/**
 * This controller is mapping saving of a new and update existing
 * lost/found pet announcements HTTP requests methods.
 *
 * @author Piotr Przechodzki
 * @since 26/03/2018
 */
@Controller
@RequestMapping("/post")
public class PostController {

    private final LostPostService lostPostService;
    private final FoundPostService foundPostService;
    private final UserService userService;
    private final PetBreedService petBreedService;
    private final LocationService locationService;

    private byte[] oldPetImage;

    @Autowired
    public PostController(LostPostService lostPostService, FoundPostService foundPostService, UserService userService, PetBreedService petBreedService, LocationService locationService) {
        this.lostPostService = lostPostService;
        this.foundPostService = foundPostService;
        this.userService = userService;
        this.petBreedService = petBreedService;
        this.locationService = locationService;
    }

    /**
     * Display page with insert new lost announcement form.
     *
     * @param model This model is to supply attributes used for rendering view.
     * @return View with the lost announcement form.
     */
    @GetMapping("/lost")
    public String showReportLostPage(Model model) {

        model.addAttribute("headerMessage", "Report a missing pet");
        model.addAttribute("announcement", new AnnouncementFormModel());
        model.addAttribute("locationList", locationService.getLocations());
        model.addAttribute("dogBreeds", petBreedService.getDogBreeds());
        model.addAttribute("catBreeds", petBreedService.getCatBreeds());

        return "post/lost-new";

    }

    /**
     * Display page with update lost announcement.
     * This method is passing the {@link AnnouncementFormModel} and display the values in the appropriate form inputs.
     *
     * @param id    This is the id number of the lost announcement object.
     * @param model This model is to supply attributes used for rendering view.
     * @return View with the form with current updating lost announcement.
     */
    @GetMapping("/lost/update")
    public String showUpdateLostPage(@RequestParam(name = "id") Long id, Model model) {

        Lost lost = lostPostService.getLostPostById(id);

        String currentUserId = userService.getCurrentUser().getId();

        if (lost.getMember().getId().equals(currentUserId)) {

            AnnouncementFormModel formModel = PostProcessUtil
                    .getFormModel(lost.getPet(), lost.getId(), lost.getLostDate(), lost.getLocation());

            oldPetImage = formModel.getOldImage();

            model.addAttribute("headerMessage", "Update a missing pet announcement");
            model.addAttribute("announcement", formModel);
            model.addAttribute("locationList", locationService.getLocations());
            model.addAttribute("dogBreeds", petBreedService.getDogBreeds());
            model.addAttribute("catBreeds", petBreedService.getCatBreeds());

            return "post/lost-update";
        }

        return "account/access-denied";
    }

    /**
     * Trigger when member want to add or updated lost announcement.
     * This method validate the input and process of the inserting or updating lost announcement.
     *
     * @param formModel          This is a special {@link AnnouncementFormModel} object used te perform operation.
     * @param bindingResult      This is to validate the form input and display error message.
     * @param model              This model is to supply attributes used for rendering view.
     * @param file               This is the pet image file.
     * @param redirectAttributes This is a model with a confirmation message.
     * @return Redirect to the list of the lost announcement if the post was added and display confirmation message. If lost announcement was updated then redirect to members account dashboard.
     * @throws IOException When image file is invalid.
     */
    @PostMapping("/lost")
    public String processReportLostForm(@Valid @ModelAttribute("announcement") AnnouncementFormModel formModel, BindingResult bindingResult, Model model, @RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) throws IOException {

        if (bindingResult.hasErrors()) {
            model.addAttribute("locationList", locationService.getLocations());
            model.addAttribute("dogBreeds", petBreedService.getDogBreeds());
            model.addAttribute("catBreeds", petBreedService.getCatBreeds());
            return "post/lost-new";
        } else {

            PostProcessUtil.setImageFile(formModel, file);

            Lost lost = PostProcessUtil.setLostAnnouncement(formModel, userService.getCurrentUser());
            lostPostService.saveOrUpdate(lost);

            if (formModel.getId() != null) {
                redirectAttributes.addFlashAttribute("message", "Your lost pet announcement have been updated!");
                return "redirect:/account/dashboard";
            } else {
                redirectAttributes.addFlashAttribute("message", "Thank you! Your announcement is awaiting moderation");
                return "redirect:/view/lost";
            }
        }
    }

    /**
     * Trigger when announcement author want to delete the lost spost from the announcement details view.
     *
     * @param id                 This is an id number of the {@link Lost} announcement object to delete.
     * @param redirectAttributes This is a model with a confirmation message.
     * @return Go back to members' dashboard or if user is not authorize to delete the announcement then display access denied page.
     */
    @GetMapping("/lost/delete")
    public String processDeleteLostPost(@RequestParam("id") Long id, RedirectAttributes redirectAttributes) {

        String lostPostById = lostPostService.getLostPostById(id).getMember().getId();
        String currentUserId = userService.getCurrentUser().getId();

        if (lostPostById.equals(currentUserId)) {
            lostPostService.deleteLostPostById(id);
            redirectAttributes.addFlashAttribute("message", "Announcement deleted!");
            return "redirect:/account/dashboard";
        } else {
            return "redirect:/account/access-denied";

        }
    }

    /**
     * Display page with insert new found announcement form.
     *
     * @param model This model is to supply attributes used for rendering view.
     * @return View with the found announcement form.
     */
    @GetMapping("/found")
    public String showReportFoundPage(Model model) {

        model.addAttribute("headerMessage", "Report a found pet");
        model.addAttribute("announcement", new AnnouncementFormModel());
        model.addAttribute("locationList", locationService.getLocations());
        model.addAttribute("dogBreeds", petBreedService.getDogBreeds());
        model.addAttribute("catBreeds", petBreedService.getCatBreeds());

        return "post/found-new";
    }


    /**
     * Display page with update found announcement.
     * This method is passing the {@link AnnouncementFormModel} and display the values in the appropriate form inputs.
     *
     * @param id    This is the id number of the lost announcement object.
     * @param model This model is to supply attributes used for rendering view.
     * @return View with the form with current updating found announcement.
     */
    @GetMapping("/found/update")
    public String showUpdateFoundPage(@RequestParam(name = "id") Long id, Model model) {

        Found found = foundPostService.getFoundPostById(id);

        String currentUserId = userService.getCurrentUser().getId();

        if (found.getMember().getId().equals(currentUserId)) {

            AnnouncementFormModel formModel = PostProcessUtil
                    .getFormModel(found.getPet(), found.getId(), found.getFoundDate(), found.getLocation());

            oldPetImage = formModel.getOldImage();

            model.addAttribute("headerMessage", "Update a found pet announcement");
            model.addAttribute("announcement", formModel);
            model.addAttribute("locationList", locationService.getLocations());
            model.addAttribute("dogBreeds", petBreedService.getDogBreeds());
            model.addAttribute("catBreeds", petBreedService.getCatBreeds());

            return "post/found-update";
        }

        return "account/access-denied";
    }


    /**
     * Trigger when member want to add or updated found announcement.
     * This method validate the input and process of the inserting or updating found announcement.
     *
     * @param formModel          This is a special {@link AnnouncementFormModel} object used te perform operation.
     * @param bindingResult      This is to validate the form input and display error message.
     * @param model              This model is to supply attributes used for rendering view.
     * @param file               This is the pet image file.
     * @param redirectAttributes This is a model with a confirmation message.
     * @return Redirect to the list of the found announcement if the post was added and display confirmation message. If lost announcement was updated then redirect to members account dashboard.
     * @throws IOException When image file is invalid.
     */
    @PostMapping("/found")
    public String processReportFoundForm(@Valid @ModelAttribute("announcement") AnnouncementFormModel formModel, BindingResult bindingResult, Model model, @RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) throws IOException {

        if (bindingResult.hasErrors()) {
            model.addAttribute("locationList", locationService.getLocations());
            model.addAttribute("dogBreeds", petBreedService.getDogBreeds());
            model.addAttribute("catBreeds", petBreedService.getCatBreeds());
            return "post/found-new";
        } else {

            PostProcessUtil.setImageFile(formModel, file);

            Found found = PostProcessUtil.setFoundAnnouncement(formModel, userService.getCurrentUser());
            foundPostService.saveOrUpdate(found);

            if (formModel.getId() != null) {
                redirectAttributes.addFlashAttribute("message", "Your found pet announcement have been updated!");
                return "redirect:/account/dashboard";
            } else {

                redirectAttributes.addFlashAttribute("message", "Thank you! Your announcement is awaiting moderation");
                return "redirect:/view/found";
            }
        }
    }

    /**
     * Trigger when announcement author want to delete the found post from the announcement details view.
     *
     * @param id                 This is an id number of the {@link Found} announcement object to delete.
     * @param redirectAttributes This is a model with a confirmation message.
     * @return Go back to members' dashboard or if user is not authorize to delete the announcement then display access denied page.
     */
    @GetMapping("/found/delete")
    public String processDeleteFoundPost(@RequestParam("id") Long id, RedirectAttributes redirectAttributes) {

        String foundPostById = foundPostService.getFoundPostById(id).getMember().getId();
        String currentUserId = userService.getCurrentUser().getId();

        if (foundPostById.equals(currentUserId)) {
            foundPostService.deleteFoundPostById(id);
            redirectAttributes.addFlashAttribute("message", "Announcement deleted!");
            return "redirect:/account/dashboard";
        } else {
            return "redirect:/account/access-denied";

        }
    }
}
