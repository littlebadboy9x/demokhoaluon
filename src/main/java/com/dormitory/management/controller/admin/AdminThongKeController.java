package com.dormitory.management.controller.admin;

import com.dormitory.management.model.DangKyPhong;
import com.dormitory.management.model.HoaDon;
import com.dormitory.management.model.SuCo;
import com.dormitory.management.model.Phong.TrangThai;
import com.dormitory.management.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/admin/thong-ke")
public class AdminThongKeController {

    @Autowired
    private ThongKeService thongKeService;

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
    public String thongKe(Model model) {
        // Thống kê tổng quan
        model.addAttribute("tongSoPhong", phongService.demTongSoPhong());
        model.addAttribute("soPhongTrong", phongService.demSoPhongConTrong());
        model.addAttribute("soPhongSuaChua", phongService.demSoPhongDangSuaChua());
        model.addAttribute("tongSoSinhVien", sinhVienService.demTongSoSinhVien());

        // Thống kê đăng ký phòng
        model.addAttribute("soDangKyChoDuyet", dangKyPhongService.countByTrangThai(DangKyPhong.TrangThai.CHO_DUYET));
        model.addAttribute("soDangKyDaDuyet", dangKyPhongService.countByTrangThai(DangKyPhong.TrangThai.DA_DUYET));
        model.addAttribute("soDangKyTuChoi", dangKyPhongService.countByTrangThai(DangKyPhong.TrangThai.TU_CHOI));

        // Thống kê hóa đơn
        model.addAttribute("soHoaDonChuaThanhToan", hoaDonService.countByTrangThai(HoaDon.TrangThai.CHUA_THANH_TOAN));
        model.addAttribute("soHoaDonDaThanhToan", hoaDonService.countByTrangThai(HoaDon.TrangThai.DA_THANH_TOAN));

        // Thống kê sự cố
                model.addAttribute("soSuCoChuaXuLy", suCoService.countByTrangThai(SuCo.TrangThai.CHO_XU_LY));        
                model.addAttribute("soSuCoDangXuLy", suCoService.countByTrangThai(SuCo.TrangThai.DANG_XU_LY));        
                model.addAttribute("soSuCoDaXuLy", suCoService.countByTrangThai(SuCo.TrangThai.DA_HOAN_THANH));

        // Thống kê doanh thu
        model.addAttribute("doanhThuThangHienTai", thongKeService.tinhDoanhThuThangHienTai());
        model.addAttribute("doanhThuNamHienTai", thongKeService.tinhDoanhThuNamHienTai());
        model.addAttribute("doanhThu6ThangGanNhat", thongKeService.thongKeDoanhThu6ThangGanNhat());

        // Thống kê tỷ lệ sinh viên theo khoa
        model.addAttribute("tyLeSinhVienTheoKhoa", thongKeService.thongKeTyLeSinhVienTheoKhoa());

        return "admin/thong-ke/index";
    }

    @GetMapping("/doanh-thu")
    public String thongKeDoanhThu(
            @RequestParam(required = false) Integer thang,
            @RequestParam(required = false) Integer nam,
            Model model) {
        
        if (thang == null || nam == null) {
            LocalDate now = LocalDate.now();
            thang = now.getMonthValue();
            nam = now.getYear();
        }

        Double doanhThu = hoaDonService.tinhTongDoanhThuTheoThangNam(thang, nam);
        model.addAttribute("thang", thang);
        model.addAttribute("nam", nam);
        model.addAttribute("doanhThu", doanhThu);
        model.addAttribute("doanhThu6ThangGanNhat", thongKeService.thongKeDoanhThu6ThangGanNhat());

        return "admin/thong-ke/doanh-thu";
    }

    @GetMapping("/phong")
    public String thongKePhong(Model model) {
        Map<String, Long> thongKePhong = new HashMap<>();
        thongKePhong.put("TONG_SO", phongService.demTongSoPhong());
        thongKePhong.put("CON_TRONG", phongService.demSoPhongConTrong());
        thongKePhong.put("DA_DU", phongService.countByTrangThai(TrangThai.DA_DU));
        thongKePhong.put("DANG_SUA_CHUA", phongService.demSoPhongDangSuaChua());

        model.addAttribute("thongKePhong", thongKePhong);
        return "admin/thong-ke/phong";
    }

    @GetMapping("/sinh-vien")
    public String thongKeSinhVien(Model model) {
        model.addAttribute("tongSoSinhVien", sinhVienService.demTongSoSinhVien());
        model.addAttribute("tyLeSinhVienTheoKhoa", thongKeService.thongKeTyLeSinhVienTheoKhoa());
        return "admin/thong-ke/sinh-vien";
    }

    @GetMapping("/su-co")
    public String thongKeSuCo(Model model) {
        Map<String, Long> thongKeSuCo = new HashMap<>();
                thongKeSuCo.put("CHO_XU_LY", suCoService.countByTrangThai(SuCo.TrangThai.CHO_XU_LY));        thongKeSuCo.put("DANG_XU_LY", suCoService.countByTrangThai(SuCo.TrangThai.DANG_XU_LY));        thongKeSuCo.put("DA_HOAN_THANH", suCoService.countByTrangThai(SuCo.TrangThai.DA_HOAN_THANH));        Map<String, Long> thongKeMucDo = new HashMap<>();        thongKeMucDo.put("THAP", suCoService.countByMucDoUuTien(SuCo.MucDoUuTien.THAP));        thongKeMucDo.put("TRUNG_BINH", suCoService.countByMucDoUuTien(SuCo.MucDoUuTien.TRUNG_BINH));        thongKeMucDo.put("CAO", suCoService.countByMucDoUuTien(SuCo.MucDoUuTien.CAO));

        model.addAttribute("thongKeSuCo", thongKeSuCo);
        model.addAttribute("thongKeMucDo", thongKeMucDo);
        return "admin/thong-ke/su-co";
    }
} 