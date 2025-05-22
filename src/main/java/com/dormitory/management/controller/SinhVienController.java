package com.dormitory.management.controller;

import com.dormitory.management.dto.SinhVienDTO;
import com.dormitory.management.exception.ResourceNotFoundException;
import com.dormitory.management.model.SinhVien;
import com.dormitory.management.service.SinhVienService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/sinh-vien")
public class SinhVienController {

    @Autowired
    private SinhVienService sinhVienService;

    @GetMapping
    public List<SinhVienDTO> getAllSinhVien() {
        return sinhVienService.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/{maSv}")
    public ResponseEntity<SinhVien> getSinhVien(@PathVariable String maSv) {
        return sinhVienService.findById(maSv)
                .map(ResponseEntity::ok)
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