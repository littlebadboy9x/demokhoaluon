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
INSERT INTO phong (ma_phong, ten_phong, loai_phong, suc_chua, so_nguoi_hien_tai, trang_thai, gia_phong, gia_dien, phi_ve_sinh, mo_ta) VALUES
('P101', 'Phòng 101', 'NAM', 8, 6, 'CON_TRONG', 500000, 3500, 50000, 'Phòng tầng 1 - Khu A'),
('P102', 'Phòng 102', 'NAM', 8, 8, 'DA_DU', 500000, 3500, 50000, 'Phòng tầng 1 - Khu A'),
('P103', 'Phòng 103', 'NAM', 8, 4, 'CON_TRONG', 500000, 3500, 50000, 'Phòng tầng 1 - Khu A'),
('P201', 'Phòng 201', 'NU', 8, 5, 'CON_TRONG', 500000, 3500, 50000, 'Phòng tầng 2 - Khu A'),
('P202', 'Phòng 202', 'NU', 8, 7, 'CON_TRONG', 500000, 3500, 50000, 'Phòng tầng 2 - Khu A'),
('P203', 'Phòng 203', 'NU', 8, 0, 'CON_TRONG', 500000, 3500, 50000, 'Phòng tầng 2 - Khu A'),
('P301', 'Phòng 301', 'NAM', 8, 0, 'DANG_SUA_CHUA', 500000, 3500, 50000, 'Phòng tầng 3 - Khu A');

-- Thêm dữ liệu mẫu cho bảng dang_ky_phong
INSERT INTO dang_ky_phong (ma_sv, ma_phong, ngay_dang_ky, trang_thai, ghi_chu) VALUES
('SV001', 'P101', '2024-05-22 10:30:00', 'CHO_DUYET', 'Đăng ký ở từ học kỳ 1'),
('SV002', 'P201', '2024-05-22 11:15:00', 'CHO_DUYET', 'Đăng ký ở từ học kỳ 1');

-- Thêm dữ liệu mẫu cho bảng phan_bo_phong
INSERT INTO phan_bo_phong (ma_sv, ma_phong, ngay_nhan_phong, trang_thai) VALUES
('SV001', 'P101', '2024-05-01', 'DANG_O'),
('SV002', 'P201', '2024-05-01', 'DANG_O');

-- Thêm dữ liệu mẫu cho bảng hoa_don
INSERT INTO hoa_don (ma_hoa_don, ma_sv, ma_phong, thang, nam, tien_phong, tien_dien, tien_nuoc, phi_dich_vu, tong_tien, han_thanh_toan, trang_thai, ghi_chu) VALUES
('HD202405101', 'SV001', 'P101', 5, 2024, 500000, 350000, 100000, 50000, 1000000, '2024-05-15', 'CHUA_THANH_TOAN', 'Hóa đơn tháng 5/2024'),
('HD202405102', 'SV002', 'P201', 5, 2024, 500000, 280000, 80000, 50000, 910000, '2024-05-15', 'DA_THANH_TOAN', 'Hóa đơn tháng 5/2024'),
('HD202404101', 'SV001', 'P101', 4, 2024, 500000, 320000, 90000, 50000, 960000, '2024-04-15', 'DA_THANH_TOAN', 'Hóa đơn tháng 4/2024'),
('HD202404102', 'SV002', 'P201', 4, 2024, 500000, 300000, 85000, 50000, 935000, '2024-04-15', 'DA_THANH_TOAN', 'Hóa đơn tháng 4/2024'),
('HD202403101', 'SV001', 'P101', 3, 2024, 500000, 380000, 110000, 50000, 1040000, '2024-03-15', 'DA_THANH_TOAN', 'Hóa đơn tháng 3/2024'),
('HD202403102', 'SV002', 'P201', 3, 2024, 500000, 290000, 75000, 50000, 915000, '2024-03-15', 'DA_THANH_TOAN', 'Hóa đơn tháng 3/2024'),
('HD202405103', 'SV003', 'P102', 5, 2024, 500000, 360000, 95000, 50000, 1005000, '2024-05-15', 'QUA_HAN', 'Hóa đơn tháng 5/2024'),
('HD202405104', 'SV004', 'P102', 5, 2024, 500000, 360000, 95000, 50000, 1005000, '2024-05-15', 'CHUA_THANH_TOAN', 'Hóa đơn tháng 5/2024'),
('HD202405105', 'SV005', 'P103', 5, 2024, 500000, 340000, 88000, 50000, 978000, '2024-05-15', 'DA_THANH_TOAN', 'Hóa đơn tháng 5/2024');

