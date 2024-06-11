package com.newbini.newbeinquiz.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@Service
@RequiredArgsConstructor
public class BookmarkController {

    @GetMapping("/bookmark")
    public String bookmarkPage() {
        return "bookmark";
    }

    @PostMapping("/bookmark")
    @ResponseBody
    public Integer bookmark(@RequestParam("index") Integer index) {
        return index;
    }
}
