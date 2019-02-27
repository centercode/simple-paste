package io.github.leaf.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HtmlController {

    @RequestMapping("/")
    public String index() {
        return "/index.html";
    }

    @RequestMapping("/{id:[a-zA-Z0-9]+}")
    public String show(@PathVariable String id) {
        return "/show.html";
    }
}