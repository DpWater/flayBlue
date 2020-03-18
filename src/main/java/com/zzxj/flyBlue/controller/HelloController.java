package com.zzxj.flyBlue.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by Administrator on 2019/12/17.
 */
@Controller
public class HelloController {

    @RequestMapping("/hello")
    @ResponseBody
    public String toTest() {
        return "Hello World";
    }

    @RequestMapping("/zxzz")
    public String index(ModelMap map) {
        map.addAttribute("host","https://github.com/Inverseli/");
        return "index";
    }
}
