package com.dormitory.management.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.Date;

@Data
@Entity
@Table(name = "dang_ky_phong")
public class DangKyPhong {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_dang_ky")
    private Long idDangKy;

    @ManyToOne
    @JoinColumn(name = "ma_sv", nullable = false)
    private SinhVien sinhVien;

    @ManyToOne
    @JoinColumn(name = "ma_phong", nullable = false)
    private Phong phong;

    @Column(name = "ngay_dang_ky")
    private Date ngayDangKy;

    @Column(name = "ngay_duyet")
    private Date ngayDuyet;

    @Column(name = "ngay_vao_o")
    private Date ngayVaoO;

    @Column(name = "ngay_roi_di")
    private Date ngayRoiDi;

    @Column(name = "trang_thai")
    @Enumerated(EnumType.STRING)
    private TrangThai trangThai = TrangThai.CHO_DUYET;

    @Column(name = "ghi_chu")
    private String ghiChu;

    public enum TrangThai {
        CHO_DUYET, DA_DUYET, TU_CHOI, DA_HUY
    }
}