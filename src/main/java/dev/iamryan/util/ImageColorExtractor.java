package dev.iamryan.util;

import dev.iamryan.model.ImageColorInfo;
import dev.iamryan.model.RgbColor;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.imageio.ImageIO;
import java.io.InputStream;
import java.net.URL;

public class ImageColorExtractor {

    public static ImageColorInfo extractDominantColors(String imageUrl) throws Exception {
        BufferedImage image = readImageFromUrl(imageUrl);
        if (image == null) {
            throw new Exception("image is null");
        }
        Map<Integer, Integer> colorCount = new HashMap<>();
        int width = image.getWidth();
        int height = image.getHeight();

        for (int y = 0; y < height; y += 5) {
            for (int x = 0; x < width; x += 5) {
                int rgb = image.getRGB(x, y);
                int alpha = (rgb >> 24) & 0xFF;
                if (alpha < 128) continue;

                int r = (rgb >> 16) & 0xFF;
                int g = (rgb >> 8) & 0xFF;
                int b = rgb & 0xFF;

                // 降维
                int simplified = (r / 10) << 16 | (g / 10) << 8 | (b / 10);
                colorCount.put(simplified, colorCount.getOrDefault(simplified, 0) + 1);
            }
        }

        List<Map.Entry<Integer, Integer>> sorted = colorCount.entrySet().stream()
                .sorted((a, b) -> b.getValue() - a.getValue())
                .collect(Collectors.toList());

        List<RgbColor> topColors = sorted.stream()
                .limit(6)
                .map(e -> {
                    int r = Math.min(((e.getKey() >> 16) & 0xFF) * 10, 255);
                    int g = Math.min(((e.getKey() >> 8) & 0xFF) * 10, 255);
                    int b = Math.min((e.getKey() & 0xFF) * 10, 255);
                    return new RgbColor(r, g, b);
                })
                .collect(Collectors.toList());

        RgbColor domain = topColors.size() > 0 ? topColors.get(0) : new RgbColor(255, 255, 255);
        RgbColor second = topColors.size() > 1 ? topColors.get(1) : domain;
        RgbColor third = topColors.size() > 2 ? topColors.get(2) : second;

        return new ImageColorInfo(domain, second, third, topColors);
    }

    private static BufferedImage readImageFromUrl(String imageUrl) throws Exception {
        URL url = new URL(imageUrl);
        try (InputStream is = url.openStream()) {
            return ImageIO.read(is);
        }
    }
    public static void main(String[] args) {
        try {
            String imageUrl = " https://ryann.me/avatar.jpeg";
            ImageColorInfo info = ImageColorExtractor.extractDominantColors(imageUrl);
            System.out.println(info.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}
