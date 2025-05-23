package com.dormitory.management.service;

import com.dormitory.management.model.PhanBoPhong;
import com.dormitory.management.model.SinhVien;
import java.util.List;
import java.util.Optional;

public interface PhanBoPhongService {
    List<PhanBoPhong> findAll();
    
    Optional<PhanBoPhong> findById(Long idPhanBo);
    
    PhanBoPhong save(PhanBoPhong phanBoPhong);
    
    void delete(Long idPhanBo);
    
    Optional<PhanBoPhong> findBySinhVienAndTrangThai(SinhVien sinhVien, PhanBoPhong.TrangThai trangThai);
}