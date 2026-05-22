package lk.ijse.serenity.service.custom;

import java.util.List;

public interface DashboardService {
    long getPatientCount();

    double getTotalRevenue();

    List<String> getSpecialPatients();

    long getTodaysSessionCount();

    long getActiveTherapistCount();

    long getPendingPaymentCount();
}
