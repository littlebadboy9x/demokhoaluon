package com.dormitory.management.controller;

import com.dormitory.management.dto.SinhVienDTO;
import com.dormitory.management.exception.ResourceNotFoundException;
import com.dormitory.management.model.DangKyPhong;
import com.dormitory.management.model.Phong;
import com.dormitory.management.model.SinhVien;
import com.dormitory.management.model.NguoiDung;
import com.dormitory.management.service.DangKyPhongService;
import com.dormitory.management.service.PhongService;
import com.dormitory.management.service.SinhVienService;
import com.dormitory.management.service.NguoiDungService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/sinh-vien")
public class SinhVienController {

    @Autowired
    private SinhVienService sinhVienService;
    
    @Autowired
    private PhongService phongService;

    @Autowired
    private DangKyPhongService dangKyPhongService;

    @Autowired
    private NguoiDungService nguoiDungService;

    @ModelAttribute
    public void addCommonAttributes(Model model, Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            String tenDangNhap = authentication.getName();
            
            // Kiểm tra đã đăng ký thông tin sinh viên chưa
            boolean daDangKySinhVien = sinhVienService.existsByTenDangNhap(tenDangNhap);
            model.addAttribute("daDangKySinhVien", daDangKySinhVien);
            
            // Kiểm tra đã đăng ký phòng chưa
            if (daDangKySinhVien) {
                Optional<SinhVien> sinhVien = sinhVienService.findByTenDangNhap(tenDangNhap);
                if (sinhVien.isPresent()) {
                    boolean daDangKyPhong = dangKyPhongService.existsBySinhVienAndTrangThaiIn(
                        sinhVien.get(),
                        DangKyPhong.TrangThai.CHO_DUYET,
                        DangKyPhong.TrangThai.DA_DUYET
                    );
                    model.addAttribute("daDangKyPhong", daDangKyPhong);
                }
            }
        }
    }

    @GetMapping
    public List<SinhVienDTO> getAllSinhVien() {
        return sinhVienService.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/{maSv}")
    public ResponseEntity<Map<String, Object>> getSinhVien(@PathVariable String maSv) {
        return sinhVienService.findById(maSv)
                .map(sinhVien -> {
                    Map<String, Object> response = new HashMap<>();
                    response.put("maSv", sinhVien.getMaSv());
                    response.put("hoTen", sinhVien.getHoTen());
                    
                    if (sinhVien.getPhong() != null) {
                        Phong phong = sinhVien.getPhong();
                        Map<String, Object> phongInfo = new HashMap<>();
                        phongInfo.put("maPhong", phong.getMaPhong());
                        phongInfo.put("tenPhong", phong.getTenPhong());
                        phongInfo.put("giaPhong", phong.getGiaPhong());
                        phongInfo.put("phiDichVu", 100000.0); // Giả định phí dịch vụ cố định
                        response.put("phong", phongInfo);
                    }
                    
                    return ResponseEntity.ok(response);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public SinhVienDTO createSinhVien(@RequestBody SinhVienDTO sinhVienDTO) {
        SinhVien sinhVien = convertToEntity(sinhVienDTO);
        return convertToDTO(sinhVienService.save(sinhVien));
    }

    @DeleteMapping("/{maSv}")
    public void deleteSinhVien(@PathVariable String maSv) {
        if (!sinhVienService.findById(maSv).isPresent()) {
            throw new ResourceNotFoundException("Sinh viên không tồn tại với mã: " + maSv);
        }
        sinhVienService.delete(maSv);
    }

    @GetMapping("/dang-ky-phong")
    public String showDangKyPhongForm(Authentication authentication, Model model, RedirectAttributes redirectAttributes) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";
        }

        String tenDangNhap = authentication.getName();
        
        // Kiểm tra đã đăng ký thông tin sinh viên chưa
        if (!sinhVienService.existsByTenDangNhap(tenDangNhap)) {
            redirectAttributes.addFlashAttribute("error", "Vui lòng đăng ký thông tin sinh viên trước!");
            return "redirect:/register/sinh-vien";
        }

        // Lấy thông tin sinh viên
        SinhVien sinhVien = sinhVienService.findByTenDangNhap(tenDangNhap)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy thông tin sinh viên"));

        // Kiểm tra đã đăng ký phòng chưa
        if (dangKyPhongService.existsBySinhVienAndTrangThaiIn(sinhVien, 
                DangKyPhong.TrangThai.CHO_DUYET, DangKyPhong.TrangThai.DA_DUYET)) {
            redirectAttributes.addFlashAttribute("error", "Bạn đã đăng ký phòng!");
            return "redirect:/sinh-vien/thong-tin-phong";
        }

        // Khởi tạo đối tượng đăng ký phòng mới
        DangKyPhong dangKyPhong = new DangKyPhong();
        dangKyPhong.setSinhVien(sinhVien);

        model.addAttribute("dangKyPhong", dangKyPhong);
        model.addAttribute("phongList", phongService.findPhongTrong());
        
        return "sinh-vien/dang-ky-phong";
    }

    @PostMapping("/dang-ky-phong")
    public String dangKyPhong(@ModelAttribute DangKyPhong dangKyPhong,
                             Authentication authentication,
                             RedirectAttributes redirectAttributes) {
        try {
            // Lấy thông tin sinh viên đang đăng nhập
            SinhVien sinhVien = sinhVienService.findByTenDangNhap(authentication.getName())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy thông tin sinh viên"));

            // Thiết lập thông tin đăng ký
            dangKyPhong.setSinhVien(sinhVien);
            dangKyPhong.setTrangThai(DangKyPhong.TrangThai.CHO_DUYET);
            dangKyPhong.setNgayDangKy(java.time.LocalDateTime.now());

            // Lưu đăng ký
            dangKyPhongService.save(dangKyPhong);

            redirectAttributes.addFlashAttribute("success", 
                "Đăng ký phòng thành công! Vui lòng chờ quản lý KTX xét duyệt.");
            return "redirect:/sinh-vien/thong-tin-phong";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Có lỗi xảy ra: " + e.getMessage());
            return "redirect:/sinh-vien/dang-ky-phong";
        }
    }

    @GetMapping("/thong-tin-phong")
    public String showThongTinPhong(Model model, Authentication authentication) {
        // Lấy thông tin sinh viên đang đăng nhập
        SinhVien sinhVien = sinhVienService.findByTenDangNhap(authentication.getName())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy thông tin sinh viên"));

        // Lấy thông tin đăng ký phòng
        DangKyPhong dangKyPhong = dangKyPhongService.findBySinhVienAndTrangThaiIn(sinhVien,
                DangKyPhong.TrangThai.CHO_DUYET, DangKyPhong.TrangThai.DA_DUYET);

        model.addAttribute("dangKyPhong", dangKyPhong);
        return "sinh-vien/thong-tin-phong";
    }

    @GetMapping("/register/sinh-vien")
    public String showRegisterSinhVienForm(Authentication authentication, Model model, RedirectAttributes redirectAttributes) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";
        }

        String tenDangNhap = authentication.getName();
        
        // Kiểm tra nếu đã đăng ký thông tin sinh viên
        if (sinhVienService.existsByTenDangNhap(tenDangNhap)) {
            redirectAttributes.addFlashAttribute("error", "Bạn đã đăng ký thông tin sinh viên!");
            return "redirect:/sinh-vien/thong-tin";
        }

        SinhVien sinhVien = new SinhVien();
        sinhVien.setTenDangNhap(tenDangNhap);
        model.addAttribute("sinhVien", sinhVien);
        return "auth/register-sinh-vien";
    }

    @PostMapping("/register/sinh-vien")
    public String registerSinhVien(@ModelAttribute SinhVien sinhVien,
                                 BindingResult result,
                                 RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "auth/register-sinh-vien";
        }

        try {
            // Kiểm tra trùng lặp
            if (sinhVienService.existsByEmail(sinhVien.getEmail())) {
                redirectAttributes.addFlashAttribute("error", "Email đã được sử dụng!");
                return "redirect:/register/sinh-vien";
            }
            if (sinhVienService.existsByMaSv(sinhVien.getMaSv())) {
                redirectAttributes.addFlashAttribute("error", "Mã sinh viên đã tồn tại!");
                return "redirect:/register/sinh-vien";
            }

            // Thiết lập thông tin mặc định
            sinhVien.setVaiTro("SINH_VIEN");
            sinhVien.setTrangThai(SinhVien.TrangThai.HOAT_DONG);
            sinhVien.setNgayDangKy(LocalDateTime.now());

            // Lưu sinh viên
            sinhVienService.save(sinhVien);

            redirectAttributes.addFlashAttribute("success", "Đăng ký thông tin sinh viên thành công! Bạn có thể đăng ký phòng ngay bây giờ.");
            return "redirect:/phong/dang-ky";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Có lỗi xảy ra: " + e.getMessage());
            return "redirect:/register/sinh-vien";
        }
    }

    private SinhVienDTO convertToDTO(SinhVien sinhVien) {
        SinhVienDTO dto = new SinhVienDTO();
        dto.setMaSv(sinhVien.getMaSv());
        dto.setTenDangNhap(sinhVien.getTenDangNhap());
        dto.setHoTen(sinhVien.getHoTen());
        dto.setNgaySinh(sinhVien.getNgaySinh());
        dto.setCccd(sinhVien.getCccd());
        dto.setSoDienThoai(sinhVien.getSoDienThoai());
        dto.setLop(sinhVien.getLop());
        dto.setNganh(sinhVien.getNganh());
        dto.setKhoa(sinhVien.getKhoa());
        dto.setDiaChi(sinhVien.getDiaChi());
        dto.setNgayDangKy(sinhVien.getNgayDangKy());
        return dto;
    }

    private SinhVien convertToEntity(SinhVienDTO dto) {
        SinhVien sinhVien = new SinhVien();
        sinhVien.setMaSv(dto.getMaSv());
        sinhVien.setTenDangNhap(dto.getTenDangNhap());
        sinhVien.setHoTen(dto.getHoTen());
        sinhVien.setNgaySinh(dto.getNgaySinh());
        sinhVien.setCccd(dto.getCccd());
        sinhVien.setSoDienThoai(dto.getSoDienThoai());
        sinhVien.setLop(dto.getLop());
        sinhVien.setNganh(dto.getNganh());
        sinhVien.setKhoa(dto.getKhoa());
        sinhVien.setDiaChi(dto.getDiaChi());
        sinhVien.setNgayDangKy(dto.getNgayDangKy());
        return sinhVien;
    }
}