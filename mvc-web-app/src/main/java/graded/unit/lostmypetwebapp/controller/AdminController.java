package graded.unit.lostmypetwebapp.controller;

import graded.unit.lostmypetwebapp.model.locations.Location;
import graded.unit.lostmypetwebapp.model.posts.Found;
import graded.unit.lostmypetwebapp.model.posts.Lost;
import graded.unit.lostmypetwebapp.model.users.User;
import graded.unit.lostmypetwebapp.service.*;
import graded.unit.lostmypetwebapp.util.ExcelReport;
import graded.unit.lostmypetwebapp.util.FilterFoundViewList;
import graded.unit.lostmypetwebapp.util.FilterLostViewList;
import graded.unit.lostmypetwebapp.util.PdfReport;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.time.DateUtils.addDays;
import static org.apache.commons.lang3.time.DateUtils.parseDate;

/**
 * This controller is mapping all admin's method HTTP requests.
 *
 * @author Piotr Przechodzki
 * @since 26/03/2018
 */
@Controller
@RequestMapping("/admin")
public class AdminController {

    private final BCryptPasswordEncoder passwordEncoder;
    private final UserService userService;
    private final LostPostService lostPostService;
    private final FoundPostService foundPostService;
    private final NotificationService notificationService;
    private final LocationService locationService;
    private final ReportService reportService;

    private String currentAdminId;
    private List<Location> locationList;

    @Autowired
    public AdminController(BCryptPasswordEncoder passwordEncoder, UserService userService, LostPostService lostPostService, FoundPostService foundPostService, NotificationService notificationService, LocationService locationService, ReportService reportService) {
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
        this.lostPostService = lostPostService;
        this.foundPostService = foundPostService;
        this.notificationService = notificationService;
        this.locationService = locationService;
        this.reportService = reportService;
    }

    /**
     * Display admin's dashboard main page which contains registration new admin account link, number of announcement that are waiting for the moderation, number of reported by user announcements, link to list of all users, link to list of all announcements, link to the generate report form page.
     *
     * @param message This message can contain feedback to the user as a result of performed actions.
     * @param model   This model is to supply attributes used for rendering view.
     * @return View with the admin's dashboard
     */
    @GetMapping("/dashboard")
    public String showDashboardPage(@ModelAttribute("message") String message, Model model) {
        model.addAttribute("countNewPosts", notificationService.showNumberAnnouncementsByEnabledFalse());
        model.addAttribute("countReports", reportService.getAllReports().size());
        model.addAttribute("headerMessage", "Administration Panel");
        return "admin/dashboard";
    }

    /**
     * Display a view with the form for register a new admin account.
     *
     * @param model This model is to supply attributes used for rendering view it contains the user model.
     * @return View with the register form.
     */
    @GetMapping("/register")
    public String showAdminRegistrationPage(Model model) {
        model.addAttribute("admin", new User());
        model.addAttribute("errorMessage", "");
        model.addAttribute("headerMessage", "Create new Admin account");
        return "admin/register";
    }

