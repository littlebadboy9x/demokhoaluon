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
    private SinhVien.GioiTinh gioiTinh;
    private String cccd;
    private String email;
    private String soDienThoai;
    private String lop;
    private String nganh;
    private String khoa;
    private String diaChi;
    private SinhVien.TrangThai trangThai;
    private LocalDateTime ngayDangKy;
}