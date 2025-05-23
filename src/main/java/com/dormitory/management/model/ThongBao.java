package com.dormitory.management.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.Date;
import org.springframework.format.annotation.DateTimeFormat;

@Data
@Entity
@Table(name = "thong_bao")
public class ThongBao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_thong_bao")
    private Long idThongBao;

    @Column(name = "tieu_de", nullable = false)
    private String tieuDe;

    @Column(name = "noi_dung", columnDefinition = "TEXT")
    private String noiDung;

    @Column(name = "ngay_tao")
    @Temporal(TemporalType.TIMESTAMP)
    private Date ngayTao;

    @Column(name = "nguoi_tao", nullable = false)
    private String nguoiTao;

    @Enumerated(EnumType.STRING)
    @Column(name = "loai_thong_bao", nullable = false)
    private LoaiThongBao loaiThongBao;

    @Column(name = "doi_tuong_nhan")
    private String doiTuongNhan;

    @Column(name = "trang_thai")
    private Boolean trangThai = true;

    @Column(name = "ngay_het_han")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date ngayHetHan;

    @ManyToOne
    @JoinColumn(name = "ma_phong")
    private Phong phong;

    public enum LoaiThongBao {
        THONG_BAO_CHUNG,
        THONG_BAO_PHONG,
        THONG_BAO_CA_NHAN
    }

    @PrePersist
    public void prePersist() {
        if (this.ngayTao == null) {
            this.ngayTao = new Date();
        }
    }

    public String getLoaiThongBaoDisplay() {
        if (loaiThongBao == null) return "";
        
        switch (loaiThongBao) {
            case THONG_BAO_CHUNG:
                return "Thông báo chung";
            case THONG_BAO_PHONG:
                return "Thông báo phòng";
            case THONG_BAO_CA_NHAN:
                return "Thông báo cá nhân";
            default:
                return "";
        }
    }
} 