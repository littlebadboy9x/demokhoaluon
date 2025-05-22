package com.dormitory.management.repository;

import com.dormitory.management.model.Phong;
import com.dormitory.management.model.Phong.TrangThai;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PhongRepository extends JpaRepository<Phong, String> {
    long countByTrangThai(TrangThai trangThai);
    
    Page<Phong> findByTrangThaiOrderByNgayTaoDesc(TrangThai trangThai, Pageable pageable);
    
    List<Phong> findByTrangThai(TrangThai trangThai);
}