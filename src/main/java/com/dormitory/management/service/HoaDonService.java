package com.dormitory.management.service;

import com.dormitory.management.model.HoaDon;
import com.dormitory.management.model.HoaDon.TrangThai;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface HoaDonService {
    List<HoaDon> findAll();
    
    Page<HoaDon> findAll(Pageable pageable);
    
    Optional<HoaDon> findById(String maHoaDon);
    
    HoaDon save(HoaDon hoaDon);
    
    void delete(String maHoaDon);
    
    long countByTrangThai(TrangThai trangThai);
    
    Page<HoaDon> findByTrangThaiOrderByNgayTaoDesc(TrangThai trangThai, Pageable pageable);
    
    Double tinhTongDoanhThuTheoThangNam(int thang, int nam);
    
    Page<HoaDon> search(String keyword, TrangThai trangThai, Integer thang, Integer nam, Pageable pageable);
    
    Double tinhTongDoanhThu(Integer thang, Integer nam);
}