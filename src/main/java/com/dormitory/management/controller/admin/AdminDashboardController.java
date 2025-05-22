package com.dormitory.management.controller.admin;

import com.dormitory.management.model.HoaDon.TrangThai;
import com.dormitory.management.model.Phong;
import com.dormitory.management.model.DangKyPhong;
import com.dormitory.management.model.SinhVien;
import com.dormitory.management.model.SuCo;
import com.dormitory.management.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/admin")
public class AdminDashboardController {

    @Autowired
    private SinhVienService sinhVienService;

    @Autowired
    private PhongService phongService;

    @Autowired
    private DangKyPhongService dangKyPhongService;

    @Autowired
    private HoaDonService hoaDonService;

    @Autowired
    private SuCoService suCoService;

    @GetMapping({"", "/", "/dashboard"})
    public String dashboard(Model model) {
        // Thống kê sinh viên
        long totalSinhVien = sinhVienService.countByTrangThai(SinhVien.TrangThai.DANG_O);
        
        // Thống kê phòng
        Map<String, Long> phongStats = new HashMap<>();
        phongStats.put("CON_TRONG", phongService.countByTrangThai(Phong.TrangThai.CON_TRONG));
        phongStats.put("DA_DU", phongService.countByTrangThai(Phong.TrangThai.DA_DU));
        phongStats.put("DANG_SUA_CHUA", phongService.countByTrangThai(Phong.TrangThai.DANG_SUA_CHUA));
        
        // Thống kê đăng ký
        long pendingRegistrations = dangKyPhongService.countByTrangThai(DangKyPhong.TrangThai.CHO_DUYET);
        
        // Thống kê hóa đơn
        long unpaidBills = hoaDonService.countByTrangThai(TrangThai.CHUA_THANH_TOAN);
        
        // Thống kê sự cố
        long pendingIssues = suCoService.countByTrangThai(SuCo.TrangThai.CHUA_XU_LY);

        // Thêm dữ liệu vào model
        model.addAttribute("totalSinhVien", totalSinhVien);
        model.addAttribute("phongStats", phongStats);
        model.addAttribute("pendingRegistrations", pendingRegistrations);
        model.addAttribute("unpaidBills", unpaidBills);
        model.addAttribute("pendingIssues", pendingIssues);

        return "admin/dashboard";
    }
} 