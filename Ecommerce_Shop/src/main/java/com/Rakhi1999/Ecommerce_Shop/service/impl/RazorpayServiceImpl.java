package com.Rakhi1999.Ecommerce_Shop.service.impl;

import com.Rakhi1999.Ecommerce_Shop.service.interf.RazorpayService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

@Service
public class RazorpayServiceImpl implements RazorpayService {

    @Value("${razorpay.key}")
    private String key;

    @Value("${razorpay.secret}")
    private String secret;

    private static final String RAZORPAY_ORDER_URL = "https://api.razorpay.com/v1/orders";

    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public JSONObject createRazorpayOrder(Long orderId, long amount, String currency, String receipt) {
        JSONObject request = new JSONObject();
        request.put("amount", amount);
        request.put("currency", currency);
        request.put("receipt", receipt);
        request.put("payment_capture", 1);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Use Java standard Base64
        String auth = Base64.getEncoder().encodeToString((key + ":" + secret).getBytes());
        headers.set("Authorization", "Basic " + auth);

        HttpEntity<String> entity = new HttpEntity<>(request.toString(), headers);

        ResponseEntity<String> response = restTemplate.postForEntity(RAZORPAY_ORDER_URL, entity, String.class);
        return new JSONObject(response.getBody());
    }

    @Override
    public boolean verifySignature(String razorpayOrderId, String razorpayPaymentId, String razorpaySignature) {
        try {
            String payload = razorpayOrderId + "|" + razorpayPaymentId;
            Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
            SecretKeySpec secret_key = new SecretKeySpec(secret.getBytes(), "HmacSHA256");
            sha256_HMAC.init(secret_key);

            String generatedSignature = bytesToHex(sha256_HMAC.doFinal(payload.getBytes()));
            return generatedSignature.equals(razorpaySignature);
        } catch (Exception e) {
            return false;
        }
    }

    private String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}
