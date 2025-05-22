package com.dormitory.management.controller;

import com.dormitory.management.model.PhanBoPhong;
import com.dormitory.management.service.PhanBoPhongService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/phan-bo-phong")
public class PhanBoPhongController {

    @Autowired
    private PhanBoPhongService phanBoPhongService;

    @GetMapping
    public List<PhanBoPhong> getAllPhanBoPhong() {
        return phanBoPhongService.findAll();
    }

    @GetMapping("/{idPhanBo}")
    public PhanBoPhong getPhanBoPhong(@PathVariable Long idPhanBo) {
        return phanBoPhongService.findById(idPhanBo).orElse(null);
    }

    @PostMapping
    public PhanBoPhong createPhanBoPhong(@RequestBody PhanBoPhong phanBoPhong) {
        return phanBoPhongService.save(phanBoPhong);
    }

    @DeleteMapping("/{idPhanBo}")
    public void deletePhanBoPhong(@PathVariable Long idPhanBo) {
        phanBoPhongService.delete(idPhanBo);
    }
}