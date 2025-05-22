package com.dormitory.management.repository;

import com.dormitory.management.model.SuCo;
import com.dormitory.management.model.SuCo.TrangThai;
import com.dormitory.management.model.SuCo.MucDoUuTien;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SuCoRepository extends JpaRepository<SuCo, Long> {
    
    Page<SuCo> findByMoTaContainingIgnoreCase(String moTa, Pageable pageable);
    
    Page<SuCo> findByTrangThai(TrangThai trangThai, Pageable pageable);
    
    Page<SuCo> findByMucDoUuTien(MucDoUuTien mucDoUuTien, Pageable pageable);
    
    Page<SuCo> findByPhong_MaPhong(String maPhong, Pageable pageable);
    
    long countByTrangThai(TrangThai trangThai);
    
    long countByMucDoUuTien(MucDoUuTien mucDoUuTien);

    Page<SuCo> findByTrangThaiOrderByNgayBaoCaoDesc(TrangThai trangThai, Pageable pageable);
} 