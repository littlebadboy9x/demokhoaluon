package com.dormitory.management.controller.api;

import com.dormitory.management.model.SinhVien;
import com.dormitory.management.model.Phong;
import com.dormitory.management.service.SinhVienService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/sinh-vien")
public class SinhVienApiController {

    @Autowired
    private SinhVienService sinhVienService;

    @GetMapping("/{maSinhVien}/phong-info")
    public ResponseEntity<?> getPhongInfo(@PathVariable String maSinhVien) {
        try {
            SinhVien sinhVien = sinhVienService.findById(maSinhVien)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy sinh viên"));

            Phong phong = sinhVien.getPhong();
            if (phong == null) {
                throw new RuntimeException("Sinh viên chưa được phân phòng");
            }

            Map<String, Object> response = new HashMap<>();
            response.put("maPhong", phong.getMaPhong());
            response.put("tenPhong", phong.getTenPhong());
            response.put("giaPhong", phong.getGiaPhong());
            response.put("phiDichVu", phong.getPhiDichVu());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
} 