package com.nrs1209.travelapp.global.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SpaForwardController {

    @GetMapping(value = {
            "",
            "/places/**",
            "/planner/**",
            "/schedule/**",
            "/city/**",
            "/bookmark/**"
    })
    public String forwardToRoute() {
        // 백엔드 API가 아닌 모든 프론트 라우트 경로를 index.html로 포워딩
        return "forward:/index.html";
    }
}
