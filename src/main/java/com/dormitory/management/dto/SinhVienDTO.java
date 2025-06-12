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
    
    // Constructor đơn giản cho API
    public SinhVienDTO() {}
    
    public SinhVienDTO(String maSv, String hoTen, String email, String soDienThoai) {
        this.maSv = maSv;
        this.hoTen = hoTen;
        this.email = email;
        this.soDienThoai = soDienThoai;
    }
    
    // Getters and Setters
    public String getMaSv() {
        return maSv;
    }
    
    public void setMaSv(String maSv) {
        this.maSv = maSv;
    }
    
    public String getHoTen() {
        return hoTen;
    }
    
    public void setHoTen(String hoTen) {
        this.hoTen = hoTen;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getSoDienThoai() {
        return soDienThoai;
    }
    
    public void setSoDienThoai(String soDienThoai) {
        this.soDienThoai = soDienThoai;
    }
}