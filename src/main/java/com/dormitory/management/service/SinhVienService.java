package com.dormitory.management.service;

import com.dormitory.management.model.Phong;
import com.dormitory.management.model.SinhVien;
import com.dormitory.management.model.SinhVien.TrangThai;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface SinhVienService {
    List<SinhVien> findAll();
    
    Page<SinhVien> findAll(Pageable pageable);
    
    Optional<SinhVien> findById(String mssv);
    
    SinhVien save(SinhVien sinhVien);
    
    void delete(String mssv);
    
    long countByTrangThai(TrangThai trangThai);
    
    Page<SinhVien> findByTrangThaiOrderByNgayDangKyDesc(TrangThai trangThai, Pageable pageable);

    long count();

    List<SinhVien> findByPhong(Phong phong);

    default long demTongSoSinhVien() {
        return count();
    }

    Page<SinhVien> search(String search, String khoa, String lop, TrangThai trangThai, Pageable pageable);

    Optional<SinhVien> findByTenDangNhap(String tenDangNhap);
}