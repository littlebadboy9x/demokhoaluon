package com.dormitory.management.controller.admin;

import com.dormitory.management.model.ThongBao;
import com.dormitory.management.service.ThongBaoService;
import com.dormitory.management.service.PhongService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/thong-bao")
public class AdminThongBaoController {

    @Autowired
    private ThongBaoService thongBaoService;

    @Autowired
    private PhongService phongService;

    @GetMapping
    public String listThongBao(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model) {
        Page<ThongBao> thongBaoPage = thongBaoService.findByTrangThai(true, 
            PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "ngayTao")));
        
        model.addAttribute("thongBaoPage", thongBaoPage);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", thongBaoPage.getTotalPages());
        
        return "admin/thong-bao/list";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        ThongBao thongBao = new ThongBao();
        thongBao.setLoaiThongBao(ThongBao.LoaiThongBao.THONG_BAO_CHUNG);
        thongBao.setTrangThai(true);
        model.addAttribute("thongBao", thongBao);
        model.addAttribute("phongList", phongService.findAll());
        return "admin/thong-bao/form";
    }

    @PostMapping("/save")
    public String saveThongBao(@ModelAttribute ThongBao thongBao, 
                              BindingResult result,
                              Authentication authentication,
                              RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "admin/thong-bao/form";
        }

        try {
            // Lấy username của người đang đăng nhập
            String nguoiTao = authentication.getName();
            thongBao.setNguoiTao(nguoiTao);

            // Xử lý loại thông báo dựa trên phòng được chọn
            if (thongBao.getPhong() != null) {
                thongBao.setLoaiThongBao(ThongBao.LoaiThongBao.THONG_BAO_PHONG);
            } else {
                thongBao.setLoaiThongBao(ThongBao.LoaiThongBao.THONG_BAO_CHUNG);
            }

            thongBaoService.save(thongBao);
            redirectAttributes.addFlashAttribute("success", 
                thongBao.getIdThongBao() == null ? "Thêm thông báo thành công!" : "Cập nhật thông báo thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Có lỗi xảy ra: " + e.getMessage());
        }

        return "redirect:/admin/thong-bao";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        ThongBao thongBao = thongBaoService.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy thông báo với id: " + id));
        
        model.addAttribute("thongBao", thongBao);
        model.addAttribute("phongList", phongService.findAll());
        return "admin/thong-bao/form";
    }

    @GetMapping("/delete/{id}")
    public String deleteThongBao(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            thongBaoService.delete(id);
            redirectAttributes.addFlashAttribute("success", "Xóa thông báo thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Có lỗi xảy ra khi xóa thông báo: " + e.getMessage());
        }
        return "redirect:/admin/thong-bao";
    }
} 