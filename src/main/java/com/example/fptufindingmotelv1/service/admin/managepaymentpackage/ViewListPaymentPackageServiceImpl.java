package com.example.fptufindingmotelv1.service.admin.managepaymentpackage;

import com.example.fptufindingmotelv1.dto.StatusDTO;
import com.example.fptufindingmotelv1.model.PaymentPackageModel;
import com.example.fptufindingmotelv1.model.StatusModel;
import com.example.fptufindingmotelv1.repository.PaymentPackageRepository;
import com.example.fptufindingmotelv1.repository.StatusRepository;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ViewListPaymentPackageServiceImpl implements ViewListPaymentPackageService {
    @Autowired
    PaymentPackageRepository paymentPackageRepository;

    @Override
    public Page<PaymentPackageModel> getListPaymentPackage(Pageable pageable) {
        try {
            return paymentPackageRepository.getAllPaymentPackage(pageable);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

}