package com.dormitory.management.controller;

import com.dormitory.management.model.SinhVien;
import com.dormitory.management.service.SinhVienService;
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
    private SinhVienService sinhVienService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/")
    public String home() {
        return "home";
    }

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("sinhVien", new SinhVien());
        return "auth/register";
    }

    @PostMapping("/register")
    public String registerSinhVien(@ModelAttribute SinhVien sinhVien,
                                 BindingResult result,
                                 RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "auth/register";
        }

        try {
            // Kiểm tra trùng lặp
            if (sinhVienService.existsByTenDangNhap(sinhVien.getTenDangNhap())) {
                redirectAttributes.addFlashAttribute("error", "Tên đăng nhập đã tồn tại!");
                return "redirect:/register";
            }
            if (sinhVienService.existsByEmail(sinhVien.getEmail())) {
                redirectAttributes.addFlashAttribute("error", "Email đã được sử dụng!");
                return "redirect:/register";
            }
            if (sinhVienService.existsByMaSv(sinhVien.getMaSv())) {
                redirectAttributes.addFlashAttribute("error", "Mã sinh viên đã tồn tại!");
                return "redirect:/register";
            }

            // Mã hóa mật khẩu
            sinhVien.setMatKhau(passwordEncoder.encode(sinhVien.getMatKhau()));
            
            // Thiết lập vai trò mặc định là SINH_VIEN
            sinhVien.setVaiTro("SINH_VIEN");
            
            // Thiết lập trạng thái hoạt động
            sinhVien.setTrangThai(SinhVien.TrangThai.HOAT_DONG);

            // Lưu sinh viên
            sinhVienService.save(sinhVien);

            redirectAttributes.addFlashAttribute("success", "Đăng ký tài khoản thành công! Vui lòng đăng nhập.");
            return "redirect:/login";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Có lỗi xảy ra: " + e.getMessage());
            return "redirect:/register";
        }
    }
} 