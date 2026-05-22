package lk.ijse.serenity.service.custom.impl;

import lk.ijse.serenity.dto.PatientDTO;
import lk.ijse.serenity.dto.PaymentDTO;
import lk.ijse.serenity.dto.TherapySessionDTO;
import lk.ijse.serenity.service.ServiceFactory;
import lk.ijse.serenity.service.custom.PatientService;
import lk.ijse.serenity.service.custom.PaymentService;
import lk.ijse.serenity.service.custom.ReportService;
import lk.ijse.serenity.service.custom.TherapySessionService;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRMapCollectionDataSource;

import java.awt.Desktop;
import java.io.File;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.*;

public class ReportServiceImpl implements ReportService {

    private final PatientService patientService =
            ServiceFactory.getInstance().getService(ServiceFactory.ServiceType.PATIENT);
    private final TherapySessionService sessionService =
            ServiceFactory.getInstance().getService(ServiceFactory.ServiceType.SESSION);
    private final PaymentService paymentService =
            ServiceFactory.getInstance().getService(ServiceFactory.ServiceType.PAYMENT);

    @Override
    public void generatePatientReport() throws Exception {
        List<PatientDTO> patients = patientService.getAllPatients();

        List<Map<String, ?>> rows = new ArrayList<>();
        for (PatientDTO p : patients) {
            Map<String, Object> row = new HashMap<>();
            row.put("patientId", p.getPatientId());
            row.put("name", p.getName() != null ? p.getName() : "");
            row.put("address", p.getAddress() != null ? p.getAddress() : "");
            row.put("email", p.getEmail() != null ? p.getEmail() : "");
            row.put("phone", p.getPhone() != null ? p.getPhone() : "");
            row.put("regDate", p.getRegDate() != null ? p.getRegDate().toString() : "");
            rows.add(row);
        }

        InputStream stream = getClass().getResourceAsStream("/reports/PatientReport.jrxml");
        if (stream == null) throw new Exception("PatientReport.jrxml not found in /reports/!");

        JasperReport jasperReport = JasperCompileManager.compileReport(stream);
        JRMapCollectionDataSource dataSource = new JRMapCollectionDataSource(rows);

        Map<String, Object> params = new HashMap<>();
        params.put("REPORT_TITLE", "Patient Records Report");
        params.put("GENERATED_DATE", LocalDate.now().toString());
        params.put("TOTAL_PATIENTS", (long) patients.size());

        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, dataSource);
        openReport(jasperPrint, "patient_report");
    }

    @Override
    public void generateSessionReport() throws Exception {
        List<TherapySessionDTO> sessions = sessionService.getAllSessions();

        List<Map<String, ?>> rows = new ArrayList<>();
        for (TherapySessionDTO s : sessions) {
            Map<String, Object> row = new HashMap<>();
            row.put("sessionId", s.getSessionId());
            row.put("sessionDate", s.getSessionDate() != null ? s.getSessionDate().toString() : "");
            row.put("patientName", s.getPatientName() != null ? s.getPatientName() : "Unknown");
            row.put("therapistName", s.getTherapistName() != null ? s.getTherapistName() : "Unknown");
            row.put("programName", s.getProgramName() != null ? s.getProgramName() : "Unknown");
            row.put("status", s.getStatus() != null ? s.getStatus() : "");
            rows.add(row);
        }

        InputStream stream = getClass().getResourceAsStream("/reports/SessionReport.jrxml");
        if (stream == null) throw new Exception("SessionReport.jrxml not found in /reports/!");

        JasperReport jasperReport = JasperCompileManager.compileReport(stream);
        JRMapCollectionDataSource dataSource = new JRMapCollectionDataSource(rows);

        Map<String, Object> params = new HashMap<>();
        params.put("REPORT_TITLE", "Therapy Sessions Report");
        params.put("GENERATED_DATE", LocalDate.now().toString());

        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, dataSource);
        openReport(jasperPrint, "session_report");
    }

    @Override
    public void generatePaymentReport() throws Exception {
        List<PaymentDTO> payments = paymentService.getAllPayments();

        List<Map<String, ?>> rows = new ArrayList<>();
        double totalPaid = 0.0;
        for (PaymentDTO p : payments) {
            Map<String, Object> row = new HashMap<>();
            row.put("paymentId", p.getPaymentId() != null ? p.getPaymentId() : "");
            row.put("sessionId", p.getSessionId());
            row.put("patientName", p.getPatientName() != null ? p.getPatientName() : "Unknown");
            row.put("programName", p.getProgramName() != null ? p.getProgramName() : "Unknown");
            row.put("amount", p.getAmount());
            row.put("date", p.getDate() != null ? p.getDate().toString() : "");
            row.put("status", p.getStatus() != null ? p.getStatus() : "");
            rows.add(row);
            if ("Paid".equalsIgnoreCase(p.getStatus())) {
                totalPaid += p.getAmount();
            }
        }

        InputStream stream = getClass().getResourceAsStream("/reports/PaymentReport.jrxml");
        if (stream == null) throw new Exception("PaymentReport.jrxml not found in /reports/!");

        JasperReport jasperReport = JasperCompileManager.compileReport(stream);
        JRMapCollectionDataSource dataSource = new JRMapCollectionDataSource(rows);

        Map<String, Object> params = new HashMap<>();
        params.put("REPORT_TITLE", "Financial Payment Report");
        params.put("GENERATED_DATE", LocalDate.now().toString());
        params.put("TOTAL_PAID", totalPaid);

        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, dataSource);
        openReport(jasperPrint, "payment_report");
    }

    private void openReport(JasperPrint print, String name) throws Exception {
        File tempFile = File.createTempFile("serenity_" + name + "_", ".pdf");
        tempFile.deleteOnExit();
        JasperExportManager.exportReportToPdfFile(print, tempFile.getAbsolutePath());
        if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.OPEN)) {
            Desktop.getDesktop().open(tempFile);
        }
    }
}
