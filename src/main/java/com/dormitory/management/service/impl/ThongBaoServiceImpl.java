package com.dormitory.management.service.impl;

import com.dormitory.management.model.ThongBao;
import com.dormitory.management.model.Phong;
import com.dormitory.management.repository.ThongBaoRepository;
import com.dormitory.management.service.ThongBaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ThongBaoServiceImpl implements ThongBaoService {

    @Autowired
    private ThongBaoRepository thongBaoRepository;

    @Override
    public Page<ThongBao> findByPhongAndTrangThai(Phong phong, Boolean trangThai, Pageable pageable) {
        return thongBaoRepository.findByPhongAndTrangThaiOrderByNgayTaoDesc(phong, trangThai, pageable);
    }

    @Override
    public Page<ThongBao> findByTrangThai(Boolean trangThai, Pageable pageable) {
        return thongBaoRepository.findByTrangThaiOrderByNgayTaoDesc(trangThai, pageable);
    }

    @Override
    public Optional<ThongBao> findById(Long id) {
        return thongBaoRepository.findById(id);
    }

    @Override
    public ThongBao save(ThongBao thongBao) {
        return thongBaoRepository.save(thongBao);
    }

    @Override
    public void delete(Long id) {
        thongBaoRepository.deleteById(id);
    }
} 