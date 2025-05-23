package com.dormitory.management.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.Date;

@Data
@Entity
@Table(name = "momo_transaction")
public class MomoTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "partner_code", nullable = false)
    private String partnerCode;

    @Column(name = "order_id", nullable = false)
    private String orderId;

    @Column(name = "request_id", nullable = false)
    private String requestId;

    @Column(name = "amount", nullable = false)
    private Double amount;

    @Column(name = "order_info", nullable = false)
    private String orderInfo;

    @Column(name = "order_type", nullable = false)
    private String orderType;

    @Column(name = "redirect_url")
    private String redirectUrl;

    @Column(name = "ipn_url")
    private String ipnUrl;

    @Column(name = "request_type")
    private String requestType;

    @Column(name = "trans_id")
    private Long transId;

    @Column(name = "result_code")
    private Integer resultCode;

    @Column(name = "message")
    private String message;

    @Column(name = "pay_type")
    private String payType;

    @Column(name = "response_time")
    private Long responseTime;

    @Column(name = "extra_data")
    private String extraData;

    @Column(name = "signature")
    private String signature;

    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;

    @PrePersist
    public void prePersist() {
        if (this.createdAt == null) {
            this.createdAt = new Date();
        }
        if (this.updatedAt == null) {
            this.updatedAt = new Date();
        }
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = new Date();
    }
} 