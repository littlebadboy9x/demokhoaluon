package com.dormitory.management.controller;

import com.dormitory.management.exception.ResourceNotFoundException;
import com.dormitory.management.model.SuCo;
import com.dormitory.management.service.SuCoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/su-co")
public class SuCoController {

    @Autowired
    private SuCoService suCoService;

    @GetMapping
    public List<SuCo> getAllSuCo() {
        return suCoService.findAll();
    }

    @GetMapping("/{maSuCo}")
    public SuCo getSuCo(@PathVariable Long maSuCo) {
        return suCoService.findById(maSuCo)
                .orElseThrow(() -> new ResourceNotFoundException("Sự cố không tồn tại với mã: " + maSuCo));
    }

    @PostMapping
    public SuCo createSuCo(@RequestBody SuCo suCo) {
        return suCoService.save(suCo);
    }

    @DeleteMapping("/{maSuCo}")
    public void deleteSuCo(@PathVariable Long maSuCo) {
        if (!suCoService.findById(maSuCo).isPresent()) {
            throw new ResourceNotFoundException("Sự cố không tồn tại với mã: " + maSuCo);
        }
        suCoService.delete(maSuCo);
    }
} 