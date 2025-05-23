package com.dormitory.management.controller.admin;

import com.dormitory.management.model.DangKyPhong;
import com.dormitory.management.model.PhanBoPhong;
import com.dormitory.management.model.Phong;
import com.dormitory.management.model.SinhVien;
import com.dormitory.management.service.DangKyPhongService;
import com.dormitory.management.service.PhanBoPhongService;
import com.dormitory.management.service.PhongService;
import com.dormitory.management.service.SinhVienService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Date;
import java.util.Optional;

@Controller
@RequestMapping("/admin/dang-ky")
public class AdminDangKyController {
    private static final Logger logger = LoggerFactory.getLogger(AdminDangKyController.class);

    @Autowired
    private DangKyPhongService dangKyPhongService;

    @Autowired
    private SinhVienService sinhVienService;

    @Autowired
    private PhongService phongService;

    @Autowired
    private PhanBoPhongService phanBoPhongService;

    @GetMapping
    public String listDangKy(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String trangThai,
            @RequestParam(required = false) String phong,
            @RequestParam(required = false, defaultValue = "ngayDangKy") String sortBy,
            @RequestParam(required = false, defaultValue = "desc") String sortDir,
            Model model) {

        try {
            // Xử lý trạng thái
            DangKyPhong.TrangThai trangThaiEnum = null;
            if (trangThai != null && !trangThai.isEmpty()) {
                trangThaiEnum = DangKyPhong.TrangThai.valueOf(trangThai);
            }

            // Tìm kiếm và phân trang
            Page<DangKyPhong> dangKyPage = dangKyPhongService.search(
                search, trangThaiEnum, phong,
                PageRequest.of(page, size, 
                    sortDir.equals("asc") ? Sort.Direction.ASC : Sort.Direction.DESC, 
                    sortBy)
            );

            model.addAttribute("dangKyPage", dangKyPage);
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", dangKyPage.getTotalPages());
            model.addAttribute("totalItems", dangKyPage.getTotalElements());
            
            // Thêm các tham số tìm kiếm vào model để giữ lại khi phân trang
            model.addAttribute("search", search);
            model.addAttribute("trangThai", trangThai);
            model.addAttribute("phong", phong);
            model.addAttribute("sortBy", sortBy);
            model.addAttribute("sortDir", sortDir);
            
            // Thêm reverse sort để đổi hướng sắp xếp
            model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");

            return "admin/dang-ky/list";
        } catch (Exception e) {
            logger.error("Error listing dang ky: ", e);
            model.addAttribute("error", "Có lỗi xảy ra khi tải danh sách đăng ký: " + e.getMessage());
            return "admin/dang-ky/list";
        }
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("dangKyPhong", new DangKyPhong());
        model.addAttribute("sinhVienList", sinhVienService.findAll());
        model.addAttribute("phongList", phongService.findAll());
        return "admin/dang-ky/form";
    }

    @GetMapping("/edit/{idDangKy}")
    public String showEditForm(@PathVariable Long idDangKy, Model model) {
        DangKyPhong dangKyPhong = dangKyPhongService.findById(idDangKy)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đăng ký với ID: " + idDangKy));
        model.addAttribute("dangKyPhong", dangKyPhong);
        model.addAttribute("sinhVienList", sinhVienService.findAll());
        model.addAttribute("phongList", phongService.findAll());
        return "admin/dang-ky/form";
    }

    @PostMapping("/save")
    public String saveDangKy(@ModelAttribute DangKyPhong dangKyPhong) {
        dangKyPhongService.save(dangKyPhong);
        return "redirect:/admin/dang-ky";
    }

    @GetMapping("/delete/{idDangKy}")
    public String deleteDangKy(@PathVariable Long idDangKy) {
        dangKyPhongService.delete(idDangKy);
        return "redirect:/admin/dang-ky";
    }

