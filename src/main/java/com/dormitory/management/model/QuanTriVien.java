package com.dormitory.management.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "quan_tri_vien")
@Data
public class QuanTriVien {
    @Id
    @Column(name = "ma_qt")
    private String maQt;

    @Column(name = "ten_dang_nhap", unique = true, nullable = false)
    private String tenDangNhap;

    @Column(name = "ho_ten", nullable = false)
    private String hoTen;

    @Column(name = "quyen_han", nullable = false)
    @Enumerated(EnumType.STRING)
    private QuyenHan quyenHan;

    @ManyToOne
    @JoinColumn(name = "ten_dang_nhap", referencedColumnName = "ten_dang_nhap", insertable = false, updatable = false)
    private NguoiDung nguoiDung;

    public enum QuyenHan {
        QUAN_TRI, QUAN_LY
    }
}