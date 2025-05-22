package com.dormitory.management.controller;

import com.dormitory.management.model.NguoiDung;
import com.dormitory.management.service.NguoiDungService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
public class LoginController {

    @Autowired
    private NguoiDungService nguoiDungService; // Dịch vụ để truy cập dữ liệu người dùng

    @GetMapping("/login")
    public String showLoginPage() {
        return "login/login";
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestParam String username, 
                                        @RequestParam String password, 
                                        HttpSession session) {
        System.out.println("Login attempt with username: " + username);
        
        // Kiểm tra thông tin đăng nhập với cơ sở dữ liệu
        NguoiDung user = nguoiDungService.findByTenDangNhap(username).orElse(null);
        
        if (user != null && user.getMatKhau().equals(password)) { // So sánh mật khẩu
            String role = user.getLoaiNguoiDung().name(); // Lấy loại người dùng
            session.setAttribute("role", role);
            return ResponseEntity.ok(role + " login successful");
        }
        
        return ResponseEntity.status(401).body("Invalid credentials");
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return "redirect:/login?logout";
    }
}
