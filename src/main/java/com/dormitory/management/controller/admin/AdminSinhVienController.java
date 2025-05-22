package com.dormitory.management.controller.admin;

import com.dormitory.management.model.SinhVien;
import com.dormitory.management.model.NguoiDung;
import com.dormitory.management.service.SinhVienService;
import com.dormitory.management.service.NguoiDungService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.sql.Timestamp;
import java.util.Date;

@Controller
@RequestMapping("/admin/sinh-vien")
public class AdminSinhVienController {

    @Autowired
    private SinhVienService sinhVienService;

    @Autowired
    private NguoiDungService nguoiDungService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping
    public String listSinhVien(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model) {
        Page<SinhVien> sinhVienPage = sinhVienService.findAll(PageRequest.of(page, size));
        
        model.addAttribute("sinhVienPage", sinhVienPage);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", sinhVienPage.getTotalPages());
        model.addAttribute("totalItems", sinhVienPage.getTotalElements());
        
        return "admin/sinh-vien/list";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("sinhVien", new SinhVien());
        return "admin/sinh-vien/add";
    }

    @PostMapping("/add")
    public String addSinhVien(@ModelAttribute SinhVien sinhVien, BindingResult result, RedirectAttributes redirectAttributes, Model model) {
        if (result.hasErrors()) {
            return "admin/sinh-vien/add";
        }

        try {
            // Kiểm tra trùng mã sinh viên
            if (sinhVienService.findById(sinhVien.getMaSv()).isPresent()) {
                model.addAttribute("error", "Mã sinh viên đã tồn tại!");
                return "admin/sinh-vien/add";
            }

            // Kiểm tra trùng tên đăng nhập
            if (nguoiDungService.existsByTenDangNhap(sinhVien.getTenDangNhap())) {
                model.addAttribute("error", "Tên đăng nhập đã tồn tại trong hệ thống!");
                return "admin/sinh-vien/add";
            }

            // Tạo tài khoản người dùng trước
            NguoiDung nguoiDung = new NguoiDung();
            nguoiDung.setTenDangNhap(sinhVien.getTenDangNhap());
            // Mặc định mật khẩu là CCCD, nếu không có CCCD thì dùng mã sinh viên
            String matKhau = sinhVien.getCccd() != null ? sinhVien.getCccd() : sinhVien.getMaSv();
            nguoiDung.setMatKhau(passwordEncoder.encode(matKhau));
            nguoiDung.setLoaiNguoiDung(NguoiDung.LoaiNguoiDung.SINH_VIEN);
            nguoiDung.setTrangThai(true);
            nguoiDung.setNgayTao(new Timestamp(System.currentTimeMillis()));
            nguoiDungService.save(nguoiDung);

            // Thiết lập thông tin sinh viên
            sinhVien.setNgayDangKy(new Date());
            sinhVien.setTrangThai(SinhVien.TrangThai.DANG_O);

            // Lưu thông tin sinh viên
            sinhVienService.save(sinhVien);
            
            redirectAttributes.addFlashAttribute("success", "Thêm sinh viên mới thành công! Mật khẩu mặc định là: " + matKhau);
            return "redirect:/admin/sinh-vien";
        } catch (Exception e) {
            model.addAttribute("error", "Có lỗi xảy ra: " + e.getMessage());
            return "admin/sinh-vien/add";
        }
    }

    @GetMapping("/edit/{maSv}")
    public String showEditForm(@PathVariable String maSv, Model model) {
        SinhVien sinhVien = sinhVienService.findById(maSv)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sinh viên với mã: " + maSv));
        model.addAttribute("sinhVien", sinhVien);
        return "admin/sinh-vien/edit";
    }

    @PostMapping("/edit")
    public String editSinhVien(@ModelAttribute SinhVien sinhVien, BindingResult result, RedirectAttributes redirectAttributes, Model model) {
        if (result.hasErrors()) {
            return "admin/sinh-vien/edit";
        }

        try {
            // Lấy thông tin sinh viên hiện tại
            SinhVien existingSinhVien = sinhVienService.findById(sinhVien.getMaSv())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy sinh viên với mã: " + sinhVien.getMaSv()));

            // Giữ nguyên các thông tin không được phép thay đổi
            sinhVien.setTenDangNhap(existingSinhVien.getTenDangNhap());
            sinhVien.setNgayDangKy(existingSinhVien.getNgayDangKy());

            sinhVienService.save(sinhVien);
            redirectAttributes.addFlashAttribute("success", "Cập nhật sinh viên thành công!");
            return "redirect:/admin/sinh-vien";
        } catch (Exception e) {
            model.addAttribute("error", "Có lỗi xảy ra: " + e.getMessage());
            return "admin/sinh-vien/edit";
        }
    }

    @GetMapping("/delete/{maSv}")
    public String deleteSinhVien(@PathVariable String maSv, RedirectAttributes redirectAttributes) {
        try {
            sinhVienService.delete(maSv);
            redirectAttributes.addFlashAttribute("success", "Xóa sinh viên thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Có lỗi xảy ra khi xóa sinh viên: " + e.getMessage());
        }
        return "redirect:/admin/sinh-vien";
    }
} 