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
    @Column(name = "id_su_co")
    private Long idSuCo;
    
    @ManyToOne
    @JoinColumn(name = "ma_phong", nullable = false)
    private Phong phong;
    
    @ManyToOne
    @JoinColumn(name = "ma_sv")
    private SinhVien sinhVien;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "loai_su_co", nullable = false)
    private LoaiSuCo loaiSuCo;
    
    @Column(name = "mo_ta", nullable = false)
    private String moTa;
    
    @Column(name = "ngay_bao_cao")
    @Temporal(TemporalType.TIMESTAMP)
    private Date ngayBaoCao;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "muc_do_uu_tien", nullable = false)
    private MucDoUuTien mucDoUuTien;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "trang_thai")
    private TrangThai trangThai = TrangThai.CHO_XU_LY;
    
    @Column(name = "nguoi_xu_ly")
    private String nguoiXuLy;
    
    @Column(name = "ngay_hoan_thanh")
    @Temporal(TemporalType.TIMESTAMP)
    private Date ngayHoanThanh;
    
    @Column(name = "ghi_chu")
    private String ghiChu;
    
    public enum LoaiSuCo {
        DIEN,
        NUOC,
        CO_SO_VAT_CHAT,
        KHAC
    }
    
    public enum MucDoUuTien {
        THAP,
        TRUNG_BINH,
        CAO,
        KHAN_CAP
    }
    
    public enum TrangThai {
        CHO_XU_LY,
        DANG_XU_LY,
        DA_HOAN_THANH,
        HUY
    }
    
    @PrePersist
    public void prePersist() {
        if (this.ngayBaoCao == null) {
            this.ngayBaoCao = new Date();
        }
    }
    
    public String getLoaiSuCoDisplay() {
        if (loaiSuCo == null) return "";
        
        switch (loaiSuCo) {
            case DIEN:
                return "Điện";
            case NUOC:
                return "Nước";
            case CO_SO_VAT_CHAT:
                return "Cơ sở vật chất";
            case KHAC:
                return "Khác";
            default:
                return "";
        }
    }
    
    public String getMucDoUuTienDisplay() {
        if (mucDoUuTien == null) return "";
        
        switch (mucDoUuTien) {
            case THAP:
                return "Thấp";
            case TRUNG_BINH:
                return "Trung bình";
            case CAO:
                return "Cao";
            case KHAN_CAP:
                return "Khẩn cấp";
            default:
                return "";
        }
    }
    
    public String getTrangThaiDisplay() {
        if (trangThai == null) return "";
        
        switch (trangThai) {
            case CHO_XU_LY:
                return "Chờ xử lý";
            case DANG_XU_LY:
                return "Đang xử lý";
            case DA_HOAN_THANH:
                return "Đã hoàn thành";
            case HUY:
                return "Hủy";
            default:
                return "";
        }
    }
} 