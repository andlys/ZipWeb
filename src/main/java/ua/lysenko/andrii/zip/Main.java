package ua.lysenko.andrii.zip;

import static ua.lysenko.andrii.zip.Print.print;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        if (args.length == 0) {
            printHelp();
            return;
        }
        switch (args[0]) {
            case "-zip":
                if (args.length != 3)
                    print("Invalid list of arguments for -zip. Read -help");
                else
                    Zipper.zip(args[1], args[2]);
                break;
            case "-unzip":
                if (args.length != 2)
                    print("Invalid list of arguments for -unzip. Read -help");
                else
                    UnZipper.unzip(args[1]);
                break;
            case "-help":
            default:
                printHelp();
                break;
        }
    }

    private static void printHelp() {
        print("This utility can be used to zip/unzip archives, usage:");
        print("-zip fileOrDirectoryToZip output.zip " + System.lineSeparator() + "-unzip file.zip");
    }

    public static void mainNoArgs() throws InterruptedException {
        String pathInput = "C:\\projects\\ZipCmd\\src\\ua\\lysenko\\andrii\\zip\\Zipper.java";
        String pathOfZipFile = "C:\\Users\\Andrii\\Desktop\\zip_file.zip";
//        wait to open VisualVM manually in time to monitor memory consumption
//        Thread.sleep(10000);
        Zipper.zip(pathInput, pathOfZipFile);
//        UnZipper.unzip(pathOfZipFile);
    }
}