    @GetMapping("/duyet/{idDangKy}")
    public String duyetDangKy(@PathVariable Long idDangKy, RedirectAttributes redirectAttributes) {
        try {
            DangKyPhong dangKyPhong = dangKyPhongService.findById(idDangKy)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy đăng ký với ID: " + idDangKy));
            
            // Kiểm tra trạng thái hiện tại
            if (dangKyPhong.getTrangThai() != DangKyPhong.TrangThai.CHO_DUYET) {
                redirectAttributes.addFlashAttribute("error", "Đăng ký này đã được xử lý trước đó!");
                return "redirect:/admin/dang-ky";
            }
            
            // Kiểm tra phòng còn chỗ không
            Phong phong = dangKyPhong.getPhong();
            if (phong.getSoNguoiHienTai() >= phong.getSucChua()) {
                redirectAttributes.addFlashAttribute("error", "Phòng " + phong.getTenPhong() + " đã đủ người!");
                return "redirect:/admin/dang-ky";
            }

            // Kiểm tra sinh viên đã có phòng chưa
            SinhVien sinhVien = dangKyPhong.getSinhVien();
            Optional<PhanBoPhong> phanBoHienTai = phanBoPhongService.findBySinhVienAndTrangThai(
                sinhVien, PhanBoPhong.TrangThai.DANG_O);
            if (phanBoHienTai.isPresent()) {
                redirectAttributes.addFlashAttribute("error", 
                    "Sinh viên " + sinhVien.getHoTen() + " đang ở phòng " + phanBoHienTai.get().getPhong().getTenPhong());
                return "redirect:/admin/dang-ky";
            }
            
            // Cập nhật thông tin đăng ký
            dangKyPhong.setTrangThai(DangKyPhong.TrangThai.DA_DUYET);
            dangKyPhong.setNgayDuyet(new Date());
            dangKyPhongService.save(dangKyPhong);
            
            // Tạo phân bổ phòng mới
            PhanBoPhong phanBoPhong = new PhanBoPhong();
            phanBoPhong.setSinhVien(sinhVien);
            phanBoPhong.setPhong(phong);
            phanBoPhong.setNgayNhanPhong(new Date());
            phanBoPhong.setTrangThai(PhanBoPhong.TrangThai.DANG_O);
            phanBoPhongService.save(phanBoPhong);

            // Cập nhật phòng cho sinh viên
            sinhVien.setPhong(phong);
            sinhVienService.save(sinhVien);
            
            // Cập nhật số người trong phòng
            phong.setSoNguoiHienTai(phong.getSoNguoiHienTai() + 1);
            if (phong.getSoNguoiHienTai() >= phong.getSucChua()) {
                phong.setTrangThai(Phong.TrangThai.DA_DU);
            }
            phongService.save(phong);
            
            redirectAttributes.addFlashAttribute("success", "Đã duyệt đăng ký phòng thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi khi duyệt đăng ký: " + e.getMessage());
        }
        return "redirect:/admin/dang-ky";
    }

    @GetMapping("/tu-choi/{idDangKy}")
    public String tuChoiDangKy(@PathVariable Long idDangKy, RedirectAttributes redirectAttributes) {
        try {
            DangKyPhong dangKyPhong = dangKyPhongService.findById(idDangKy)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy đăng ký với ID: " + idDangKy));
            
            // Kiểm tra trạng thái hiện tại
            if (dangKyPhong.getTrangThai() != DangKyPhong.TrangThai.CHO_DUYET) {
                redirectAttributes.addFlashAttribute("error", "Đăng ký này đã được xử lý trước đó!");
                return "redirect:/admin/dang-ky";
            }
            
            // Cập nhật thông tin đăng ký
            dangKyPhong.setTrangThai(DangKyPhong.TrangThai.TU_CHOI);
            dangKyPhong.setNgayDuyet(new Date());
            dangKyPhongService.save(dangKyPhong);
            
            redirectAttributes.addFlashAttribute("success", "Đã từ chối đăng ký phòng!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi khi từ chối đăng ký: " + e.getMessage());
        }
        return "redirect:/admin/dang-ky";
    }
} 