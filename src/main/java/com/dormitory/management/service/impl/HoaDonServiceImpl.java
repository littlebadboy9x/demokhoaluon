package com.dormitory.management.service.impl;

import com.dormitory.management.model.HoaDon;
import com.dormitory.management.model.HoaDon.TrangThai;
import com.dormitory.management.repository.HoaDonRepository;
import com.dormitory.management.service.HoaDonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class HoaDonServiceImpl implements HoaDonService {

    @Autowired
    private HoaDonRepository hoaDonRepository;

    @Override
    @Transactional(readOnly = true)
    public List<HoaDon> findAll() {
        return hoaDonRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<HoaDon> findAll(Pageable pageable) {
        return hoaDonRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<HoaDon> findById(String id) {
        return hoaDonRepository.findById(id);
    }

    @Override
    public HoaDon save(HoaDon hoaDon) {
        return hoaDonRepository.save(hoaDon);
    }

    @Override
    public void delete(String id) {
        hoaDonRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public long countByTrangThai(TrangThai trangThai) {
        return hoaDonRepository.countByTrangThai(trangThai);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<HoaDon> findByTrangThaiOrderByNgayTaoDesc(TrangThai trangThai, Pageable pageable) {
        return hoaDonRepository.findByTrangThaiOrderByNgayTaoDesc(trangThai, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Double tinhTongDoanhThuTheoThangNam(int thang, int nam) {
        return hoaDonRepository.tinhTongDoanhThuTheoThangNam(thang, nam, TrangThai.DA_THANH_TOAN);
    }

    @Override
    @Transactional(readOnly = true)
    public Double tinhTongDoanhThu(Integer thang, Integer nam) {
        return hoaDonRepository.tinhTongDoanhThu(thang, nam);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<HoaDon> search(String keyword, TrangThai trangThai, Integer thang, Integer nam, Pageable pageable) {
        return hoaDonRepository.search(keyword, trangThai, thang, nam, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public long count() {
        return hoaDonRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public List<HoaDon> findByTrangThai(TrangThai trangThai) {
        return hoaDonRepository.findByTrangThai(trangThai);
    }
} 