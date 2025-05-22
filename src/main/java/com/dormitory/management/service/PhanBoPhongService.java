package com.dormitory.management.service;

import com.dormitory.management.model.PhanBoPhong;
import com.dormitory.management.repository.PhanBoPhongRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PhanBoPhongService {

    @Autowired
    private PhanBoPhongRepository phanBoPhongRepository;

    public List<PhanBoPhong> findAll() {
        return phanBoPhongRepository.findAll();
    }

    public Optional<PhanBoPhong> findById(Long idPhanBo) {
        return phanBoPhongRepository.findById(idPhanBo);
    }

    public PhanBoPhong save(PhanBoPhong phanBoPhong) {
        return phanBoPhongRepository.save(phanBoPhong);
    }

    public void delete(Long idPhanBo) {
        phanBoPhongRepository.deleteById(idPhanBo);
    }
}