    /**
     * Trigger when admin post the form for registration a new admin account.
     * This method will validate if the email address already exists in the database
     *
     * @param admin              This is the admin model created from the post form.
     * @param requestParams      This is the map with all request URL request params.
     * @param bindingResult      This is to validate the form input and display error message.
     * @param model              his model is to supply attributes used for rendering view.
     * @param redirectAttributes This is a model with a confirmation message.
     * @return Go back to registration page if the is any error, if not then redirect to the admin dashboard and display confirmation message.
     */
    @PostMapping("/register")
    public String processAdminRegistrationForm(@Valid @ModelAttribute("admin") User admin, @RequestParam Map requestParams, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {

        // Lookup user exists in database by e-mail
        boolean userExists = userService.existsWithEmail(admin.getEmail());

        if (userExists) {
            model.addAttribute("errorMessage", "Oops!  User registered with the email provided.");

            return "admin/register";
        }

        if (bindingResult.hasErrors()) {
            return "admin/register";
        } else {
            admin.setPassword(passwordEncoder.encode(String.valueOf(requestParams.get("password"))));
            userService.saveOrUpdateAdmin(admin);
            redirectAttributes.addFlashAttribute("message", "New Admin account was successfully created!");
        }
        return "redirect:/admin/dashboard";
    }

    /**
     * Display a list of users account in the table with the delete selected account button.
     *
     * @param message This message can contain feedback to the user as a result of performed actions.
     * @param model   This model is to supply attributes used for rendering view. It contains a list of members and admins objects.
     * @return View with the list of users.
     */
    @GetMapping("/users")
    public String showUsersListPage(@ModelAttribute("message") String message, Model model) {
        currentAdminId = userService.getCurrentUser().getId();
        List<User> allUsers = userService.getAllUsers();

        List<User> members = allUsers.stream()
                .filter(u -> u.getRoles().iterator().next().getRole().equals("ROLE_USER"))
                .collect(Collectors.toList());

        List<User> admins = allUsers.stream()
                .filter(u -> u.getRoles().iterator().next().getRole().equals("ROLE_ADMIN"))
                .collect(Collectors.toList());

        model.addAttribute("membersList", members);
        model.addAttribute("adminsList", admins);
        model.addAttribute("currentAdminId", currentAdminId);
        model.addAttribute("headerMessage", "List of all Members");
        return "admin/users";
    }

    /**
     * Trigged when the admin click delete button on the selected member account.
     *
     * @param id                 This is user id number of the object to be deleted.
     * @param redirectAttributes This is a model with a confirmation message.
     * @return Redirect to users list page and display confirmation message or if the user is not authorised to perform delete action then move to access denied page.
     */
    @GetMapping("/users/delete")
    public String processDeleteUserAccount(@RequestParam("id") String id, RedirectAttributes redirectAttributes) {
        if (!id.equals(currentAdminId)) {
            userService.deleteUser(id);

            redirectAttributes.addFlashAttribute("message", "User Account was successfully deleted!");
            return "redirect:/admin/users";
        } else {
            return "redirect:/account/access-denied";
        }
    }

    /**
     * Display a page with the generate report form.
     * The form contains a several different options to generate the report, like download the Excel document or the PDF, choose the dates, announcement type, pet type, add all moderated or not moderated announcement or display all and filter by location.
     *
     * @param request To provide request information for HTTP servlets.
     * @param model   This model is to supply attributes used for rendering view and form attributes. It contains alo a list of location objects.
     * @return View with the generate report form.
     */
    @GetMapping("/report")
    public String showReportFormPage(HttpServletRequest request, Model model) {

        locationList = locationService.getLocations();

        model.addAttribute("documentType", request.getParameter("document"));
        model.addAttribute("filterAnnouncements", request.getParameter("announcements"));
        model.addAttribute("fromDate", request.getParameter("fromDate"));
        model.addAttribute("toDate", request.getParameter("toDate"));
        model.addAttribute("filterType", request.getParameter("type"));
        model.addAttribute("filterEnabled", request.getParameter("enabled"));
        model.addAttribute("filterLocation", request.getParameter("location"));
        model.addAttribute("locationList", locationList);
        model.addAttribute("headerMessage", "Generate Report");
        return "admin/report";
    }

    /**
     * Triggle when user presss the generate report button.
     * This method process all the selected options in the form by getting them from passed request URL parameters. Next it create a new lost/found list objects and build a {@link ExcelReport} or {@link PdfReport} report do download.
     *
     * @param document      This request param describes the document type (excel or pdf)
     * @param announcements This request param describes the announcement type (all, found or lost)
     * @param fromDate      This request param describes start date (lost/found date)
     * @param toDate        This request param describes end date (lost/found date)
     * @param type          This request param describes the pet type (all, cats or dogs)
     * @param enabled       This request param describes whatever the announcement mus be enabled, disabled (moderated) or display all types.
     * @param location      This request param describes the location (all or one of the location on the list)
     * @return Excel or PDF file with the report.
     */
    @GetMapping("/report/download")
    public ModelAndView getReportToDownload(
            @RequestParam(value = "documentType", required = false, defaultValue = "xlsx") String document,
            @RequestParam(value = "announcements", required = false, defaultValue = "all") String announcements,
            @RequestParam(value = "fromDate", required = false, defaultValue = "all") String fromDate,
            @RequestParam(value = "toDate", required = false, defaultValue = "all") String toDate,
            @RequestParam(value = "type", required = false, defaultValue = "all") String type,
            @RequestParam(value = "enabled", required = false, defaultValue = "2") int enabled,
            @RequestParam(value = "location", required = false, defaultValue = "all") String location) {

        locationList = locationService.getLocations();

        String datePattern = "dd/MM/yyyy";

        Date start = new Date(0L); // 01/01/1970
        try {
            start = addDays(parseDate(fromDate, datePattern), -1);
        } catch (ParseException e) {
            // Cannot parse the dates, so will not filter by date and display all dates
        }

        Date end = new Date(); // Date now
        try {
            end = addDays(parseDate(toDate, datePattern), 1);
        } catch (ParseException e) {
            // Cannot parse the dates, so will not filter by date and display all dates
        }

        List<Lost> lostList;
        List<Found> foundList;
        Map<String, Object> announcementsMap = new HashMap<>();

        switch (announcements) {
            case "lost":
                lostList = new FilterLostViewList(lostPostService)
                        .processFilter(start, end, type, locationList, location, enabled);
                announcementsMap.put("lostList", lostList);
                break;
            case "found":
                foundList = new FilterFoundViewList(foundPostService)
                        .processFilter(start, end, type, locationList, location, enabled);
                announcementsMap.put("foundList", foundList);
                break;
            default:
                lostList = new FilterLostViewList(lostPostService)
                        .processFilter(start, end, type, locationList, location, enabled);
                foundList = new FilterFoundViewList(foundPostService)
                        .processFilter(start, end, type, locationList, location, enabled);
                announcementsMap.put("lostList", lostList);
                announcementsMap.put("foundList", foundList);
                break;
        }

        if (StringUtils.equalsIgnoreCase(document, "pdf")) {
            return new ModelAndView(new PdfReport(), announcementsMap);
        } else {
            return new ModelAndView(new ExcelReport(), announcementsMap);
        }
    }

    /**
     * Display a list o the users announcements with the filter option, button to view the announcement details, button to delete and button to enable (make visible and moderated) selected announcement.
     *
     * @param type     This request param describes the filter pet type (all, cats or dogs)
     * @param enabled  This request param describes filter whatever the announcement mus be enabled, disabled (moderated) or display all types.
     * @param location This request param describes the filter location (all or one of the location on the list)
     * @param request  To provide request information for HTTP servlets.
     * @param model    model This model is to supply attributes used for rendering view.
     * @param message  This message can contain feedback to the user as a result of performed actions.
     * @return View with the list of filtered announcements.
     */
    @GetMapping("/announcements")
    public String viewMemberAnnouncements(
            @RequestParam(value = "type", required = false, defaultValue = "all") String type,
            @RequestParam(value = "enabled", required = false, defaultValue = "2") int enabled,
            @RequestParam(value = "location", required = false, defaultValue = "all") String location,
            HttpServletRequest request, Model model,
            @ModelAttribute("message") String message) {

        locationList = locationService.getLocations();

        List<Lost> lostList = new FilterLostViewList(lostPostService)
                .processFilter(type, locationList, location, enabled);

        List<Found> foundList = new FilterFoundViewList(foundPostService)
                .processFilter(type, locationList, location, enabled);

        model.addAttribute("filterType", request.getParameter("type"));
        model.addAttribute("filterEnabled", request.getParameter("enabled"));
        model.addAttribute("filterLocation", request.getParameter("location"));
        model.addAttribute("lostList", lostList);
        model.addAttribute("foundList", foundList);
        model.addAttribute("locationList", locationList);
        model.addAttribute("headerMessage", "Announcements list");
        return "admin/announcements";
    }

    /**
     * Triggle when admin click on enable button which will change {@link Lost#enabled} to true and the lost announcement start to be visible for other users.
     *
     * @param type               This request param describes the pet type (all, cats or dogs) it is used to keep the selected filters after the process of the method.
     * @param enabled            This request param describes whatever the announcement mus be enabled, disabled (moderated) or display all types it is used to keep the selected filters after the process of the method.
     * @param location           This request param describes the location (all or one of the location on the list) it is used to keep the selected filters after the process of the method.
     * @param lostId             This is the id number of enabling lost announcement.
     * @param redirectAttributes This is a model with a confirmation message.
     * @return Redirect to the list of announcement page with same filter list passed using the request parameters or if there is no more announcements that needs to be moderated then move the admin to the dashboard page.
     */
    @GetMapping("/announcements/lost/enabled")
    public String processSetEnableLostPost(
            @RequestParam(value = "type", required = false, defaultValue = "all") String type,
            @RequestParam(value = "enabled", required = false, defaultValue = "2") int enabled,
            @RequestParam(value = "location", required = false, defaultValue = "all") String location,
            @RequestParam("id") Long lostId, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("message", "Lost pet announcement was successfully enabled!");
        lostPostService.setLostPostEnabled(lostId);

        if (notificationService.showNumberAnnouncementsByEnabledFalse().equals("0")) {
            return "redirect:/admin/dashboard";
        }

        String params = "?type=" + type + "&enabled=" + enabled + "&location=" + location;
        return "redirect:/admin/announcements" + params;
    }

    /**
     * Triggle when admin click on enable button which will change {@link Found#enabled} to true and the found announcement start to be visible for other users.
     *
     * @param type               This request param describes the pet type (all, cats or dogs) it is used to keep the selected filters after the process of the method.
     * @param enabled            This request param describes whatever the announcement mus be enabled, disabled (moderated) or display all types it is used to keep the selected filters after the process of the method.
     * @param location           This request param describes the location (all or one of the location on the list) it is used to keep the selected filters after the process of the method.
     * @param foundId            This is the id number of enabling found announcement.
     * @param redirectAttributes This is a model with a confirmation message.
     * @return Redirect to the list of announcement page with same filter list passed using the request parameters or if there is no more announcements that needs to be moderated then move the admin to the dashboard page.
     */
    @GetMapping("/announcements/found/enabled")
    public String processSetEnableFoundPost(
            @RequestParam(value = "type", required = false, defaultValue = "all") String type,
            @RequestParam(value = "enabled", required = false, defaultValue = "2") int enabled,
            @RequestParam(value = "location", required = false, defaultValue = "all") String location,
            @RequestParam("id") Long foundId, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("message", "Found pet announcement was successfully enabled!");
        foundPostService.setFoundPostEnabled(foundId);

        if (notificationService.showNumberAnnouncementsByEnabledFalse().equals("0")) {
            return "redirect:/admin/dashboard";
        }

        String params = "?type=" + type + "&enabled=" + enabled + "&location=" + location;
        return "redirect:/admin/announcements" + params;
    }

    /**
     * Triggle when the admin click on selected lost announcement the delete button.
     * This method will perform the operations with deleting specified lost announcement.
     *
     * @param type               This request param describes the pet type (all, cats or dogs) it is used to keep the selected filters after the process of the method.
     * @param enabled            This request param describes whatever the announcement mus be enabled, disabled (moderated) or display all types it is used to keep the selected filters after the process of the method.
     * @param location           This request param describes the location (all or one of the location on the list) it is used to keep the selected filters after the process of the method.
     * @param lostId             This is the id number of enabling lost announcement.
     * @param redirectAttributes This is a model with a confirmation message.
     * @return Redirect to the list of announcement page with same filter list passed using the request parameters or if there is no more announcements that needs to be moderated then move the admin to the dashboard page.
     */
    @GetMapping("/announcements/lost/delete")
    public String deleteMemberLostAnnouncement(
            @RequestParam(value = "type", required = false, defaultValue = "all") String type,
            @RequestParam(value = "enabled", required = false, defaultValue = "2") int enabled,
            @RequestParam(value = "location", required = false, defaultValue = "all") String location,
            @RequestParam("id") Long lostId, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("message", "Lost pet announcement was successfully deleted!");
        lostPostService.deleteLostPostById(lostId);

        String params = "?type=" + type + "&enabled=" + enabled + "&location=" + location;
        return "redirect:/admin/announcements" + params;
    }

    /**
     * Triggle when the admin click on selected found announcement the delete button.
     * This method will perform the operations with deleting specified found announcement.
     *
     * @param type               This request param describes the pet type (all, cats or dogs) it is used to keep the selected filters after the process of the method.
     * @param enabled            This request param describes whatever the announcement mus be enabled, disabled (moderated) or display all types it is used to keep the selected filters after the process of the method.
     * @param location           This request param describes the location (all or one of the location on the list) it is used to keep the selected filters after the process of the method.
     * @param foundId            This is the id number of enabling found announcement.
     * @param redirectAttributes This is a model with a confirmation message.
     * @return Redirect to the list of announcement page with same filter list passed using the request parameters or if there is no more announcements that needs to be moderated then move the admin to the dashboard page.
     */
    @GetMapping("/announcements/found/delete")
    public String deleteMemberFoundAnnouncement(
            @RequestParam(value = "type", required = false, defaultValue = "all") String type,
            @RequestParam(value = "enabled", required = false, defaultValue = "2") int enabled,
            @RequestParam(value = "location", required = false, defaultValue = "all") String location,
            @RequestParam("id") Long foundId, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("message", "Lost pet announcement was successfully deleted!");
        foundPostService.deleteFoundPostById(foundId);

        String params = "?type=" + type + "&enabled=" + enabled + "&location=" + location;
        return "redirect:/admin/announcements" + params;
    }

    /**
     * Display the list of reported by members announcements.
     * This method display the list of reports, the date added, user email and message describing why this announcement should be reported. Also display the button to view the selected announcement and delete the report.
     *
     * @param model   This model is to supply attributes used for rendering view. It contains the report list object.
     * @param message This message can contain feedback to the user as a result of performed actions.
     * @return View withe the list of user's reports.
     */
    @GetMapping("/announcements/reported")
    public String showReportsList(Model model, @ModelAttribute("message") String message) {
        model.addAttribute("reportList", reportService.getAllReports());
        model.addAttribute("headerMessage", "Reported posts");
        model.addAttribute("message", message);
        return "admin/announcements-reported";
    }

    /**
     * Triggle when admin click on delete selected announcement button.
     * This function perform operations to delete the user report.
     *
     * @param id                 This is the id of the user report to be deleted.
     * @param redirectAttributes This is a model with a confirmation message.
     * @return Go back to list of the report announcements or if there is no any report then redirect to the admin's dashboard.
     */
    @GetMapping("/announcements/reported/delete")
    public String deleteReport(
            @RequestParam("id") Long id,
            RedirectAttributes redirectAttributes) {
        reportService.deleteReport(id);

        redirectAttributes.addFlashAttribute("message", "Member report successfully deleted!");

        if (reportService.getAllReports().size() == 0) {
            return "redirect:/admin/dashboard";
        }

        return "redirect:/admin/announcements/reported";
    }
}