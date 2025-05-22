package com.dormitory.management.controller;

import com.dormitory.management.exception.ResourceNotFoundException;
import com.dormitory.management.model.Phong;
import com.dormitory.management.service.PhongService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/phong")
public class PhongController {

    @Autowired
    private PhongService phongService;

    @GetMapping
    public List<Phong> getAllPhong() {
        return phongService.findAll();
    }

    @GetMapping("/{maPhong}")
    public Phong getPhong(@PathVariable String maPhong) {
        return phongService.findById(maPhong)
                .orElseThrow(() -> new ResourceNotFoundException("Phòng không tồn tại với mã: " + maPhong));
    }

    @PostMapping
    public Phong createPhong(@RequestBody Phong phong) {
        return phongService.save(phong);
    }

    @DeleteMapping("/{maPhong}")
    public void deletePhong(@PathVariable String maPhong) {
        if (!phongService.findById(maPhong).isPresent()) {
            throw new ResourceNotFoundException("Phòng không tồn tại với mã: " + maPhong);
        }
        phongService.delete(maPhong);
    }
}