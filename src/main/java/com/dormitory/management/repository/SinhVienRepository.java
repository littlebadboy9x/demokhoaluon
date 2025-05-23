package com.dormitory.management.repository;

import com.dormitory.management.model.Phong;
import com.dormitory.management.model.SinhVien;
import com.dormitory.management.model.SinhVien.TrangThai;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SinhVienRepository extends JpaRepository<SinhVien, String>, JpaSpecificationExecutor<SinhVien> {
    List<SinhVien> findByHoTenContainingOrMaSvContainingOrLopContaining(String hoTen, String maSv, String lop);
    long countByTrangThai(TrangThai trangThai);
    Page<SinhVien> findByTrangThaiOrderByNgayDangKyDesc(TrangThai trangThai, Pageable pageable);
    List<SinhVien> findByPhong(Phong phong);
    Optional<SinhVien> findByTenDangNhap(String tenDangNhap);
}