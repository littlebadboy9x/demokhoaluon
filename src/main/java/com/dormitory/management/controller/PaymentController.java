package com.dormitory.management.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PaymentController {

    @GetMapping("/admin/payment-management")
    public String payment() {
        return "admin/payment-management"; 
    }
}
