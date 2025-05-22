package com.dormitory.management.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.Date;

@Data
@Entity
@Table(name = "sinh_vien")
public class SinhVien {
    @Id
    @Column(name = "ma_sv")
    private String maSv;

    @Column(name = "ten_dang_nhap", nullable = false, unique = true)
    private String tenDangNhap;

    @Column(name = "ho_ten", nullable = false)
    private String hoTen;

    @Column(name = "ngay_sinh")
    private Date ngaySinh;

    @Column(name = "gioi_tinh", nullable = false)
    @Enumerated(EnumType.STRING)
    private GioiTinh gioiTinh;

    @Column(name = "cccd", unique = true)
    private String cccd;

    @Column(name = "email")
    private String email;

    @Column(name = "so_dien_thoai")
    private String soDienThoai;

    @Column(name = "dia_chi")
    private String diaChi;

    @Column(name = "lop")
    private String lop;

    @Column(name = "nganh")
    private String nganh;

    @Column(name = "khoa")
    private String khoa;

    @Column(name = "ngay_dang_ky")
    private Date ngayDangKy;

    @Column(name = "trang_thai")
    @Enumerated(EnumType.STRING)
    private TrangThai trangThai = TrangThai.DANG_O;

    @ManyToOne
    @JoinColumn(name = "ten_dang_nhap", referencedColumnName = "ten_dang_nhap", insertable = false, updatable = false)
    private NguoiDung nguoiDung;

    public enum GioiTinh {
        NAM, NU
    }

    public enum TrangThai {
        DANG_O, DA_RUT, BI_DINH_CHI
    }
}