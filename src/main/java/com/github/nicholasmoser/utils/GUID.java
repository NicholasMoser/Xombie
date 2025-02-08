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
}
