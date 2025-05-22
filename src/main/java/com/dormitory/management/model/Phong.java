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

    @Column(name = "suc_chua", nullable = false)
    private Integer sucChua;

    @Column(name = "so_nguoi_hien_tai")
    private Integer soNguoiHienTai = 0;

    @Column(name = "trang_thai")
    @Enumerated(EnumType.STRING)
    private TrangThai trangThai = TrangThai.CON_TRONG;

    @Column(name = "gia_phong", nullable = false)
    private Double giaPhong;

    @Column(name = "gia_dien", nullable = false)
    private Double giaDien;

    @Column(name = "phi_ve_sinh", nullable = false)
    private Double phiVeSinh;

    @Column(name = "ngay_tao")
    private Date ngayTao;

    public enum TrangThai {
        CON_TRONG, DA_DU, DANG_SUA_CHUA
    }
}