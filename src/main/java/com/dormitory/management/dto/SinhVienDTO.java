package com.dormitory.management.dto;

import lombok.Data;
import java.util.Date;

@Data
public class SinhVienDTO {
    private Long id;
    private String maSv;
    private String tenDangNhap;
    private String hoTen;
    private Date ngaySinh;
    private String cccd;
    private String soDienThoai;
    private String email;
    private String lop;
    private String nganh;
    private String khoa;
    private String queQuan;
    private String trangThai;
    private Date ngayDangKy;
}