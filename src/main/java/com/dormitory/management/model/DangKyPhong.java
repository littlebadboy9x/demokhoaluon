package com.dormitory.management.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

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
    @JoinColumn(name = "ma_phong")
    private Phong phong;

    @Column(name = "ngay_dang_ky")
    private LocalDateTime ngayDangKy;

    @Enumerated(EnumType.STRING)
    @Column(name = "trang_thai")
    private TrangThai trangThai = TrangThai.CHO_DUYET;

    @Column(name = "nguoi_duyet")
    private String nguoiDuyet;

    @Column(name = "ngay_duyet")
    private LocalDateTime ngayDuyet;

    @Column(name = "ghi_chu")
    private String ghiChu;

    public enum TrangThai {
        CHO_DUYET,
        DA_DUYET,
        TU_CHOI
    }

    @PrePersist
    public void prePersist() {
        if (this.ngayDangKy == null) {
            this.ngayDangKy = LocalDateTime.now();
        }
    }

    public String getTrangThaiDisplay() {
        if (trangThai == null) return "";
        
        switch (trangThai) {
            case CHO_DUYET:
                return "Chờ duyệt";
            case DA_DUYET:
                return "Đã duyệt";
            case TU_CHOI:
                return "Từ chối";
            default:
                return "";
        }
    }
}