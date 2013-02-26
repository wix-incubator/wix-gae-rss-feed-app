package com.wixpress.app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller to show that an app is running
 */
@Controller
@RequestMapping(value = "/")
public class HelpController {

    @RequestMapping()
    public String widget(Model model) {
        return "help";
    }

}
