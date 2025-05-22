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
    
    @Column(name = "tieu_de", nullable = false)
    private String tieuDe;
    
    @Column(name = "mo_ta", nullable = false, columnDefinition = "TEXT")
    private String moTa;
    
    @Column(name = "muc_do", nullable = false)
    @Enumerated(EnumType.STRING)
    private MucDo mucDo = MucDo.BINH_THUONG;
    
    @Column(name = "trang_thai", nullable = false)
    @Enumerated(EnumType.STRING)
    private TrangThai trangThai = TrangThai.CHUA_XU_LY;
    
    @Column(name = "ngay_bao_cao", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date ngayBaoCao;
    
    @Column(name = "ngay_xu_ly")
    @Temporal(TemporalType.TIMESTAMP)
    private Date ngayXuLy;
    
    @Column(name = "nguoi_xu_ly")
    private String nguoiXuLy;
    
    @Column(name = "ghi_chu")
    private String ghiChu;
    
    public enum MucDo {
        BINH_THUONG,
        QUAN_TRONG,
        KHAN_CAP
    }
    
    public enum TrangThai {
        CHUA_XU_LY,
        DANG_XU_LY,
        DA_XU_LY,
        DA_HUY
    }
} 