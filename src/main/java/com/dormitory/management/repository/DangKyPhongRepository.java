package com.dormitory.management.repository;

import com.dormitory.management.model.DangKyPhong;
import com.dormitory.management.model.DangKyPhong.TrangThai;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface DangKyPhongRepository extends JpaRepository<DangKyPhong, Long>, JpaSpecificationExecutor<DangKyPhong> {
    long countByTrangThai(TrangThai trangThai);
    
    Page<DangKyPhong> findByTrangThaiOrderByNgayDangKyDesc(TrangThai trangThai, Pageable pageable);
}