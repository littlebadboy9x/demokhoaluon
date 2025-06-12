package com.dormitory.management.controller.admin;

import com.dormitory.management.model.HoaDon;
import com.dormitory.management.model.SinhVien;
import com.dormitory.management.service.HoaDonService;
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
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.http.ResponseEntity;

import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

@Controller
@RequestMapping("/admin/hoa-don")
public class AdminHoaDonController {
    private static final Logger logger = LoggerFactory.getLogger(AdminHoaDonController.class);

    @Autowired
    private HoaDonService hoaDonService;

    @Autowired
    private PhongService phongService;

    @Autowired
    private SinhVienService sinhVienService;

    @GetMapping
    public String listHoaDon(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String trangThai,
            @RequestParam(required = false) String thangNam,
            @RequestParam(required = false, defaultValue = "ngayTao") String sortBy,
            @RequestParam(required = false, defaultValue = "desc") String sortDir,
            Model model) {

        try {
            // Xử lý tháng năm nếu có
            Integer thang = null;
            Integer nam = null;
            if (thangNam != null && !thangNam.isEmpty()) {
                String[] parts = thangNam.split("-");
                if (parts.length == 2) {
                    nam = Integer.parseInt(parts[0]);
                    thang = Integer.parseInt(parts[1]);
                }
            }

            // Xử lý trạng thái
            HoaDon.TrangThai trangThaiEnum = null;
            if (trangThai != null && !trangThai.isEmpty()) {
                trangThaiEnum = HoaDon.TrangThai.valueOf(trangThai);
            }

            // Tìm kiếm và phân trang
            Page<HoaDon> hoaDonPage = hoaDonService.search(
                search, trangThaiEnum, thang, nam, 
                PageRequest.of(page, size, 
                    sortDir.equals("asc") ? Sort.Direction.ASC : Sort.Direction.DESC, 
                    sortBy)
            );

            model.addAttribute("hoaDonPage", hoaDonPage);
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", hoaDonPage.getTotalPages());
            model.addAttribute("totalItems", hoaDonPage.getTotalElements());
            
            // Thêm các tham số tìm kiếm vào model để giữ lại khi phân trang
            model.addAttribute("search", search);
            model.addAttribute("trangThai", trangThai);
            model.addAttribute("thangNam", thangNam);
            model.addAttribute("sortBy", sortBy);
            model.addAttribute("sortDir", sortDir);
            
            // Thêm reverse sort để đổi hướng sắp xếp
            model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");

            return "admin/hoa-don/list";
        } catch (Exception e) {
            logger.error("Error listing hoa don: ", e);
            model.addAttribute("error", "Có lỗi xảy ra khi tải danh sách hóa đơn: " + e.getMessage());
            return "admin/hoa-don/list";
        }
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        HoaDon hoaDon = new HoaDon();
        
        // Set giá trị mặc định
        Calendar cal = Calendar.getInstance();
        hoaDon.setThang(cal.get(Calendar.MONTH) + 1); // Tháng hiện tại
        hoaDon.setNam(cal.get(Calendar.YEAR)); // Năm hiện tại
        hoaDon.setNgayTao(new Date());
        hoaDon.setTrangThai(HoaDon.TrangThai.CHUA_THANH_TOAN);
        
        // Set giá trị 0 cho các khoản phí
        hoaDon.setTienPhong(0.0);
        hoaDon.setTienDien(0.0);
        hoaDon.setTienNuoc(0.0);
        hoaDon.setPhiDichVu(0.0);
        hoaDon.setTongTien(0.0);
        
        model.addAttribute("hoaDon", hoaDon);
        model.addAttribute("phongList", phongService.findAll());
        model.addAttribute("sinhVienList", sinhVienService.findAll());
        return "admin/hoa-don/form";
    }

    @PostMapping("/add")
    public String addHoaDon(@ModelAttribute HoaDon hoaDon, BindingResult result, RedirectAttributes redirectAttributes, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("phongList", phongService.findAll());
            model.addAttribute("sinhVienList", sinhVienService.findAll());
            return "admin/hoa-don/form";
        }

