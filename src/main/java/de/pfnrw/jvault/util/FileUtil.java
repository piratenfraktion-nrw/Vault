package de.pfnrw.jvault.util;

import org.apache.commons.codec.binary.Base64;

import java.io.*;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;

public class FileUtil {
    public static String base64Encode(byte[] data) {
        Base64 coder = new Base64();

        return coder.encodeAsString(data);
    }

    public static byte[] base64Decode(String data) {
        Base64 coder = new Base64();

        return coder.decode(data);
    }

    public static byte[] readBase64File(String filename) throws IOException {
        return base64Decode(readFile(filename));
    }

    public static byte[] readBase64File(File file) throws IOException {
        return base64Decode(readFile(file));
    }

    public static String readFile(String filename) throws IOException {
        return readFile(filename, "UTF-8");
    }

    public static String readFile(File file) throws IOException {
        return readFile(file, "UTF-8");
    }

    public static String readFile(String filename, String encoding) throws IOException {
        return readFile(new File(filename), encoding);
    }

    public static String readFile(File file, String encoding) throws IOException {
        FileInputStream stream = new FileInputStream(file);
        try {
            FileChannel fc = stream.getChannel();
            MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());

            return Charset.forName(encoding).decode(bb).toString();
        } finally {
            stream.close();
        }
    }

    public static void saveBase64File(String filename, String data) {
        try {
            saveFile(new File(filename), base64Encode(data.getBytes("UTF-8")));
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public static void saveBase64File(File file, String data) {
        try {
            saveFile(file, base64Encode(data.getBytes("UTF-8")));
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public static void saveBase64File(String filename, byte[] data) {
        saveFile(new File(filename), data);
    }

    public static void saveBase64File(File file, byte[] data) {
        saveFile(file, base64Encode(data));
    }

    public static void saveFile(String filename, String data) {
        saveFile(new File(filename), data);
    }

    public static void saveFile(File file, String data) {
        saveFile(file, data, "UTF-8");
    }

    public static void saveFile(File file, String data, String encoding) {
        try {
            saveFile(file, data.getBytes(encoding));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public static void saveFile(File file, byte[] data) {
        try {
            FileOutputStream out = new FileOutputStream(file);
            out.write(data);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}