package com.github.nicholasmoser.graphics;

import com.github.nicholasmoser.utils.CRC32;
import com.github.nicholasmoser.xom.Worms3D;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

public class TGATest {
    @Test
    public void testReadWriteLicense() throws Exception {
        Path tgaPath = Worms3D.dir().resolve("files/Logos/License.tga");
        int expected = CRC32.getHash(tgaPath);
        TGA tga = TGAUtil.readFromFile(tgaPath);
        Path outPath = Files.createTempFile("testReadWriteTGA", ".tga");
        tga.writeToFile(outPath, false);
        int actual = CRC32.getHash(outPath);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void testReadWriteMusyXDolby() throws Exception {
        Path tgaPath = Worms3D.dir().resolve("files/Frontend/Icons/musyxdolby.tga");
        int expected = CRC32.getHash(tgaPath);
        TGA tga = TGAUtil.readFromFile(tgaPath);
        Path outPath = Files.createTempFile("testReadWriteTGA", ".tga");
        tga.writeToFile(outPath, false);
        int actual = CRC32.getHash(outPath);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void testReadWriteMusyXDolbaText() throws Exception {
        Path tgaPath = Worms3D.dir().resolve("files/Logos/musyxdolbytext.tga");
        int expected = CRC32.getHash(tgaPath);
        TGA tga = TGAUtil.readFromFile(tgaPath);
        Path outPath = Files.createTempFile("testReadWriteTGA", ".tga");
        tga.writeToFile(outPath, false);
        int actual = CRC32.getHash(outPath);
        assertThat(actual).isEqualTo(expected);
    }
}
