package com.dormitory.management.dto;

import lombok.Data;
import java.sql.Timestamp;
import com.dormitory.management.model.ThanhToan.PhuongThuc;

@Data
public class ThanhToanDTO {
    private Long idThanhToan;
    private String maHoaDon;
    private Double soTien;
    private PhuongThuc phuongThuc;
    private Timestamp ngayThanhToan;
    private String nguoiThu;
    private String ghiChu;
    private Long idHoaDon;
}