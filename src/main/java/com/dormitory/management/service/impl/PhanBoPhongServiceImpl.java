package com.dormitory.management.service.impl;

import com.dormitory.management.model.PhanBoPhong;
import com.dormitory.management.model.SinhVien;
import com.dormitory.management.model.Phong;
import com.dormitory.management.repository.PhanBoPhongRepository;
import com.dormitory.management.service.PhanBoPhongService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PhanBoPhongServiceImpl implements PhanBoPhongService {

    @Autowired
    private PhanBoPhongRepository phanBoPhongRepository;

    @Override
    public List<PhanBoPhong> findAll() {
        return phanBoPhongRepository.findAll();
    }

    @Override
    public Optional<PhanBoPhong> findById(Long idPhanBo) {
        return phanBoPhongRepository.findById(idPhanBo);
    }

    @Override
    public PhanBoPhong save(PhanBoPhong phanBoPhong) {
        return phanBoPhongRepository.save(phanBoPhong);
    }

    @Override
    public void delete(Long idPhanBo) {
        phanBoPhongRepository.deleteById(idPhanBo);
    }

    @Override
    public Optional<PhanBoPhong> findBySinhVienAndTrangThai(SinhVien sinhVien, PhanBoPhong.TrangThai trangThai) {
        return phanBoPhongRepository.findBySinhVienAndTrangThai(sinhVien, trangThai);
    }
} 