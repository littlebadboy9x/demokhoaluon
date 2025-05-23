package com.dormitory.management.controller;

import com.dormitory.management.model.NguoiDung;
import com.dormitory.management.service.NguoiDungService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class HomeController {

    @Autowired
    private NguoiDungService nguoiDungService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/")
    public String home() {
        return "home";
    }

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("nguoiDung", new NguoiDung());
        return "auth/register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute NguoiDung nguoiDung,
                         @ModelAttribute("matKhau") String matKhau,
                         BindingResult result,
                         RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "auth/register";
        }

        try {
            // Kiểm tra trùng lặp
            if (nguoiDungService.existsByTenDangNhap(nguoiDung.getTenDangNhap())) {
                redirectAttributes.addFlashAttribute("error", "Tên đăng nhập đã tồn tại!");
                return "redirect:/register";
            }

            // Thiết lập thông tin người dùng
            nguoiDung.setMatKhau(passwordEncoder.encode(matKhau));
            nguoiDung.setLoaiNguoiDung(NguoiDung.LoaiNguoiDung.SINH_VIEN);
            nguoiDung.setTrangThai(true);
            
            // Lưu người dùng
            nguoiDungService.save(nguoiDung);

            redirectAttributes.addFlashAttribute("success", "Đăng ký tài khoản thành công! Vui lòng đăng nhập để tiếp tục đăng ký thông tin sinh viên.");
            return "redirect:/login";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Có lỗi xảy ra: " + e.getMessage());
            return "redirect:/register";
        }
    }
} 