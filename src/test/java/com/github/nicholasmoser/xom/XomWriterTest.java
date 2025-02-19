package com.github.nicholasmoser.xom;

import com.github.nicholasmoser.graphics.Palette;
import com.github.nicholasmoser.graphics.TGA;
import com.github.nicholasmoser.graphics.TGAReader;
import com.github.nicholasmoser.utils.CRC32;
import com.github.nicholasmoser.xom.ctnr.Ref;
import com.github.nicholasmoser.xom.ctnr.XContainer;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;

public class XomWriterTest {
    @Test
    public void testModifyBundle03() throws Exception {
        Path in = Paths.get("E:\\GNTLargeFiles\\Extracted\\Worms3D\\files\\Bundles\\Bundle03.xom");
        Path out = Files.createTempFile("testModifyBundle03", ".xom");

        // Read xom file and write it back out to file
        Xom xom = XomParser.parse(in);
        XContainer license = xom.containers().get(13);
        XContainer musyXDolby = xom.containers().get(14);
        XContainer musyXDolbyText = xom.containers().get(15);
        Palette palette1 = Palette.get(license, xom.containers());
        Palette palette2 = Palette.get(musyXDolby, xom.containers());
        Palette palette3 = Palette.get(musyXDolbyText, xom.containers());
        Path baseDir = Paths.get("C:\\Users\\Nick\\Desktop\\ok");
        TGA licenseTGA = TGAReader.fromXContainer(license, palette1);
        TGA musyXDolbyTGA = TGAReader.fromXContainer(musyXDolby, palette2);
        TGA musyXDolbyTextTGA = TGAReader.fromXContainer(musyXDolbyText, palette3);
        licenseTGA.writeToFile(baseDir.resolve(licenseTGA.getFileName()));
        musyXDolbyTGA.writeToFile(baseDir.resolve(musyXDolbyTGA.getFileName()));
        musyXDolbyTextTGA.writeToFile(baseDir.resolve(musyXDolbyTextTGA.getFileName()));
        try (RandomAccessFile raf = new RandomAccessFile(out.toFile(), "rw")) {
            XomWriter.write(xom, raf);
        }

        // Compare hashes to verify files are the same
        int expected = 1234;
        int actual = CRC32.getHash(out);
        assertThat(expected).isEqualTo(actual);
    }

    @Test
    public void testModifyBundle02() throws Exception {
        Path in = Paths.get("E:\\GNTLargeFiles\\Extracted\\Worms3D\\files\\Bundles\\Bundle02.xom");
        Path out = Files.createTempFile("testModifyBundle03", ".xom");

        // Read xom file and write it back out to file
        Xom xom = XomParser.parse(in);
        XContainer ngcmc00 = xom.containers().get(15);
        XContainer ngcmc01 = xom.containers().get(16);
        XContainer ngcmc02 = xom.containers().get(17);
        XContainer ngcmc03 = xom.containers().get(18);
        Path baseDir = Paths.get("C:\\Users\\Nick\\Desktop\\ok");
        TGA ngcmc00TGA = TGAReader.fromXContainer(ngcmc00, null);
        TGA ngcmc01TGA = TGAReader.fromXContainer(ngcmc01, null);
        TGA ngcmc02TGA = TGAReader.fromXContainer(ngcmc02, null);
        TGA ngcmc03TGA = TGAReader.fromXContainer(ngcmc03, null);
        ngcmc00TGA.writeToFile(baseDir.resolve(ngcmc00TGA.getFileName()));
        ngcmc01TGA.writeToFile(baseDir.resolve(ngcmc01TGA.getFileName()));
        ngcmc02TGA.writeToFile(baseDir.resolve(ngcmc02TGA.getFileName()));
        ngcmc03TGA.writeToFile(baseDir.resolve(ngcmc03TGA.getFileName()));
        try (RandomAccessFile raf = new RandomAccessFile(out.toFile(), "rw")) {
            XomWriter.write(xom, raf);
        }

        // Compare hashes to verify files are the same
        int expected = 1234;
        int actual = CRC32.getHash(out);
        assertThat(expected).isEqualTo(actual);
    }

    @Test
    public void testBundle03() throws Exception {
        Path in = Paths.get("E:\\GNTLargeFiles\\Extracted\\Worms3D\\files\\Bundles\\Bundle03.xom");
        Path out = Files.createTempFile("testBundle03", ".xom");
        testXom(in, out);
    }

    @Test
    public void testBundle02() throws Exception {
        Path in = Paths.get("E:\\GNTLargeFiles\\Extracted\\Worms3D\\files\\Bundles\\Bundle02.xom");
        Path out = Files.createTempFile("testBundle02", ".xom");
        testXom(in, out);
    }

    @Test
    public void testAITwk() throws Exception {
        Path in = Paths.get("E:\\GNTLargeFiles\\Extracted\\Worms3D\\files\\AITwk.xom");
        Path out = Files.createTempFile("testAITwk", ".xom");
        testXom(in, out);
    }

    @Test
    public void testCamTwk() throws Exception {
        Path in = Paths.get("E:\\GNTLargeFiles\\Extracted\\Worms3D\\files\\CamTwk.xom");
        Path out = Files.createTempFile("testCamTwk", ".xom");
        testXom(in, out);
    }

    public void testXom(Path in, Path out) throws IOException {
        System.out.println(out);

        // Read xom file and write it back out to file
        Xom xom = XomParser.parse(in);
        try (RandomAccessFile raf = new RandomAccessFile(out.toFile(), "rw")) {
            XomWriter.write(xom, raf);
        }

        // Compare hashes to verify files are the same
        int expected = CRC32.getHash(in);
        int actual = CRC32.getHash(out);
        assertThat(expected).isEqualTo(actual);
    }
}
