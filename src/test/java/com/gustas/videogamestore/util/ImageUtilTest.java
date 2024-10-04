package com.gustas.videogamestore.util;

import com.gustas.videogamestore.AbstractIntegrationTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.zip.DataFormatException;

import static com.gustas.videogamestore.constants.TestConstants.IMAGE_COMPRESSION_NAME;
import static org.junit.Assert.assertThrows;

class ImageUtilTest extends AbstractIntegrationTest {

    @Test
    void testCompressAndDecompressImage() throws IOException, DataFormatException {
        byte[] originalData = IMAGE_COMPRESSION_NAME.getBytes();
        byte[] compressedData = ImageUtil.compressImage(originalData);
        byte[] decompressedData = ImageUtil.decompressImage(compressedData);

        Assertions.assertArrayEquals(originalData, decompressedData);
    }

    @Test
    void testCompressImageWithEmptyData() throws IOException, DataFormatException {
        byte[] originalData = new byte[0];
        byte[] compressedData = ImageUtil.compressImage(originalData);
        byte[] decompressedData = ImageUtil.decompressImage(compressedData);

        Assertions.assertArrayEquals(originalData, decompressedData);
    }

    @Test
    void testDecompressImageWithCorruptedData() {
        byte[] corruptedData = new byte[]{1, 2, 3, 4, 5};

        assertThrows(DataFormatException.class, () -> {
            ImageUtil.decompressImage(corruptedData);
        });
    }

}
