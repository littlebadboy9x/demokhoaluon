package com.dormitory.management.service;

import com.dormitory.management.model.SuCo;
import com.dormitory.management.model.SuCo.TrangThai;
import com.dormitory.management.model.SuCo.MucDoUuTien;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface SuCoService {
    List<SuCo> findAll();
    
    Page<SuCo> findAll(Pageable pageable);
    
    Optional<SuCo> findById(Long idSuCo);
    
    SuCo save(SuCo suCo);
    
    void delete(Long idSuCo);
    
    long countByTrangThai(TrangThai trangThai);
    
    Page<SuCo> findByTrangThaiOrderByNgayBaoCaoDesc(TrangThai trangThai, Pageable pageable);
    
    Page<SuCo> search(String keyword, TrangThai trangThai, MucDoUuTien mucDoUuTien, String maPhong, Pageable pageable);
    
    long countByMucDoUuTien(MucDoUuTien mucDoUuTien);
} 