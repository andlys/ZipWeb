package ua.lysenko.andrii.zip;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static ua.lysenko.andrii.zip.Print.print;

public class Zipper {

    public static void zip(String pathIn, String pathOut) {
        File fileIn = new File(pathIn);
        File fileOut = new File(pathOut);
        try (ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(fileOut))) {
            zip(fileIn, zos);
            print("Success packing to " + fileOut.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void zip(File fileIn, ZipOutputStream zos) throws IOException {
        LinkedList<File> list = new LinkedList<>();
        list.addFirst(fileIn);
        final Path baseRelativeDir = fileIn.toPath();
        while (!list.isEmpty()) {
            LinkedList<File> newList = new LinkedList<>();
            for (File file : list) {
                if (file.isDirectory()) {
                    zipDirectory(file, zos, baseRelativeDir);
                    File[] files = file.listFiles();
                    if (Objects.nonNull(files)) {
                        newList.addAll(Arrays.asList(files));
                    }
                } else {
                    zipFile(file, zos, baseRelativeDir);
                }
            }
            list = newList;
        }
    }

    private static void zipFile(File fileIn, ZipOutputStream zos, Path relativeDir) throws IOException {
        String zipPath = "";
        if (relativeDir.toFile().isDirectory()) {
            zipPath = relativeDir.getFileName() + "\\" + relativeDir.relativize(fileIn.toPath());
        } else {
            zipPath = fileIn.getName();
        }
        zos.putNextEntry(new ZipEntry(zipPath));
        writeBytes(zos, fileIn);
        zos.closeEntry();
        print("filePath " + zipPath);
    }

    private static void zipDirectory(File fileIn, ZipOutputStream zos, Path relativeDir) throws IOException {
        String zipEntryName;
        if (fileIn.toPath().equals(relativeDir)) {
            zipEntryName = String.format("%s\\", relativeDir.getFileName());
        } else {
            zipEntryName = String.format("%s\\%s\\", relativeDir.getFileName(), relativeDir.relativize(fileIn.toPath()));
        }
        print("dirPath " + zipEntryName);
        zos.putNextEntry(new ZipEntry(zipEntryName));
        zos.closeEntry();
    }

    private static void writeBytes(ZipOutputStream zos, File fileIn) throws IOException {
        try (FileInputStream inputStream = new FileInputStream(fileIn)) {
            final int fiveMb = 5*1024*1024;
            byte[] buffer = new byte[fiveMb];
            for (int readBytesAmount = inputStream.read(buffer); readBytesAmount >= 0; readBytesAmount = inputStream.read(buffer)) {
                zos.write(buffer, 0, readBytesAmount);
            }
        }
    }
}
