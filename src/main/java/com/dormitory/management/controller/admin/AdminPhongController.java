package com.dormitory.management.controller.admin;

import com.dormitory.management.model.Phong;
import com.dormitory.management.model.Phong.TrangThai;
import com.dormitory.management.model.Phong.LoaiPhong;
import com.dormitory.management.service.PhongService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.sql.Timestamp;

@Controller
@RequestMapping("/admin/phong")
public class AdminPhongController {

    @Autowired
    private PhongService phongService;

    @GetMapping
    public String listPhong(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) LoaiPhong loaiPhong,
            @RequestParam(required = false) TrangThai trangThai,
            Model model) {
        Page<Phong> phongPage;
        
        if (search != null && !search.trim().isEmpty() || 
            loaiPhong != null || 
            trangThai != null) {
            phongPage = phongService.search(search, loaiPhong, trangThai, PageRequest.of(page, size));
        } else {
            phongPage = phongService.findAll(PageRequest.of(page, size));
        }
        
        model.addAttribute("phongPage", phongPage);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", phongPage.getTotalPages());
        model.addAttribute("totalItems", phongPage.getTotalElements());
        model.addAttribute("search", search);
        model.addAttribute("loaiPhong", loaiPhong);
        model.addAttribute("trangThai", trangThai);
        model.addAttribute("loaiPhongValues", LoaiPhong.values());
        model.addAttribute("trangThaiValues", TrangThai.values());
        
        return "admin/phong/list";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("phong", new Phong());
        model.addAttribute("loaiPhongValues", LoaiPhong.values());
        model.addAttribute("trangThaiValues", TrangThai.values());
        return "admin/phong/add";
    }

    @PostMapping("/add")
    public String addPhong(@ModelAttribute Phong phong, BindingResult result, RedirectAttributes redirectAttributes, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("loaiPhongValues", LoaiPhong.values());
            model.addAttribute("trangThaiValues", TrangThai.values());
            return "admin/phong/add";
        }

        try {
            // Kiểm tra trùng mã phòng
            if (phongService.findById(phong.getMaPhong()).isPresent()) {
                model.addAttribute("error", "Mã phòng đã tồn tại!");
                model.addAttribute("loaiPhongValues", LoaiPhong.values());
                model.addAttribute("trangThaiValues", TrangThai.values());
                return "admin/phong/add";
            }

            // Thiết lập ngày tạo
            phong.setNgayTao(new Timestamp(System.currentTimeMillis()));

            phongService.save(phong);
            redirectAttributes.addFlashAttribute("success", "Thêm phòng mới thành công!");
            return "redirect:/admin/phong";
        } catch (Exception e) {
            model.addAttribute("error", "Có lỗi xảy ra: " + e.getMessage());
            model.addAttribute("loaiPhongValues", LoaiPhong.values());
            model.addAttribute("trangThaiValues", TrangThai.values());
            return "admin/phong/add";
        }
    }

    @GetMapping("/edit/{maPhong}")
    public String showEditForm(@PathVariable String maPhong, Model model) {
        try {
            Phong phong = phongService.findById(maPhong)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy phòng với mã: " + maPhong));
            model.addAttribute("phong", phong);
            model.addAttribute("loaiPhongValues", LoaiPhong.values());
            model.addAttribute("trangThaiValues", TrangThai.values());
            return "admin/phong/edit";
        } catch (Exception e) {
            return "redirect:/admin/phong";
        }
    }

    @PostMapping("/edit")
    public String editPhong(@ModelAttribute Phong phong, BindingResult result, RedirectAttributes redirectAttributes, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("loaiPhongValues", LoaiPhong.values());
            model.addAttribute("trangThaiValues", TrangThai.values());
            return "admin/phong/edit";
        }

        try {
            // Kiểm tra phòng tồn tại
            Phong existingPhong = phongService.findById(phong.getMaPhong())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy phòng với mã: " + phong.getMaPhong()));

            // Giữ nguyên ngày tạo
            phong.setNgayTao(existingPhong.getNgayTao());

            phongService.save(phong);
            redirectAttributes.addFlashAttribute("success", "Cập nhật phòng thành công!");
            return "redirect:/admin/phong";
        } catch (Exception e) {
            model.addAttribute("error", "Có lỗi xảy ra: " + e.getMessage());
            model.addAttribute("loaiPhongValues", LoaiPhong.values());
            model.addAttribute("trangThaiValues", TrangThai.values());
            return "admin/phong/edit";
        }
    }

    @GetMapping("/delete/{maPhong}")
    public String deletePhong(@PathVariable String maPhong, RedirectAttributes redirectAttributes) {
        try {
            phongService.delete(maPhong);
            redirectAttributes.addFlashAttribute("success", "Xóa phòng thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Có lỗi xảy ra khi xóa phòng: " + e.getMessage());
        }
        return "redirect:/admin/phong";
    }
} 