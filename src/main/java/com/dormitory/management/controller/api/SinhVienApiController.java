package com.dormitory.management.controller.api;

import com.dormitory.management.model.SinhVien;
import com.dormitory.management.model.Phong;
import com.dormitory.management.service.SinhVienService;
import com.dormitory.management.service.PhongService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

@RestController
@RequestMapping("/api/sinh-vien")
public class SinhVienApiController {
    private static final Logger logger = LoggerFactory.getLogger(SinhVienApiController.class);

    @Autowired
    private SinhVienService sinhVienService;

    @Autowired
    private PhongService phongService;

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

    /**
     * Test endpoint đơn giản
     */
    @GetMapping("/test")
    public ResponseEntity<Map<String, Object>> test() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "OK");
        response.put("message", "API hoạt động bình thường");
        return ResponseEntity.ok(response);
    }

    /**
     * API endpoint để lấy danh sách sinh viên theo phòng
     */
    @GetMapping("/by-phong/{maPhong}")
    public ResponseEntity<?> getSinhVienTheoPhong(@PathVariable String maPhong) {
        try {
            logger.info("=== Starting getSinhVienTheoPhong for phong: {} ===", maPhong);
            
            // Kiểm tra phòng có tồn tại không
            Phong phong = phongService.findById(maPhong).orElse(null);
            if (phong == null) {
                logger.error("Phong not found: {}", maPhong);
                Map<String, String> error = new HashMap<>();
                error.put("error", "Không tìm thấy phòng: " + maPhong);
                return ResponseEntity.badRequest().body(error);
            }
            
            logger.info("Found phong: {}", phong.getTenPhong());
            
            // Lấy danh sách sinh viên
            List<SinhVien> sinhVienList;
            try {
                sinhVienList = sinhVienService.findByPhong(phong);
                logger.info("Found {} sinh vien in phong", sinhVienList != null ? sinhVienList.size() : 0);
            } catch (Exception e) {
                logger.error("Error getting sinh vien list", e);
                Map<String, String> error = new HashMap<>();
                error.put("error", "Lỗi khi lấy danh sách sinh viên: " + e.getMessage());
                return ResponseEntity.badRequest().body(error);
            }
            
            // Convert sang format đơn giản
            List<Map<String, Object>> result = new ArrayList<>();
            if (sinhVienList != null) {
                for (SinhVien sv : sinhVienList) {
                    try {
                        Map<String, Object> sinhVienMap = new HashMap<>();
                        sinhVienMap.put("maSv", sv.getMaSv() != null ? sv.getMaSv() : "");
                        sinhVienMap.put("hoTen", sv.getHoTen() != null ? sv.getHoTen() : "");
                        result.add(sinhVienMap);
                        logger.debug("Added sinh vien: {} - {}", sv.getMaSv(), sv.getHoTen());
                    } catch (Exception e) {
                        logger.error("Error processing sinh vien: {}", sv.getMaSv(), e);
                    }
                }
            }
            
            logger.info("Returning {} sinh vien", result.size());
            return ResponseEntity.ok(result);
            
        } catch (Exception e) {
            logger.error("Unexpected error in getSinhVienTheoPhong", e);
            Map<String, String> error = new HashMap<>();
            error.put("error", "Lỗi hệ thống: " + e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
} 