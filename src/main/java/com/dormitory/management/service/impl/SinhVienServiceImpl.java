package com.dormitory.management.service.impl;

import com.dormitory.management.model.SinhVien;
import com.dormitory.management.model.SinhVien.TrangThai;
import com.dormitory.management.repository.SinhVienRepository;
import com.dormitory.management.service.SinhVienService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SinhVienServiceImpl implements SinhVienService {

    @Autowired
    private SinhVienRepository sinhVienRepository;

    @Override
    public List<SinhVien> findAll() {
        return sinhVienRepository.findAll();
    }

    @Override
    public Page<SinhVien> findAll(Pageable pageable) {
        return sinhVienRepository.findAll(pageable);
    }

    @Override
    public Optional<SinhVien> findById(String maSv) {
        return sinhVienRepository.findById(maSv);
    }

    @Override
    public SinhVien save(SinhVien sinhVien) {
        return sinhVienRepository.save(sinhVien);
    }

    @Override
    public void delete(String maSv) {
        sinhVienRepository.deleteById(maSv);
    }

    @Override
    public long countByTrangThai(TrangThai trangThai) {
        return sinhVienRepository.countByTrangThai(trangThai);
    }

    @Override
    public Page<SinhVien> findByTrangThaiOrderByNgayDangKyDesc(TrangThai trangThai, Pageable pageable) {
        return sinhVienRepository.findByTrangThaiOrderByNgayDangKyDesc(trangThai, pageable);
    }

    @Override
    public long count() {
        return sinhVienRepository.count();
    }
} 