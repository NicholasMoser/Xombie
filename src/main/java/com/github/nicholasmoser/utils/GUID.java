package com.github.nicholasmoser.utils;

public class GUID {
    public static String bytesToString(byte[] bytes) {
        StringBuilder string = new StringBuilder(36);
        string.append(String.format("%02X", bytes[0x3]));
        string.append(String.format("%02X", bytes[0x2]));
        string.append(String.format("%02X", bytes[0x1]));
        string.append(String.format("%02X", bytes[0x0]));
        string.append('-');
        string.append(String.format("%02X", bytes[0x5]));
        string.append(String.format("%02X", bytes[0x4]));
        string.append('-');
        string.append(String.format("%02X", bytes[0x7]));
        string.append(String.format("%02X", bytes[0x6]));
        string.append('-');
        string.append(String.format("%02X", bytes[0x8]));
        string.append(String.format("%02X", bytes[0x9]));
        string.append('-');
        string.append(String.format("%02X", bytes[0xA]));
        string.append(String.format("%02X", bytes[0xB]));
        string.append(String.format("%02X", bytes[0xC]));
        string.append(String.format("%02X", bytes[0xD]));
        string.append(String.format("%02X", bytes[0xE]));
        string.append(String.format("%02X", bytes[0xF]));
        return string.toString();
    }

    public static byte[] stringToBytes(String guid) {
        String hex = guid.replace("-", "");
        byte[] bytes = ByteUtils.hexTextToBytes(hex);
        byte[] actual = new byte[bytes.length];
        actual[0x0] = bytes[0x3];
        actual[0x1] = bytes[0x2];
        actual[0x2] = bytes[0x1];
        actual[0x3] = bytes[0x0];
        actual[0x4] = bytes[0x5];
        actual[0x5] = bytes[0x4];
        actual[0x6] = bytes[0x7];
        actual[0x7] = bytes[0x6];
        actual[0x8] = bytes[0x8];
        actual[0x9] = bytes[0x9];
        actual[0xA] = bytes[0xA];
        actual[0xB] = bytes[0xB];
        actual[0xC] = bytes[0xC];
        actual[0xD] = bytes[0xD];
        actual[0xE] = bytes[0xE];
        actual[0xF] = bytes[0xF];
        return actual;
    }
}
