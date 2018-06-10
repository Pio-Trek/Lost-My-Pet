package graded.unit.lostmypetwebapp.controller;

import graded.unit.lostmypetwebapp.model.locations.Location;
import graded.unit.lostmypetwebapp.model.messages.Conversation;
import graded.unit.lostmypetwebapp.model.messages.Message;
import graded.unit.lostmypetwebapp.model.pets.Dog;
import graded.unit.lostmypetwebapp.model.pets.Pet;
import graded.unit.lostmypetwebapp.model.posts.Found;
import graded.unit.lostmypetwebapp.model.posts.Lost;
import graded.unit.lostmypetwebapp.model.reports.Report;
import graded.unit.lostmypetwebapp.model.users.User;
import graded.unit.lostmypetwebapp.service.*;
import graded.unit.lostmypetwebapp.util.FilterFoundViewList;
import graded.unit.lostmypetwebapp.util.FilterLostViewList;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

/**
 * This controller is mapping lost/found pet announcements HTTP requests methods from the '/view' URL.
 * Its main role is to provide the users with the information about all moderated lost/found announcements.
 * The logged in members and administrators can also delete selected announcements (member only if
 * is the owner).
 * Members can also report suspicious announcements to the administrator and/or write a message to
 * the author of
 * the announcement.
 *
 * @author Piotr Przechodzki
 * @since 26/03/2018
 */
@Controller
@RequestMapping("/view")
public class ViewPostController {

    private final LostPostService lostPostService;
    private final FoundPostService foundPostService;
    private final LocationService locationService;
    private final ReportService reportService;
    private final UserService userService;
    private final MessageService messageService;

    private List<Location> locationList;
    private Lost lostPost;
    private Found foundPost;

    @Autowired
    public ViewPostController(LostPostService lostPostService, FoundPostService foundPostService, LocationService locationService, ReportService reportService, UserService userService, MessageService messageService) {
        this.lostPostService = lostPostService;
        this.foundPostService = foundPostService;
        this.locationService = locationService;
        this.reportService = reportService;
        this.userService = userService;
        this.messageService = messageService;
    }

    /**
     * Display a page with list of lost announcement.
     * This method can also filter the announcements that are displayed by pet type, location or sort by added or lost pet date.
     *
     * @param message  This message can contain feedback to the user as a result of performed actions.
     * @param type     This request param describes the pet type (all, cats or dogs).
     * @param location This request param describes the location (all or one of the location on the list).
     * @param sortBy   This request param describe the sort by filter (sort by added or lost pet date).
     * @param request  To provide request information for HTTP servlets.
     * @param model    This model is to supply attributes used for rendering view.
     * @return View with filtered list of lost announcements.
     */
    @GetMapping("/lost")
    public String listLostPosts(
            @ModelAttribute("message") String message,
            @RequestParam(value = "type", required = false) String type,
            @RequestParam(value = "location", required = false) String location,
            @RequestParam(value = "sortBy", required = false) String sortBy,
            HttpServletRequest request, Model model) {

        locationList = locationService.getLocations();

        List<Lost> lostList = new FilterLostViewList(lostPostService)
                .processFilter(type, locationList, location, sortBy, 1);

        model.addAttribute("filterType", request.getParameter("type"));
        model.addAttribute("filterSortBy", request.getParameter("sortBy"));
        model.addAttribute("filterLocation", request.getParameter("location"));
        model.addAttribute("locationList", locationList);
        model.addAttribute("headerMessage", "Missing pets reported by their owner");
        model.addAttribute("lost", lostList);
        return "view/list-lost";
    }

