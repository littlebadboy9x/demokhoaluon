package com.dormitory.management.controller.admin;

import com.dormitory.management.model.DangKyPhong;
import com.dormitory.management.model.HoaDon;
import com.dormitory.management.model.SuCo;
import com.dormitory.management.model.Phong.TrangThai;
import com.dormitory.management.service.PhongService;
import com.dormitory.management.service.SinhVienService;
import com.dormitory.management.service.DangKyPhongService;
import com.dormitory.management.service.HoaDonService;
import com.dormitory.management.service.SuCoService;
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
    private PhongService phongService;

    @Autowired
    private SinhVienService sinhVienService;

    @Autowired
    private DangKyPhongService dangKyPhongService;

    @Autowired
    private HoaDonService hoaDonService;

    @Autowired
    private SuCoService suCoService;

    @GetMapping
    public String dashboard(Model model) {
        // Thống kê phòng
        Map<String, Long> phongStats = new HashMap<>();
        phongStats.put("CON_TRONG", phongService.countByTrangThai(TrangThai.CON_TRONG));
        phongStats.put("DA_DU", phongService.countByTrangThai(TrangThai.DA_DU));
        phongStats.put("DANG_SUA_CHUA", phongService.countByTrangThai(TrangThai.DANG_SUA_CHUA));
        model.addAttribute("phongStats", phongStats);

        // Tổng số sinh viên
        model.addAttribute("totalSinhVien", sinhVienService.count());

        // Đơn đăng ký chờ duyệt
        model.addAttribute("pendingRegistrations", dangKyPhongService.countByTrangThai(DangKyPhong.TrangThai.CHO_DUYET));

        // Hóa đơn chưa thanh toán
        model.addAttribute("unpaidBills", hoaDonService.countByTrangThai(HoaDon.TrangThai.CHUA_THANH_TOAN));

        // Sự cố chờ xử lý
        model.addAttribute("pendingIssues", suCoService.countByTrangThai(SuCo.TrangThai.CHUA_XU_LY));

        return "admin/dashboard";
    }
} 