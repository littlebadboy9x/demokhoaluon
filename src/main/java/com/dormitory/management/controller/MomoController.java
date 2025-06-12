package com.dormitory.management.controller;

import com.dormitory.management.model.HoaDon;
import com.dormitory.management.model.MomoTransaction;
import com.dormitory.management.model.ThanhToan;
import com.dormitory.management.service.HoaDonService;
import com.dormitory.management.service.MomoService;
import com.dormitory.management.service.ThanhToanService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Date;
import java.util.Map;

@Controller
public class MomoController {
    private static final Logger logger = LoggerFactory.getLogger(MomoController.class);

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
            logger.info("=== MoMo Return Callback ===");
            logger.info("Received params: {}", params);
            
            String orderId = params.get("orderId");
            String requestId = params.get("requestId");
            Integer resultCode = Integer.parseInt(params.get("resultCode"));
            String message = params.get("message");
            String transId = params.get("transId");

            logger.info("OrderId: {}, ResultCode: {}, Message: {}, TransId: {}", 
                       orderId, resultCode, message, transId);

            if (resultCode == 0) { // Thanh toán thành công
                // Lấy mã hóa đơn từ orderId 
                // Format: HD{maHoaDon}_{timestamp} -> lấy phần giữa HD và _
                String maHoaDon = extractMaHoaDonFromOrderId(orderId);
                logger.info("Extracted maHoaDon: {}", maHoaDon);

                // Cập nhật trạng thái hóa đơn
                HoaDon hoaDon = hoaDonService.findById(maHoaDon)
                        .orElseThrow(() -> new RuntimeException("Không tìm thấy hóa đơn với mã: " + maHoaDon));

                logger.info("Found hoaDon: {} with status: {}", hoaDon.getMaHoaDon(), hoaDon.getTrangThai());

                // Kiểm tra hóa đơn đã được thanh toán chưa
                if (hoaDon.getTrangThai() == HoaDon.TrangThai.DA_THANH_TOAN) {
                    logger.warn("HoaDon {} already paid, skipping update", maHoaDon);
                    redirectAttributes.addFlashAttribute("info", "Hóa đơn đã được thanh toán trước đó!");
                    return "redirect:/sinh-vien/hoa-don";
                }

                // Tạo thanh toán mới
                ThanhToan thanhToan = new ThanhToan();
                thanhToan.setHoaDon(hoaDon);
                thanhToan.setSoTien(hoaDon.getTongTien());
                thanhToan.setPhuongThuc(ThanhToan.PhuongThuc.MOMO);
                thanhToan.setNgayThanhToan(new Date());
                thanhToan.setMaGiaoDichMomo(transId);
                thanhToan.setGhiChu("Thanh toán qua MoMo - OrderId: " + orderId);
                
                logger.info("Creating ThanhToan record");
                thanhToanService.save(thanhToan);

                // Cập nhật hóa đơn
                hoaDon.setTrangThai(HoaDon.TrangThai.DA_THANH_TOAN);
                hoaDon.setNgayThanhToan(new Date());
                
                logger.info("Updating hoaDon status to DA_THANH_TOAN");
                hoaDonService.save(hoaDon);

                logger.info("Payment processing completed successfully");
                redirectAttributes.addFlashAttribute("success", "Thanh toán hóa đơn thành công!");
            } else {
                logger.error("Payment failed with resultCode: {} and message: {}", resultCode, message);
                redirectAttributes.addFlashAttribute("error", "Thanh toán thất bại: " + message);
            }
        } catch (Exception e) {
            logger.error("Error processing MoMo callback", e);
            redirectAttributes.addFlashAttribute("error", "Có lỗi xảy ra: " + e.getMessage());
        }

        return "redirect:/sinh-vien/hoa-don";
    }

    private String extractMaHoaDonFromOrderId(String orderId) {
        try {
            // Format: HD{maHoaDon}_{timestamp}
            if (orderId.startsWith("HD")) {
                int underscoreIndex = orderId.indexOf("_");
                if (underscoreIndex > 2) {
                    return orderId.substring(2, underscoreIndex);
                } else {
                    // Fallback: nếu không có underscore, lấy từ vị trí 2 đến hết
                    return orderId.substring(2);
                }
            }
            throw new IllegalArgumentException("Invalid orderId format: " + orderId);
        } catch (Exception e) {
            logger.error("Error extracting maHoaDon from orderId: {}", orderId, e);
            throw new RuntimeException("Không thể parse mã hóa đơn từ orderId: " + orderId);
        }
    }

    @PostMapping("/api/momo/notify")
    @ResponseBody
    public ResponseEntity<String> handleMomoNotify(@RequestBody Map<String, String> payload) {
        try {
            logger.info("=== MoMo IPN Callback ===");
            logger.info("Received payload: {}", payload);
            
            String orderId = payload.get("orderId");
            Integer resultCode = Integer.parseInt(payload.get("resultCode"));

            // TODO: Verify signature from MoMo

            if (resultCode == 0) { // Thanh toán thành công
                // Xử lý tương tự như return callback
                String maHoaDon = extractMaHoaDonFromOrderId(orderId);
                logger.info("IPN - Processing payment for hoaDon: {}", maHoaDon);
                
                // TODO: Implement duplicate payment processing logic here
            }

            return ResponseEntity.ok("OK");
        } catch (Exception e) {
            logger.error("Error processing MoMo IPN", e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
} 