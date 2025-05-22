package com.dormitory.management.controller;

import com.dormitory.management.exception.ResourceNotFoundException;
import com.dormitory.management.model.DangKyPhong;
import com.dormitory.management.service.DangKyPhongService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/dang-ky-phong")
public class DangKyPhongController {

    @Autowired
    private DangKyPhongService dangKyPhongService;

    @GetMapping
    public List<DangKyPhong> getAllDangKyPhong() {
        return dangKyPhongService.findAll();
    }

    @GetMapping("/{idDangKy}")
    public DangKyPhong getDangKyPhong(@PathVariable Long idDangKy) {
        return dangKyPhongService.findById(idDangKy)
                .orElseThrow(() -> new ResourceNotFoundException("Đăng ký phòng không tồn tại với ID: " + idDangKy));
    }

    @PostMapping
    public DangKyPhong createDangKyPhong(@RequestBody DangKyPhong dangKyPhong) {
        return dangKyPhongService.save(dangKyPhong);
    }

    @DeleteMapping("/{idDangKy}")
    public void deleteDangKyPhong(@PathVariable Long idDangKy) {
        if (!dangKyPhongService.findById(idDangKy).isPresent()) {
            throw new ResourceNotFoundException("Đăng ký phòng không tồn tại với ID: " + idDangKy);
        }
        dangKyPhongService.delete(idDangKy);
    }
}