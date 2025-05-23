package com.dormitory.management.controller;

import com.dormitory.management.exception.ResourceNotFoundException;
import com.dormitory.management.model.SuCo;
import com.dormitory.management.model.SinhVien;
import com.dormitory.management.service.SuCoService;
import com.dormitory.management.service.SinhVienService;
import com.dormitory.management.service.PhongService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

@Controller
@RequestMapping("/sinh-vien/su-co")
public class SuCoController {

    @Autowired
    private SuCoService suCoService;

    @Autowired
    private SinhVienService sinhVienService;

    @Autowired
    private PhongService phongService;

    @GetMapping
    public String listSuCo(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Authentication authentication,
            Model model) {
        
        // Lấy thông tin sinh viên đang đăng nhập
        SinhVien sinhVien = sinhVienService.findByTenDangNhap(authentication.getName())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy thông tin sinh viên"));

        // Lấy danh sách sự cố của sinh viên
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "ngayBaoCao"));
        Page<SuCo> suCoPage = suCoService.findAll(pageRequest);

        // Chuẩn bị dữ liệu cho view
        Map<SuCo.TrangThai, String> trangThaiMap = new LinkedHashMap<>();
        trangThaiMap.put(SuCo.TrangThai.CHO_XU_LY, "Chờ xử lý");
        trangThaiMap.put(SuCo.TrangThai.DANG_XU_LY, "Đang xử lý");
        trangThaiMap.put(SuCo.TrangThai.DA_HOAN_THANH, "Đã hoàn thành");
        trangThaiMap.put(SuCo.TrangThai.HUY, "Hủy");

        Map<SuCo.MucDoUuTien, String> mucDoUuTienMap = new LinkedHashMap<>();
        mucDoUuTienMap.put(SuCo.MucDoUuTien.THAP, "Thấp");
        mucDoUuTienMap.put(SuCo.MucDoUuTien.TRUNG_BINH, "Trung bình");
        mucDoUuTienMap.put(SuCo.MucDoUuTien.CAO, "Cao");
        mucDoUuTienMap.put(SuCo.MucDoUuTien.KHAN_CAP, "Khẩn cấp");

        model.addAttribute("suCoPage", suCoPage);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", suCoPage.getTotalPages());
        model.addAttribute("trangThaiMap", trangThaiMap);
        model.addAttribute("mucDoUuTienMap", mucDoUuTienMap);

        return "sinh-vien/su-co/list";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model, Authentication authentication, RedirectAttributes redirectAttributes) {
        // Lấy thông tin sinh viên đang đăng nhập
        SinhVien sinhVien = sinhVienService.findByTenDangNhap(authentication.getName())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy thông tin sinh viên"));

        // Kiểm tra nếu sinh viên chưa được phân phòng
        if (sinhVien.getPhong() == null) {
            redirectAttributes.addFlashAttribute("error", "Bạn chưa được phân phòng nên không thể báo cáo sự cố!");
            return "redirect:/sinh-vien/su-co";
        }

        // Tạo đối tượng sự cố mới
        SuCo suCo = new SuCo();
        suCo.setSinhVien(sinhVien);
        suCo.setPhong(sinhVien.getPhong());
        suCo.setTrangThai(SuCo.TrangThai.CHO_XU_LY);

        model.addAttribute("suCo", suCo);
        model.addAttribute("loaiSuCoList", SuCo.LoaiSuCo.values());
        model.addAttribute("mucDoUuTienList", SuCo.MucDoUuTien.values());

        return "sinh-vien/su-co/form";
    }

    @PostMapping("/save")
    public String saveSuCo(@ModelAttribute SuCo suCo,
                          Authentication authentication,
                          RedirectAttributes redirectAttributes) {
        try {
            // Lấy thông tin sinh viên đang đăng nhập
            SinhVien sinhVien = sinhVienService.findByTenDangNhap(authentication.getName())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy thông tin sinh viên"));

            // Kiểm tra nếu sinh viên chưa được phân phòng
            if (sinhVien.getPhong() == null) {
                redirectAttributes.addFlashAttribute("error", "Bạn chưa được phân phòng nên không thể báo cáo sự cố!");
                return "redirect:/sinh-vien/su-co";
            }

            // Thiết lập thông tin cho sự cố
            suCo.setSinhVien(sinhVien);
            suCo.setPhong(sinhVien.getPhong());
            suCo.setNgayBaoCao(new Date());
            suCo.setTrangThai(SuCo.TrangThai.CHO_XU_LY);

            // Lưu sự cố
            suCoService.save(suCo);

            redirectAttributes.addFlashAttribute("success", "Báo cáo sự cố thành công!");
            return "redirect:/sinh-vien/su-co";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Có lỗi xảy ra: " + e.getMessage());
            return "redirect:/sinh-vien/su-co/create";
        }
    }
} 