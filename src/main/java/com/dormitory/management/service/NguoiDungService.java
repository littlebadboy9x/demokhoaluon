package com.dormitory.management.service;

import com.dormitory.management.model.NguoiDung;
import com.dormitory.management.repository.NguoiDungRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NguoiDungService {

    @Autowired
    private NguoiDungRepository nguoiDungRepository;

    public List<NguoiDung> findAll() {
        return nguoiDungRepository.findAll();
    }

    public Optional<NguoiDung> findByTenDangNhap(String tenDangNhap) {
        return nguoiDungRepository.findById(tenDangNhap);
    }

    public boolean existsByTenDangNhap(String tenDangNhap) {
        return nguoiDungRepository.existsById(tenDangNhap);
    }

    public NguoiDung save(NguoiDung nguoiDung) {
        return nguoiDungRepository.save(nguoiDung);
    }

    public void delete(String tenDangNhap) {
        nguoiDungRepository.deleteById(tenDangNhap);
    }

    
}