package com.hotel.reservation_service.util;

import com.hotel.reservation_service.entity.Reservation;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;

public class PDFGenerator {

    public static ByteArrayInputStream generateUserReservationPDF(List<Reservation> reservations) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try (PdfWriter writer = new PdfWriter(out); 
             PdfDocument pdfDoc = new PdfDocument(writer);
             Document document = new Document(pdfDoc)) {

            document.add(new Paragraph("User Reservations Report"));

            for (Reservation reservation : reservations) {
                document.add(new Paragraph("Reservation ID: " + reservation.getId()));
                document.add(new Paragraph("Check-in Date: " + reservation.getCheckInDate()));
                document.add(new Paragraph("Check-out Date: " + reservation.getCheckOutDate()));
                document.add(new Paragraph("Status: " + reservation.getStatus().name()));
                document.add(new Paragraph("-----------------------------------------------------"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ByteArrayInputStream(out.toByteArray());
    }

    public static ByteArrayInputStream generateAdminReservationReportPDF(List<Reservation> reservations) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try (PdfWriter writer = new PdfWriter(out); 
             PdfDocument pdfDoc = new PdfDocument(writer);
             Document document = new Document(pdfDoc)) {

            document.add(new Paragraph("Admin Reservation Report"));

            for (Reservation reservation : reservations) {
                document.add(new Paragraph("Reservation ID: " + reservation.getId()));
                document.add(new Paragraph("User ID: " + reservation.getUserId()));
                document.add(new Paragraph("Room ID: " + reservation.getRoomId()));
                document.add(new Paragraph("Check-in Date: " + reservation.getCheckInDate()));
                document.add(new Paragraph("Check-out Date: " + reservation.getCheckOutDate()));
                document.add(new Paragraph("Status: " + reservation.getStatus().name()));
                document.add(new Paragraph("-----------------------------------------------------"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ByteArrayInputStream(out.toByteArray());
    }
}
