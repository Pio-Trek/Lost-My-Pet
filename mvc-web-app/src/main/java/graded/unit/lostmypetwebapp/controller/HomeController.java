package graded.unit.lostmypetwebapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * This controller is mapping all main static pages - index and contact.
 *
 * @author Piotr Przechodzki
 * @since 26/03/2018
 */
@Controller
@RequestMapping
public class HomeController {

    /**
     * Display the home page with the information about Lost My Pet.
     *
     * @param message This message can contain feedback to the user as a result of performed actions.
     * @param model This model is to supply attributes used for rendering view.
     * @return View with home page.
     */
    @GetMapping("/")
    public String home(@ModelAttribute("message") String message, Model model) {
        model.addAttribute("headerMessage", "The City of Hillwood largest missing and found pet database");
        model.addAttribute("message", message);
        return "home/index";
    }

    /**
     * Display contact page.
     *
     * @param model This model is to supply attributes used for rendering view.
     * @return View with contact page.
     */
    @GetMapping("/contact")
    public String contact(Model model) {
        model.addAttribute("headerMessage", "Contact Us");
        return "home/contact";
    }


}
