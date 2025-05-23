package com.dormitory.management.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import org.springframework.format.annotation.DateTimeFormat;

@Data
@Entity
@Table(name = "phan_bo_phong")
public class PhanBoPhong {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_phan_bo")
    private Long idPhanBo;

    @ManyToOne
    @JoinColumn(name = "ma_sv", nullable = false)
    private SinhVien sinhVien;

    @ManyToOne
    @JoinColumn(name = "ma_phong", nullable = false)
    private Phong phong;

    @Column(name = "ngay_nhan_phong", nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime ngayNhanPhong;

    @Column(name = "ngay_tra_phong")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime ngayTraPhong;

    @Enumerated(EnumType.STRING)
    @Column(name = "trang_thai")
    private TrangThai trangThai = TrangThai.DANG_O;

    public enum TrangThai {
        DANG_O,
        DA_KET_THUC,
        BI_HUY
    }

    public String getTrangThaiDisplay() {
        if (trangThai == null) return "";
        
        switch (trangThai) {
            case DANG_O:
                return "Đang ở";
            case DA_KET_THUC:
                return "Đã kết thúc";
            case BI_HUY:
                return "Bị hủy";
            default:
                return "";
        }
    }
}