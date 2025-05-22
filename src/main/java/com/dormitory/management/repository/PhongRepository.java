package com.dormitory.management.repository;

import com.dormitory.management.model.Phong;
import com.dormitory.management.model.Phong.TrangThai;
import com.dormitory.management.model.Phong.LoaiPhong;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PhongRepository extends JpaRepository<Phong, String> {
    long countByTrangThai(TrangThai trangThai);
    List<Phong> findByTrangThai(TrangThai trangThai);
    Page<Phong> findByTrangThaiOrderByNgayTaoDesc(TrangThai trangThai, Pageable pageable);

    @Query("SELECT p FROM Phong p WHERE " +
            "(:search IS NULL OR p.maPhong LIKE %:search% OR p.tenPhong LIKE %:search%) " +
            "AND (:loaiPhong IS NULL OR p.loaiPhong = :loaiPhong) " +
            "AND (:trangThai IS NULL OR p.trangThai = :trangThai)")
    Page<Phong> search(
            @Param("search") String search,
            @Param("loaiPhong") LoaiPhong loaiPhong,
            @Param("trangThai") TrangThai trangThai,
            Pageable pageable);
}