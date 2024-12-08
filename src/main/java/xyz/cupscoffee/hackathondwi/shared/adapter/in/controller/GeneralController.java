package xyz.cupscoffee.hackathondwi.shared.adapter.in.controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import io.swagger.v3.oas.annotations.Hidden;
import xyz.cupscoffee.hackathondwi.shared.adapter.annotations.WebControllerAdapter;

/**
 * General controller.
 */
@Hidden
@WebControllerAdapter
public class GeneralController {
    /**
     * Home page.
     *
     * @return the home page
     */
    @GetMapping("/")
    public String getHome() {
        return "home";
    }

    /**
     * Home page.
     *
     * @return the home page
     */
    @GetMapping("/home")
    public String getHome2() {
        return "home";
    }

    /**
     * Health page.
     *
     * @param model the view.
     * @return the health page
     */
    @GetMapping("/health")
    public String getHealth(Model model) {
        model.addAttribute("jsf", "JSF Works!");
        model.addAttribute("jstl", "JSTL Works!");

        return "health";
    }
}
