package com.github.nicholasmoser.gfx;

import com.github.nicholasmoser.graphics.TGA;
import com.github.nicholasmoser.utils.CRC32;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;

public class TGATest {
    @Test
    public void testReadWriteLicense() throws Exception {
        Path tgaPath = Paths.get("src/test/resources/tga/License.tga");
        int expected = CRC32.getHash(tgaPath);
        TGA tga = TGA.readFromFile(tgaPath);
        Path outPath = Files.createTempFile("testReadWriteTGA", ".tga");
        tga.writeToFile(outPath);
        int actual = CRC32.getHash(outPath);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void testReadWriteMusyXDolby() throws Exception {
        Path tgaPath = Paths.get("src/test/resources/tga/musyxdolby.tga");
        int expected = CRC32.getHash(tgaPath);
        TGA tga = TGA.readFromFile(tgaPath);
        Path outPath = Files.createTempFile("testReadWriteTGA", ".tga");
        tga.writeToFile(outPath);
        int actual = CRC32.getHash(outPath);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void testReadWriteMusyXDolbaText() throws Exception {
        Path tgaPath = Paths.get("src/test/resources/tga/musyxdolbytext.tga");
        int expected = CRC32.getHash(tgaPath);
        TGA tga = TGA.readFromFile(tgaPath);
        Path outPath = Files.createTempFile("testReadWriteTGA", ".tga");
        tga.writeToFile(outPath);
        int actual = CRC32.getHash(outPath);
        assertThat(actual).isEqualTo(expected);
    }
}
