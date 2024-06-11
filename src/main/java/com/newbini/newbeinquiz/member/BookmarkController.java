package com.newbini.newbeinquiz.member;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Service
@RequiredArgsConstructor
public class BookmarkController {

    @GetMapping("/bookmark")
    public String bookmarkPage() {
        return "bookmark";
    }
}
