package com.dormitory.management.service;

import com.dormitory.management.model.ThanhToan;
import com.dormitory.management.repository.ThanhToanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ThanhToanService {

    @Autowired
    private ThanhToanRepository thanhToanRepository;

    public List<ThanhToan> findAll() {
        return thanhToanRepository.findAll();
    }

    public Optional<ThanhToan> findById(Long idThanhToan) {
        return thanhToanRepository.findById(idThanhToan);
    }

    public ThanhToan save(ThanhToan thanhToan) {
        return thanhToanRepository.save(thanhToan);
    }

    public void delete(Long idThanhToan) {
        thanhToanRepository.deleteById(idThanhToan);
    }
}