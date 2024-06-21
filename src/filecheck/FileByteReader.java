/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package filecheck;

/**
 *
 * @author Admin
 */
import java.io.FileInputStream;
import java.io.IOException;

import java.io.FileInputStream;
import java.io.IOException;

public class FileByteReader {

    public static byte[] readFileToByteArray(String filePath, int bufferSize) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(filePath);
        byte[] buffer = new byte[bufferSize];
        int bytesRead;
        try {
            bytesRead = fileInputStream.read(buffer);
        } finally {
            fileInputStream.close();
        }
        byte[] result = new byte[bytesRead];
        System.arraycopy(buffer, 0, result, 0, bytesRead);
        return result;
    }
}



