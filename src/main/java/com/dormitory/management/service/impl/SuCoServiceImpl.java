package com.dormitory.management.service.impl;

import com.dormitory.management.model.SuCo;
import com.dormitory.management.model.SuCo.TrangThai;
import com.dormitory.management.model.SuCo.MucDoUuTien;
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
    public Optional<SuCo> findById(Long idSuCo) {
        return suCoRepository.findById(idSuCo);
    }

    @Override
    public SuCo save(SuCo suCo) {
        return suCoRepository.save(suCo);
    }

    @Override
    public void delete(Long idSuCo) {
        suCoRepository.deleteById(idSuCo);
    }

    @Override
    public long countByTrangThai(TrangThai trangThai) {
        return suCoRepository.countByTrangThai(trangThai);
    }

    @Override
    public Page<SuCo> findByTrangThaiOrderByNgayBaoCaoDesc(TrangThai trangThai, Pageable pageable) {
        return suCoRepository.findByTrangThaiOrderByNgayBaoCaoDesc(trangThai, pageable);
    }

    @Override
    public long countByMucDoUuTien(MucDoUuTien mucDoUuTien) {
        return suCoRepository.countByMucDoUuTien(mucDoUuTien);
    }

    @Override
    public Page<SuCo> search(String keyword, TrangThai trangThai, MucDoUuTien mucDoUuTien, String maPhong, Pageable pageable) {
        if (keyword != null && !keyword.trim().isEmpty()) {
            return suCoRepository.findByMoTaContainingIgnoreCase(keyword, pageable);
        } else if (trangThai != null) {
            return suCoRepository.findByTrangThai(trangThai, pageable);
        } else if (mucDoUuTien != null) {
            return suCoRepository.findByMucDoUuTien(mucDoUuTien, pageable);
        } else if (maPhong != null && !maPhong.trim().isEmpty()) {
            return suCoRepository.findByPhong_MaPhong(maPhong, pageable);
        }
        return suCoRepository.findAll(pageable);
    }
} 