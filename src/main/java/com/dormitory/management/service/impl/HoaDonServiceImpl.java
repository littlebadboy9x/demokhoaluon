package com.dormitory.management.service.impl;

import com.dormitory.management.model.HoaDon;
import com.dormitory.management.model.HoaDon.TrangThai;
import com.dormitory.management.repository.HoaDonRepository;
import com.dormitory.management.service.HoaDonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class HoaDonServiceImpl implements HoaDonService {

    @Autowired
    private HoaDonRepository hoaDonRepository;

    @Override
    public List<HoaDon> findAll() {
        return hoaDonRepository.findAll();
    }

    @Override
    public Page<HoaDon> findAll(Pageable pageable) {
        return hoaDonRepository.findAll(pageable);
    }

    @Override
    public Optional<HoaDon> findById(Long id) {
        return hoaDonRepository.findById(id);
    }

    @Override
    public HoaDon save(HoaDon hoaDon) {
        return hoaDonRepository.save(hoaDon);
    }

    @Override
    public void delete(Long id) {
        hoaDonRepository.deleteById(id);
    }

    @Override
    public long countByTrangThai(TrangThai trangThai) {
        return hoaDonRepository.countByTrangThai(trangThai);
    }

    @Override
    public Page<HoaDon> findByTrangThaiOrderByNgayTaoDesc(TrangThai trangThai, Pageable pageable) {
        return hoaDonRepository.findByTrangThaiOrderByNgayTaoDesc(trangThai, pageable);
    }

    @Override
    public Double tinhTongDoanhThuTheoThangNam(int thang, int nam) {
        return hoaDonRepository.tinhTongDoanhThuTheoThangNam(thang, nam, TrangThai.DA_THANH_TOAN);
    }
} 