package com.dormitory.management.dto;

import lombok.Data;

@Data
public class NguoiDungDTO {
    private Long id;
    private String tenDangNhap;
    private String matKhau;
    private String vaiTro;
    private Boolean trangThai;
}