package com.dormitory.management.service;

import com.dormitory.management.model.DangKyPhong;
import com.dormitory.management.model.DangKyPhong.TrangThai;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface DangKyPhongService {
    List<DangKyPhong> findAll();
    
    Page<DangKyPhong> findAll(Pageable pageable);
    
    Optional<DangKyPhong> findById(Long id);
    
    DangKyPhong save(DangKyPhong dangKyPhong);
    
    void delete(Long id);
    
    long countByTrangThai(TrangThai trangThai);
    
    Page<DangKyPhong> findByTrangThaiOrderByNgayDangKyDesc(TrangThai trangThai, Pageable pageable);
    
    Page<DangKyPhong> search(String search, TrangThai trangThai, String phong, Pageable pageable);
}