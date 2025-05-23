package com.dormitory.management.dto;

import com.dormitory.management.model.SinhVien;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class SinhVienDTO {
    private String maSv;
    private String tenDangNhap;
    private String hoTen;
    private LocalDate ngaySinh;
    private String cccd;
    private String soDienThoai;
    private String lop;
    private String nganh;
    private SinhVien.Khoa khoa;
    private String diaChi;
    private LocalDateTime ngayDangKy;
}