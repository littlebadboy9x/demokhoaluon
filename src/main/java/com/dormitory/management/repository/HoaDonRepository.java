package com.dormitory.management.repository;

import com.dormitory.management.model.HoaDon;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface HoaDonRepository extends JpaRepository<HoaDon, String> {
    
    Page<HoaDon> findByMaHoaDonContainingIgnoreCaseAndTrangThaiAndThangAndNam(
        String maHoaDon, HoaDon.TrangThai trangThai, Integer thang, Integer nam, Pageable pageable);
    
    Page<HoaDon> findByTrangThaiAndThangAndNam(
        HoaDon.TrangThai trangThai, Integer thang, Integer nam, Pageable pageable);
    
    Page<HoaDon> findByThangAndNam(Integer thang, Integer nam, Pageable pageable);
    
    Page<HoaDon> findByTrangThaiOrderByNgayTaoDesc(HoaDon.TrangThai trangThai, Pageable pageable);
    
    long countByTrangThai(HoaDon.TrangThai trangThai);
    
    @Query("SELECT SUM(h.tongTien) FROM HoaDon h WHERE h.thang = :thang AND h.nam = :nam AND h.trangThai = :trangThai")
    Double sumTongTienByThangAndNamAndTrangThai(
        @Param("thang") Integer thang, 
        @Param("nam") Integer nam, 
        @Param("trangThai") HoaDon.TrangThai trangThai);
}