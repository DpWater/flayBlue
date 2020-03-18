package com.zzxj.flyBlue.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by Administrator on 2019/12/10.
 */
@Controller
public class MainController {

    @RequestMapping(value = "/zzz", method = RequestMethod.GET)
    @ResponseBody
    public String zzx(){
        return "zzz;";
    }

}

