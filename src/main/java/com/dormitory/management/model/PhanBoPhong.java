package com.dormitory.management.model;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Date;

@Entity
@Table(name = "phan_bo_phong")
@Data
public class PhanBoPhong {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_phan_bo")
    private Long idPhanBo;

    @Column(name = "ma_sv", nullable = false)
    private String maSv;

    @Column(name = "ma_phong", nullable = false)
    private String maPhong;

    @Column(name = "ngay_nhan_phong", nullable = false)
    private Date ngayNhanPhong;

    @Column(name = "ngay_tra_phong")
    private Date ngayTraPhong;

    @Column(name = "trang_thai")
    @Enumerated(EnumType.STRING)
    private TrangThaiPhanBo trangThai;

    @ManyToOne
    @JoinColumn(name = "ma_sv", referencedColumnName = "ma_sv", insertable = false, updatable = false)
    private SinhVien sinhVien;

    @ManyToOne
    @JoinColumn(name = "ma_phong", referencedColumnName = "ma_phong", insertable = false, updatable = false)
    private Phong phong;

    public enum TrangThaiPhanBo {
        DANG_O, DA_KET_THUC, BI_HUY
    }
}