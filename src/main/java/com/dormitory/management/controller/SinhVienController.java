package com.dormitory.management.controller;

import com.dormitory.management.dto.SinhVienDTO;
import com.dormitory.management.exception.ResourceNotFoundException;
import com.dormitory.management.model.*;
import com.dormitory.management.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.ControllerAdvice;

@Controller
@RequestMapping("/sinh-vien")
@ControllerAdvice(assignableTypes = SinhVienController.class)
public class SinhVienController {

    @Autowired
    private SinhVienService sinhVienService;
    
    @Autowired
    private PhongService phongService;

    @Autowired
    private DangKyPhongService dangKyPhongService;

    @Autowired
    private NguoiDungService nguoiDungService;

    @Autowired
    private HoaDonService hoaDonService;

    @Autowired
    private ThongBaoService thongBaoService;

    @Autowired
    private SuCoService suCoService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ThanhToanService thanhToanService;

    @Autowired
    private MomoService momoService;

    @ModelAttribute
    public void addCommonAttributes(Model model, Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            String tenDangNhap = authentication.getName();
            
            // Kiểm tra đã đăng ký thông tin sinh viên chưa
            boolean daDangKySinhVien = sinhVienService.existsByTenDangNhap(tenDangNhap);
            model.addAttribute("daDangKySinhVien", daDangKySinhVien);
            
            // Kiểm tra đã có phòng hoặc đăng ký phòng chưa
            if (daDangKySinhVien) {
                Optional<SinhVien> sinhVien = sinhVienService.findByTenDangNhap(tenDangNhap);
                if (sinhVien.isPresent()) {
                    boolean coPhong = sinhVien.get().getPhong() != null;
                    boolean daDangKyPhong = dangKyPhongService.existsBySinhVienAndTrangThaiIn(
                        sinhVien.get(),
                        DangKyPhong.TrangThai.CHO_DUYET,
                        DangKyPhong.TrangThai.DA_DUYET
                    );
                    model.addAttribute("coPhong", coPhong);
                    model.addAttribute("daDangKyPhong", daDangKyPhong);
                }
            }
        }
    }

    @ModelAttribute
    public void addAttributes(Model model, HttpServletRequest request) {
        String currentPath = request.getServletPath();
        model.addAttribute("currentPath", currentPath);
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

        // Kiểm tra sinh viên đã có phòng chưa
        if (sinhVien.getPhong() != null) {
            redirectAttributes.addFlashAttribute("error", "Bạn đã có phòng! Nếu muốn đổi phòng, vui lòng sử dụng chức năng đổi phòng.");
            return "redirect:/sinh-vien/thong-tin-phong";
        }

        // Kiểm tra đã đăng ký phòng chưa
        if (dangKyPhongService.existsBySinhVienAndTrangThaiIn(sinhVien, 
                DangKyPhong.TrangThai.CHO_DUYET, DangKyPhong.TrangThai.DA_DUYET)) {
            redirectAttributes.addFlashAttribute("error", "Bạn đã có đơn đăng ký phòng đang chờ duyệt!");
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

            // Kiểm tra sinh viên đã có phòng chưa
            if (sinhVien.getPhong() != null) {
                throw new RuntimeException("Bạn đã có phòng! Nếu muốn đổi phòng, vui lòng sử dụng chức năng đổi phòng.");
            }

            // Kiểm tra đã có đơn đăng ký nào đang chờ duyệt không
            if (dangKyPhongService.existsBySinhVienAndTrangThaiIn(sinhVien, 
                    DangKyPhong.TrangThai.CHO_DUYET, DangKyPhong.TrangThai.DA_DUYET)) {
                throw new RuntimeException("Bạn đã có đơn đăng ký phòng đang chờ duyệt!");
            }

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
                DangKyPhong.TrangThai.CHO_DUYET,
                DangKyPhong.TrangThai.DA_DUYET);

        // Kiểm tra trạng thái phòng
        boolean coPhong = sinhVien.getPhong() != null;
        boolean daDangKyPhong = dangKyPhong != null;

        model.addAttribute("sinhVien", sinhVien);
        model.addAttribute("dangKyPhong", dangKyPhong);
        model.addAttribute("coPhong", coPhong);
        model.addAttribute("daDangKyPhong", daDangKyPhong);

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
            sinhVien.setTrangThai(SinhVien.TrangThai.DANG_O);
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

    @GetMapping("/dang-ky-thong-tin")
    public String showDangKyThongTinForm(Model model, Authentication authentication) {
        // Kiểm tra nếu đã đăng ký thông tin rồi thì chuyển hướng
        if (sinhVienService.existsByTenDangNhap(authentication.getName())) {
            return "redirect:/sinh-vien/thong-tin";
        }

        SinhVien sinhVien = new SinhVien();
        sinhVien.setTenDangNhap(authentication.getName());
        model.addAttribute("sinhVien", sinhVien);
        return "sinh-vien/dang-ky-thong-tin";
    }

    @PostMapping("/dang-ky-thong-tin")
    public String dangKyThongTin(@ModelAttribute SinhVien sinhVien,
                                BindingResult result,
                                RedirectAttributes redirectAttributes,
                                Authentication authentication) {
        if (result.hasErrors()) {
            return "sinh-vien/dang-ky-thong-tin";
        }

        try {
            // Kiểm tra trùng lặp
            if (sinhVienService.existsByEmail(sinhVien.getEmail())) {
                redirectAttributes.addFlashAttribute("error", "Email đã được sử dụng!");
                return "redirect:/sinh-vien/dang-ky-thong-tin";
            }
            if (sinhVienService.existsByMaSv(sinhVien.getMaSv())) {
                redirectAttributes.addFlashAttribute("error", "Mã sinh viên đã tồn tại!");
                return "redirect:/sinh-vien/dang-ky-thong-tin";
            }

            // Đảm bảo tenDangNhap khớp với người dùng đang đăng nhập
            sinhVien.setTenDangNhap(authentication.getName());
            
            // Thiết lập trạng thái mặc định
            sinhVien.setTrangThai(SinhVien.TrangThai.DANG_O);

            // Lưu thông tin sinh viên
            sinhVienService.save(sinhVien);

            redirectAttributes.addFlashAttribute("success", "Đăng ký thông tin sinh viên thành công! Bạn có thể tiến hành đăng ký phòng.");
            return "redirect:/sinh-vien/dang-ky-phong";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Có lỗi xảy ra: " + e.getMessage());
            return "redirect:/sinh-vien/dang-ky-thong-tin";
        }
    }

    @GetMapping("/dashboard")
    public String showDashboard(Model model, Authentication authentication) {
        String tenDangNhap = authentication.getName();
        
        // Lấy thông tin sinh viên
        SinhVien sinhVien = sinhVienService.findByTenDangNhap(tenDangNhap)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy thông tin sinh viên"));
        model.addAttribute("sinhVien", sinhVien);
        
        // Lấy số hóa đơn chưa thanh toán
        long hoaDonChuaThanhToan = hoaDonService.countByTrangThai(HoaDon.TrangThai.CHUA_THANH_TOAN);
        model.addAttribute("hoaDonChuaThanhToan", hoaDonChuaThanhToan);
        
        // Lấy số thông báo mới
        Page<ThongBao> thongBaoPage = thongBaoService.findByPhongAndTrangThai(
            sinhVien.getPhong(), true, 
            PageRequest.of(0, 5, Sort.by(Sort.Direction.DESC, "ngayTao"))
        );
        model.addAttribute("thongBaoMoi", thongBaoPage.getTotalElements());
        model.addAttribute("thongBaoList", thongBaoPage.getContent());
        
        // Lấy số sự cố đang xử lý
        long suCoDangXuLy = suCoService.countByTrangThai(SuCo.TrangThai.CHO_XU_LY);
        model.addAttribute("suCoDangXuLy", suCoDangXuLy);
        
        // Lấy danh sách phòng trống
        List<Phong> phongTrongList = phongService.findPhongTrong();
        model.addAttribute("phongTrongList", phongTrongList);
        
        return "sinh-vien/dashboard";
    }

    @GetMapping("/thong-tin")
    public String showThongTin(Model model, Authentication authentication) {
        String tenDangNhap = authentication.getName();
        
        // Lấy thông tin sinh viên
        SinhVien sinhVien = sinhVienService.findByTenDangNhap(tenDangNhap)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy thông tin sinh viên"));
        
        model.addAttribute("sinhVien", sinhVien);
        return "sinh-vien/thong-tin";
    }

    @GetMapping("/danh-sach-phong")
    public String showDanhSachPhong(Model model, Authentication authentication) {
        String tenDangNhap = authentication.getName();
        
        // Lấy thông tin sinh viên
        SinhVien sinhVien = sinhVienService.findByTenDangNhap(tenDangNhap)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy thông tin sinh viên"));
        
        // Lấy danh sách phòng trống phù hợp với giới tính
        List<Phong> phongTrongList = phongService.findPhongTrong().stream()
                .filter(phong -> phong.getLoaiPhong() == (sinhVien.getGioiTinh() == SinhVien.GioiTinh.NAM ? 
                        Phong.LoaiPhong.NAM : Phong.LoaiPhong.NU))
                .collect(Collectors.toList());
        
        model.addAttribute("sinhVien", sinhVien);
        model.addAttribute("phongTrongList", phongTrongList);
        model.addAttribute("phongHienTai", sinhVien.getPhong());
        
        return "sinh-vien/danh-sach-phong";
    }

    @PostMapping("/doi-phong")
    public String doiPhong(@RequestParam("maPhongMoi") String maPhongMoi,
                          Authentication authentication,
                          RedirectAttributes redirectAttributes) {
        try {
            // Lấy thông tin sinh viên
            SinhVien sinhVien = sinhVienService.findByTenDangNhap(authentication.getName())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy thông tin sinh viên"));
            
            // Lấy thông tin phòng mới
            Phong phongMoi = phongService.findById(maPhongMoi)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy phòng"));
            
            // Kiểm tra phòng còn trống
            if (phongMoi.getSoNguoiHienTai() >= phongMoi.getSucChua()) {
                throw new RuntimeException("Phòng đã đầy!");
            }
            
            // Kiểm tra phù hợp giới tính
            boolean isPhongNam = phongMoi.getLoaiPhong() == Phong.LoaiPhong.NAM;
            boolean isSinhVienNam = sinhVien.getGioiTinh() == SinhVien.GioiTinh.NAM;
            if (isPhongNam != isSinhVienNam) {
                throw new RuntimeException("Phòng không phù hợp với giới tính của bạn!");
            }
            
            // Tạo đơn đăng ký đổi phòng
            DangKyPhong dangKyDoiPhong = new DangKyPhong();
            dangKyDoiPhong.setSinhVien(sinhVien);
            dangKyDoiPhong.setPhong(phongMoi);
            dangKyDoiPhong.setTrangThai(DangKyPhong.TrangThai.CHO_DUYET);
            dangKyDoiPhong.setNgayDangKy(LocalDateTime.now());
            dangKyDoiPhong.setGhiChu("Đơn xin đổi phòng từ " + sinhVien.getPhong().getTenPhong());
            
            dangKyPhongService.save(dangKyDoiPhong);
            
            redirectAttributes.addFlashAttribute("success", 
                "Đã gửi đơn xin đổi phòng thành công! Vui lòng chờ quản lý KTX xét duyệt.");
            
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Có lỗi xảy ra: " + e.getMessage());
        }
        
        return "redirect:/sinh-vien/danh-sach-phong";
    }

    @GetMapping("/hoa-don")
    public String showHoaDon(Model model, Authentication authentication,
                           @RequestParam(defaultValue = "0") int page,
                           @RequestParam(defaultValue = "10") int size) {
        String tenDangNhap = authentication.getName();
        
        // Lấy thông tin sinh viên
        SinhVien sinhVien = sinhVienService.findByTenDangNhap(tenDangNhap)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy thông tin sinh viên"));

        // Lấy danh sách hóa đơn của sinh viên, sắp xếp theo ngày tạo giảm dần
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "ngayTao"));
        Page<HoaDon> hoaDonPage = hoaDonService.findBySinhVien(sinhVien, pageRequest);
        
        // Tính tổng tiền chưa thanh toán
        double tongTienChuaThanhToan = hoaDonService.tinhTongTienChuaThanhToan(sinhVien);
        
        model.addAttribute("hoaDonPage", hoaDonPage);
        model.addAttribute("tongTienChuaThanhToan", tongTienChuaThanhToan);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", hoaDonPage.getTotalPages());
        
        return "sinh-vien/hoa-don";
    }

    @PostMapping("/hoa-don/{maHoaDon}/thanh-toan")
    public String thanhToanHoaDon(@PathVariable String maHoaDon,
                                 @RequestParam(required = false) String phuongThuc,
                                 Authentication authentication,
                                 RedirectAttributes redirectAttributes) {
        try {
            // Lấy thông tin sinh viên
            SinhVien sinhVien = sinhVienService.findByTenDangNhap(authentication.getName())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy thông tin sinh viên"));
            
            // Lấy thông tin hóa đơn
            HoaDon hoaDon = hoaDonService.findById(maHoaDon)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy hóa đơn"));
            
            // Kiểm tra hóa đơn có phải của sinh viên này không
            if (!hoaDon.getSinhVien().getMaSv().equals(sinhVien.getMaSv())) {
                throw new RuntimeException("Bạn không có quyền thanh toán hóa đơn này!");
            }
            
            // Kiểm tra trạng thái hóa đơn
            if (hoaDon.getTrangThai() == HoaDon.TrangThai.DA_THANH_TOAN) {
                throw new RuntimeException("Hóa đơn này đã được thanh toán!");
            }

            // Tạo đối tượng thanh toán
            ThanhToan thanhToan = new ThanhToan();
            thanhToan.setHoaDon(hoaDon);
            thanhToan.setSoTien(hoaDon.getTongTien());
            thanhToan.setNgayThanhToan(new Date());
            
            // Xử lý theo phương thức thanh toán
            if ("TIEN_MAT".equals(phuongThuc)) {
                thanhToan.setPhuongThuc(ThanhToan.PhuongThuc.TIEN_MAT);
                thanhToan = thanhToanService.save(thanhToan);
                
                // Cập nhật trạng thái hóa đơn
                hoaDon.setTrangThai(HoaDon.TrangThai.DA_THANH_TOAN);
                hoaDon.setNgayThanhToan(new Date());
                hoaDonService.save(hoaDon);
                
                redirectAttributes.addFlashAttribute("success", "Thanh toán hóa đơn thành công!");
            } else {
                throw new RuntimeException("Phương thức thanh toán không hợp lệ!");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Có lỗi xảy ra: " + e.getMessage());
        }
        
        return "redirect:/sinh-vien/hoa-don";
    }

    @GetMapping("/hoa-don/{maHoaDon}/thanh-toan-momo")
    public String thanhToanMomo(@PathVariable String maHoaDon,
                               Authentication authentication,
                               RedirectAttributes redirectAttributes) {
        try {
            // Lấy thông tin sinh viên
            SinhVien sinhVien = sinhVienService.findByTenDangNhap(authentication.getName())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy thông tin sinh viên"));
            
            // Lấy thông tin hóa đơn
            HoaDon hoaDon = hoaDonService.findById(maHoaDon)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy hóa đơn"));
            
            // Kiểm tra hóa đơn có phải của sinh viên này không
            if (!hoaDon.getSinhVien().getMaSv().equals(sinhVien.getMaSv())) {
                throw new RuntimeException("Bạn không có quyền thanh toán hóa đơn này!");
            }
            
            // Kiểm tra trạng thái hóa đơn
            if (hoaDon.getTrangThai() == HoaDon.TrangThai.DA_THANH_TOAN) {
                throw new RuntimeException("Hóa đơn này đã được thanh toán!");
            }

            // Tạo giao dịch MoMo
            String redirectUrl = momoService.createPayment(hoaDon);
            return "redirect:" + redirectUrl;
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Có lỗi xảy ra: " + e.getMessage());
            return "redirect:/sinh-vien/hoa-don";
        }
    }

    @GetMapping("/hoa-don/thanh-toan-tat-ca")
    public String thanhToanTatCa(Authentication authentication, RedirectAttributes redirectAttributes) {
        try {
            // Lấy thông tin sinh viên
            SinhVien sinhVien = sinhVienService.findByTenDangNhap(authentication.getName())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy thông tin sinh viên"));
            
            // Lấy danh sách hóa đơn chưa thanh toán
            List<HoaDon> danhSachHoaDon = hoaDonService.findByTrangThai(HoaDon.TrangThai.CHUA_THANH_TOAN)
                    .stream()
                    .filter(hoaDon -> hoaDon.getSinhVien().getMaSv().equals(sinhVien.getMaSv()))
                    .collect(Collectors.toList());
            
            if (danhSachHoaDon.isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "Không có hóa đơn nào cần thanh toán!");
                return "redirect:/sinh-vien/hoa-don";
            }

            // Tính tổng tiền cần thanh toán
            double tongTien = danhSachHoaDon.stream()
                    .mapToDouble(HoaDon::getTongTien)
                    .sum();

            // Tạo giao dịch MoMo cho tất cả hóa đơn
            String redirectUrl = momoService.createBulkPayment(danhSachHoaDon, tongTien);
            return "redirect:" + redirectUrl;
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Có lỗi xảy ra: " + e.getMessage());
            return "redirect:/sinh-vien/hoa-don";
        }
    }

    @GetMapping("/hoa-don/{maHoaDon}/thanh-toan-form")
    public String showThanhToanForm(@PathVariable String maHoaDon,
                                  Authentication authentication,
                                  Model model,
                                  RedirectAttributes redirectAttributes) {
        try {
            // Lấy thông tin sinh viên
            SinhVien sinhVien = sinhVienService.findByTenDangNhap(authentication.getName())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy thông tin sinh viên"));
            
            // Lấy thông tin hóa đơn
            HoaDon hoaDon = hoaDonService.findById(maHoaDon)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy hóa đơn"));
            
            // Kiểm tra hóa đơn có phải của sinh viên này không
            if (!hoaDon.getSinhVien().getMaSv().equals(sinhVien.getMaSv())) {
                throw new RuntimeException("Bạn không có quyền thanh toán hóa đơn này!");
            }
            
            // Kiểm tra trạng thái hóa đơn
            if (hoaDon.getTrangThai() == HoaDon.TrangThai.DA_THANH_TOAN) {
                throw new RuntimeException("Hóa đơn này đã được thanh toán!");
            }

            model.addAttribute("hoaDon", hoaDon);
            return "sinh-vien/thanh-toan";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Có lỗi xảy ra: " + e.getMessage());
            return "redirect:/sinh-vien/hoa-don";
        }
    }

    @GetMapping("/doi-mat-khau")
    public String showDoiMatKhauForm() {
        return "sinh-vien/doi-mat-khau";
    }

    @PostMapping("/doi-mat-khau")
    public String doiMatKhau(@RequestParam String matKhauCu,
                            @RequestParam String matKhauMoi,
                            @RequestParam String xacNhanMatKhau,
                            Authentication authentication,
                            RedirectAttributes redirectAttributes) {
        try {
            // Kiểm tra mật khẩu mới và xác nhận mật khẩu
            if (!matKhauMoi.equals(xacNhanMatKhau)) {
                redirectAttributes.addFlashAttribute("error", "Mật khẩu mới và xác nhận mật khẩu không khớp!");
                return "redirect:/sinh-vien/doi-mat-khau";
            }

            // Lấy thông tin người dùng
            NguoiDung nguoiDung = nguoiDungService.findByTenDangNhap(authentication.getName())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy thông tin người dùng"));

            // Kiểm tra mật khẩu cũ
            if (!passwordEncoder.matches(matKhauCu, nguoiDung.getMatKhau())) {
                redirectAttributes.addFlashAttribute("error", "Mật khẩu cũ không chính xác!");
                return "redirect:/sinh-vien/doi-mat-khau";
            }

            // Cập nhật mật khẩu mới
            nguoiDung.setMatKhau(passwordEncoder.encode(matKhauMoi));
            nguoiDungService.save(nguoiDung);

            redirectAttributes.addFlashAttribute("success", "Đổi mật khẩu thành công!");
            return "redirect:/sinh-vien/thong-tin";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Có lỗi xảy ra: " + e.getMessage());
            return "redirect:/sinh-vien/doi-mat-khau";
        }
    }

    @GetMapping("/cap-nhat-thong-tin")
    public String showCapNhatThongTinForm(Model model, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";
        }

        String tenDangNhap = authentication.getName();
        SinhVien sinhVien = sinhVienService.findByTenDangNhap(tenDangNhap)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy thông tin sinh viên"));

        model.addAttribute("sinhVien", sinhVien);
        return "sinh-vien/cap-nhat-thong-tin";
    }

    @PostMapping("/cap-nhat-thong-tin")
    public String capNhatThongTin(@ModelAttribute SinhVien sinhVienCapNhat,
                                 BindingResult result,
                                 RedirectAttributes redirectAttributes,
                                 Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";
        }

        try {
            String tenDangNhap = authentication.getName();
            SinhVien sinhVienHienTai = sinhVienService.findByTenDangNhap(tenDangNhap)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy thông tin sinh viên"));

            // Cập nhật thông tin có thể thay đổi
            sinhVienHienTai.setHoTen(sinhVienCapNhat.getHoTen());
            sinhVienHienTai.setEmail(sinhVienCapNhat.getEmail());
            sinhVienHienTai.setSoDienThoai(sinhVienCapNhat.getSoDienThoai());
            sinhVienHienTai.setNgaySinh(sinhVienCapNhat.getNgaySinh());
            sinhVienHienTai.setGioiTinh(sinhVienCapNhat.getGioiTinh());
            sinhVienHienTai.setCccd(sinhVienCapNhat.getCccd());
            sinhVienHienTai.setLop(sinhVienCapNhat.getLop());
            sinhVienHienTai.setNganh(sinhVienCapNhat.getNganh());
            sinhVienHienTai.setKhoa(sinhVienCapNhat.getKhoa());
            sinhVienHienTai.setDiaChi(sinhVienCapNhat.getDiaChi());

            sinhVienService.save(sinhVienHienTai);
            redirectAttributes.addFlashAttribute("success", "Cập nhật thông tin thành công!");
            return "redirect:/sinh-vien/thong-tin";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Có lỗi xảy ra: " + e.getMessage());
            return "redirect:/sinh-vien/cap-nhat-thong-tin";
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