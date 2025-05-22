package com.dormitory.management.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.Date;

@Data
@Entity
@Table(name = "phong")
public class Phong {
    @Id
    @Column(name = "ma_phong")
    private String maPhong;

    @Column(name = "ten_phong", nullable = false)
    private String tenPhong;

    @Enumerated(EnumType.STRING)
    @Column(name = "loai_phong", nullable = false)
    private LoaiPhong loaiPhong;

    @Column(name = "mo_ta")
    private String moTa;

    @Column(name = "suc_chua", nullable = false)
    private Integer sucChua = 8;

    @Column(name = "so_nguoi_hien_tai", nullable = false)
    private Integer soNguoiHienTai = 0;

    @Column(name = "gia_phong", nullable = false)
    private Double giaPhong = 500000.0;

    @Column(name = "gia_dien", nullable = false)
    private Double giaDien = 3500.0;

    @Column(name = "phi_ve_sinh", nullable = false)
    private Double phiVeSinh = 50000.0;

    @Column(name = "phi_dich_vu", nullable = false)
    private Double phiDichVu = 100000.0;

    @Enumerated(EnumType.STRING)
    @Column(name = "trang_thai", nullable = false)
    private TrangThai trangThai = TrangThai.CON_TRONG;

    @Column(name = "ngay_tao", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date ngayTao;

    public enum LoaiPhong {
        NAM,
        NU
    }

    public enum TrangThai {
        CON_TRONG,
        DA_DU,
        DANG_SUA_CHUA
    }

    @PrePersist
    public void prePersist() {
        if (this.ngayTao == null) {
            this.ngayTao = new Date();
        }
        if (this.soNguoiHienTai == null) {
            this.soNguoiHienTai = 0;
        }
        if (this.sucChua == null) {
            this.sucChua = 8;
        }
        if (this.giaPhong == null) {
            this.giaPhong = 500000.0;
        }
        if (this.giaDien == null) {
            this.giaDien = 3500.0;
        }
        if (this.phiVeSinh == null) {
            this.phiVeSinh = 50000.0;
        }
        if (this.phiDichVu == null) {
            this.phiDichVu = 100000.0;
        }
    }

    public String getTrangThaiDisplay() {
        if (trangThai == null) return "";
        
        switch (trangThai) {
            case CON_TRONG:
                return "Còn trống";
            case DA_DU:
                return "Đã đủ";
            case DANG_SUA_CHUA:
                return "Đang sửa chữa";
            default:
                return "";
        }
    }

    public String getLoaiPhongDisplay() {
        if (loaiPhong == null) return "";
        
        switch (loaiPhong) {
            case NAM:
                return "Nam";
            case NU:
                return "Nữ";
            default:
                return "";
        }
    }
}