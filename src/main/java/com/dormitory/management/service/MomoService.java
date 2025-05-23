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
            String orderId = String.format("HD%s_%s", hoaDon.getMaHoaDon(), System.currentTimeMillis());
            String requestId = String.format("RQ%s_%s", hoaDon.getMaHoaDon(), System.currentTimeMillis());
            
            // Tạo giao dịch MoMo mới
            MomoTransaction transaction = new MomoTransaction();
            transaction.setPartnerCode(partnerCode);
            transaction.setOrderId(orderId);
            transaction.setRequestId(requestId);
            transaction.setAmount(hoaDon.getTongTien());
            transaction.setOrderInfo("Thanh toan hoa don KTX - " + hoaDon.getMaHoaDon());
            transaction.setOrderType("billpayment");
            transaction.setRedirectUrl(returnUrl);
            transaction.setIpnUrl(notifyUrl);
            transaction.setRequestType("captureWallet");
            transaction.setExtraData("");
            transaction.setCreatedAt(new Date());
            
            // Tạo payload cho API MoMo
            Map<String, Object> requestBody = new LinkedHashMap<>();
            requestBody.put("partnerCode", partnerCode);
            requestBody.put("requestId", requestId);
            requestBody.put("amount", hoaDon.getTongTien().longValue());
            requestBody.put("orderId", orderId);
            requestBody.put("orderInfo", transaction.getOrderInfo());
            requestBody.put("redirectUrl", returnUrl);
            requestBody.put("ipnUrl", notifyUrl);
            requestBody.put("requestType", "captureWallet");
            requestBody.put("extraData", "");
            requestBody.put("lang", "vi");
            
            // Tạo chữ ký
            StringBuilder rawSignature = new StringBuilder();
            rawSignature.append("accessKey=").append(accessKey)
                    .append("&amount=").append(hoaDon.getTongTien().longValue())
                    .append("&extraData=")
                    .append("&ipnUrl=").append(notifyUrl)
                    .append("&orderId=").append(orderId)
                    .append("&orderInfo=").append(transaction.getOrderInfo())
                    .append("&partnerCode=").append(partnerCode)
                    .append("&redirectUrl=").append(returnUrl)
                    .append("&requestId=").append(requestId)
                    .append("&requestType=captureWallet");

            logger.debug("Raw signature: {}", rawSignature.toString());
            
            String signature = generateSignature(rawSignature.toString(), secretKey);
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
            
            if (response != null) {
                if (response.containsKey("resultCode")) {
                    Integer resultCode = Integer.parseInt(response.get("resultCode").toString());
                    transaction.setResultCode(resultCode);
                    
                    if (resultCode != 0) {
                        String message = response.containsKey("message") ? 
                            response.get("message").toString() : "Unknown error";
                        transaction.setMessage(message);
                        momoTransactionRepository.save(transaction);
                        throw new RuntimeException("Lỗi từ MoMo: " + message);
                    }
                }
                
                if (response.containsKey("payUrl")) {
                    String payUrl = response.get("payUrl").toString();
                    transaction.setMessage(response.containsKey("message") ? 
                        response.get("message").toString() : "Success");
                    
                    // Lưu thông tin giao dịch
                    momoTransactionRepository.save(transaction);
                    
                    return payUrl;
                }
            }
            
            throw new RuntimeException("Không nhận được payUrl từ MoMo");
        } catch (Exception e) {
            logger.error("Lỗi khi tạo giao dịch MoMo", e);
            throw new RuntimeException("Không thể tạo giao dịch MoMo: " + e.getMessage());
        }
    }

    public String createBulkPayment(List<HoaDon> danhSachHoaDon, double tongTien) {
        try {
            String orderId = String.format("HD_BULK_%s", System.currentTimeMillis());
            String requestId = String.format("RQ_BULK_%s", System.currentTimeMillis());
            
            // Tạo giao dịch MoMo mới
            MomoTransaction transaction = new MomoTransaction();
            transaction.setPartnerCode(partnerCode);
            transaction.setOrderId(orderId);
            transaction.setRequestId(requestId);
            transaction.setAmount(tongTien);
            transaction.setOrderInfo("Thanh toan nhieu hoa don KTX");
            transaction.setOrderType("billpayment");
            transaction.setRedirectUrl(returnUrl);
            transaction.setIpnUrl(notifyUrl);
            transaction.setRequestType("captureWallet");
            transaction.setExtraData("");
            transaction.setCreatedAt(new Date());
            
            // Tạo payload cho API MoMo
            Map<String, Object> requestBody = new LinkedHashMap<>();
            requestBody.put("partnerCode", partnerCode);
            requestBody.put("requestId", requestId);
            requestBody.put("amount", (long) tongTien);
            requestBody.put("orderId", orderId);
            requestBody.put("orderInfo", transaction.getOrderInfo());
            requestBody.put("redirectUrl", returnUrl);
            requestBody.put("ipnUrl", notifyUrl);
            requestBody.put("requestType", "captureWallet");
            requestBody.put("extraData", "");
            requestBody.put("lang", "vi");
            
            // Tạo chữ ký
            StringBuilder rawSignature = new StringBuilder();
            rawSignature.append("accessKey=").append(accessKey)
                    .append("&amount=").append((long) tongTien)
                    .append("&extraData=")
                    .append("&ipnUrl=").append(notifyUrl)
                    .append("&orderId=").append(orderId)
                    .append("&orderInfo=").append(transaction.getOrderInfo())
                    .append("&partnerCode=").append(partnerCode)
                    .append("&redirectUrl=").append(returnUrl)
                    .append("&requestId=").append(requestId)
                    .append("&requestType=captureWallet.");
            
            logger.debug("Raw signature: {}", rawSignature.toString());
            
            String signature = generateSignature(rawSignature.toString(), secretKey);
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
            
            if (response != null) {
                if (response.containsKey("resultCode")) {
                    Integer resultCode = Integer.parseInt(response.get("resultCode").toString());
                    transaction.setResultCode(resultCode);
                    
                    if (resultCode != 0) {
                        String message = response.containsKey("message") ? 
                            response.get("message").toString() : "Unknown error";
                        transaction.setMessage(message);
                        momoTransactionRepository.save(transaction);
                        throw new RuntimeException("Lỗi từ MoMo: " + message);
                    }
                }
                
                if (response.containsKey("payUrl")) {
                    String payUrl = response.get("payUrl").toString();
                    transaction.setMessage(response.containsKey("message") ? 
                        response.get("message").toString() : "Success");
                    
                    // Lưu thông tin giao dịch
                    momoTransactionRepository.save(transaction);
                    
                    return payUrl;
                }
            }
            
            throw new RuntimeException("Không nhận được payUrl từ MoMo");
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
        StringBuilder sb = new StringBuilder();
        for (byte b : hash) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
} 