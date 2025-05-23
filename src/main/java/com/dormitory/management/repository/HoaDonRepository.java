package com.dormitory.management.repository;

import com.dormitory.management.model.HoaDon;
import com.dormitory.management.model.HoaDon.TrangThai;
import com.dormitory.management.model.SinhVien;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HoaDonRepository extends JpaRepository<HoaDon, String> {
    long countByTrangThai(TrangThai trangThai);
    long countBySinhVienAndTrangThai(SinhVien sinhVien, TrangThai trangThai);
    List<HoaDon> findByTrangThai(TrangThai trangThai);
    Page<HoaDon> findByTrangThaiOrderByNgayTaoDesc(TrangThai trangThai, Pageable pageable);
    
    @Query("SELECT SUM(h.tongTien) FROM HoaDon h WHERE h.thang = :thang AND h.nam = :nam AND h.trangThai = :trangThai")
    Double tinhTongDoanhThuTheoThangNam(@Param("thang") int thang, @Param("nam") int nam, @Param("trangThai") TrangThai trangThai);

    @Query("SELECT SUM(h.tongTien) FROM HoaDon h WHERE (:thang IS NULL OR h.thang = :thang) AND h.nam = :nam AND h.trangThai = :trangThai")
    Double tinhTongDoanhThu(@Param("thang") Integer thang, @Param("nam") Integer nam, @Param("trangThai") TrangThai trangThai);

    @Query("SELECT h FROM HoaDon h WHERE " +
           "(:keyword IS NULL OR h.maHoaDon LIKE %:keyword% OR h.phong.maPhong LIKE %:keyword%) AND " +
           "(:trangThai IS NULL OR h.trangThai = :trangThai) AND " +
           "(:thang IS NULL OR h.thang = :thang) AND " +
           "(:nam IS NULL OR h.nam = :nam)")
    Page<HoaDon> search(
        @Param("keyword") String keyword,
        @Param("trangThai") TrangThai trangThai,
        @Param("thang") Integer thang,
        @Param("nam") Integer nam,
        Pageable pageable
    );

    Page<HoaDon> findBySinhVien(SinhVien sinhVien, Pageable pageable);
    
    List<HoaDon> findBySinhVienAndTrangThai(SinhVien sinhVien, TrangThai trangThai);
    
    @Query("SELECT SUM(h.tongTien) FROM HoaDon h WHERE h.sinhVien = :sinhVien AND h.trangThai = :trangThai")
    Double tinhTongTienTheoTrangThai(@Param("sinhVien") SinhVien sinhVien, @Param("trangThai") TrangThai trangThai);
}