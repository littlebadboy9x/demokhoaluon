package com.dormitory.management.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.Date;

@Data
@Entity
@Table(name = "hoa_don")
public class HoaDon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ma_hoa_don")
    private Long maHoaDon;

    @ManyToOne
    @JoinColumn(name = "ma_phong", nullable = false)
    private Phong phong;

    @Column(name = "thang", nullable = false)
    private Integer thang;

    @Column(name = "nam", nullable = false)
    private Integer nam;

    @Column(name = "so_dien")
    private Integer soDien;

    @Column(name = "tien_dien")
    private Double tienDien;

    @Column(name = "tien_phong")
    private Double tienPhong;

    @Column(name = "phi_dich_vu")
    private Double phiDichVu;

    @Column(name = "tong_tien")
    private Double tongTien;

    @Column(name = "ngay_tao")
    private Date ngayTao;

    @Column(name = "ngay_thanh_toan")
    private Date ngayThanhToan;

    @Column(name = "trang_thai")
    @Enumerated(EnumType.STRING)
    private TrangThai trangThai = TrangThai.CHUA_THANH_TOAN;

    @Column(name = "ghi_chu")
    private String ghiChu;

    public enum TrangThai {
        CHUA_THANH_TOAN, DA_THANH_TOAN, QUA_HAN
    }
}