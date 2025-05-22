package com.dormitory.management.controller.admin;

import com.dormitory.management.model.Phong;
import com.dormitory.management.service.PhongService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/phong")
public class AdminPhongController {

    @Autowired
    private PhongService phongService;

    @GetMapping
    public String listPhong(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model) {
        Page<Phong> phongPage = phongService.findAll(PageRequest.of(page, size));
        
        model.addAttribute("phongPage", phongPage);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", phongPage.getTotalPages());
        model.addAttribute("totalItems", phongPage.getTotalElements());
        
        return "admin/phong/list";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("phong", new Phong());
        return "admin/phong/form";
    }

    @GetMapping("/edit/{maPhong}")
    public String showEditForm(@PathVariable String maPhong, Model model) {
        Phong phong = phongService.findById(maPhong)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy phòng với mã: " + maPhong));
        model.addAttribute("phong", phong);
        return "admin/phong/form";
    }

    @PostMapping("/save")
    public String savePhong(@ModelAttribute Phong phong) {
        phongService.save(phong);
        return "redirect:/admin/phong";
    }

    @GetMapping("/delete/{maPhong}")
    public String deletePhong(@PathVariable String maPhong) {
        phongService.delete(maPhong);
        return "redirect:/admin/phong";
    }
} 