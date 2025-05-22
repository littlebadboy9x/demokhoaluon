package com.dormitory.management.service;

import com.dormitory.management.model.Phong;
import com.dormitory.management.model.Phong.TrangThai;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface PhongService {
    List<Phong> findAll();
    
    Page<Phong> findAll(Pageable pageable);
    
    Optional<Phong> findById(String maPhong);
    
    Phong save(Phong phong);
    
    void delete(String maPhong);
    
    long countByTrangThai(TrangThai trangThai);
    
    Page<Phong> findByTrangThaiOrderByNgayTaoDesc(TrangThai trangThai, Pageable pageable);
    
    List<Phong> findByTrangThai(TrangThai trangThai);

    default long demTongSoPhong() {
        return findAll().size();
    }

    default long demSoPhongConTrong() {
        return countByTrangThai(TrangThai.CON_TRONG);
    }

    default long demSoPhongDangSuaChua() {
        return countByTrangThai(TrangThai.DANG_SUA_CHUA);
    }
}