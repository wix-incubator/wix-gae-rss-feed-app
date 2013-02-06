package com.wixpress.testapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Yoav
 * @since 2/5/13
 */
@Controller
@RequestMapping(value = "/help")
public class HelpController {

    @RequestMapping()
    public String widget(Model model) {
        return "help";
    }

}
