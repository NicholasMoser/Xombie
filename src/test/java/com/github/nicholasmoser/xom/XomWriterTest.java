package com.github.nicholasmoser.xom;

import com.github.nicholasmoser.utils.CRC32;
import org.junit.jupiter.api.Test;

import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;

public class XomWriterTest {
    @Test
    public void testBundle02() throws Exception {
        Path in = Paths.get("E:\\GNTLargeFiles\\Extracted\\Worms3D\\files\\Bundles\\Bundle02.xom");
        Path out = Files.createTempFile("testBundle02", ".xom");
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
