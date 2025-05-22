package com.dormitory.management.repository;

import com.dormitory.management.model.HoaDon;
import com.dormitory.management.model.HoaDon.TrangThai;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface HoaDonRepository extends JpaRepository<HoaDon, Long> {
    Page<HoaDon> findByTrangThaiOrderByNgayTaoDesc(TrangThai trangThai, Pageable pageable);
    long countByTrangThai(TrangThai trangThai);

    @Query("SELECT SUM(h.tongTien) FROM HoaDon h WHERE h.thang = :thang AND h.nam = :nam AND h.trangThai = :trangThai")
    Double tinhTongDoanhThuTheoThangNam(@Param("thang") int thang, @Param("nam") int nam, @Param("trangThai") TrangThai trangThai);

    @Query("SELECT SUM(h.tongTien) FROM HoaDon h WHERE h.nam = :nam AND h.trangThai = :trangThai")
    Double tinhTongDoanhThuTheoNam(@Param("nam") int nam, @Param("trangThai") TrangThai trangThai);
}