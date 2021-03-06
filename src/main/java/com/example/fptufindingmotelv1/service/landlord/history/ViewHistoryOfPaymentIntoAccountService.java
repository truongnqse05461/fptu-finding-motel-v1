package com.example.fptufindingmotelv1.service.landlord.history;

import com.example.fptufindingmotelv1.dto.PaymentDTO;
import com.example.fptufindingmotelv1.model.PaymentModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ViewHistoryOfPaymentIntoAccountService {
    Page<PaymentModel> getPaymentsByLandlord(PaymentDTO paymentDTO, Pageable pageable);
}
