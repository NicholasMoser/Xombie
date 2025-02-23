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
        Path in = Worms3D.dir().resolve("files\\Bundles\\Bundle03.xom");
        Path out = Files.createTempFile("testModifyBundle03", ".xom");
        Xom xom = XomParser.parse(in);
        assertThat(xom.containers().size()).isEqualTo(22);
        assertThat(xom.types().size()).isEqualTo(19);

        // Remove palette containers
        XomModify.removeXContainer(16, xom);
        XomModify.removeXContainer(16, xom);
        XomModify.removeXContainer(16, xom);
        // Remove palette type
        XomModify.removeXType(14, xom);

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
        Path w3d = Worms3D.dir();
        Path in = w3d.resolve("files\\Bundles\\Bundle03.xom");

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
        Path licenseOut = baseDir.resolve(licenseTGA.fileName());
        Path musyXDolbyOut = baseDir.resolve(musyXDolbyTGA.fileName());
        Path musyXDolbyTextOut = baseDir.resolve(musyXDolbyTextTGA.fileName());

        licenseTGA.writeToFile(licenseOut, false);
        musyXDolbyTGA.writeToFile(musyXDolbyOut, false);
        musyXDolbyTextTGA.writeToFile(musyXDolbyTextOut, false);

        // Get hash from expected and actual and compare
        int actualLicenseHash = CRC32.getHash(licenseOut);
        int actualMusyXDolbyHash = CRC32.getHash(musyXDolbyOut);
        int actualMusyXDolbyTextHash = CRC32.getHash(musyXDolbyTextOut);
        int expectedLicenseHash = CRC32.getHash(w3d.resolve("files/Logos/License.tga"));
        int expectedMusyXDolbyHash = CRC32.getHash(w3d.resolve("files/Frontend/Icons/musyxdolby.tga"));
        int expectedMusyXDolbyTextHash = CRC32.getHash(w3d.resolve("files/Logos/musyxdolbytext.tga"));
        assertThat(actualLicenseHash).isEqualTo(expectedLicenseHash);
        assertThat(actualMusyXDolbyHash).isEqualTo(expectedMusyXDolbyHash);
        assertThat(actualMusyXDolbyTextHash).isEqualTo(expectedMusyXDolbyTextHash);
    }

    @Test
    public void testCheckBundle03() throws Exception {
        // Test loading a CI8 image from bundle03 and saving it as an RGBA TGA file. Used to debug CI8 code.
        Path in = Paths.get("E:\\GNTLargeFiles\\Extracted\\Worms3DKerfuffle\\files\\Bundles\\bundle03.xom");
        Path out = Files.createTempFile("checkBundle03", ".tga");
        Xom xom = XomParser.parse(in);
        XContainer license = xom.containers().get(13);
        Palette palette = Palette.get(license, xom.containers());
        TGA tga = TGAUtil.readFromXContainer(license, palette);
        tga.writeToFile(out, false, "kImageFormat_NgcCI8");
    }

    @Test
    public void testModifyBundle03() throws Exception {
        Path in = Worms3D.dir().resolve("files\\Bundles\\Bundle03.xom");
        Path out = Paths.get("E:\\GNTLargeFiles\\Extracted\\Worms3DKerfuffle\\files\\Bundles\\bundle03.xom");

        // Read xom file and write it back out to file
        Xom xom = XomParser.parse(in);

        XContainer license = xom.containers().get(13);
        XContainer musyXDolby = xom.containers().get(14);
        XContainer musyXDolbyText = xom.containers().get(15);

        // Read new TGAs
        TGA newLicenseTGA = TGAUtil.readFromFile(Paths.get("src/test/resources/tga/License_RGBA_To_Index.tga"));
        TGA newMusyXDolbyTGA = TGAUtil.readFromFile(Paths.get("src/test/resources/tga/musyxdolby_Photoshop.tga"));
        TGA newMusyXDolbyTextTGA = TGAUtil.readFromFile(Paths.get("src/test/resources/tga/musyxdolbytext_RGBA_To_Index.tga"));

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
    public void testBundle03() throws Exception {
        Path in = Worms3D.dir().resolve("files\\Bundles\\Bundle03.xom");
        Path out = Files.createTempFile("testBundle03", ".xom");
        testXom(in, out);
    }

    @Test
    public void testBundle02() throws Exception {
        Path in = Worms3D.dir().resolve("files\\Bundles\\Bundle02.xom");
        Path out = Files.createTempFile("testBundle02", ".xom");
        testXom(in, out);
    }

    @Test
    public void testAITwk() throws Exception {
        Path in = Worms3D.dir().resolve("files\\AITwk.xom");
        Path out = Files.createTempFile("testAITwk", ".xom");
        testXom(in, out);
    }

    @Test
    public void testCamTwk() throws Exception {
        Path in = Worms3D.dir().resolve("files\\CamTwk.xom");
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
