package com.dormitory.management.dto;

import lombok.Data;
import java.util.Date;

@Data
public class PhanBoPhongDTO {
    private Long id;
    private String maSv;
    private String maPhong;
    private Date ngayPhanBo;
    private Date ngayRoiDi;
    private String trangThai;
    private String ghiChu;
}