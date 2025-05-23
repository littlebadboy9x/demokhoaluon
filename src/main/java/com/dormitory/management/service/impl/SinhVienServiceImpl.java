package com.dormitory.management.service.impl;

import com.dormitory.management.model.Phong;
import com.dormitory.management.model.SinhVien;
import com.dormitory.management.model.SinhVien.TrangThai;
import com.dormitory.management.repository.SinhVienRepository;
import com.dormitory.management.service.SinhVienService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import jakarta.persistence.criteria.Predicate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class SinhVienServiceImpl implements SinhVienService {

    @Autowired
    private SinhVienRepository sinhVienRepository;

    @Override
    public List<SinhVien> findAll() {
        return sinhVienRepository.findAll();
    }

    @Override
    public Page<SinhVien> findAll(Pageable pageable) {
        return sinhVienRepository.findAll(pageable);
    }

    @Override
    public Optional<SinhVien> findById(String mssv) {
        return sinhVienRepository.findById(mssv);
    }

    @Override
    public SinhVien save(SinhVien sinhVien) {
        return sinhVienRepository.save(sinhVien);
    }

    @Override
    public void delete(String mssv) {
        sinhVienRepository.deleteById(mssv);
    }

    @Override
    public long countByTrangThai(TrangThai trangThai) {
        return sinhVienRepository.countByTrangThai(trangThai);
    }

    @Override
    public Page<SinhVien> findByTrangThaiOrderByNgayDangKyDesc(TrangThai trangThai, Pageable pageable) {
        return sinhVienRepository.findByTrangThaiOrderByNgayDangKyDesc(trangThai, pageable);
    }

    @Override
    public long count() {
        return sinhVienRepository.count();
    }

    @Override
    public List<SinhVien> findByPhong(Phong phong) {
        return sinhVienRepository.findByPhong(phong);
    }

    @Override
    public Page<SinhVien> search(String search, String khoa, String lop, TrangThai trangThai, Pageable pageable) {
        Specification<SinhVien> spec = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Tìm kiếm theo tên hoặc mã sinh viên
            if (search != null && !search.isEmpty()) {
                predicates.add(criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("hoTen")), "%" + search.toLowerCase() + "%"),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("maSv")), "%" + search.toLowerCase() + "%")
                ));
            }

            // Lọc theo khoa
            if (khoa != null && !khoa.isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("khoa"), khoa));
            }

            // Lọc theo lớp
            if (lop != null && !lop.isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("lop"), lop));
            }

            // Lọc theo trạng thái
            if (trangThai != null) {
                predicates.add(criteriaBuilder.equal(root.get("trangThai"), trangThai));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };

        return sinhVienRepository.findAll(spec, pageable);
    }

    @Override
    public Optional<SinhVien> findByTenDangNhap(String tenDangNhap) {
        return sinhVienRepository.findByTenDangNhap(tenDangNhap);
    }

    @Override
    public boolean existsByTenDangNhap(String tenDangNhap) {
        return sinhVienRepository.existsByTenDangNhap(tenDangNhap);
    }

    @Override
    public boolean existsByEmail(String email) {
        return sinhVienRepository.existsByEmail(email);
    }

    @Override
    public boolean existsByMaSv(String maSv) {
        return sinhVienRepository.existsByMaSv(maSv);
    }
} 