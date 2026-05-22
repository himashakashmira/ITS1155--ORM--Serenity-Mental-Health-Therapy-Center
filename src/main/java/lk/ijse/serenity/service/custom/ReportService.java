package lk.ijse.serenity.service.custom;

public interface ReportService {
    void generatePatientReport() throws Exception;

    void generateSessionReport() throws Exception;

    void generatePaymentReport() throws Exception;
}
