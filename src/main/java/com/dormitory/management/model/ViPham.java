package com.dormitory.management.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.Date;
import org.springframework.format.annotation.DateTimeFormat;

@Data
@Entity
@Table(name = "vi_pham")
public class ViPham {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_vi_pham")
    private Long idViPham;

    @ManyToOne
    @JoinColumn(name = "ma_sv", nullable = false)
    private SinhVien sinhVien;

    @Column(name = "ngay_vi_pham", nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date ngayViPham;

    @Column(name = "mo_ta", nullable = false)
    private String moTa;

    @Enumerated(EnumType.STRING)
    @Column(name = "muc_do", nullable = false)
    private MucDo mucDo;

    @Column(name = "hinh_thuc_xu_ly")
    private String hinhThucXuLy;

    @Column(name = "nguoi_lap_bien_ban")
    private String nguoiLapBienBan;

    @Column(name = "ngay_lap_bien_ban")
    @Temporal(TemporalType.TIMESTAMP)
    private Date ngayLapBienBan;

    @Enumerated(EnumType.STRING)
    @Column(name = "trang_thai")
    private TrangThai trangThai = TrangThai.CHO_XU_LY;

    public enum MucDo {
        NHE,
        TRUNG_BINH,
        NGHIEM_TRONG
    }

    public enum TrangThai {
        CHO_XU_LY,
        DA_XU_LY,
        HUY
    }

    @PrePersist
    public void prePersist() {
        if (this.ngayLapBienBan == null) {
            this.ngayLapBienBan = new Date();
        }
    }

    public String getMucDoDisplay() {
        if (mucDo == null) return "";
        
        switch (mucDo) {
            case NHE:
                return "Nhẹ";
            case TRUNG_BINH:
                return "Trung bình";
            case NGHIEM_TRONG:
                return "Nghiêm trọng";
            default:
                return "";
        }
    }

    public String getTrangThaiDisplay() {
        if (trangThai == null) return "";
        
        switch (trangThai) {
            case CHO_XU_LY:
                return "Chờ xử lý";
            case DA_XU_LY:
                return "Đã xử lý";
            case HUY:
                return "Hủy";
            default:
                return "";
        }
    }
} 