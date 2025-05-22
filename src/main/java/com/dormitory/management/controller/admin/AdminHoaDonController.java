package com.dormitory.management.controller.admin;

import com.dormitory.management.model.HoaDon;
import com.dormitory.management.service.HoaDonService;
import com.dormitory.management.service.PhongService;
import com.dormitory.management.service.SinhVienService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Date;
import java.text.SimpleDateFormat;

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
            Model model) {
        Page<HoaDon> hoaDonPage = hoaDonService.findAll(PageRequest.of(page, size));
        
        model.addAttribute("hoaDonPage", hoaDonPage);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", hoaDonPage.getTotalPages());
        model.addAttribute("totalItems", hoaDonPage.getTotalElements());
        
        return "admin/hoa-don/list";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        HoaDon hoaDon = new HoaDon();
        hoaDon.setNgayTao(new Date());
        
        model.addAttribute("hoaDon", hoaDon);
        model.addAttribute("phongList", phongService.findAll());
        model.addAttribute("sinhVienList", sinhVienService.findAll());
        return "admin/hoa-don/add";
    }

    @PostMapping("/create")
    public String createHoaDon(@ModelAttribute HoaDon hoaDon, BindingResult result, RedirectAttributes redirectAttributes, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("phongList", phongService.findAll());
            model.addAttribute("sinhVienList", sinhVienService.findAll());
            return "admin/hoa-don/add";
        }

        try {
            // Kiểm tra sinh viên và phòng
            if (hoaDon.getSinhVien() == null || hoaDon.getPhong() == null) {
                model.addAttribute("error", "Vui lòng chọn sinh viên và phòng!");
                model.addAttribute("phongList", phongService.findAll());
                model.addAttribute("sinhVienList", sinhVienService.findAll());
                return "admin/hoa-don/add";
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
            return "admin/hoa-don/add";
        }
    }

    @GetMapping("/edit/{maHoaDon}")
    public String showEditForm(@PathVariable String maHoaDon, Model model) {
        HoaDon hoaDon = hoaDonService.findById(maHoaDon)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy hóa đơn với mã: " + maHoaDon));
        logger.debug("Loading hóa đơn for edit: {}", hoaDon);
        model.addAttribute("hoaDon", hoaDon);
        model.addAttribute("phongList", phongService.findAll());
        model.addAttribute("sinhVienList", sinhVienService.findAll());
        return "admin/hoa-don/edit";
    }

    @PostMapping("/update")
    public String updateHoaDon(@ModelAttribute HoaDon hoaDon, BindingResult result, RedirectAttributes redirectAttributes, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("phongList", phongService.findAll());
            model.addAttribute("sinhVienList", sinhVienService.findAll());
            return "admin/hoa-don/edit";
        }

        try {
            logger.debug("Updating hóa đơn with data: {}", hoaDon);
            
            // Lấy thông tin hóa đơn cũ
            HoaDon hoaDonCu = hoaDonService.findById(hoaDon.getMaHoaDon())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy hóa đơn với mã: " + hoaDon.getMaHoaDon()));
            logger.debug("Found existing hóa đơn: {}", hoaDonCu);
            
            // Chỉ set ngày thanh toán nếu chuyển từ trạng thái chưa thanh toán sang đã thanh toán
            if (hoaDon.getTrangThai() == HoaDon.TrangThai.DA_THANH_TOAN 
                && hoaDonCu.getTrangThai() != HoaDon.TrangThai.DA_THANH_TOAN) {
                hoaDon.setNgayThanhToan(new Date());
                logger.debug("Setting ngày thanh toán to current date");
            } else if (hoaDon.getTrangThai() != HoaDon.TrangThai.DA_THANH_TOAN) {
                // Nếu không phải trạng thái đã thanh toán, xóa ngày thanh toán
                hoaDon.setNgayThanhToan(null);
                logger.debug("Clearing ngày thanh toán");
            } else {
                // Giữ nguyên ngày thanh toán nếu đã thanh toán từ trước
                hoaDon.setNgayThanhToan(hoaDonCu.getNgayThanhToan());
                logger.debug("Keeping existing ngày thanh toán: {}", hoaDonCu.getNgayThanhToan());
            }

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
            return "admin/hoa-don/edit";
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