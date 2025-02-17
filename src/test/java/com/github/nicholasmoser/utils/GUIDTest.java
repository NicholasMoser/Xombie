package com.github.nicholasmoser.utils;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class GUIDTest {
    @Test
    public void testGUID() {
        byte[] expected = new byte[] {0x4F, 0x79, (byte) 0xCF, (byte) 0x87, (byte) 0xAA, (byte) 0xA2, 0x44, 0x4C, (byte) 0xA9, (byte) 0xDB, 0x7C, (byte) 0x90, (byte) 0x91, 0x24, 0x5C, (byte) 0xB3};
        String guid = GUID.bytesToString(expected);
        assertThat(guid).isEqualTo("87CF794F-A2AA-4C44-A9DB-7C9091245CB3");
        byte[] actual = GUID.stringToBytes(guid);
        assertThat(expected).isEqualTo(actual);
    }
}
