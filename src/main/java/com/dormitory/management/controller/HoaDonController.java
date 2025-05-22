package com.dormitory.management.controller;

import com.dormitory.management.exception.ResourceNotFoundException;
import com.dormitory.management.model.HoaDon;
import com.dormitory.management.service.HoaDonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/hoa-don")
public class HoaDonController {

    @Autowired
    private HoaDonService hoaDonService;

    @GetMapping
    public List<HoaDon> getAllHoaDon() {
        return hoaDonService.findAll();
    }

    @GetMapping("/{maHoaDon}")
    public HoaDon getHoaDon(@PathVariable String maHoaDon) {
        return hoaDonService.findById(maHoaDon)
                .orElseThrow(() -> new ResourceNotFoundException("Hóa đơn không tồn tại với mã: " + maHoaDon));
    }

    @PostMapping
    public HoaDon createHoaDon(@RequestBody HoaDon hoaDon) {
        return hoaDonService.save(hoaDon);
    }

    @DeleteMapping("/{maHoaDon}")
    public void deleteHoaDon(@PathVariable String maHoaDon) {
        if (!hoaDonService.findById(maHoaDon).isPresent()) {
            throw new ResourceNotFoundException("Hóa đơn không tồn tại với mã: " + maHoaDon);
        }
        hoaDonService.delete(maHoaDon);
    }
}