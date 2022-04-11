package com.company;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

public class Main {

    public static final String[] extensions = {".jpg", ".jpeg", ".gif", ".png", ".mp4", ".mov", ".wmv", ".avi", ".flv", ".mkv",
                                               ".JPG", ".JPEG", ".GIF", ".PNG", ".MP4", ".MOV", ".WMV", ".AVI", ".FLV", ".MKV"};
    public static int fileCtr = 0;

    public static void main(String[] args) throws InterruptedException {
        String dir = System.getProperty("user.dir");
        File src = new File(dir);
        try {
            new File(dir + "\\dest").mkdirs();
        } catch (Exception e) {
            System.out.println("Could not create dir");
        }
        File dest = new File(dir + "\\dest");
        ArrayList<File> dirs = getDirs(src);
        if (menu(dest)) {
            Thread mainTask = new Thread() {
                public void run() {
                    moveFiles(dirs, dest);
                }
            };
            Thread spinner = new Thread() {
                public void run() {
                    try {
                        while (mainTask.isAlive()) {
                            spinner();
                        }
                    } catch (Exception e) {}
                }
            };
            spinner.start();
            mainTask.start();
            mainTask.join();
            spinner.join();
        }
        System.out.print("\rDone!");
        System.out.println("\n" + fileCtr + " files copied!" +
                           "\nExiting...");
    }

    public static ArrayList<File> getDirs(File file) {
        try {
            ArrayList<File> dirs = new ArrayList<File>(
                Arrays.asList(file.listFiles(File::isDirectory))
            );
            return dirs;
        } catch (Exception e) {
            System.out.println("Error");
            return null;
        }
    }

    public static void moveFiles(ArrayList<File> dirs, File dest) {
        for (int i = 0; i < dirs.size(); i++) {
            if (dirs.get(i).toString().equals(dest.toString())) {
                continue;
            }
            try {
                ArrayList<File> files = new ArrayList<File>(
                    Arrays.asList(dirs.get(i).listFiles())
                );
                for (int j = 0; j < files.size(); j++) {
                    for (int k = 0; k < extensions.length; k++) {
                        if (files.get(j).toString().contains(extensions[k])) {
                            File fileDest = new File(dest.toString() + getFileName(files.get(j)));
                            Files.copy(files.get(j).toPath(), fileDest.toPath(), REPLACE_EXISTING);
                            fileCtr++;
                            break;
                        }
                    }
                }
            } catch (Exception e) {
                System.out.println("dir not found");
            }
        }
    }

    public static String getFileName(File file) {
        String[] split = file.toString().split("\\\\");
        return ("\\" + split[split.length - 1]);
    }

    public static boolean menu(File dest) {
        Scanner read = new Scanner(System.in);
        String in = "";
        System.out.println("\n*********************************" +
                           "\n* Photo/Video merging tool v1.0 *" +
                           "\n*********************************");
        System.out.println("\nPlace me in the directory that contains all your folders!");
        System.out.println("\nDestination folder: " + dest.toString() +
                           "\nProceed? (Y/N)");
        in = read.nextLine();
        return in.equals("Y") || in.equals("y");
    }

    public static void spinner() throws InterruptedException {
        System.out.print("\rWorking on it... -");
        Thread.sleep(500);
        System.out.print("\rWorking on it... \\");
        Thread.sleep(500);
        System.out.print("\rWorking on it... |");
        Thread.sleep(500);
        System.out.print("\rWorking on it... /");
        Thread.sleep(500);
    }
}
