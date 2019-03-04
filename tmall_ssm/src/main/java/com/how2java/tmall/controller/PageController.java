package com.how2java.tmall.controller;
 
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
 
/**
 * 用来处理页面跳转的controller，包括注册，登录，注册成功，以及支付宝跳转
 * @author chenzhuo
 * date:2018年9月13日
 *
 */

@Controller
@RequestMapping("")
public class PageController {
    @RequestMapping("registerPage")
    public String registerPage() {
        return "fore/register";
    }
    @RequestMapping("registerSuccessPage")
    public String registerSuccessPage() {
        return "fore/registerSuccess";
    }
    @RequestMapping("loginPage")
    public String loginPage(@ModelAttribute("msg") String msg) {
        return "fore/login";
    }
    @RequestMapping("forealipay")
    public String alipay(){
        return "fore/alipay";
    }
}