package com.dormitory.management.repository;

import com.dormitory.management.model.DangKyPhong;
import com.dormitory.management.model.SinhVien;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Arrays;

@Repository
public interface DangKyPhongRepository extends JpaRepository<DangKyPhong, Long>, JpaSpecificationExecutor<DangKyPhong> {
    boolean existsBySinhVienAndTrangThaiIn(SinhVien sinhVien, List<DangKyPhong.TrangThai> trangThais);
    
    DangKyPhong findFirstBySinhVienAndTrangThaiInOrderByNgayDangKyDesc(SinhVien sinhVien, List<DangKyPhong.TrangThai> trangThais);
    
    List<DangKyPhong> findByTrangThaiOrderByNgayDangKyDesc(DangKyPhong.TrangThai trangThai);
    
    Page<DangKyPhong> findByTrangThaiOrderByNgayDangKyDesc(DangKyPhong.TrangThai trangThai, Pageable pageable);
    
    long countByTrangThai(DangKyPhong.TrangThai trangThai);
    
    List<DangKyPhong> findByTrangThai(DangKyPhong.TrangThai trangThai);
}