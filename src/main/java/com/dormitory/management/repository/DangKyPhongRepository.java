package com.dormitory.management.repository;

import com.dormitory.management.model.DangKyPhong;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DangKyPhongRepository extends JpaRepository<DangKyPhong, Long> {
    Page<DangKyPhong> findByTrangThaiOrderByNgayDangKyDesc(DangKyPhong.TrangThai trangThai, Pageable pageable);
    long countByTrangThai(DangKyPhong.TrangThai trangThai);
}