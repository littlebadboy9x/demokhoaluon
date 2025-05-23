package com.dormitory.management.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "sinh_vien")
public class SinhVien {
    @Id
    @Column(name = "ma_sv")
    private String maSv;

    @Column(name = "ho_ten", nullable = false)
    private String hoTen;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "so_dien_thoai")
    private String soDienThoai;

    @Column(name = "ten_dang_nhap", unique = true, nullable = false)
    private String tenDangNhap;

    @Column(name = "mat_khau", nullable = false)
    private String matKhau;

    @Column(name = "ngay_sinh")
    private LocalDate ngaySinh;

    @Column(name = "cccd")
    private String cccd;

    @Column(name = "lop")
    private String lop;

    @Column(name = "nganh")
    private String nganh;

    @Column(name = "khoa", nullable = false)
    @Enumerated(EnumType.STRING)
    private Khoa khoa;

    @Column(name = "dia_chi")
    private String diaChi;

    @Column(name = "vai_tro", nullable = false)
    private String vaiTro;

    @Column(name = "trang_thai", nullable = false)
    @Enumerated(EnumType.STRING)
    private TrangThai trangThai = TrangThai.HOAT_DONG;

    @Column(name = "ngay_dang_ky")
    private LocalDateTime ngayDangKy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ma_phong")
    private Phong phong;

    public enum Khoa {
        CNTT,
        DIEN_DIEN_TU,
        CO_KHI,
        KE_TOAN
    }

    public enum TrangThai {
        HOAT_DONG,
        NGUNG_HOAT_DONG
    }

    @PrePersist
    public void prePersist() {
        if (ngayDangKy == null) {
            ngayDangKy = LocalDateTime.now();
        }
        if (trangThai == null) {
            trangThai = TrangThai.HOAT_DONG;
        }
    }
}