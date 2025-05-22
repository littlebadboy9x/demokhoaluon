package com.dormitory.management.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.Date;
import org.springframework.format.annotation.DateTimeFormat;
import java.util.Calendar;

@Data
@Entity
@Table(name = "hoa_don")
public class HoaDon {
    @Id
    @Column(name = "ma_hoa_don")
    private String maHoaDon;

    @ManyToOne
    @JoinColumn(name = "ma_sv", nullable = false)
    private SinhVien sinhVien;

    @ManyToOne
    @JoinColumn(name = "ma_phong", nullable = false)
    private Phong phong;

    @Column(name = "thang", nullable = false)
    private Integer thang;

    @Column(name = "nam", nullable = false)
    private Integer nam;

    @Column(name = "tien_phong", nullable = false)
    private Double tienPhong;

    @Column(name = "tien_dien", nullable = false)
    private Double tienDien;

    @Column(name = "tien_nuoc", nullable = false)
    private Double tienNuoc;

    @Column(name = "phi_dich_vu", nullable = false)
    private Double phiDichVu;

    @Column(name = "tong_tien", nullable = false)
    private Double tongTien;

    @Column(name = "ngay_tao", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date ngayTao;

    @Column(name = "han_thanh_toan", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private Date hanThanhToan;

    @Column(name = "ngay_thanh_toan")
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date ngayThanhToan;

    @Enumerated(EnumType.STRING)
    @Column(name = "trang_thai", nullable = false, length = 20)
    private TrangThai trangThai = TrangThai.CHUA_THANH_TOAN;

    @Column(name = "ghi_chu")
    private String ghiChu;

    public enum TrangThai {
        CHUA_THANH_TOAN,
        DA_THANH_TOAN,
        QUA_HAN
    }

    @PrePersist
    @PreUpdate
    public void calculateTongTien() {
        this.tongTien = this.tienPhong + this.tienDien + this.tienNuoc + this.phiDichVu;
        
        // Tự động set hạn thanh toán là cuối tháng khi tạo mới hóa đơn
        if (this.hanThanhToan == null) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(this.nam, this.thang - 1, 1); // Tháng trong Calendar bắt đầu từ 0
            calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
            calendar.set(Calendar.HOUR_OF_DAY, 23);
            calendar.set(Calendar.MINUTE, 59);
            calendar.set(Calendar.SECOND, 59);
            this.hanThanhToan = calendar.getTime();
        }
    }
}