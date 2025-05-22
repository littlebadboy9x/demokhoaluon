package com.dormitory.management.model;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Timestamp;

@Entity
@Table(name = "thanh_toan")
@Data
public class ThanhToan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_thanh_toan")
    private Long idThanhToan;

    @Column(name = "ma_hoa_don", nullable = false)
    private String maHoaDon;

    @Column(name = "so_tien", nullable = false)
    private Double soTien;

    @Column(name = "phuong_thuc", nullable = false)
    @Enumerated(EnumType.STRING)
    private PhuongThuc phuongThuc;

    @Column(name = "ngay_thanh_toan")
    private Timestamp ngayThanhToan;

    @Column(name = "nguoi_thu")
    private String nguoiThu;

    @Column(name = "ghi_chu")
    private String ghiChu;

    @Column(name = "ma_giao_dich_momo")
    private String maGiaoDichMomo;

    @Column(name = "trang_thai_momo")
    private String trangThaiMomo;

    @Column(name = "url_thanh_toan")
    private String urlThanhToan;

    @Column(name = "thoi_gian_het_han")
    private Timestamp thoiGianHetHan;

    @ManyToOne
    @JoinColumn(name = "ma_hoa_don", referencedColumnName = "ma_hoa_don", insertable = false, updatable = false)
    private HoaDon hoaDon;

    public enum PhuongThuc {
        TIEN_MAT, CHUYEN_KHOAN, THE
    }
}