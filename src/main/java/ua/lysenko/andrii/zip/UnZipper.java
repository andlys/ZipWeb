package ua.lysenko.andrii.zip;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static ua.lysenko.andrii.zip.Print.print;

public class UnZipper {

    public static void unzip(String pathZipFile) {
        File zipFile = new File(pathZipFile);
        try (ZipInputStream zos = new ZipInputStream(new FileInputStream(zipFile))) {
            unzip(zipFile, zos);
            print("Success unzipping to " + getOutputDirectory(zipFile));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void unzip(File zipFile, ZipInputStream zipInputStream) throws IOException {
        Files.createDirectories(Paths.get(getOutputDirectory(zipFile)));
        ZipEntry zipEntry;
        while ((Objects.nonNull(zipEntry = zipInputStream.getNextEntry()))) {
            if (isDirectory(zipEntry)) {
                Files.createDirectories(Paths.get(getOutputDirectory(zipFile), zipEntry.getName()));
                print("dirPath " + zipEntry.getName());
            } else {
                writeFile(zipFile, zipInputStream, zipEntry);
                print("filePath " + zipEntry.getName());
            }
        }
    }

    private static void writeFile(File zipFile, ZipInputStream zipInputStream, ZipEntry zipEntry) throws IOException {
        Path outputFile = Paths.get(getOutputDirectory(zipFile), zipEntry.getName());
        Files.createFile(outputFile);
        try (FileOutputStream outputStream = new FileOutputStream(outputFile.toString())) {
            final int fiveMb = 5 * 1024 * 1024;
            byte[] buffer = new byte[fiveMb];
            while (zipInputStream.available() > 0) {
                int bytesReadAmount = zipInputStream.read(buffer, 0, buffer.length);
                if (bytesReadAmount != -1) {
                    outputStream.write(buffer, 0, bytesReadAmount);
                }
            }
        }
    }

    private static boolean isDirectory(ZipEntry entry) {
        return entry.getName().endsWith("\\") || entry.isDirectory();
    }

    private static String fileNameWithoutExtension(File file) {
        return file.getName().substring(0, file.getName().lastIndexOf('.'));
    }

    private static String getOutputDirectory(File zipFile) {
        return Paths.get(zipFile.getParentFile().toPath().toString(), fileNameWithoutExtension(zipFile)).toString();
    }

}
