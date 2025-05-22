package com.dormitory.management.dto;

import lombok.Data;

@Data
public class PhongDTO {
    private Long id;
    private String maPhong;
    private String tenPhong;
    private String loaiPhong;
    private Integer tang;
    private Integer sucChua;
    private Integer soNguoiHienTai;
    private Double giaPhong;
    private String trangThai;
    private String ghiChu;
}