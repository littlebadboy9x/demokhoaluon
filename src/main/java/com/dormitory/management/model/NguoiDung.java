package com.dormitory.management.model;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Timestamp;

@Entity
@Table(name = "nguoi_dung")
@Data
public class NguoiDung {
    @Id
    @Column(name = "ten_dang_nhap")
    private String tenDangNhap;

    @Column(name = "mat_khau", nullable = false)
    private String matKhau;

    @Column(name = "loai_nguoi_dung", nullable = false)
    @Enumerated(EnumType.STRING)
    private LoaiNguoiDung loaiNguoiDung;

    @Column(name = "trang_thai")
    private Boolean trangThai;

    @Column(name = "ngay_tao")
    private Timestamp ngayTao;

    public enum LoaiNguoiDung {
        SINH_VIEN, QUAN_TRI_VIEN
    }
}