-- Thêm cột phi_dich_vu vào bảng phong
ALTER TABLE phong
ADD COLUMN phi_dich_vu DOUBLE PRECISION NOT NULL DEFAULT 100000.0;

-- Thêm cột ma_phong vào bảng sinh_vien
ALTER TABLE sinh_vien
ADD COLUMN ma_phong VARCHAR(50),
ADD CONSTRAINT fk_sinh_vien_phong
    FOREIGN KEY (ma_phong)
    REFERENCES phong (ma_phong); 