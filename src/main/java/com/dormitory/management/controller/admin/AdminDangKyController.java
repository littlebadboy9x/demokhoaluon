package com.dormitory.management.controller.admin;

import com.dormitory.management.model.DangKyPhong;
import com.dormitory.management.service.DangKyPhongService;
import com.dormitory.management.service.PhongService;
import com.dormitory.management.service.SinhVienService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/dang-ky")
public class AdminDangKyController {

    @Autowired
    private DangKyPhongService dangKyPhongService;

    @Autowired
    private SinhVienService sinhVienService;

    @Autowired
    private PhongService phongService;

    @GetMapping
    public String listDangKy(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model) {
        Page<DangKyPhong> dangKyPage = dangKyPhongService.findAll(PageRequest.of(page, size));
        
        model.addAttribute("dangKyPage", dangKyPage);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", dangKyPage.getTotalPages());
        model.addAttribute("totalItems", dangKyPage.getTotalElements());
        
        return "admin/dang-ky/list";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("dangKyPhong", new DangKyPhong());
        model.addAttribute("sinhVienList", sinhVienService.findAll());
        model.addAttribute("phongList", phongService.findAll());
        return "admin/dang-ky/form";
    }

    @GetMapping("/edit/{idDangKy}")
    public String showEditForm(@PathVariable Long idDangKy, Model model) {
        DangKyPhong dangKyPhong = dangKyPhongService.findById(idDangKy)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đăng ký với ID: " + idDangKy));
        model.addAttribute("dangKyPhong", dangKyPhong);
        model.addAttribute("sinhVienList", sinhVienService.findAll());
        model.addAttribute("phongList", phongService.findAll());
        return "admin/dang-ky/form";
    }

    @PostMapping("/save")
    public String saveDangKy(@ModelAttribute DangKyPhong dangKyPhong) {
        dangKyPhongService.save(dangKyPhong);
        return "redirect:/admin/dang-ky";
    }

    @GetMapping("/delete/{idDangKy}")
    public String deleteDangKy(@PathVariable Long idDangKy) {
        dangKyPhongService.delete(idDangKy);
        return "redirect:/admin/dang-ky";
    }

    @GetMapping("/duyet/{idDangKy}")
    public String duyetDangKy(@PathVariable Long idDangKy) {
        DangKyPhong dangKyPhong = dangKyPhongService.findById(idDangKy)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đăng ký với ID: " + idDangKy));
        dangKyPhong.setTrangThai(DangKyPhong.TrangThai.DA_DUYET);
        dangKyPhongService.save(dangKyPhong);
        return "redirect:/admin/dang-ky";
    }

    @GetMapping("/tu-choi/{idDangKy}")
    public String tuChoiDangKy(@PathVariable Long idDangKy) {
        DangKyPhong dangKyPhong = dangKyPhongService.findById(idDangKy)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đăng ký với ID: " + idDangKy));
        dangKyPhong.setTrangThai(DangKyPhong.TrangThai.TU_CHOI);
        dangKyPhongService.save(dangKyPhong);
        return "redirect:/admin/dang-ky";
    }
} 