ALTER TABLE thong_bao
ADD COLUMN ma_phong VARCHAR(20),
ADD CONSTRAINT fk_thong_bao_phong
    FOREIGN KEY (ma_phong)
    REFERENCES phong(ma_phong); 