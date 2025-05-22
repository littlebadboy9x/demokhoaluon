package com.dormitory.management.service.impl;

import com.dormitory.management.model.DangKyPhong;
import com.dormitory.management.model.DangKyPhong.TrangThai;
import com.dormitory.management.repository.DangKyPhongRepository;
import com.dormitory.management.service.DangKyPhongService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DangKyPhongServiceImpl implements DangKyPhongService {

    @Autowired
    private DangKyPhongRepository dangKyPhongRepository;

    @Override
    public List<DangKyPhong> findAll() {
        return dangKyPhongRepository.findAll();
    }

    @Override
    public Page<DangKyPhong> findAll(Pageable pageable) {
        return dangKyPhongRepository.findAll(pageable);
    }

    @Override
    public Optional<DangKyPhong> findById(Long id) {
        return dangKyPhongRepository.findById(id);
    }

    @Override
    public DangKyPhong save(DangKyPhong dangKyPhong) {
        return dangKyPhongRepository.save(dangKyPhong);
    }

    @Override
    public void delete(Long id) {
        dangKyPhongRepository.deleteById(id);
    }

    @Override
    public long countByTrangThai(TrangThai trangThai) {
        return dangKyPhongRepository.countByTrangThai(trangThai);
    }

    @Override
    public Page<DangKyPhong> findByTrangThaiOrderByNgayDangKyDesc(TrangThai trangThai, Pageable pageable) {
        return dangKyPhongRepository.findByTrangThaiOrderByNgayDangKyDesc(trangThai, pageable);
    }
} 