package com.dormitory.management.controller.admin;

import com.dormitory.management.model.HoaDon;
import com.dormitory.management.service.HoaDonService;
import com.dormitory.management.service.PhongService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@Controller
@RequestMapping("/admin/hoa-don")
public class AdminHoaDonController {

    @Autowired
    private HoaDonService hoaDonService;

    @Autowired
    private PhongService phongService;

    @GetMapping
    public String listHoaDon(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model) {
        Page<HoaDon> hoaDonPage = hoaDonService.findAll(PageRequest.of(page, size));
        
        model.addAttribute("hoaDonPage", hoaDonPage);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", hoaDonPage.getTotalPages());
        model.addAttribute("totalItems", hoaDonPage.getTotalElements());
        
        return "admin/hoa-don/list";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("hoaDon", new HoaDon());
        model.addAttribute("phongList", phongService.findAll());
        return "admin/hoa-don/form";
    }

    @GetMapping("/edit/{maHoaDon}")
    public String showEditForm(@PathVariable String maHoaDon, Model model) {
        HoaDon hoaDon = hoaDonService.findById(maHoaDon)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy hóa đơn với mã: " + maHoaDon));
        model.addAttribute("hoaDon", hoaDon);
        model.addAttribute("phongList", phongService.findAll());
        return "admin/hoa-don/form";
    }

    @PostMapping("/save")
    public String saveHoaDon(@ModelAttribute HoaDon hoaDon) {
        if (hoaDon.getMaHoaDon() == null) {
            hoaDon.setNgayTao(new Date());
        }
        hoaDonService.save(hoaDon);
        return "redirect:/admin/hoa-don";
    }

    @GetMapping("/delete/{maHoaDon}")
    public String deleteHoaDon(@PathVariable String maHoaDon) {
        hoaDonService.delete(maHoaDon);
        return "redirect:/admin/hoa-don";
    }

    @GetMapping("/thanh-toan/{maHoaDon}")
    public String thanhToanHoaDon(@PathVariable String maHoaDon) {
        HoaDon hoaDon = hoaDonService.findById(maHoaDon)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy hóa đơn với mã: " + maHoaDon));
        hoaDon.setTrangThai(HoaDon.TrangThai.DA_THANH_TOAN);
        hoaDon.setNgayThanhToan(new Date());
        hoaDonService.save(hoaDon);
        return "redirect:/admin/hoa-don";
    }

    @GetMapping("/huy/{maHoaDon}")
    public String huyHoaDon(@PathVariable String maHoaDon) {
        HoaDon hoaDon = hoaDonService.findById(maHoaDon)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy hóa đơn với mã: " + maHoaDon));
        hoaDon.setTrangThai(HoaDon.TrangThai.DA_HUY);
        hoaDonService.save(hoaDon);
        return "redirect:/admin/hoa-don";
    }
} 