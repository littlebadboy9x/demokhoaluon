package com.dormitory.management.controller;

import com.dormitory.management.model.NguoiDung;
import com.dormitory.management.service.NguoiDungService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/nguoi-dung")
public class NguoiDungController {

    @Autowired
    private NguoiDungService nguoiDungService;

    @GetMapping
    public List<NguoiDung> getAllNguoiDung() {
        return nguoiDungService.findAll();
    }

    @GetMapping("/{tenDangNhap}")
    public NguoiDung getNguoiDung(@PathVariable String tenDangNhap) {
        return nguoiDungService.findByTenDangNhap(tenDangNhap).orElse(null);
    }

    @PostMapping("/register")
    public NguoiDung register(@RequestBody NguoiDung nguoiDung) {
        return nguoiDungService.save(nguoiDung);
    }

    @DeleteMapping("/{tenDangNhap}")
    public void deleteNguoiDung(@PathVariable String tenDangNhap) {
        nguoiDungService.delete(tenDangNhap);
    }
}