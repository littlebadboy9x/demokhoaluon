package com.dormitory.management.service;

import com.dormitory.management.model.SuCo;
import com.dormitory.management.model.SuCo.TrangThai;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface SuCoService {
    List<SuCo> findAll();
    
    Page<SuCo> findAll(Pageable pageable);
    
    Optional<SuCo> findById(Long id);
    
    SuCo save(SuCo suCo);
    
    void delete(Long id);
    
    long countByTrangThai(TrangThai trangThai);
    
    Page<SuCo> findByTrangThaiOrderByNgayBaoCaoDesc(TrangThai trangThai, Pageable pageable);
} 