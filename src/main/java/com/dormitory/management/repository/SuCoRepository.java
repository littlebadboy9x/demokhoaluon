package com.dormitory.management.repository;

import com.dormitory.management.model.SuCo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SuCoRepository extends JpaRepository<SuCo, Long> {
    
    Page<SuCo> findByTieuDeContainingIgnoreCaseOrMoTaContainingIgnoreCase(
        String tieuDe, String moTa, Pageable pageable);
    
    Page<SuCo> findByTrangThai(SuCo.TrangThai trangThai, Pageable pageable);
    
    Page<SuCo> findByMucDo(SuCo.MucDo mucDo, Pageable pageable);
    
    Page<SuCo> findByPhong_MaPhong(String maPhong, Pageable pageable);
    
    long countByTrangThai(SuCo.TrangThai trangThai);
    
    long countByMucDo(SuCo.MucDo mucDo);
} 