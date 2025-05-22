package com.dormitory.management.controller;

import com.dormitory.management.model.QuanTriVien;
import com.dormitory.management.service.QuanTriVienService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/quan-tri-vien")
public class QuanTriVienController {

    @Autowired
    private QuanTriVienService quanTriVienService;

    @GetMapping
    public List<QuanTriVien> getAllQuanTriVien() {
        return quanTriVienService.findAll();
    }

    @GetMapping("/{maQt}")
    public QuanTriVien getQuanTriVien(@PathVariable String maQt) {
        return quanTriVienService.findByMaQt(maQt).orElse(null);
    }

    @PostMapping
    public QuanTriVien createQuanTriVien(@RequestBody QuanTriVien quanTriVien) {
        return quanTriVienService.save(quanTriVien);
    }

    @DeleteMapping("/{maQt}")
    public void deleteQuanTriVien(@PathVariable String maQt) {
        quanTriVienService.delete(maQt);
    }
}