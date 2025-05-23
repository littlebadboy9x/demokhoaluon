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

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "so_dien_thoai")
    private String soDienThoai;

    @Column(name = "ten_dang_nhap", unique = true, nullable = false)
    private String tenDangNhap;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ten_dang_nhap", referencedColumnName = "ten_dang_nhap", insertable = false, updatable = false)
    private NguoiDung nguoiDung;

    @Column(name = "ngay_sinh")
    private LocalDate ngaySinh;

    @Column(name = "gioi_tinh", nullable = false)
    @Enumerated(EnumType.STRING)
    private GioiTinh gioiTinh;

    @Column(name = "cccd")
    private String cccd;

    @Column(name = "lop")
    private String lop;

    @Column(name = "nganh")
    private String nganh;

    @Column(name = "khoa")
    private String khoa;

    @Column(name = "dia_chi")
    private String diaChi;

    @Column(name = "trang_thai")
    @Enumerated(EnumType.STRING)
    private TrangThai trangThai = TrangThai.DANG_O;

    @Column(name = "ngay_dang_ky")
    private LocalDateTime ngayDangKy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ma_phong")
    private Phong phong;

    public enum GioiTinh {
        NAM, NU
    }

    public enum TrangThai {
        DANG_O, DA_RUT, BI_DINH_CHI
    }

    @PrePersist
    public void prePersist() {
        if (ngayDangKy == null) {
            ngayDangKy = LocalDateTime.now();
        }
        if (trangThai == null) {
            trangThai = TrangThai.DANG_O;
        }
    }
}