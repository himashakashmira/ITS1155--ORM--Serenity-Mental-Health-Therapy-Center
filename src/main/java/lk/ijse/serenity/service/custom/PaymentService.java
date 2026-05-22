package lk.ijse.serenity.service.custom;

import lk.ijse.serenity.dto.PaymentDTO;

import java.util.List;

public interface PaymentService {
    boolean savePayment(PaymentDTO dto);

    boolean updatePayment(PaymentDTO dto);

    boolean deletePayment(String id);

    List<PaymentDTO> getAllPayments();

}