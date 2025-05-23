package com.dormitory.management.service;

import com.dormitory.management.model.HoaDon;
import com.dormitory.management.model.MomoTransaction;
import com.dormitory.management.repository.MomoTransactionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
public class MomoService {
    private static final Logger logger = LoggerFactory.getLogger(MomoService.class);

    @Value("${momo.partner-code}")
    private String partnerCode;

    @Value("${momo.access-key}")
    private String accessKey;

    @Value("${momo.secret-key}")
    private String secretKey;

    @Value("${momo.api-endpoint}")
    private String apiEndpoint;

    @Value("${momo.return-url}")
    private String returnUrl;

    @Value("${momo.notify-url}")
    private String notifyUrl;

    @Autowired
    private MomoTransactionRepository momoTransactionRepository;

    @Autowired
    private RestTemplate restTemplate;

    public String createPayment(HoaDon hoaDon) {
        try {
            String orderId = "HD" + hoaDon.getMaHoaDon();
            String requestId = UUID.randomUUID().toString();
            
            // Tạo giao dịch MoMo mới
            MomoTransaction transaction = new MomoTransaction();
            transaction.setPartnerCode(partnerCode);
            transaction.setOrderId(orderId);
            transaction.setRequestId(requestId);
            transaction.setAmount(hoaDon.getTongTien());
            transaction.setOrderInfo("Thanh toán hóa đơn KTX - " + hoaDon.getMaHoaDon());
            transaction.setOrderType("billpayment");
            transaction.setRedirectUrl(returnUrl);
            transaction.setIpnUrl(notifyUrl);
            transaction.setRequestType("captureMoMoWallet");
            transaction.setExtraData("");
            transaction.setCreatedAt(new Date());
            
            // Tạo payload cho API MoMo
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("partnerCode", partnerCode);
            requestBody.put("accessKey", accessKey);
            requestBody.put("requestId", requestId);
            requestBody.put("amount", hoaDon.getTongTien().longValue());
            requestBody.put("orderId", orderId);
            requestBody.put("orderInfo", transaction.getOrderInfo());
            requestBody.put("returnUrl", transaction.getRedirectUrl());
            requestBody.put("notifyUrl", transaction.getIpnUrl());
            requestBody.put("requestType", transaction.getRequestType());
            requestBody.put("extraData", "");
            
            // Tạo chữ ký
            String rawSignature = "partnerCode=" + partnerCode +
                    "&accessKey=" + accessKey +
                    "&requestId=" + requestId +
                    "&amount=" + hoaDon.getTongTien().longValue() +
                    "&orderId=" + orderId +
                    "&orderInfo=" + transaction.getOrderInfo() +
                    "&returnUrl=" + transaction.getRedirectUrl() +
                    "&notifyUrl=" + transaction.getIpnUrl() +
                    "&extraData=";

            String signature = generateSignature(rawSignature, secretKey);
            requestBody.put("signature", signature);
            transaction.setSignature(signature);
            
            // Log request
            logger.debug("MoMo request payload: {}", requestBody);
            
            // Gọi API MoMo
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);
            
            logger.debug("Calling MoMo API: {}", apiEndpoint);
            
            Map<String, Object> response = restTemplate.postForObject(
                apiEndpoint, request, Map.class);
            
            logger.debug("MoMo API response: {}", response);
            
            if (response != null && response.get("payUrl") != null) {
                String payUrl = response.get("payUrl").toString();
                if (response.get("errorCode") != null) {
                    transaction.setResultCode(Integer.parseInt(response.get("errorCode").toString()));
                }
                if (response.get("message") != null) {
                    transaction.setMessage(response.get("message").toString());
                }
                
                // Lưu thông tin giao dịch
                momoTransactionRepository.save(transaction);
                
                return payUrl;
            } else {
                throw new RuntimeException("Không nhận được payUrl từ MoMo");
            }
        } catch (Exception e) {
            logger.error("Lỗi khi tạo giao dịch MoMo", e);
            throw new RuntimeException("Không thể tạo giao dịch MoMo: " + e.getMessage());
        }
    }

    public String createBulkPayment(List<HoaDon> danhSachHoaDon, double tongTien) {
        try {
            String orderId = UUID.randomUUID().toString();
            String requestId = UUID.randomUUID().toString();
            
            // Tạo giao dịch MoMo mới
            MomoTransaction transaction = new MomoTransaction();
            transaction.setPartnerCode(partnerCode);
            transaction.setOrderId(orderId);
            transaction.setRequestId(requestId);
            transaction.setAmount(tongTien);
            transaction.setOrderInfo("Thanh toán nhiều hóa đơn KTX");
            transaction.setOrderType("billpayment");
            transaction.setRedirectUrl(returnUrl);
            transaction.setIpnUrl(notifyUrl);
            transaction.setRequestType("captureMoMoWallet");
            transaction.setExtraData("");
            transaction.setCreatedAt(new Date());
            
            // Tạo payload cho API MoMo
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("partnerCode", partnerCode);
            requestBody.put("accessKey", accessKey);
            requestBody.put("requestId", requestId);
            requestBody.put("amount", (long) tongTien);
            requestBody.put("orderId", orderId);
            requestBody.put("orderInfo", transaction.getOrderInfo());
            requestBody.put("returnUrl", transaction.getRedirectUrl());
            requestBody.put("notifyUrl", transaction.getIpnUrl());
            requestBody.put("requestType", transaction.getRequestType());
            requestBody.put("extraData", "");
            
            // Tạo chữ ký
            String rawSignature = "partnerCode=" + partnerCode +
                    "&accessKey=" + accessKey +
                    "&requestId=" + requestId +
                    "&amount=" + (long) tongTien +
                    "&orderId=" + orderId +
                    "&orderInfo=" + transaction.getOrderInfo() +
                    "&returnUrl=" + transaction.getRedirectUrl() +
                    "&notifyUrl=" + transaction.getIpnUrl() +
                    "&extraData=";

            String signature = generateSignature(rawSignature, secretKey);
            requestBody.put("signature", signature);
            transaction.setSignature(signature);
            
            // Log request
            logger.debug("MoMo request payload: {}", requestBody);
            
            // Gọi API MoMo
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);
            
            logger.debug("Calling MoMo API: {}", apiEndpoint);
            
            Map<String, Object> response = restTemplate.postForObject(
                apiEndpoint, request, Map.class);
            
            logger.debug("MoMo API response: {}", response);
            
            if (response != null && response.get("payUrl") != null) {
                String payUrl = response.get("payUrl").toString();
                if (response.get("errorCode") != null) {
                    transaction.setResultCode(Integer.parseInt(response.get("errorCode").toString()));
                }
                if (response.get("message") != null) {
                    transaction.setMessage(response.get("message").toString());
                }
                
                // Lưu thông tin giao dịch
                momoTransactionRepository.save(transaction);
                
                return payUrl;
            } else {
                throw new RuntimeException("Không nhận được payUrl từ MoMo");
            }
        } catch (Exception e) {
            logger.error("Lỗi khi tạo giao dịch MoMo", e);
            throw new RuntimeException("Không thể tạo giao dịch MoMo: " + e.getMessage());
        }
    }

    private String generateSignature(String message, String key) throws Exception {
        Mac hmacSHA256 = Mac.getInstance("HmacSHA256");
        SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        hmacSHA256.init(secretKey);
        byte[] hash = hmacSHA256.doFinal(message.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(hash);
    }
} 