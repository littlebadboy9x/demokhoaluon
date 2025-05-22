package com.dormitory.management.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.Date;

@Data
@Entity
@Table(name = "cau_hinh_he_thong")
public class CauHinhHeThong {
    @Id
    @Column(name = "id_cau_hinh")
    private String idCauHinh;

    @Column(name = "mo_ta")
    private String moTa;

    @Column(name = "gia_tri", nullable = false)
    private String giaTri;

    @Column(name = "ngay_cap_nhat")
    @Temporal(TemporalType.TIMESTAMP)
    private Date ngayCapNhat;

    @Column(name = "nguoi_cap_nhat")
    private String nguoiCapNhat;

    @PrePersist
    @PreUpdate
    public void prePersist() {
        if (this.ngayCapNhat == null) {
            this.ngayCapNhat = new Date();
        }
    }
} 