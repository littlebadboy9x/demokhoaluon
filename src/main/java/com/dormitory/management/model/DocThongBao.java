package com.dormitory.management.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.Date;

@Data
@Entity
@Table(name = "doc_thong_bao")
public class DocThongBao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_doc")
    private Long idDoc;

    @ManyToOne
    @JoinColumn(name = "id_thong_bao", nullable = false)
    private ThongBao thongBao;

    @ManyToOne
    @JoinColumn(name = "ma_sv", nullable = false)
    private SinhVien sinhVien;

    @Column(name = "ngay_doc")
    @Temporal(TemporalType.TIMESTAMP)
    private Date ngayDoc;

    @PrePersist
    public void prePersist() {
        if (this.ngayDoc == null) {
            this.ngayDoc = new Date();
        }
    }
} 