package ua.lysenko.andrii.zip;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.io.IOException;

public class FileUtils {

    public static void writeBytes(OutputStream fos, File fileIn) throws IOException {
        try (FileInputStream inputStream = new FileInputStream(fileIn)) {
            final int fiveMb = 5*1024*1024;
            byte[] buffer = new byte[fiveMb];
            for (int readBytesAmount = inputStream.read(buffer); readBytesAmount >= 0; readBytesAmount = inputStream.read(buffer)) {
                fos.write(buffer, 0, readBytesAmount);
            }
        }
    }

}
