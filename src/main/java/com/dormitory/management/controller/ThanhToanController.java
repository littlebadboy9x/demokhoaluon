package com.dormitory.management.controller;

import com.dormitory.management.model.ThanhToan;
import com.dormitory.management.service.ThanhToanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/thanh-toan")
public class ThanhToanController {

    @Autowired
    private ThanhToanService thanhToanService;

    @GetMapping
    public List<ThanhToan> getAllThanhToan() {
        return thanhToanService.findAll();
    }

    @GetMapping("/{idThanhToan}")
    public ThanhToan getThanhToan(@PathVariable Long idThanhToan) {
        return thanhToanService.findById(idThanhToan).orElse(null);
    }

    @PostMapping
    public ThanhToan createThanhToan(@RequestBody ThanhToan thanhToan) {
        return thanhToanService.save(thanhToan);
    }

    @DeleteMapping("/{idThanhToan}")
    public void deleteThanhToan(@PathVariable Long idThanhToan) {
        thanhToanService.delete(idThanhToan);
    }
}