    /**
     * Display the details page with selected lost announcements.
     *
     * @param message This message can contain feedback to the user as a result of performed actions.
     * @param model   This model is to supply attributes used for rendering view.
     * @param id      This is an id number of the {@link Lost} object to be displayed.
     * @return View with the details of selected lost announcements.
     */
    @GetMapping("/lost/{id}")
    public String lostPostDetails(
            @ModelAttribute("message") String message,
            Model model, @PathVariable("id") Long id) {
        lostPost = lostPostService.getLostPostById(id);

        Pet pet = lostPost.getPet();

        String headerMessage;
        if (pet instanceof Dog) {
            headerMessage = "Missing Dog " + pet.getName();
        } else {
            headerMessage = "Missing Cat " + pet.getName();
        }

        String headerEventDate = new SimpleDateFormat("EEEE dd MMMM yyyy").format(lostPost.getLostDate());

        model.addAttribute("headerMessage", headerMessage);
        model.addAttribute("headerEventDate", "Missing since " + headerEventDate);
        model.addAttribute("lost", lostPost);
        model.addAttribute("message", message);
        model.addAttribute("report", new Report());
        return "view/details-lost";
    }

    /**
     * Display a page with list of found announcement.
     * This method can also filter the announcements that are displayed by pet type, location or sort by added or found pet date.
     *
     * @param message  This message can contain feedback to the user as a result of performed actions.
     * @param type     This request param describes the pet type (all, cats or dogs).
     * @param location This request param describes the location (all or one of the location on the list).
     * @param sortBy   This request param describe the sort by filter (sort by added or found pet date).
     * @param request  To provide request information for HTTP servlets.
     * @param model    This model is to supply attributes used for rendering view.
     * @return View with filtered list of found announcements.
     */
    @GetMapping("/found")
    public String listFoundPosts(
            @ModelAttribute("message") String message,
            @RequestParam(value = "type", required = false) String type,
            @RequestParam(value = "location", required = false) String location,
            @RequestParam(value = "sortBy", required = false) String sortBy,
            HttpServletRequest request, Model model) {

        locationList = locationService.getLocations();

        List<Found> foundList = new FilterFoundViewList(foundPostService)
                .processFilter(type, locationList, location, sortBy, 1);

        model.addAttribute("filterType", request.getParameter("type"));
        model.addAttribute("filterSortBy", request.getParameter("sortBy"));
        model.addAttribute("filterLocation", request.getParameter("location"));
        model.addAttribute("locationList", locationList);
        model.addAttribute("headerMessage", "Found pets reported by a member of the public");
        model.addAttribute("found", foundList);
        return "view/list-found";
    }

    /**
     * Display the details page with selected found announcements.
     *
     * @param message This message can contain feedback to the user as a result of performed actions.
     * @param model   This model is to supply attributes used for rendering view.
     * @param id      This is an id number of the {@link Found} object to be displayed.
     * @return View with the details of selected found announcements.
     */
    @GetMapping("/found/{id}")
    public String lostFoundDetails(
            @ModelAttribute("message") String message,
            Model model, @PathVariable("id") Long id) {
        foundPost = foundPostService.getFoundPostById(id);

        Pet pet = foundPost.getPet();

        String headerMessage;
        if (pet instanceof Dog) {
            headerMessage = "Found Dog";
        } else {
            headerMessage = "Found Cat";
        }

        String headerEventDate = new SimpleDateFormat("EEEE dd MMMM yyyy").format(foundPost.getFoundDate());

        model.addAttribute("headerMessage", headerMessage);
        model.addAttribute("headerEventDate", "Found on " + headerEventDate);
        model.addAttribute("found", foundPost);
        model.addAttribute("report", new Report());
        model.addAttribute("message", message);
        return "view/details-found";
    }

    /**
     * Trigger when the owner or administrator want to delete selected lost announcement.
     * This method get selected lost announcement id number and delete it from the database.
     *
     * @param announcementId This is the id number of {@link Lost} announcement to bede deleted.
     * @return Redirect to the found announcement list view
     */
    @GetMapping("/lost/delete")
    public String deleteLostPost(@RequestParam("id") Long announcementId, RedirectAttributes redirectAttributes) {
        this.lostPostService.deleteLostPostById(announcementId);
        redirectAttributes.addFlashAttribute("message", "Announcement deleted!");
        return "redirect:/view/lost";
    }

