package com.dormitory.management.dto;

import lombok.Data;
import java.util.Date;
import com.dormitory.management.model.HoaDon.TrangThai;

@Data
public class HoaDonDTO {
    private Long maHoaDon;
    private String maPhong;
    private Integer thang;
    private Integer nam;
    private Integer soDien;
    private Double tienDien;
    private Double tienPhong;
    private Double phiDichVu;
    private Double tongTien;
    private Date ngayTao;
    private Date ngayThanhToan;
    private TrangThai trangThai;
    private String ghiChu;
}