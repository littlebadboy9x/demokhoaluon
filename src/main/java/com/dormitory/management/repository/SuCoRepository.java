package com.dormitory.management.repository;

import com.dormitory.management.model.SuCo;
import com.dormitory.management.model.SuCo.TrangThai;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SuCoRepository extends JpaRepository<SuCo, Long> {
    long countByTrangThai(TrangThai trangThai);
    
    Page<SuCo> findByTrangThaiOrderByNgayBaoCaoDesc(TrangThai trangThai, Pageable pageable);
} 