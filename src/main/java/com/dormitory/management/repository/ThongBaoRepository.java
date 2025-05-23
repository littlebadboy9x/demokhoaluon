package com.dormitory.management.repository;

import com.dormitory.management.model.ThongBao;
import com.dormitory.management.model.Phong;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ThongBaoRepository extends JpaRepository<ThongBao, Long>, JpaSpecificationExecutor<ThongBao> {
    Page<ThongBao> findByPhongAndTrangThaiOrderByNgayTaoDesc(Phong phong, Boolean trangThai, Pageable pageable);
    Page<ThongBao> findByTrangThaiOrderByNgayTaoDesc(Boolean trangThai, Pageable pageable);
} 