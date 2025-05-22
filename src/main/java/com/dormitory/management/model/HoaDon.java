package com.dormitory.management.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.Date;

@Data
@Entity
@Table(name = "hoa_don")
public class HoaDon {
    @Id
    @Column(name = "ma_hoa_don")
    private String maHoaDon;

    @ManyToOne
    @JoinColumn(name = "ma_phong", nullable = false)
    private Phong phong;

    @Column(name = "thang", nullable = false)
    private Integer thang;

    @Column(name = "nam", nullable = false)
    private Integer nam;

    @Column(name = "tien_phong", nullable = false)
    private Double tienPhong;

    @Column(name = "tien_dien", nullable = false)
    private Double tienDien;

    @Column(name = "tien_nuoc", nullable = false)
    private Double tienNuoc;

    @Column(name = "phi_dich_vu", nullable = false)
    private Double phiDichVu;

    @Column(name = "tong_tien", nullable = false)
    private Double tongTien;

    @Column(name = "ngay_tao", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date ngayTao;

    @Column(name = "ngay_thanh_toan")
    @Temporal(TemporalType.TIMESTAMP)
    private Date ngayThanhToan;

    @Column(name = "trang_thai", nullable = false)
    @Enumerated(EnumType.STRING)
    private TrangThai trangThai = TrangThai.CHUA_THANH_TOAN;

    @Column(name = "ghi_chu")
    private String ghiChu;

    public enum TrangThai {
        CHUA_THANH_TOAN,
        DA_THANH_TOAN,
        DA_HUY
    }

    @PrePersist
    @PreUpdate
    public void calculateTongTien() {
        this.tongTien = this.tienPhong + this.tienDien + this.tienNuoc + this.phiDichVu;
    }
}