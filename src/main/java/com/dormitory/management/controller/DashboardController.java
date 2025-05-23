package com.dormitory.management.controller;

import com.dormitory.management.service.SinhVienService;
import com.dormitory.management.service.DangKyPhongService;
import com.dormitory.management.model.SinhVien;
import com.dormitory.management.model.DangKyPhong;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    @Autowired
    private SinhVienService sinhVienService;

    @Autowired
    private DangKyPhongService dangKyPhongService;

    @GetMapping("/dashboard")
    public String dashboard(Authentication authentication) {
        if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            return "redirect:/admin";
        } else if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_SINH_VIEN"))) {
            String tenDangNhap = authentication.getName();
            
            // Kiểm tra đã đăng ký thông tin sinh viên chưa
            if (!sinhVienService.existsByTenDangNhap(tenDangNhap)) {
                return "redirect:/sinh-vien/dang-ky-thong-tin";
            }
            
            // Kiểm tra đã đăng ký phòng chưa
            SinhVien sinhVien = sinhVienService.findByTenDangNhap(tenDangNhap)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy thông tin sinh viên"));
            
            boolean daDangKyPhong = dangKyPhongService.existsBySinhVienAndTrangThaiIn(
                sinhVien,
                DangKyPhong.TrangThai.CHO_DUYET,
                DangKyPhong.TrangThai.DA_DUYET
            );
            
            if (!daDangKyPhong) {
                return "redirect:/sinh-vien/dang-ky-phong";
            }
            
            return "redirect:/sinh-vien/thong-tin-phong";
        }
        return "redirect:/";
    }
} 