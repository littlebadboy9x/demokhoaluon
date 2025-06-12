-- Xóa database nếu tồn tại
DROP DATABASE IF EXISTS dormitory_management;

-- Tạo database mới
CREATE DATABASE dormitory_management
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

-- Sử dụng database
USE dormitory_management;

-- Tạo các bảng
-- Bảng người dùng
CREATE TABLE nguoi_dung (
    ten_dang_nhap VARCHAR(50) PRIMARY KEY,
    mat_khau VARCHAR(255) NOT NULL,
    loai_nguoi_dung ENUM('SINH_VIEN', 'QUAN_TRI_VIEN') NOT NULL,
    trang_thai BOOLEAN DEFAULT true,
    ngay_tao TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Bảng sinh viên
CREATE TABLE sinh_vien (
    ma_sv VARCHAR(20) PRIMARY KEY,
    ten_dang_nhap VARCHAR(50) NOT NULL UNIQUE,
    ho_ten VARCHAR(100) NOT NULL,
    ngay_sinh DATE,
    gioi_tinh ENUM('NAM', 'NU') NOT NULL,
    cccd VARCHAR(20) UNIQUE,
    email VARCHAR(100),
    so_dien_thoai VARCHAR(15),
    dia_chi TEXT,
    lop VARCHAR(20),
    nganh VARCHAR(100),
    khoa VARCHAR(100),
    ngay_dang_ky TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    trang_thai ENUM('DANG_O', 'DA_RUT', 'BI_DINH_CHI') DEFAULT 'DANG_O',
    ma_phong VARCHAR(20),
    FOREIGN KEY (ten_dang_nhap) REFERENCES nguoi_dung(ten_dang_nhap)
);

-- Bảng quản trị viên
CREATE TABLE quan_tri_vien (
    ma_qt VARCHAR(20) PRIMARY KEY,
    ten_dang_nhap VARCHAR(50) NOT NULL UNIQUE,
    ho_ten VARCHAR(100) NOT NULL,
    quyen_han ENUM('QUAN_TRI', 'QUAN_LY') NOT NULL,
    FOREIGN KEY (ten_dang_nhap) REFERENCES nguoi_dung(ten_dang_nhap)
);

-- Bảng phòng
CREATE TABLE phong (
    ma_phong VARCHAR(20) PRIMARY KEY,
    ten_phong VARCHAR(255),
    loai_phong ENUM('NAM', 'NU') NOT NULL,
    suc_chua INT NOT NULL,
    so_nguoi_hien_tai INT DEFAULT 0,
    trang_thai ENUM('CON_TRONG', 'DA_DU', 'DANG_SUA_CHUA') DEFAULT 'CON_TRONG',
    gia_phong DOUBLE NOT NULL,
    gia_dien DOUBLE NOT NULL,
    phi_ve_sinh DOUBLE NOT NULL,
    phi_dich_vu DOUBLE NOT NULL DEFAULT 100000.0,
    mo_ta TEXT,
    ngay_tao TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Thêm khóa ngoại cho bảng sinh_viên sau khi đã tạo bảng phòng
ALTER TABLE sinh_vien
ADD CONSTRAINT fk_sinh_vien_phong
    FOREIGN KEY (ma_phong)
    REFERENCES phong(ma_phong);

-- Bảng đăng ký phòng
CREATE TABLE dang_ky_phong (
    id_dang_ky BIGINT AUTO_INCREMENT PRIMARY KEY,
    ma_sv VARCHAR(20) NOT NULL,
    ma_phong VARCHAR(20),
    ngay_dang_ky TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    trang_thai ENUM('CHO_DUYET', 'DA_DUYET', 'TU_CHOI') DEFAULT 'CHO_DUYET',
    nguoi_duyet VARCHAR(50),
    ngay_duyet TIMESTAMP,
    ghi_chu VARCHAR(255),
    FOREIGN KEY (ma_sv) REFERENCES sinh_vien(ma_sv),
    FOREIGN KEY (ma_phong) REFERENCES phong(ma_phong)
);

-- Bảng phân bổ phòng
CREATE TABLE phan_bo_phong (
    id_phan_bo BIGINT AUTO_INCREMENT PRIMARY KEY,
    ma_sv VARCHAR(20) NOT NULL,
    ma_phong VARCHAR(20) NOT NULL,
    ngay_nhan_phong DATE NOT NULL,
    ngay_tra_phong DATE,
    trang_thai ENUM('DANG_O', 'DA_KET_THUC', 'BI_HUY') DEFAULT 'DANG_O',
    FOREIGN KEY (ma_sv) REFERENCES sinh_vien(ma_sv),
    FOREIGN KEY (ma_phong) REFERENCES phong(ma_phong)
);

-- Bảng hóa đơn
CREATE TABLE hoa_don (
    ma_hoa_don VARCHAR(50) PRIMARY KEY,
    ma_sv VARCHAR(20) NOT NULL,
    ma_phong VARCHAR(20) NOT NULL,
    thang INT NOT NULL,
    nam INT NOT NULL,
    tien_phong DOUBLE NOT NULL,
    tien_dien DOUBLE,
    tien_nuoc DOUBLE,
    phi_dich_vu DOUBLE,
    tong_tien DOUBLE NOT NULL,
    han_thanh_toan DATE NOT NULL,
    trang_thai ENUM('CHUA_THANH_TOAN', 'DA_THANH_TOAN', 'QUA_HAN') DEFAULT 'CHUA_THANH_TOAN',
    ngay_tao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    ngay_thanh_toan TIMESTAMP NULL,
    ghi_chu VARCHAR(255),
    FOREIGN KEY (ma_sv) REFERENCES sinh_vien(ma_sv),
    FOREIGN KEY (ma_phong) REFERENCES phong(ma_phong)
);

-- Bảng thanh toán
CREATE TABLE thanh_toan (
    id_thanh_toan BIGINT AUTO_INCREMENT PRIMARY KEY,
    ma_hoa_don VARCHAR(50) NOT NULL,
    so_tien DOUBLE NOT NULL,
    phuong_thuc ENUM('TIEN_MAT', 'CHUYEN_KHOAN', 'THE') NOT NULL,
    ngay_thanh_toan TIMESTAMP,
    nguoi_thu VARCHAR(50),
    ghi_chu TEXT,
    ma_giao_dich_momo VARCHAR(50),
    trang_thai_momo VARCHAR(50),
    url_thanh_toan TEXT,
    thoi_gian_het_han TIMESTAMP,
    FOREIGN KEY (ma_hoa_don) REFERENCES hoa_don(ma_hoa_don)
);

-- Bảng vi phạm
CREATE TABLE vi_pham (
    id_vi_pham BIGINT AUTO_INCREMENT PRIMARY KEY,
    ma_sv VARCHAR(20) NOT NULL,
    ngay_vi_pham DATE NOT NULL,
    mo_ta TEXT NOT NULL,
    muc_do ENUM('NHE', 'TRUNG_BINH', 'NGHIEM_TRONG') NOT NULL,
    hinh_thuc_xu_ly VARCHAR(200),
    nguoi_lap_bien_ban VARCHAR(50),
    ngay_lap_bien_ban TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    trang_thai ENUM('CHO_XU_LY', 'DA_XU_LY', 'HUY') DEFAULT 'CHO_XU_LY',
    FOREIGN KEY (ma_sv) REFERENCES sinh_vien(ma_sv)
);

-- Bảng sự cố
CREATE TABLE su_co (
    id_su_co BIGINT AUTO_INCREMENT PRIMARY KEY,
    ma_phong VARCHAR(20) NOT NULL,
    ma_sv VARCHAR(20),
    loai_su_co ENUM('DIEN', 'NUOC', 'CO_SO_VAT_CHAT', 'KHAC') NOT NULL,
    mo_ta TEXT NOT NULL,
    ngay_bao_cao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    muc_do_uu_tien ENUM('THAP', 'TRUNG_BINH', 'CAO', 'KHAN_CAP') NOT NULL,
    trang_thai ENUM('CHO_XU_LY', 'DANG_XU_LY', 'DA_HOAN_THANH', 'HUY') DEFAULT 'CHO_XU_LY',
    nguoi_xu_ly VARCHAR(50),
    ngay_hoan_thanh TIMESTAMP,
    ghi_chu TEXT,
    FOREIGN KEY (ma_phong) REFERENCES phong(ma_phong),
    FOREIGN KEY (ma_sv) REFERENCES sinh_vien(ma_sv)
);

-- Bảng thông báo
CREATE TABLE thong_bao (
    id_thong_bao BIGINT AUTO_INCREMENT PRIMARY KEY,
    tieu_de VARCHAR(200) NOT NULL,
    noi_dung TEXT NOT NULL,
    ngay_tao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    nguoi_tao VARCHAR(50) NOT NULL,
    loai_thong_bao ENUM('THONG_BAO_CHUNG', 'THONG_BAO_PHONG', 'THONG_BAO_CA_NHAN') NOT NULL,
    doi_tuong_nhan VARCHAR(20),
    trang_thai BOOLEAN DEFAULT true,
    ngay_het_han DATE
);

-- Bảng đọc thông báo
CREATE TABLE doc_thong_bao (
    id_doc BIGINT AUTO_INCREMENT PRIMARY KEY,
    id_thong_bao BIGINT NOT NULL,
    ma_sv VARCHAR(20) NOT NULL,
    ngay_doc TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_thong_bao) REFERENCES thong_bao(id_thong_bao),
    FOREIGN KEY (ma_sv) REFERENCES sinh_vien(ma_sv)
);

-- Bảng tài sản phòng
CREATE TABLE tai_san_phong (
    id_tai_san BIGINT AUTO_INCREMENT PRIMARY KEY,
    ma_phong VARCHAR(20) NOT NULL,
    ten_tai_san VARCHAR(100) NOT NULL,
    so_luong INT NOT NULL DEFAULT 1,
    tinh_trang ENUM('TOT', 'HONG', 'DANG_SUA_CHUA') DEFAULT 'TOT',
    ngay_cap_nhat TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    ghi_chu TEXT,
    FOREIGN KEY (ma_phong) REFERENCES phong(ma_phong)
);

-- Bảng kiểm tra phòng
CREATE TABLE kiem_tra_phong (
    id_kiem_tra BIGINT AUTO_INCREMENT PRIMARY KEY,
    ma_phong VARCHAR(20) NOT NULL,
    ngay_kiem_tra DATE NOT NULL,
    nguoi_kiem_tra VARCHAR(50) NOT NULL,
    noi_dung_kiem_tra TEXT,
    danh_gia ENUM('TOT', 'KHA', 'TRUNG_BINH', 'YEU') NOT NULL,
    ghi_chu TEXT,
    FOREIGN KEY (ma_phong) REFERENCES phong(ma_phong)
);

-- Bảng cấu hình hệ thống
CREATE TABLE cau_hinh_he_thong (
    id_cau_hinh VARCHAR(50) PRIMARY KEY,
    mo_ta VARCHAR(200),
    gia_tri TEXT NOT NULL,
    ngay_cap_nhat TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    nguoi_cap_nhat VARCHAR(50)
);

-- Bảng giao dịch MoMo
CREATE TABLE momo_transaction (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    partner_code VARCHAR(50) NOT NULL,
    order_id VARCHAR(50) NOT NULL,
    request_id VARCHAR(50) NOT NULL,
    amount DOUBLE NOT NULL,
    order_info VARCHAR(255) NOT NULL,
    order_type VARCHAR(50) NOT NULL,
    trans_id BIGINT,
    result_code INT,
    message TEXT,
    pay_type VARCHAR(50),
    response_time BIGINT,
    extra_data TEXT,
    signature VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Tạo các indexes
CREATE INDEX idx_sv_ten_dang_nhap ON sinh_vien(ten_dang_nhap);
CREATE INDEX idx_qtv_ten_dang_nhap ON quan_tri_vien(ten_dang_nhap);
CREATE INDEX idx_hd_ma_sv ON hoa_don(ma_sv);
CREATE INDEX idx_hd_ma_phong ON hoa_don(ma_phong);
CREATE INDEX idx_pb_ma_sv ON phan_bo_phong(ma_sv);
CREATE INDEX idx_pb_ma_phong ON phan_bo_phong(ma_phong);
CREATE INDEX idx_dk_ma_sv ON dang_ky_phong(ma_sv);
CREATE INDEX idx_dk_ma_phong ON dang_ky_phong(ma_phong);
CREATE INDEX idx_vi_pham_ma_sv ON vi_pham(ma_sv);
CREATE INDEX idx_su_co_ma_phong ON su_co(ma_phong);
CREATE INDEX idx_su_co_ma_sv ON su_co(ma_sv);
CREATE INDEX idx_thong_bao_loai ON thong_bao(loai_thong_bao);
CREATE INDEX idx_doc_thong_bao_ma_sv ON doc_thong_bao(ma_sv);
CREATE INDEX idx_tai_san_ma_phong ON tai_san_phong(ma_phong);
CREATE INDEX idx_kiem_tra_ma_phong ON kiem_tra_phong(ma_phong); 

ALTER TABLE thong_bao
ADD COLUMN ma_phong VARCHAR(20),
ADD CONSTRAINT fk_thong_bao_phong
    FOREIGN KEY (ma_phong)
    REFERENCES phong(ma_phong);

-- Drop bảng cũ nếu tồn tại
DROP TABLE IF EXISTS momo_transaction;

-- Tạo lại bảng mới với đầy đủ các cột
CREATE TABLE momo_transaction (
                                  id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                  partner_code VARCHAR(50) NOT NULL,
                                  order_id VARCHAR(50) NOT NULL,
                                  request_id VARCHAR(50) NOT NULL,
                                  amount DOUBLE NOT NULL,
                                  order_info VARCHAR(255) NOT NULL,
                                  order_type VARCHAR(50) NOT NULL,
                                  redirect_url VARCHAR(255),
                                  ipn_url VARCHAR(255),
                                  request_type VARCHAR(50),
                                  trans_id BIGINT,
                                  result_code INT,
                                  message VARCHAR(255),
                                  pay_type VARCHAR(50),
                                  response_time BIGINT,
                                  extra_data TEXT,
                                  signature VARCHAR(255),
                                  created_at TIMESTAMP,
                                  updated_at TIMESTAMP
);

-- Migration script để cập nhật schema database
-- Thêm giá trị MOMO vào ENUM phuong_thuc trong bảng thanh_toan

USE dormitory_management;

-- Cập nhật ENUM để thêm giá trị MOMO
ALTER TABLE thanh_toan MODIFY COLUMN phuong_thuc ENUM('TIEN_MAT', 'CHUYEN_KHOAN', 'MOMO', 'THE') NOT NULL;

-- Kiểm tra kết quả
DESCRIBE thanh_toan; 