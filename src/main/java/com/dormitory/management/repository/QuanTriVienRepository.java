package com.dormitory.management.repository;

import com.dormitory.management.model.QuanTriVien;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface QuanTriVienRepository extends JpaRepository<QuanTriVien, String> {
    Optional<QuanTriVien> findByTenDangNhap(String tenDangNhap);
}