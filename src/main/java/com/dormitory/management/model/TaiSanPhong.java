package com.dormitory.management.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.Date;

@Data
@Entity
@Table(name = "tai_san_phong")
public class TaiSanPhong {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tai_san")
    private Long idTaiSan;

    @ManyToOne
    @JoinColumn(name = "ma_phong", nullable = false)
    private Phong phong;

    @Column(name = "ten_tai_san", nullable = false)
    private String tenTaiSan;

    @Column(name = "so_luong", nullable = false)
    private Integer soLuong = 1;

    @Enumerated(EnumType.STRING)
    @Column(name = "tinh_trang")
    private TinhTrang tinhTrang = TinhTrang.TOT;

    @Column(name = "ngay_cap_nhat")
    @Temporal(TemporalType.TIMESTAMP)
    private Date ngayCapNhat;

    @Column(name = "ghi_chu")
    private String ghiChu;

    public enum TinhTrang {
        TOT,
        HONG,
        DANG_SUA_CHUA
    }

    @PrePersist
    @PreUpdate
    public void prePersist() {
        if (this.ngayCapNhat == null) {
            this.ngayCapNhat = new Date();
        }
    }

    public String getTinhTrangDisplay() {
        if (tinhTrang == null) return "";
        
        switch (tinhTrang) {
            case TOT:
                return "Tốt";
            case HONG:
                return "Hỏng";
            case DANG_SUA_CHUA:
                return "Đang sửa chữa";
            default:
                return "";
        }
    }
} 