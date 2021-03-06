package com.example.fptufindingmotelv1.controller.admin.managepaymentpackage;

import com.example.fptufindingmotelv1.dto.PaymentPackageDTO;
import com.example.fptufindingmotelv1.model.PaymentPackageModel;
import com.example.fptufindingmotelv1.service.admin.managepaymentpackage.SavePaymentPackageService;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class SavePaymentPackageController {

    @Autowired
    SavePaymentPackageService savePaymentPackageService;

    public JSONObject responseMsg(String code, String message, Object data) {
        JSONObject msg = new JSONObject();
        msg.put("code", code);
        msg.put("message", message);
        msg.put("data", data);
        return msg;
    }

    @ResponseBody
    @RequestMapping(value = "/api-save-payment-package")
    public JSONObject savePaymentPackage(@RequestBody PaymentPackageDTO paymentPackageDTO) {
        try {
            PaymentPackageModel paymentPackageModel = savePaymentPackageService.savePaymentPackage(paymentPackageDTO);

            return paymentPackageModel != null
                    ? responseMsg("000", "Success!", new PaymentPackageDTO(paymentPackageModel))
                    : responseMsg("999", "Lỗi hệ thống!", null);
        } catch (Exception e) {
            return responseMsg("999", "Lỗi hệ thống!", null);
        }
    }
}
