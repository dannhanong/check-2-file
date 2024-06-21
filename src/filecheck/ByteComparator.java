/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package filecheck;

/**
 *
 * @author Admin
 */
import javax.swing.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;

public class ByteComparator {

    public static void compareFiles(String sourceFilePath, String modifiedFilePath, JTextArea logOutputArea, String outputFilePath, String hexToSearch, JProgressBar progressBar) {
        try {
            int bufferSize = 8192;
            File sourceFile = new File(sourceFilePath);
            File modifiedFile = new File(modifiedFilePath);
            long totalBytes = Math.max(sourceFile.length(), modifiedFile.length());
            long processedBytes = 0;

            FileInputStream sourceStream = new FileInputStream(sourceFile);
            FileInputStream modifiedStream = new FileInputStream(modifiedFile);
            byte[] sourceBuffer = new byte[bufferSize];
            byte[] modifiedBuffer = new byte[bufferSize];
            boolean searchSpecificByte = !hexToSearch.isEmpty();
            byte searchByte = searchSpecificByte ? (byte) Integer.parseInt(hexToSearch, 16) : 0;

            int sourceBytesRead, modifiedBytesRead;
            int position = 0;
            StringBuilder report = new StringBuilder();

            while ((sourceBytesRead = sourceStream.read(sourceBuffer)) != -1 &&
                   (modifiedBytesRead = modifiedStream.read(modifiedBuffer)) != -1) {
                int minBytesRead = Math.min(sourceBytesRead, modifiedBytesRead);
                for (int i = 0; i < minBytesRead; i++) {
                    if (!searchSpecificByte && sourceBuffer[i] != modifiedBuffer[i]) {
                        String diff = String.format("Byte at position %d: %02X (source) != %02X (modified)%n", position + i, sourceBuffer[i], modifiedBuffer[i]);
                        logOutputArea.append(diff);
                        report.append(diff);
                    }
                    if (searchSpecificByte) {
                        if (sourceBuffer[i] == searchByte) {
                            String match = String.format("Found byte %02X (source) at position %d%n", searchByte, position + i);
                            logOutputArea.append(match);
                            report.append(match);
                        }
                        if (modifiedBuffer[i] == searchByte) {
                            String match = String.format("Found byte %02X (modified) at position %d%n", searchByte, position + i);
                            logOutputArea.append(match);
                            report.append(match);
                        }
                    }
                }
                position += minBytesRead;
                processedBytes += minBytesRead;
                int progress = (int) ((processedBytes * 100) / totalBytes);
                SwingUtilities.invokeLater(() -> progressBar.setValue(progress));
            }

            // Kiểm tra phần còn lại nếu tệp có độ dài khác nhau
            while (!searchSpecificByte && (sourceBytesRead = sourceStream.read(sourceBuffer)) != -1) {
                for (int i = 0; i < sourceBytesRead; i++) {
                    String diff = String.format("Byte at position %d: %02X (source) != end of file%n", position + i, sourceBuffer[i]);
                    logOutputArea.append(diff);
                    report.append(diff);
                }
                position += sourceBytesRead;
                processedBytes += sourceBytesRead;
                int progress = (int) ((processedBytes * 100) / totalBytes);
                SwingUtilities.invokeLater(() -> progressBar.setValue(progress));
            }

            while (!searchSpecificByte && (modifiedBytesRead = modifiedStream.read(modifiedBuffer)) != -1) {
                for (int i = 0; i < modifiedBytesRead; i++) {
                    String diff = String.format("Byte at position %d: end of file != %02X (modified)%n", position + i, modifiedBuffer[i]);
                    logOutputArea.append(diff);
                    report.append(diff);
                }
                position += modifiedBytesRead;
                processedBytes += modifiedBytesRead;
                int progress = (int) ((processedBytes * 100) / totalBytes);
                SwingUtilities.invokeLater(() -> progressBar.setValue(progress));
            }

            // Lưu báo cáo vào tệp tin
            if (!outputFilePath.isEmpty()) {
                FileWriter writer = new FileWriter(outputFilePath);
                writer.write(report.toString());
                writer.close();
                logOutputArea.append("Report saved to " + outputFilePath + "\n");
            }

            sourceStream.close();
            modifiedStream.close();

        } catch (IOException e) {
            e.printStackTrace();
            logOutputArea.append("An error occurred while comparing files.\n");
        }
    }
}







