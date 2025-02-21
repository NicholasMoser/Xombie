package com.github.nicholasmoser.graphics;

import com.github.nicholasmoser.kaitai.Tga;
import com.github.nicholasmoser.utils.CRC32;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;

public class TGATest {
    @Test
    public void testKaitaiTGA() throws Exception {
        Tga tga = Tga.fromFile("E:\\Code\\tga-test-suite\\conformance\\ucm8.tga");
        Tga license = Tga.fromFile("C:\\Users\\Nick\\AppData\\Local\\Temp\\License.tga");
        System.out.println();
    }

    @Test
    public void testReadWriteLicense() throws Exception {
        Path tgaPath = Paths.get("src/test/resources/tga/License.tga");
        int expected = CRC32.getHash(tgaPath);
        TGA tga = TGAUtil.readFromFile(tgaPath);
        Path outPath = Files.createTempFile("testReadWriteTGA", ".tga");
        tga.writeToFile(outPath, true);
        int actual = CRC32.getHash(outPath);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void testReadWriteMusyXDolby() throws Exception {
        Path tgaPath = Paths.get("src/test/resources/tga/musyxdolby.tga");
        int expected = CRC32.getHash(tgaPath);
        TGA tga = TGAUtil.readFromFile(tgaPath);
        Path outPath = Files.createTempFile("testReadWriteTGA", ".tga");
        tga.writeToFile(outPath, true);
        int actual = CRC32.getHash(outPath);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void testReadWriteMusyXDolbaText() throws Exception {
        Path tgaPath = Paths.get("src/test/resources/tga/musyxdolbytext.tga");
        int expected = CRC32.getHash(tgaPath);
        TGA tga = TGAUtil.readFromFile(tgaPath);
        Path outPath = Files.createTempFile("testReadWriteTGA", ".tga");
        tga.writeToFile(outPath, true);
        int actual = CRC32.getHash(outPath);
        assertThat(actual).isEqualTo(expected);
    }
}
