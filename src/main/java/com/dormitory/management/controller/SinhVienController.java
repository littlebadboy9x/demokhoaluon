package com.dormitory.management.controller;

import com.dormitory.management.dto.SinhVienDTO;
import com.dormitory.management.exception.ResourceNotFoundException;
import com.dormitory.management.model.Phong;
import com.dormitory.management.model.SinhVien;
import com.dormitory.management.service.PhongService;
import com.dormitory.management.service.SinhVienService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/sinh-vien")
public class SinhVienController {

    @Autowired
    private SinhVienService sinhVienService;
    
    @Autowired
    private PhongService phongService;

    @GetMapping
    public List<SinhVienDTO> getAllSinhVien() {
        return sinhVienService.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/{maSv}")
    public ResponseEntity<Map<String, Object>> getSinhVien(@PathVariable String maSv) {
        return sinhVienService.findById(maSv)
                .map(sinhVien -> {
                    Map<String, Object> response = new HashMap<>();
                    response.put("maSv", sinhVien.getMaSv());
                    response.put("hoTen", sinhVien.getHoTen());
                    
                    if (sinhVien.getPhong() != null) {
                        Phong phong = sinhVien.getPhong();
                        Map<String, Object> phongInfo = new HashMap<>();
                        phongInfo.put("maPhong", phong.getMaPhong());
                        phongInfo.put("tenPhong", phong.getTenPhong());
                        phongInfo.put("giaPhong", phong.getGiaPhong());
                        phongInfo.put("phiDichVu", 100000.0); // Giả định phí dịch vụ cố định
                        response.put("phong", phongInfo);
                    }
                    
                    return ResponseEntity.ok(response);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public SinhVienDTO createSinhVien(@RequestBody SinhVienDTO sinhVienDTO) {
        SinhVien sinhVien = convertToEntity(sinhVienDTO);
        return convertToDTO(sinhVienService.save(sinhVien));
    }

    @DeleteMapping("/{maSv}")
    public void deleteSinhVien(@PathVariable String maSv) {
        if (!sinhVienService.findById(maSv).isPresent()) {
            throw new ResourceNotFoundException("Sinh viên không tồn tại với mã: " + maSv);
        }
        sinhVienService.delete(maSv);
    }

    private SinhVienDTO convertToDTO(SinhVien sinhVien) {
        SinhVienDTO dto = new SinhVienDTO();
        dto.setMaSv(sinhVien.getMaSv());
        dto.setTenDangNhap(sinhVien.getTenDangNhap());
        dto.setHoTen(sinhVien.getHoTen());
        dto.setNgaySinh(sinhVien.getNgaySinh());
        dto.setCccd(sinhVien.getCccd());
        dto.setSoDienThoai(sinhVien.getSoDienThoai());
        dto.setLop(sinhVien.getLop());
        dto.setNganh(sinhVien.getNganh());
        dto.setKhoa(sinhVien.getKhoa());
        dto.setNgayDangKy(sinhVien.getNgayDangKy());
        return dto;
    }

    private SinhVien convertToEntity(SinhVienDTO dto) {
        SinhVien sinhVien = new SinhVien();
        sinhVien.setMaSv(dto.getMaSv());
        sinhVien.setTenDangNhap(dto.getTenDangNhap());
        sinhVien.setHoTen(dto.getHoTen());
        sinhVien.setNgaySinh(dto.getNgaySinh());
        sinhVien.setCccd(dto.getCccd());
        sinhVien.setSoDienThoai(dto.getSoDienThoai());
        sinhVien.setLop(dto.getLop());
        sinhVien.setNganh(dto.getNganh());
        sinhVien.setKhoa(dto.getKhoa());
        sinhVien.setNgayDangKy(dto.getNgayDangKy());
        return sinhVien;
    }
}