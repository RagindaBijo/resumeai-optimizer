package com.luka.resumeai_optimizer.util;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.io.RandomAccessReadBuffer;  // ← ADD THIS IMPORT
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@Component
public class PdfTextExtractor {

    public String extractTextFromPdf(MultipartFile pdfFile) throws IOException {
        if (pdfFile == null || pdfFile.isEmpty()) {
            throw new IllegalArgumentException("PDF file is empty or null");
        }

        try (InputStream inputStream = pdfFile.getInputStream()) {
            // Wrap the InputStream in RandomAccessReadBuffer (loads to memory)
            RandomAccessReadBuffer buffer = new RandomAccessReadBuffer(inputStream);

            try (PDDocument document = Loader.loadPDF(buffer)) {  // ← Use Loader.loadPDF(RandomAccessRead)

                if (document.isEncrypted()) {
                    throw new IOException("PDF is encrypted and cannot be processed");
                }

                PDFTextStripper stripper = new PDFTextStripper();
                stripper.setSortByPosition(true); // Improves layout/order in extracted text

                return stripper.getText(document).trim();
            }
        }
    }

    // Bonus: your byte[] fallback (already good, but update to match)
    public String extractTextFromBytes(byte[] pdfBytes) throws IOException {
        try (PDDocument document = Loader.loadPDF(new RandomAccessReadBuffer(pdfBytes))) {
            PDFTextStripper stripper = new PDFTextStripper();
            return stripper.getText(document).trim();
        }
    }
}