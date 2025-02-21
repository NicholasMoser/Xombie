package com.github.nicholasmoser.xom;

import com.github.nicholasmoser.graphics.Palette;
import com.github.nicholasmoser.graphics.TGA;
import com.github.nicholasmoser.graphics.TGAUtil;
import com.github.nicholasmoser.utils.CRC32;
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
    public void testRemoveContainer() throws Exception {
        Path in = Paths.get("E:\\GNTLargeFiles\\Extracted\\Worms3D\\files\\Bundles\\Bundle03.xom");
        Path out = Files.createTempFile("testModifyBundle03", ".xom");
        Xom xom = XomParser.parse(in);
        assertThat(xom.containers().size()).isEqualTo(22);
        assertThat(xom.types().size()).isEqualTo(19);

        // Remove palette containers
        XomModify.removeXContainer(16, xom);
        XomModify.removeXContainer(16, xom);
        XomModify.removeXContainer(16, xom);
        // Remove palette type
        xom.types().remove(14);
        // Update xom header with new type and container count
        XomHeader header = xom.header();
        xom = new Xom(new XomHeader(header.flag(), header.numberOfTypes() - 1, header.maxCount() - 3, header.rootCount()), xom.types(), xom.schmType(), xom.stringTable(), xom.containers());

        // Write xom to file
        try (RandomAccessFile raf = new RandomAccessFile(out.toFile(), "rw")) {
            XomWriter.write(xom, raf);
        }
        Xom actual = XomParser.parse(out);
        assertThat(actual.containers().size()).isEqualTo(19);
        assertThat(actual.types().size()).isEqualTo(18);
    }

    @Test
    public void testWriteTGAs() throws Exception {
        Path in = Paths.get("E:\\GNTLargeFiles\\Extracted\\Worms3D\\files\\Bundles\\Bundle03.xom");

        // Read xom file and write it back out to file
        Xom xom = XomParser.parse(in);

        XContainer license = xom.containers().get(13);
        XContainer musyXDolby = xom.containers().get(14);
        XContainer musyXDolbyText = xom.containers().get(15);

        // Read old palettes and TGAs
        Palette palette1 = Palette.get(license, xom.containers());
        Palette palette2 = Palette.get(musyXDolby, xom.containers());
        Palette palette3 = Palette.get(musyXDolbyText, xom.containers());
        TGA licenseTGA = TGAUtil.readFromXContainer(license, palette1);
        TGA musyXDolbyTGA = TGAUtil.readFromXContainer(musyXDolby, palette2);
        TGA musyXDolbyTextTGA = TGAUtil.readFromXContainer(musyXDolbyText, palette3);

        // Write TGAs to file
        Path baseDir = Paths.get(System.getProperty("java.io.tmpdir"));
        Files.createDirectories(baseDir.resolve(licenseTGA.fileName()).getParent());

        // Write TGAs to file
        licenseTGA.writeToFile(baseDir.resolve(licenseTGA.fileName()));
        musyXDolbyTGA.writeToFile(baseDir.resolve(musyXDolbyTGA.fileName()));
        musyXDolbyTextTGA.writeToFile(baseDir.resolve(musyXDolbyTextTGA.fileName()));
    }

    @Test
    public void testModifyBundle03() throws Exception {
        Path in = Paths.get("E:\\GNTLargeFiles\\Extracted\\Worms3D\\files\\Bundles\\Bundle03.xom");
        Path out = Files.createTempFile("testModifyBundle03", ".xom");

        // Read xom file and write it back out to file
        Xom xom = XomParser.parse(in);

        XContainer license = xom.containers().get(13);
        XContainer musyXDolby = xom.containers().get(14);
        XContainer musyXDolbyText = xom.containers().get(15);

        // Read old palettes and TGAs
        Palette palette1 = Palette.get(license, xom.containers());
        Palette palette2 = Palette.get(musyXDolby, xom.containers());
        Palette palette3 = Palette.get(musyXDolbyText, xom.containers());
        TGA licenseTGA = TGAUtil.readFromXContainer(license, palette1);
        TGA musyXDolbyTGA = TGAUtil.readFromXContainer(musyXDolby, palette2);
        TGA musyXDolbyTextTGA = TGAUtil.readFromXContainer(musyXDolbyText, palette3);

        Path baseDir = Paths.get(System.getProperty("java.io.tmpdir"));
        Files.createDirectories(baseDir.resolve(licenseTGA.fileName()).getParent());

        // Read new TGAs
        TGA newLicenseTGA = TGAUtil.readFromFile(Paths.get("src/test/resources/tga/License.tga"));
        TGA newMusyXDolbyTGA = TGAUtil.readFromFile(Paths.get("src/test/resources/tga/musyxdolby.tga"));
        TGA newMusyXDolbyTextTGA = TGAUtil.readFromFile(Paths.get("src/test/resources/tga/musyxdolbytext.tga"));

        // Replace TGAs
        TGAUtil.replaceTGA(license, newLicenseTGA);
        TGAUtil.replaceTGA(musyXDolby, newMusyXDolbyTGA);
        TGAUtil.replaceTGA(musyXDolbyText, newMusyXDolbyTextTGA);

        // Write xom to file
        try (RandomAccessFile raf = new RandomAccessFile(out.toFile(), "rw")) {
            XomWriter.write(xom, raf);
        }
        Xom actual = XomParser.parse(out);
    }

    @Test
    public void testModifyBundle02() throws Exception {
        Path in = Paths.get("E:\\GNTLargeFiles\\Extracted\\Worms3D\\files\\Bundles\\Bundle02.xom");
        Path out = Files.createTempFile("testModifyBundle02", ".xom");

        // Read xom file and write it back out to file
        Xom xom = XomParser.parse(in);
        XContainer ngcmc00 = xom.containers().get(15);
        XContainer ngcmc01 = xom.containers().get(16);
        XContainer ngcmc02 = xom.containers().get(17);
        XContainer ngcmc03 = xom.containers().get(18);

        // Read TGAs
        TGA ngcmc00TGA = TGAUtil.readFromXContainer(ngcmc00, null);
        TGA ngcmc01TGA = TGAUtil.readFromXContainer(ngcmc01, null);
        TGA ngcmc02TGA = TGAUtil.readFromXContainer(ngcmc02, null);
        TGA ngcmc03TGA = TGAUtil.readFromXContainer(ngcmc03, null);

        // Write TGAs to file
        Path baseDir = Paths.get(System.getProperty("java.io.tmpdir"));
        Files.createDirectories(baseDir.resolve(ngcmc00TGA.fileName()).getParent());
        ngcmc00TGA.writeToFile(baseDir.resolve(ngcmc00TGA.fileName()));
        ngcmc01TGA.writeToFile(baseDir.resolve(ngcmc01TGA.fileName()));
        ngcmc02TGA.writeToFile(baseDir.resolve(ngcmc02TGA.fileName()));
        ngcmc03TGA.writeToFile(baseDir.resolve(ngcmc03TGA.fileName()));

        // Write xom to file
        try (RandomAccessFile raf = new RandomAccessFile(out.toFile(), "rw")) {
            XomWriter.write(xom, raf);
        }
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
