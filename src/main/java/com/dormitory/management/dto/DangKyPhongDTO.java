package com.dormitory.management.dto;

import lombok.Data;
import java.util.Date;

@Data
public class DangKyPhongDTO {
    private Long idDangKy;
    private String maSv;
    private String maPhong;
    private Date ngayDangKy;
    private Date ngayDuyet;
    private Date ngayVaoO;
    private Date ngayRoiDi;
    private String trangThai;
    private String ghiChu;
}