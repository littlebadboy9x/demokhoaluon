package com.dormitory.management.service.impl;

import com.dormitory.management.model.SuCo;
import com.dormitory.management.model.SuCo.TrangThai;
import com.dormitory.management.repository.SuCoRepository;
import com.dormitory.management.service.SuCoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SuCoServiceImpl implements SuCoService {

    @Autowired
    private SuCoRepository suCoRepository;

    @Override
    public List<SuCo> findAll() {
        return suCoRepository.findAll();
    }

    @Override
    public Page<SuCo> findAll(Pageable pageable) {
        return suCoRepository.findAll(pageable);
    }

    @Override
    public Optional<SuCo> findById(Long maSuCo) {
        return suCoRepository.findById(maSuCo);
    }

    @Override
    public SuCo save(SuCo suCo) {
        return suCoRepository.save(suCo);
    }

    @Override
    public void delete(Long maSuCo) {
        suCoRepository.deleteById(maSuCo);
    }

    @Override
    public long countByTrangThai(TrangThai trangThai) {
        return suCoRepository.countByTrangThai(trangThai);
    }

    @Override
    public Page<SuCo> findByTrangThaiOrderByNgayBaoCaoDesc(TrangThai trangThai, Pageable pageable) {
        return suCoRepository.findByTrangThaiOrderByNgayBaoCaoDesc(trangThai, pageable);
    }
} 