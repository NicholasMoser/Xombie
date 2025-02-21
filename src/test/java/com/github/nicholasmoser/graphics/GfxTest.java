package com.github.nicholasmoser.graphics;

import org.junit.jupiter.api.Test;

import java.security.SecureRandom;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;

public class GfxTest {
    @Test
    public void testIndicesCI8ToTGAtoCI8() {
        byte[] expected = new byte[60000];
        Random random = new SecureRandom();
        for (int i = 0; i < expected.length; i++) {
            expected[i] = (byte) random.nextInt();
        }
        byte[] tga = Gfx.convertCI8IndicesToTGA(expected, 100, 600);
        byte[] actual = Gfx.convertTGAIndicesToCI8(tga, 100, 600, false);
        assertThat(expected).isEqualTo(actual);
    }
}
