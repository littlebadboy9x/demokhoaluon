package com.dormitory.management.controller.admin;

import com.dormitory.management.model.SuCo;
import com.dormitory.management.model.SuCo.TrangThai;
import com.dormitory.management.model.SuCo.MucDoUuTien;
import com.dormitory.management.model.SuCo.LoaiSuCo;
import com.dormitory.management.service.SuCoService;
import com.dormitory.management.service.PhongService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

@Controller
@RequestMapping("/admin/su-co")
public class AdminSuCoController {

    @Autowired
    private SuCoService suCoService;

    @Autowired
    private PhongService phongService;

    private Map<TrangThai, String> getTrangThaiMap() {
        Map<TrangThai, String> map = new LinkedHashMap<>();
        map.put(TrangThai.CHO_XU_LY, "Chờ xử lý");
        map.put(TrangThai.DANG_XU_LY, "Đang xử lý");
        map.put(TrangThai.DA_HOAN_THANH, "Đã hoàn thành");
        map.put(TrangThai.HUY, "Hủy");
        return map;
    }

    private Map<MucDoUuTien, String> getMucDoUuTienMap() {
        Map<MucDoUuTien, String> map = new LinkedHashMap<>();
        map.put(MucDoUuTien.THAP, "Thấp");
        map.put(MucDoUuTien.TRUNG_BINH, "Trung bình");
        map.put(MucDoUuTien.CAO, "Cao");
        map.put(MucDoUuTien.KHAN_CAP, "Khẩn cấp");
        return map;
    }

    private Map<LoaiSuCo, String> getLoaiSuCoMap() {
        Map<LoaiSuCo, String> map = new LinkedHashMap<>();
        map.put(LoaiSuCo.DIEN, "Điện");
        map.put(LoaiSuCo.NUOC, "Nước");
        map.put(LoaiSuCo.CO_SO_VAT_CHAT, "Cơ sở vật chất");
        map.put(LoaiSuCo.KHAC, "Khác");
        return map;
    }

    private void addCommonAttributes(Model model) {
        model.addAttribute("phongList", phongService.findAll());
        model.addAttribute("trangThaiMap", getTrangThaiMap());
        model.addAttribute("mucDoUuTienMap", getMucDoUuTienMap());
        model.addAttribute("loaiSuCoMap", getLoaiSuCoMap());
    }

    @GetMapping
    public String listSuCo(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) TrangThai trangThai,
            @RequestParam(required = false) MucDoUuTien mucDoUuTien,
            @RequestParam(required = false) String maPhong,
            Model model) {
        
        Page<SuCo> suCoPage = suCoService.search(search, trangThai, mucDoUuTien, maPhong, PageRequest.of(page, size));
        
        model.addAttribute("suCoPage", suCoPage);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", suCoPage.getTotalPages());
        model.addAttribute("totalItems", suCoPage.getTotalElements());
        model.addAttribute("search", search);
        model.addAttribute("trangThai", trangThai);
        model.addAttribute("mucDoUuTien", mucDoUuTien);
        model.addAttribute("maPhong", maPhong);
        
        addCommonAttributes(model);
        
        return "admin/su-co/list";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("suCo", new SuCo());
        addCommonAttributes(model);
        return "admin/su-co/form";
    }

    @PostMapping("/add")
    public String addSuCo(@ModelAttribute SuCo suCo, BindingResult result, RedirectAttributes redirectAttributes, Model model) {
        if (result.hasErrors()) {
            addCommonAttributes(model);
            return "admin/su-co/form";
        }

        try {
            suCo.setNgayBaoCao(new Date());
            suCo.setTrangThai(TrangThai.CHO_XU_LY);
            suCoService.save(suCo);
            redirectAttributes.addFlashAttribute("success", "Thêm sự cố thành công!");
            return "redirect:/admin/su-co";
        } catch (Exception e) {
            model.addAttribute("error", "Có lỗi xảy ra: " + e.getMessage());
            addCommonAttributes(model);
            return "admin/su-co/form";
        }
    }

    @GetMapping("/edit/{idSuCo}")
    public String showEditForm(@PathVariable Long idSuCo, Model model) {
        try {
            SuCo suCo = suCoService.findById(idSuCo)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy sự cố với ID: " + idSuCo));
            model.addAttribute("suCo", suCo);
            addCommonAttributes(model);
            return "admin/su-co/form";
        } catch (Exception e) {
            return "redirect:/admin/su-co";
        }
    }

    @PostMapping("/edit")
    public String editSuCo(@ModelAttribute SuCo suCo, BindingResult result, RedirectAttributes redirectAttributes, Model model) {
        if (result.hasErrors()) {
            addCommonAttributes(model);
            return "admin/su-co/form";
        }

        try {
            SuCo existingSuCo = suCoService.findById(suCo.getIdSuCo())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy sự cố với ID: " + suCo.getIdSuCo()));

            // Giữ nguyên ngày báo cáo
            suCo.setNgayBaoCao(existingSuCo.getNgayBaoCao());

            // Cập nhật ngày hoàn thành nếu trạng thái là đã hoàn thành
            if (suCo.getTrangThai() == TrangThai.DA_HOAN_THANH) {
                suCo.setNgayHoanThanh(new Date());
            }

            suCoService.save(suCo);
            redirectAttributes.addFlashAttribute("success", "Cập nhật sự cố thành công!");
            return "redirect:/admin/su-co";
        } catch (Exception e) {
            model.addAttribute("error", "Có lỗi xảy ra: " + e.getMessage());
            addCommonAttributes(model);
            return "admin/su-co/form";
        }
    }

    @GetMapping("/delete/{idSuCo}")
    public String deleteSuCo(@PathVariable Long idSuCo, RedirectAttributes redirectAttributes) {
        try {
            suCoService.delete(idSuCo);
            redirectAttributes.addFlashAttribute("success", "Xóa sự cố thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Có lỗi xảy ra khi xóa sự cố: " + e.getMessage());
        }
        return "redirect:/admin/su-co";
    }
} 