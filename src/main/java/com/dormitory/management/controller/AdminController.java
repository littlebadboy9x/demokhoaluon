package com.dormitory.management.controller;

import com.dormitory.management.model.DangKyPhong;
import com.dormitory.management.model.PhanBoPhong;
import com.dormitory.management.model.Phong;
import com.dormitory.management.model.SinhVien;
import com.dormitory.management.repository.DangKyPhongRepository;
import com.dormitory.management.repository.PhanBoPhongRepository;
import com.dormitory.management.repository.PhongRepository;
import com.dormitory.management.repository.SinhVienRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private DangKyPhongRepository dangKyPhongRepository;

    @Autowired
    private PhongRepository phongRepository;

    @Autowired
    private SinhVienRepository sinhVienRepository;

    @Autowired
    private PhanBoPhongRepository phanBoPhongRepository;

    @GetMapping("/quan-ly/don-doi-phong")
    public String quanLyDonDoiPhong(Model model) {
        List<DangKyPhong> danhSachDonDoiPhong = dangKyPhongRepository.findByTrangThaiOrderByNgayDangKyDesc(DangKyPhong.TrangThai.CHO_DUYET);
        model.addAttribute("danhSachDonDoiPhong", danhSachDonDoiPhong);
        return "admin/quan-ly-don-doi-phong";
    }

    @PostMapping("/quan-ly/don-doi-phong/{id}/duyet")
    public String duyetDonDoiPhong(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            DangKyPhong donDoiPhong = dangKyPhongRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn đổi phòng"));

            // Kiểm tra trạng thái hiện tại
            if (donDoiPhong.getTrangThai() != DangKyPhong.TrangThai.CHO_DUYET) {
                throw new RuntimeException("Đơn đổi phòng không ở trạng thái chờ duyệt");
            }

            SinhVien sinhVien = donDoiPhong.getSinhVien();
            Phong phongCu = sinhVien.getPhong();
            Phong phongMoi = donDoiPhong.getPhong();

            // Kiểm tra phòng mới còn chỗ không
            if (phongMoi.getSoNguoiHienTai() >= phongMoi.getSucChua()) {
                throw new RuntimeException("Phòng mới đã đầy");
            }

            // Kiểm tra giới tính phù hợp
            if ((sinhVien.getGioiTinh() == SinhVien.GioiTinh.NAM && phongMoi.getLoaiPhong() != Phong.LoaiPhong.NAM) ||
                (sinhVien.getGioiTinh() == SinhVien.GioiTinh.NU && phongMoi.getLoaiPhong() != Phong.LoaiPhong.NU)) {
                throw new RuntimeException("Loại phòng không phù hợp với giới tính của sinh viên");
            }

            // Cập nhật số người ở phòng cũ
            if (phongCu != null) {
                phongCu.setSoNguoiHienTai(phongCu.getSoNguoiHienTai() - 1);
                if (phongCu.getSoNguoiHienTai() < phongCu.getSucChua()) {
                    phongCu.setTrangThai(Phong.TrangThai.CON_TRONG);
                }
                phongRepository.save(phongCu);
            }

            // Cập nhật số người ở phòng mới
            phongMoi.setSoNguoiHienTai(phongMoi.getSoNguoiHienTai() + 1);
            if (phongMoi.getSoNguoiHienTai() >= phongMoi.getSucChua()) {
                phongMoi.setTrangThai(Phong.TrangThai.DA_DU);
            }
            phongRepository.save(phongMoi);

            // Cập nhật thông tin sinh viên
            sinhVien.setPhong(phongMoi);
            sinhVienRepository.save(sinhVien);

            // Cập nhật trạng thái đơn đổi phòng
            donDoiPhong.setTrangThai(DangKyPhong.TrangThai.DA_DUYET);
            donDoiPhong.setNgayDuyet(LocalDateTime.now());
            dangKyPhongRepository.save(donDoiPhong);

            // Tạo bản ghi phân bổ phòng mới
            PhanBoPhong phanBoPhong = new PhanBoPhong();
            phanBoPhong.setSinhVien(sinhVien);
            phanBoPhong.setPhong(phongMoi);
            phanBoPhong.setNgayNhanPhong(LocalDateTime.now());
            phanBoPhong.setTrangThai(PhanBoPhong.TrangThai.DANG_O);
            phanBoPhongRepository.save(phanBoPhong);

            // Cập nhật trạng thái phân bổ phòng cũ (nếu có)
            if (phongCu != null) {
                PhanBoPhong phanBoPhongCu = phanBoPhongRepository.findBySinhVienAndTrangThai(
                    sinhVien, PhanBoPhong.TrangThai.DANG_O)
                    .orElse(null);
                if (phanBoPhongCu != null) {
                    phanBoPhongCu.setNgayTraPhong(LocalDateTime.now());
                    phanBoPhongCu.setTrangThai(PhanBoPhong.TrangThai.DA_KET_THUC);
                    phanBoPhongRepository.save(phanBoPhongCu);
                }
            }

            redirectAttributes.addFlashAttribute("success", 
                "Đã duyệt đơn đổi phòng thành công cho sinh viên " + sinhVien.getHoTen());
            return "redirect:/admin/quan-ly/don-doi-phong";

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/admin/quan-ly/don-doi-phong";
        }
    }

    @PostMapping("/quan-ly/don-doi-phong/{id}/tu-choi")
    public String tuChoiDonDoiPhong(@PathVariable Long id, @RequestParam String lyDo, 
            RedirectAttributes redirectAttributes) {
        try {
            DangKyPhong donDoiPhong = dangKyPhongRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn đổi phòng"));

            if (donDoiPhong.getTrangThai() != DangKyPhong.TrangThai.CHO_DUYET) {
                throw new RuntimeException("Đơn đổi phòng không ở trạng thái chờ duyệt");
            }

            donDoiPhong.setTrangThai(DangKyPhong.TrangThai.TU_CHOI);
            donDoiPhong.setNguoiDuyet("Admin"); // Có thể thay bằng tên người dùng đang đăng nhập
            donDoiPhong.setNgayDuyet(LocalDateTime.now());
            donDoiPhong.setGhiChu(lyDo);
            dangKyPhongRepository.save(donDoiPhong);

            redirectAttributes.addFlashAttribute("success", 
                String.format("Đã từ chối đơn đổi phòng của sinh viên %s", donDoiPhong.getSinhVien().getHoTen()));

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi khi từ chối đơn: " + e.getMessage());
        }
        return "redirect:/admin/quan-ly/don-doi-phong";
    }
} 