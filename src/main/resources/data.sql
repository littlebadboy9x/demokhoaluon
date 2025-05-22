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
    suc_chua INT NOT NULL,
    so_nguoi_hien_tai INT DEFAULT 0,
    trang_thai ENUM('CON_TRONG', 'DA_DU', 'DANG_SUA_CHUA') DEFAULT 'CON_TRONG',
    gia_phong DOUBLE NOT NULL,
    gia_dien DOUBLE NOT NULL,
    phi_ve_sinh DOUBLE NOT NULL,
    ngay_tao TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Bảng đăng ký phòng
CREATE TABLE dang_ky_phong (
    id_dang_ky BIGINT AUTO_INCREMENT PRIMARY KEY,
    ma_sv VARCHAR(20) NOT NULL,
    ma_phong VARCHAR(20),
    ngay_dang_ky TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    trang_thai ENUM('CHO_DUYET', 'DA_DUYET', 'TU_CHOI') DEFAULT 'CHO_DUYET',
    nguoi_duyet VARCHAR(50),
    ngay_duyet TIMESTAMP,
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
    dien_tieu_thu DOUBLE,
    tien_dien DOUBLE,
    phi_ve_sinh DOUBLE,
    phi_khac DOUBLE,
    tong_tien DOUBLE NOT NULL,
    han_thanh_toan DATE NOT NULL,
    trang_thai ENUM('CHO_THANH_TOAN', 'DA_THANH_TOAN', 'QUA_HAN') DEFAULT 'CHO_THANH_TOAN',
    ngay_tao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
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

-- Insert cấu hình MoMo
INSERT INTO cau_hinh_he_thong (id_cau_hinh, mo_ta, gia_tri, ngay_cap_nhat) VALUES
('MOMO_PARTNER_CODE', 'Mã đối tác MoMo', 'MOMOV2OF20220801', CURRENT_TIMESTAMP),
('MOMO_ACCESS_KEY', 'Access Key MoMo', 'M8brj9K6E22vXoDB', CURRENT_TIMESTAMP),
('MOMO_SECRET_KEY', 'Secret Key MoMo', 'nqQiVSgDMy809JoPF6OzP5OdBUB550Y4', CURRENT_TIMESTAMP),
('MOMO_ENDPOINT', 'API Endpoint MoMo', 'https://test-payment.momo.vn/v2/gateway/api/create', CURRENT_TIMESTAMP);

-- Thêm dữ liệu mẫu cho bảng nguoi_dung
INSERT INTO nguoi_dung (ten_dang_nhap, mat_khau, loai_nguoi_dung, trang_thai) VALUES
('admin', '$2a$12$kgdEsi9NRMVl5jzoxz5Nt.U85irGvVnKEqGUWxTpXmfQNqaokzM5O', 'QUAN_TRI_VIEN', true),
('sv001', '$2a$12$kgdEsi9NRMVl5jzoxz5Nt.U85irGvVnKEqGUWxTpXmfQNqaokzM5O', 'SINH_VIEN', true),
('sv002', '$2a$12$kgdEsi9NRMVl5jzoxz5Nt.U85irGvVnKEqGUWxTpXmfQNqaokzM5O', 'SINH_VIEN', true),
('sv003', '$2a$12$kgdEsi9NRMVl5jzoxz5Nt.U85irGvVnKEqGUWxTpXmfQNqaokzM5O', 'SINH_VIEN', true),
('sv004', '$2a$12$kgdEsi9NRMVl5jzoxz5Nt.U85irGvVnKEqGUWxTpXmfQNqaokzM5O', 'SINH_VIEN', true),
('sv005', '$2a$12$kgdEsi9NRMVl5jzoxz5Nt.U85irGvVnKEqGUWxTpXmfQNqaokzM5O', 'SINH_VIEN', true);

-- Thêm dữ liệu mẫu cho bảng quan_tri_vien
INSERT INTO quan_tri_vien (ma_qt, ten_dang_nhap, ho_ten, quyen_han) VALUES
('QTV001', 'admin', 'Administrator', 'QUAN_TRI');

-- Thêm dữ liệu mẫu cho bảng sinh_vien
INSERT INTO sinh_vien (ma_sv, ten_dang_nhap, ho_ten, ngay_sinh, gioi_tinh, cccd, email, so_dien_thoai, lop, nganh, khoa) VALUES
('SV001', 'sv001', 'Nguyễn Văn A', '2003-05-15', 'NAM', '001203012345', 'sva@gmail.com', '0123456789', 'CNTT1', 'Công nghệ thông tin', 'Công nghệ thông tin'),
('SV002', 'sv002', 'Trần Thị B', '2003-08-20', 'NU', '001203012346', 'svb@gmail.com', '0123456788', 'CNTT2', 'Công nghệ thông tin', 'Công nghệ thông tin'),
('SV003', 'sv003', 'Lê Văn C', '2003-03-10', 'NAM', '001203012347', 'svc@gmail.com', '0123456787', 'CK1', 'Cơ khí', 'Cơ khí'),
('SV004', 'sv004', 'Phạm Thị D', '2003-12-25', 'NU', '001203012348', 'svd@gmail.com', '0123456786', 'KT1', 'Kế toán', 'Kế toán'),
('SV005', 'sv005', 'Hoàng Văn E', '2003-07-30', 'NAM', '001203012349', 'sve@gmail.com', '0123456785', 'DDT1', 'Điện - Điện tử', 'Điện - Điện tử');

-- Thêm dữ liệu mẫu cho bảng phong
INSERT INTO phong (ma_phong, suc_chua, so_nguoi_hien_tai, trang_thai, gia_phong, gia_dien, phi_ve_sinh) VALUES
('P101', 8, 6, 'CON_TRONG', 500000, 3500, 50000),
('P102', 8, 8, 'DA_DU', 500000, 3500, 50000),
('P103', 8, 4, 'CON_TRONG', 500000, 3500, 50000),
('P201', 8, 5, 'CON_TRONG', 500000, 3500, 50000),
('P202', 8, 7, 'CON_TRONG', 500000, 3500, 50000),
('P203', 8, 0, 'CON_TRONG', 500000, 3500, 50000),
('P301', 8, 0, 'DANG_SUA_CHUA', 500000, 3500, 50000);

-- Thêm dữ liệu mẫu cho bảng dang_ky_phong
INSERT INTO dang_ky_phong (ma_sv, ma_phong, ngay_dang_ky, trang_thai) VALUES
('SV001', 'P101', '2024-05-22 10:30:00', 'CHO_DUYET'),
('SV002', 'P201', '2024-05-22 11:15:00', 'CHO_DUYET');

-- Thêm dữ liệu mẫu cho bảng phan_bo_phong
INSERT INTO phan_bo_phong (ma_sv, ma_phong, ngay_nhan_phong, trang_thai) VALUES
('SV001', 'P101', '2024-05-01', 'DANG_O'),
('SV002', 'P201', '2024-05-01', 'DANG_O');

-- Thêm dữ liệu mẫu cho bảng hoa_don
INSERT INTO hoa_don (ma_hoa_don, ma_sv, ma_phong, thang, nam, tien_phong, dien_tieu_thu, tien_dien, phi_ve_sinh, tong_tien, han_thanh_toan, trang_thai) VALUES
('HD202405101', 'SV001', 'P101', 5, 2024, 500000, 100, 350000, 50000, 900000, '2024-05-15', 'CHO_THANH_TOAN'),
('HD202405102', 'SV002', 'P201', 5, 2024, 500000, 80, 280000, 50000, 830000, '2024-05-15', 'DA_THANH_TOAN');

-- Thêm dữ liệu mẫu cho bảng thanh_toan
INSERT INTO thanh_toan (ma_hoa_don, so_tien, phuong_thuc, ngay_thanh_toan, nguoi_thu) VALUES
('HD202405102', 830000, 'CHUYEN_KHOAN', '2024-05-10 15:30:00', 'admin');

-- Thêm dữ liệu mẫu cho bảng su_co
INSERT INTO su_co (ma_phong, ma_sv, loai_su_co, mo_ta, muc_do_uu_tien, trang_thai) VALUES
('P301', NULL, 'CO_SO_VAT_CHAT', 'Hỏng quạt trần', 'CAO', 'DANG_XU_LY'),
('P101', 'SV001', 'NUOC', 'Vòi nước rò rỉ', 'TRUNG_BINH', 'DA_HOAN_THANH'),
('P202', NULL, 'DIEN', 'Bóng đèn hỏng', 'THAP', 'CHO_XU_LY');

-- Thêm dữ liệu mẫu cho bảng thong_bao
INSERT INTO thong_bao (tieu_de, noi_dung, nguoi_tao, loai_thong_bao) VALUES
('Thông báo về việc đóng tiền phòng tháng 5', 'Đề nghị các phòng đóng tiền phòng tháng 5 trước ngày 15/5/2024', 'admin', 'THONG_BAO_CHUNG'),
('Thông báo kiểm tra phòng định kỳ', 'Sẽ tiến hành kiểm tra phòng định kỳ vào ngày 25/5/2024', 'admin', 'THONG_BAO_CHUNG'),
('Thông báo sửa chữa cơ sở vật chất', 'Sẽ tiến hành sửa chữa, bảo dưỡng điều hòa các phòng từ ngày 01/6/2024', 'admin', 'THONG_BAO_CHUNG');

-- Thêm dữ liệu mẫu cho bảng tai_san_phong
INSERT INTO tai_san_phong (ma_phong, ten_tai_san, so_luong, tinh_trang) VALUES
('P101', 'Giường tầng', 4, 'TOT'),
('P101', 'Quạt trần', 2, 'TOT'),
('P101', 'Điều hòa', 1, 'TOT'),
('P201', 'Giường tầng', 4, 'TOT'),
('P201', 'Quạt trần', 2, 'TOT'),
('P201', 'Điều hòa', 1, 'TOT');

-- Thêm dữ liệu mẫu cho bảng kiem_tra_phong
INSERT INTO kiem_tra_phong (ma_phong, ngay_kiem_tra, nguoi_kiem_tra, danh_gia, noi_dung_kiem_tra) VALUES
('P101', '2024-05-01', 'admin', 'TOT', 'Kiểm tra định kỳ tháng 5'),
('P201', '2024-05-01', 'admin', 'KHA', 'Kiểm tra định kỳ tháng 5');

 