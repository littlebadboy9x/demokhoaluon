package com.dormitory.management.controller.admin;

import com.dormitory.management.model.SinhVien;
import com.dormitory.management.service.SinhVienService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/sinh-vien")
public class AdminSinhVienController {

    @Autowired
    private SinhVienService sinhVienService;

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
        return "admin/sinh-vien/form";
    }

    @GetMapping("/edit/{maSv}")
    public String showEditForm(@PathVariable String maSv, Model model) {
        SinhVien sinhVien = sinhVienService.findById(maSv)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sinh viên với mã: " + maSv));
        model.addAttribute("sinhVien", sinhVien);
        return "admin/sinh-vien/form";
    }

    @PostMapping("/save")
    public String saveSinhVien(@ModelAttribute SinhVien sinhVien) {
        sinhVienService.save(sinhVien);
        return "redirect:/admin/sinh-vien";
    }

    @GetMapping("/delete/{maSv}")
    public String deleteSinhVien(@PathVariable String maSv) {
        sinhVienService.delete(maSv);
        return "redirect:/admin/sinh-vien";
    }
} 