        try {
            // Kiểm tra sinh viên và phòng
            if (hoaDon.getSinhVien() == null || hoaDon.getPhong() == null) {
                model.addAttribute("error", "Vui lòng chọn sinh viên và phòng!");
                model.addAttribute("phongList", phongService.findAll());
                model.addAttribute("sinhVienList", sinhVienService.findAll());
                return "admin/hoa-don/form";
            }

            // Tạo mã hóa đơn mới
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
            String maHoaDon = "HD" + sdf.format(new Date());
            hoaDon.setMaHoaDon(maHoaDon);
            
            // Set các giá trị mặc định
            hoaDon.setNgayTao(new Date());
            hoaDon.setTrangThai(HoaDon.TrangThai.CHUA_THANH_TOAN);
            
            // Tính tổng tiền
            hoaDon.setTongTien(hoaDon.getTienPhong() + hoaDon.getTienDien() + 
                              hoaDon.getTienNuoc() + hoaDon.getPhiDichVu());
            
            // Set hạn thanh toán là cuối tháng
            Calendar calendar = Calendar.getInstance();
            calendar.set(hoaDon.getNam(), hoaDon.getThang() - 1, 1);
            calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
            calendar.set(Calendar.HOUR_OF_DAY, 23);
            calendar.set(Calendar.MINUTE, 59);
            calendar.set(Calendar.SECOND, 59);
            hoaDon.setHanThanhToan(calendar.getTime());
            
            logger.debug("Creating new hóa đơn: {}", hoaDon);
            HoaDon savedHoaDon = hoaDonService.save(hoaDon);
            logger.debug("Created hóa đơn: {}", savedHoaDon);
            
            redirectAttributes.addFlashAttribute("success", "Tạo hóa đơn mới thành công!");
            return "redirect:/admin/hoa-don";
        } catch (Exception e) {
            logger.error("Error creating hóa đơn: ", e);
            model.addAttribute("error", "Có lỗi xảy ra: " + e.getMessage());
            model.addAttribute("phongList", phongService.findAll());
            model.addAttribute("sinhVienList", sinhVienService.findAll());
            return "admin/hoa-don/form";
        }
    }

    @GetMapping("/edit/{maHoaDon}")
    public String showEditForm(@PathVariable String maHoaDon, Model model, RedirectAttributes redirectAttributes) {
        try {
            HoaDon hoaDon = hoaDonService.findById(maHoaDon)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy hóa đơn với mã: " + maHoaDon));
            logger.debug("Loading hóa đơn for edit: {}", hoaDon);
            model.addAttribute("hoaDon", hoaDon);
            model.addAttribute("phongList", phongService.findAll());
            model.addAttribute("sinhVienList", sinhVienService.findAll());
            return "admin/hoa-don/form";
        } catch (Exception e) {
            logger.error("Error loading hóa đơn for edit: ", e);
            redirectAttributes.addFlashAttribute("error", "Có lỗi xảy ra khi tải thông tin hóa đơn: " + e.getMessage());
            return "redirect:/admin/hoa-don";
        }
    }

    @PostMapping("/edit")
    public String editHoaDon(@ModelAttribute HoaDon hoaDon, BindingResult result, RedirectAttributes redirectAttributes, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("phongList", phongService.findAll());
            model.addAttribute("sinhVienList", sinhVienService.findAll());
            return "admin/hoa-don/form";
        }

        try {
            logger.debug("Updating hóa đơn with data: {}", hoaDon);
            
            // Lấy thông tin hóa đơn cũ
            HoaDon hoaDonCu = hoaDonService.findById(hoaDon.getMaHoaDon())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy hóa đơn với mã: " + hoaDon.getMaHoaDon()));
            logger.debug("Found existing hóa đơn: {}", hoaDonCu);
            
            // Giữ nguyên các thông tin quan trọng
            hoaDon.setNgayTao(hoaDonCu.getNgayTao());
            hoaDon.setNgayThanhToan(hoaDonCu.getNgayThanhToan());
            hoaDon.setTrangThai(hoaDonCu.getTrangThai());
            hoaDon.setHanThanhToan(hoaDonCu.getHanThanhToan());
            
            // Tính lại tổng tiền
            hoaDon.setTongTien(hoaDon.getTienPhong() + hoaDon.getTienDien() + 
                              hoaDon.getTienNuoc() + hoaDon.getPhiDichVu());
            
            HoaDon savedHoaDon = hoaDonService.save(hoaDon);
            logger.debug("Saved updated hóa đơn: {}", savedHoaDon);
            
            redirectAttributes.addFlashAttribute("success", "Cập nhật hóa đơn thành công!");
            return "redirect:/admin/hoa-don";
        } catch (Exception e) {
            logger.error("Error updating hóa đơn: ", e);
            model.addAttribute("error", "Có lỗi xảy ra: " + e.getMessage());
            model.addAttribute("phongList", phongService.findAll());
            model.addAttribute("sinhVienList", sinhVienService.findAll());
            return "admin/hoa-don/form";
        }
    }

    @GetMapping("/delete/{maHoaDon}")
    public String deleteHoaDon(@PathVariable String maHoaDon, RedirectAttributes redirectAttributes) {
        try {
            hoaDonService.delete(maHoaDon);
            redirectAttributes.addFlashAttribute("success", "Xóa hóa đơn thành công!");
        } catch (Exception e) {
            logger.error("Error deleting hóa đơn: ", e);
            redirectAttributes.addFlashAttribute("error", "Có lỗi xảy ra khi xóa hóa đơn: " + e.getMessage());
        }
        return "redirect:/admin/hoa-don";
    }

    @GetMapping("/thanh-toan/{maHoaDon}")
    public String thanhToanHoaDon(@PathVariable String maHoaDon, RedirectAttributes redirectAttributes) {
        try {
            HoaDon hoaDon = hoaDonService.findById(maHoaDon)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy hóa đơn với mã: " + maHoaDon));
            hoaDon.setTrangThai(HoaDon.TrangThai.DA_THANH_TOAN);
            hoaDon.setNgayThanhToan(new Date());
            hoaDonService.save(hoaDon);
            redirectAttributes.addFlashAttribute("success", "Thanh toán hóa đơn thành công!");
        } catch (Exception e) {
            logger.error("Error paying hóa đơn: ", e);
            redirectAttributes.addFlashAttribute("error", "Có lỗi xảy ra khi thanh toán hóa đơn: " + e.getMessage());
        }
        return "redirect:/admin/hoa-don";
    }

    @GetMapping("/qua-han/{maHoaDon}")
    public String quaHanHoaDon(@PathVariable String maHoaDon, RedirectAttributes redirectAttributes) {
        try {
            HoaDon hoaDon = hoaDonService.findById(maHoaDon)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy hóa đơn với mã: " + maHoaDon));
            hoaDon.setTrangThai(HoaDon.TrangThai.QUA_HAN);
            hoaDonService.save(hoaDon);
            redirectAttributes.addFlashAttribute("success", "Đã chuyển hóa đơn sang trạng thái quá hạn!");
        } catch (Exception e) {
            logger.error("Error marking hóa đơn as overdue: ", e);
            redirectAttributes.addFlashAttribute("error", "Có lỗi xảy ra khi chuyển trạng thái hóa đơn: " + e.getMessage());
        }
        return "redirect:/admin/hoa-don";
    }


} 