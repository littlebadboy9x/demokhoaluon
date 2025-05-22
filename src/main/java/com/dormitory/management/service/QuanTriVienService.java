package com.dormitory.management.service;

import com.dormitory.management.model.QuanTriVien;
import com.dormitory.management.repository.QuanTriVienRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class QuanTriVienService {

    @Autowired
    private QuanTriVienRepository quanTriVienRepository;

    public List<QuanTriVien> findAll() {
        return quanTriVienRepository.findAll();
    }

    public Optional<QuanTriVien> findByMaQt(String maQt) {
        return quanTriVienRepository.findById(maQt);
    }

    public QuanTriVien save(QuanTriVien quanTriVien) {
        return quanTriVienRepository.save(quanTriVien);
    }

    public void delete(String maQt) {
        quanTriVienRepository.deleteById(maQt);
    }
}