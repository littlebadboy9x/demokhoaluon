package com.dormitory.management.controller;

import com.dormitory.management.model.HoaDon;
import com.dormitory.management.model.MomoTransaction;
import com.dormitory.management.model.ThanhToan;
import com.dormitory.management.service.HoaDonService;
import com.dormitory.management.service.MomoService;
import com.dormitory.management.service.ThanhToanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Date;
import java.util.Map;

@Controller
public class MomoController {

    @Autowired
    private MomoService momoService;

    @Autowired
    private HoaDonService hoaDonService;

    @Autowired
    private ThanhToanService thanhToanService;

    @GetMapping("/sinh-vien/hoa-don/momo-return")
    public String handleMomoReturn(@RequestParam Map<String, String> params,
                                 RedirectAttributes redirectAttributes) {
        try {
            String orderId = params.get("orderId");
            String requestId = params.get("requestId");
            Integer resultCode = Integer.parseInt(params.get("resultCode"));
            String message = params.get("message");

            // TODO: Verify signature from MoMo

            if (resultCode == 0) { // Thanh toán thành công
                // Lấy mã hóa đơn từ orderId (bỏ prefix "HD")
                String maHoaDon = orderId.substring(2);

                // Cập nhật trạng thái hóa đơn
                HoaDon hoaDon = hoaDonService.findById(maHoaDon)
                        .orElseThrow(() -> new RuntimeException("Không tìm thấy hóa đơn"));

                // Tạo thanh toán mới
                ThanhToan thanhToan = new ThanhToan();
                thanhToan.setHoaDon(hoaDon);
                thanhToan.setSoTien(hoaDon.getTongTien());
                thanhToan.setPhuongThuc(ThanhToan.PhuongThuc.MOMO);
                thanhToan.setNgayThanhToan(new Date());
                thanhToan.setMaGiaoDichMomo(params.get("transId"));
                thanhToanService.save(thanhToan);

                // Cập nhật hóa đơn
                hoaDon.setTrangThai(HoaDon.TrangThai.DA_THANH_TOAN);
                hoaDon.setNgayThanhToan(new Date());
                hoaDonService.save(hoaDon);

                redirectAttributes.addFlashAttribute("success", "Thanh toán hóa đơn thành công!");
            } else {
                redirectAttributes.addFlashAttribute("error", "Thanh toán thất bại: " + message);
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Có lỗi xảy ra: " + e.getMessage());
        }

        return "redirect:/sinh-vien/hoa-don";
    }

    @PostMapping("/api/momo/notify")
    @ResponseBody
    public ResponseEntity<String> handleMomoNotify(@RequestBody Map<String, String> payload) {
        try {
            String orderId = payload.get("orderId");
            Integer resultCode = Integer.parseInt(payload.get("resultCode"));

            // TODO: Verify signature from MoMo

            if (resultCode == 0) { // Thanh toán thành công
                // Cập nhật trạng thái giao dịch MoMo
                // TODO: Implement this
            }

            return ResponseEntity.ok("OK");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
} 