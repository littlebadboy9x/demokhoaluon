package com.dormitory.management.service;

import com.dormitory.management.model.HoaDon;
import com.dormitory.management.model.HoaDon.TrangThai;
import com.dormitory.management.model.SinhVien;
import com.dormitory.management.repository.HoaDonRepository;
import com.dormitory.management.repository.SinhVienRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ThongKeService {

    private final HoaDonRepository hoaDonRepository;
    private final SinhVienRepository sinhVienRepository;

    public Double tinhDoanhThuThangHienTai() {
        LocalDate now = LocalDate.now();
        return hoaDonRepository.tinhTongDoanhThuTheoThangNam(now.getMonthValue(), now.getYear(), TrangThai.DA_THANH_TOAN);
    }

    public Double tinhDoanhThuNamHienTai() {
        int namHienTai = LocalDate.now().getYear();
        return hoaDonRepository.tinhTongDoanhThuTheoNam(namHienTai, TrangThai.DA_THANH_TOAN);
    }

    public List<Map<String, Object>> thongKeDoanhThu6ThangGanNhat() {
        List<Map<String, Object>> result = new ArrayList<>();
        LocalDate now = LocalDate.now();
        
        for (int i = 5; i >= 0; i--) {
            LocalDate date = now.minusMonths(i);
            Double doanhThu = hoaDonRepository.tinhTongDoanhThuTheoThangNam(
                date.getMonthValue(), 
                date.getYear(),
                TrangThai.DA_THANH_TOAN
            );
            
            Map<String, Object> thongKe = new HashMap<>();
            thongKe.put("thang", date.getMonthValue());
            thongKe.put("nam", date.getYear());
            thongKe.put("doanhThu", doanhThu != null ? doanhThu : 0.0);
            
            result.add(thongKe);
        }
        
        return result;
    }

    public Map<String, Double> thongKeTyLeSinhVienTheoKhoa() {
        List<SinhVien> danhSachSinhVien = sinhVienRepository.findAll();
        Map<String, Double> result = new HashMap<>();
        
        if (danhSachSinhVien.isEmpty()) {
            return result;
        }

        // Đếm số sinh viên theo khoa
        Map<String, Integer> demTheoKhoa = new HashMap<>();
        for (SinhVien sv : danhSachSinhVien) {
            String khoa = sv.getKhoa();
            if (khoa != null && !khoa.trim().isEmpty()) {
                demTheoKhoa.merge(khoa, 1, Integer::sum);
            }
        }

        // Tính tỷ lệ phần trăm
        double tongSo = danhSachSinhVien.size();
        demTheoKhoa.forEach((khoa, soLuong) -> {
            double tyLe = (soLuong * 100.0) / tongSo;
            result.put(khoa, Math.round(tyLe * 10.0) / 10.0); // Làm tròn 1 chữ số thập phân
        });

        return result;
    }
} 