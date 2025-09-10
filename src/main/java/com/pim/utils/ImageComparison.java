package com.pim.utils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageComparison {

    public static void main(String[] args) throws IOException {
        // Load the images to compare
        BufferedImage img1 = ImageIO.read(new File("C:\\Users\\Imran.Ansari\\Downloads\\ImageCompare\\TestData\\ss0.27413438048143823.jpg"));
        BufferedImage img2 = ImageIO.read(new File("C:\\Users\\Imran.Ansari\\Downloads\\ImageCompare\\TestData\\1000109_US_front_01.jpg"));

        // Compare the images
        boolean areIdentical = compareImages(img1, img2);
        System.out.println("Images are identical: " + areIdentical);
    }

    public static boolean compareImages(BufferedImage img1, BufferedImage img2) {
        // Check if the images have the same dimensions
        if (img1.getWidth() != img2.getWidth() || img1.getHeight() != img2.getHeight()) {
            return false;  // Different dimensions
        }

        // Compare pixel by pixel
        for (int x = 0; x < img1.getWidth(); x++) {
            for (int y = 0; y < img1.getHeight(); y++) {
                if (img1.getRGB(x, y) != img2.getRGB(x, y)) {
                    return false;  // Found a difference
                }
            }
        }

        // No differences found, the images are identical
        return true;
    }
}
