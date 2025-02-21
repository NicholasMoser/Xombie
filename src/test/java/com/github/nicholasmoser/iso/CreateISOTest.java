package com.github.nicholasmoser.iso;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.github.nicholasmoser.utils.CRC32;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import com.github.nicholasmoser.xom.Worms3D;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class CreateISOTest {

  //Path wormsPath = Paths.get("D:\\Roms\\Gamecube&Wii\\Worms 3D (USA) (En,Fr).ciso");
  Path outputDir = Worms3D.dir();
  Path outputIso = Worms3D.dir().resolve("worms.iso");

  @Test
  @Disabled("Skip for now")
  public void createISOFromDirectory() throws Exception {
    DirectoryParser dirParser = new DirectoryParser(outputDir, true);
    ISOHeader isoHeader = dirParser.getISOHeader();
    ISOCreator creator = new ISOCreator(outputDir, outputIso);
    creator.create(true, isoHeader);
    //validateISO(outputIso);
  }

  /**
   * Validate the ISO is the correct size and hash for a vanilla ISO.
   *
   * @param testIso The ISO to validate.
   * @throws IOException If an I/O error occurs
   */
  private void validateISO(Path testIso) throws IOException {
    assertEquals(ISO.DISC_SIZE, Files.size(testIso));
    int hash = CRC32.getHash(testIso);
    assertEquals(0x55EE8B1A, hash, "Hash of new ISO does not match original");
  }

  /**
   * Validate the header file values are correct for a vanilla ISO.
   *
   * @param isoHeader The ISOHeader.
   */
  private void validateHeaderFiles(ISOHeader isoHeader) {
    ISOFile bootBin = isoHeader.getBootBin();
    assertEquals(ISO.BOOT_BIN_LEN, bootBin.getLen());
    ISOFile bi2Bin = isoHeader.getBi2Bin();
    assertEquals(ISO.BI_2_LEN, bi2Bin.getLen());
    ISOFile apploaderImg = isoHeader.getApploaderImg();
    assertEquals(0x1DE58, apploaderImg.getLen());
    ISOFile mainDol = isoHeader.getMainDol();
    assertEquals(0x224A00, mainDol.getLen());
    ISOFile fstBin = isoHeader.getFstBin();
    assertEquals(0x33E7, fstBin.getLen());
    List<ISOItem> files = isoHeader.getFiles();
    assertEquals(ISO.W3D_ISO_ITEMS_SIZE, files.size());
  }
}
