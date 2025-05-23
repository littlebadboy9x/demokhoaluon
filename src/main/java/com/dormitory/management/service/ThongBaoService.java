package com.dormitory.management.service;

import com.dormitory.management.model.ThongBao;
import com.dormitory.management.model.Phong;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ThongBaoService {
    Page<ThongBao> findByPhongAndTrangThai(Phong phong, Boolean trangThai, Pageable pageable);
    Page<ThongBao> findByTrangThai(Boolean trangThai, Pageable pageable);
    Optional<ThongBao> findById(Long id);
    ThongBao save(ThongBao thongBao);
    void delete(Long id);
} 