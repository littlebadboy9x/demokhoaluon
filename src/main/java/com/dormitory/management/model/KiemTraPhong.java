package com.dormitory.management.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.Date;
import org.springframework.format.annotation.DateTimeFormat;

@Data
@Entity
@Table(name = "kiem_tra_phong")
public class KiemTraPhong {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_kiem_tra")
    private Long idKiemTra;

    @ManyToOne
    @JoinColumn(name = "ma_phong", nullable = false)
    private Phong phong;

    @Column(name = "ngay_kiem_tra", nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date ngayKiemTra;

    @Column(name = "nguoi_kiem_tra", nullable = false)
    private String nguoiKiemTra;

    @Column(name = "noi_dung_kiem_tra")
    private String noiDungKiemTra;

    @Enumerated(EnumType.STRING)
    @Column(name = "danh_gia", nullable = false)
    private DanhGia danhGia;

    @Column(name = "ghi_chu")
    private String ghiChu;

    public enum DanhGia {
        TOT,
        KHA,
        TRUNG_BINH,
        YEU
    }

    public String getDanhGiaDisplay() {
        if (danhGia == null) return "";
        
        switch (danhGia) {
            case TOT:
                return "Tốt";
            case KHA:
                return "Khá";
            case TRUNG_BINH:
                return "Trung bình";
            case YEU:
                return "Yếu";
            default:
                return "";
        }
    }
} 