    /**
     * Trigger when the owner or administrator want to delete selected found announcement.
     * This method get selected found announcement id number and delete it from the database.
     *
     * @param announcementId This is the id number of {@link Found} announcement to bede deleted.
     * @return Redirect to the found announcement list view
     */
    @GetMapping("/found/delete")
    public String deleteFoundPost(@RequestParam("id") Long announcementId, RedirectAttributes redirectAttributes) {
        this.foundPostService.deleteFoundPostById(announcementId);
        redirectAttributes.addFlashAttribute("message", "Announcement deleted!");
        return "redirect:/view/found";
    }

    /**
     * Trigger when member user want to post the report form about the suspicious announcement.
     *
     * @param postType
     * @param report             This request param describes the announcement type (found or lost).
     *                           It is used to use a proper function for saving found or lost announcements.
     * @param redirectAttributes This is a model with a confirmation message.
     * @return Redirect to the lost or found view list page and display confirmation message.
     * If the user is not authorised to perform this action then display access denied page.
     */
    @PostMapping("/report")
    public String processReportForm(
            @RequestParam(value = "postType") String postType,
            @Valid @ModelAttribute("report") Report report,
            RedirectAttributes redirectAttributes) {
        User currentUser = userService.getCurrentUser();
        if (currentUser != null) {
            report.setUser(currentUser);

            switch (postType) {
                case "lost":
                    report.setAnnouncement(lostPost);
                    redirectAttributes.addFlashAttribute("message", "Thank you");
                    reportService.saveReport(report);
                    return "redirect:/view/lost/" + lostPost.getId();
                case "found":
                    report.setAnnouncement(foundPost);
                    redirectAttributes.addFlashAttribute("message", "Thank you");
                    reportService.saveReport(report);
                    return "redirect:/view/found/" + foundPost.getId();
                default:
                    return "redirect:/account/access-denied";
            }
        }
        return "redirect:/account/access-denied";
    }

    /**
     * Trigger when member want to send a message about selected announcement to the author of that post.
     * This method also perform some basic validation functions.
     *
     * @param requestParams      This is the map with all request URL request params.
     * @param redirectAttributes This is a model with a confirmation message.
     * @return Redirect to the current announcement details page and display confirmation message. If there is
     * an error then display error message.
     */
    @PostMapping("/message")
    public String processMessageForm(
            @RequestParam Map requestParams,
            RedirectAttributes redirectAttributes) {
        String announcementType = String.valueOf(requestParams.get("announcementType"));

        User currentUser = userService.getCurrentUser();

        Message message = new Message();
        message.setSubject(String.valueOf(requestParams.get("subject")));
        message.setSender(currentUser);

        if (StringUtils.equalsIgnoreCase(announcementType, "lost")) {
            message.setRecipient(lostPost.getMember());
        } else if (StringUtils.equalsIgnoreCase(announcementType, "found")) {
            message.setRecipient(foundPost.getMember());
        } else {
            redirectAttributes.addFlashAttribute("message", "Oops!  Wrong announcement type!");
        }

        Message messageResponseEntity = messageService.saveMessage(message).getBody();

        Conversation conversation = new Conversation();
        conversation.setBody(String.valueOf(requestParams.get("body")));
        conversation.setAuthor(currentUser);
        conversation.setMessage(messageResponseEntity);

        messageService.saveConversation(conversation);

        redirectAttributes.addFlashAttribute("message", "Thank you! Your message has been sent.");

        if (StringUtils.equalsIgnoreCase(announcementType, "lost")) {
            return "redirect:/view/lost/" + lostPost.getId();
        } else {
            return "redirect:/view/found/" + foundPost.getId();
        }
    }
}