package com.dormitory.management.repository;

import com.dormitory.management.model.PhanBoPhong;
import com.dormitory.management.model.SinhVien;
import com.dormitory.management.model.Phong;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PhanBoPhongRepository extends JpaRepository<PhanBoPhong, Long> {
    Optional<PhanBoPhong> findBySinhVienAndTrangThai(SinhVien sinhVien, PhanBoPhong.TrangThai trangThai);
}