package com.dormitory.management.controller;

import com.dormitory.management.model.SinhVien;
import com.dormitory.management.model.ThongBao;
import com.dormitory.management.service.SinhVienService;
import com.dormitory.management.service.ThongBaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/thong-bao")
public class ThongBaoController {

    @Autowired
    private ThongBaoService thongBaoService;

    @Autowired
    private SinhVienService sinhVienService;

    @GetMapping
    public String listThongBao(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Authentication authentication,
            Model model) {
        
        // Lấy thông tin sinh viên đang đăng nhập
        String tenDangNhap = authentication.getName();
        SinhVien sinhVien = sinhVienService.findByTenDangNhap(tenDangNhap)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy thông tin sinh viên"));

        // Lấy danh sách thông báo cho phòng của sinh viên và thông báo chung
        Page<ThongBao> thongBaoPage = thongBaoService.findByPhongAndTrangThai(
            sinhVien.getPhong(), true, PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "ngayTao")));

        model.addAttribute("thongBaoPage", thongBaoPage);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", thongBaoPage.getTotalPages());

        return "thong-bao/list";
    }

    @GetMapping("/{id}")
    public String viewThongBao(@PathVariable Long id, Authentication authentication, Model model) {
        // Lấy thông tin sinh viên đang đăng nhập
        String tenDangNhap = authentication.getName();
        SinhVien sinhVien = sinhVienService.findByTenDangNhap(tenDangNhap)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy thông tin sinh viên"));

        // Lấy thông tin thông báo
        ThongBao thongBao = thongBaoService.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy thông báo"));

        // Kiểm tra quyền xem thông báo
        if (thongBao.getPhong() != null && !thongBao.getPhong().equals(sinhVien.getPhong())) {
            throw new RuntimeException("Bạn không có quyền xem thông báo này");
        }

        model.addAttribute("thongBao", thongBao);
        return "thong-bao/view";
    }
} 