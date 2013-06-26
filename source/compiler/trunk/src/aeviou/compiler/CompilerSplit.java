package aeviou.compiler;

import java.util.Scanner;
import java.io.*;

public class CompilerSplit{

    private void mergeFile(String in1, String in2, String out) throws IOException {
        File file1 = new File(in1);
        File file2 = new File(in2);
        File file = new File(out);
        file.createNewFile();
        FileInputStream in1Stream = new FileInputStream(file1);
        FileInputStream in2Stream = new FileInputStream(file2);
        FileOutputStream outStream = new FileOutputStream(file);
        int count = 0;
        byte[] buffer = new byte[8 * 1024];
        while ((count = in1Stream.read(buffer)) > 0) {
            outStream.write(buffer, 0, count);
        }

        while ((count = in2Stream.read(buffer)) > 0) {
            outStream.write(buffer, 0, count);
        }
        outStream.flush();
        outStream.close();
        in1Stream.close();
        in2Stream.close();
    }

    public static void main(String[] args) {
        Scanner console = new Scanner(System.in);

        try {
            String fileName = console.nextLine();
            File file = new File(fileName);
            File file1 = new File(fileName + "1");
            File file2 = new File(fileName + "2");
            FileInputStream in = new FileInputStream(file);
            FileOutputStream out1 = null;
            FileOutputStream out2 = null;
            int count = 0;
            int bytes = 0;

            file1.createNewFile();
            file2.createNewFile();
            out1 = new FileOutputStream(file1);
            out2 = new FileOutputStream(file2);
            byte[] buffer = new byte[1024 * 8];
            while ((count = in.read(buffer)) > 0) {
                bytes += count;
                if (bytes < 800 * 1024) {
                    out1.write(buffer, 0, count);
                } else {
                    out2.write(buffer, 0, count);
                }
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }
}
