package com.dormitory.management.service.impl;

import com.dormitory.management.model.Phong;
import com.dormitory.management.model.Phong.TrangThai;
import com.dormitory.management.model.Phong.LoaiPhong;
import com.dormitory.management.repository.PhongRepository;
import com.dormitory.management.service.PhongService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PhongServiceImpl implements PhongService {

    @Autowired
    private PhongRepository phongRepository;

    @Override
    public List<Phong> findAll() {
        return phongRepository.findAll();
    }

    @Override
    public Page<Phong> findAll(Pageable pageable) {
        return phongRepository.findAll(pageable);
    }

    @Override
    public Optional<Phong> findById(String maPhong) {
        return phongRepository.findById(maPhong);
    }

    @Override
    public Phong save(Phong phong) {
        return phongRepository.save(phong);
    }

    @Override
    public void delete(String maPhong) {
        phongRepository.deleteById(maPhong);
    }

    @Override
    public long count() {
        return phongRepository.count();
    }

    @Override
    public List<Phong> findPhongTrong() {
        return phongRepository.findByTrangThai(TrangThai.CON_TRONG);
    }

    @Override
    public long countByTrangThai(TrangThai trangThai) {
        return phongRepository.countByTrangThai(trangThai);
    }

    @Override
    public Page<Phong> findByTrangThaiOrderByNgayTaoDesc(TrangThai trangThai, Pageable pageable) {
        return phongRepository.findByTrangThaiOrderByNgayTaoDesc(trangThai, pageable);
    }

    @Override
    public List<Phong> findByTrangThai(TrangThai trangThai) {
        return phongRepository.findByTrangThai(trangThai);
    }

    @Override
    public Page<Phong> search(String search, LoaiPhong loaiPhong, TrangThai trangThai, Pageable pageable) {
        return phongRepository.search(
            search != null && !search.trim().isEmpty() ? search.trim() : null,
            loaiPhong,
            trangThai,
            pageable
        );
    }
} 