-- Thêm dữ liệu mẫu cho bảng thanh_toan
INSERT INTO thanh_toan (ma_hoa_don, so_tien, phuong_thuc, ngay_thanh_toan, nguoi_thu, ghi_chu) VALUES
('HD202405102', 910000, 'CHUYEN_KHOAN', '2024-05-10 15:30:00', 'admin', 'Thanh toán qua chuyển khoản');

-- Thêm dữ liệu mẫu cho bảng su_co
INSERT INTO su_co (ma_phong, ma_sv, loai_su_co, mo_ta, muc_do_uu_tien, trang_thai, ghi_chu) VALUES
('P301', NULL, 'CO_SO_VAT_CHAT', 'Hỏng quạt trần', 'CAO', 'DANG_XU_LY', 'Cần thay mới quạt trần'),
('P101', 'SV001', 'NUOC', 'Vòi nước rò rỉ', 'TRUNG_BINH', 'DA_HOAN_THANH', 'Đã sửa xong vòi nước'),
('P202', NULL, 'DIEN', 'Bóng đèn hỏng', 'THAP', 'CHO_XU_LY', 'Cần thay bóng đèn mới');

-- Thêm dữ liệu mẫu cho bảng thong_bao
INSERT INTO thong_bao (tieu_de, noi_dung, nguoi_tao, loai_thong_bao, ngay_het_han) VALUES
('Thông báo về việc đóng tiền phòng tháng 5', 'Đề nghị các phòng đóng tiền phòng tháng 5 trước ngày 15/5/2024', 'admin', 'THONG_BAO_CHUNG', '2024-05-15'),
('Thông báo kiểm tra phòng định kỳ', 'Sẽ tiến hành kiểm tra phòng định kỳ vào ngày 25/5/2024', 'admin', 'THONG_BAO_CHUNG', '2024-05-25'),
('Thông báo sửa chữa cơ sở vật chất', 'Sẽ tiến hành sửa chữa, bảo dưỡng điều hòa các phòng từ ngày 01/6/2024', 'admin', 'THONG_BAO_CHUNG', '2024-06-01');

-- Thêm dữ liệu mẫu cho bảng doc_thong_bao
INSERT INTO doc_thong_bao (id_thong_bao, ma_sv) VALUES
(1, 'SV001'),
(1, 'SV002'),
(2, 'SV001');

-- Thêm dữ liệu mẫu cho bảng tai_san_phong
INSERT INTO tai_san_phong (ma_phong, ten_tai_san, so_luong, tinh_trang, ghi_chu) VALUES
('P101', 'Giường tầng', 4, 'TOT', 'Mới thay 4 giường tầng'),
('P101', 'Quạt trần', 2, 'TOT', 'Hoạt động tốt'),
('P101', 'Điều hòa', 1, 'TOT', 'Mới bảo dưỡng'),
('P201', 'Giường tầng', 4, 'TOT', 'Hoạt động tốt'),
('P201', 'Quạt trần', 2, 'TOT', 'Hoạt động tốt'),
('P201', 'Điều hòa', 1, 'TOT', 'Mới bảo dưỡng');

-- Thêm dữ liệu mẫu cho bảng kiem_tra_phong
INSERT INTO kiem_tra_phong (ma_phong, ngay_kiem_tra, nguoi_kiem_tra, danh_gia, noi_dung_kiem_tra, ghi_chu) VALUES
('P101', '2024-05-01', 'admin', 'TOT', 'Kiểm tra định kỳ tháng 5', 'Phòng sạch sẽ, ngăn nắp'),
('P201', '2024-05-01', 'admin', 'KHA', 'Kiểm tra định kỳ tháng 5', 'Cần dọn dẹp thêm');

 