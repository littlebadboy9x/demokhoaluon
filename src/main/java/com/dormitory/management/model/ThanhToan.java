package com.dormitory.management.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.Date;

@Data
@Entity
@Table(name = "thanh_toan")
public class ThanhToan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_thanh_toan")
    private Long idThanhToan;

    @ManyToOne
    @JoinColumn(name = "ma_hoa_don", nullable = false)
    private HoaDon hoaDon;

    @Column(name = "so_tien", nullable = false)
    private Double soTien;

    @Enumerated(EnumType.STRING)
    @Column(name = "phuong_thuc", nullable = false)
    private PhuongThuc phuongThuc;

    @Column(name = "ngay_thanh_toan")
    @Temporal(TemporalType.TIMESTAMP)
    private Date ngayThanhToan;

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
    @Temporal(TemporalType.TIMESTAMP)
    private Date thoiGianHetHan;

    public enum PhuongThuc {
        TIEN_MAT,
        CHUYEN_KHOAN,
        MOMO,
        THE
    }
}