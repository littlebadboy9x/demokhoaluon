package com.dormitory.management.service;

import com.dormitory.management.model.SinhVien;
import com.dormitory.management.model.SinhVien.TrangThai;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface SinhVienService {
    List<SinhVien> findAll();
    
    Page<SinhVien> findAll(Pageable pageable);
    
    Optional<SinhVien> findById(String maSv);
    
    SinhVien save(SinhVien sinhVien);
    
    void delete(String maSv);
    
    long countByTrangThai(TrangThai trangThai);
    
    Page<SinhVien> findByTrangThaiOrderByNgayDangKyDesc(TrangThai trangThai, Pageable pageable);

    long count();

    default long demTongSoSinhVien() {
        return count();
    }
}