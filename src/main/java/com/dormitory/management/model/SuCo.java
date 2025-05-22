package com.dormitory.management.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.Date;

@Data
@Entity
@Table(name = "su_co")
public class SuCo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tieu_de", nullable = false)
    private String tieuDe;

    @Column(name = "mo_ta")
    private String moTa;

    @Column(name = "ngay_bao_cao")
    private Date ngayBaoCao;

    @Column(name = "ngay_xu_ly")
    private Date ngayXuLy;

    @Column(name = "trang_thai")
    @Enumerated(EnumType.STRING)
    private TrangThai trangThai = TrangThai.CHUA_XU_LY;

    @ManyToOne
    @JoinColumn(name = "ma_phong")
    private Phong phong;

    @ManyToOne
    @JoinColumn(name = "ma_sv")
    private SinhVien sinhVien;

    public enum TrangThai {
        CHUA_XU_LY, DANG_XU_LY, DA_XU_LY, HUY
    }
} 