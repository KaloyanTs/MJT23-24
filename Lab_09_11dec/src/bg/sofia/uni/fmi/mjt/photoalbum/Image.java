package bg.sofia.uni.fmi.mjt.photoalbum;

import java.awt.image.BufferedImage;

public class Image {
    String name;
    BufferedImage data;

    public Image(String name, BufferedImage data) {
        this.name = name;
        this.data = data;
    }
}

#uses:
#javax.imageio.ImageIO;
#java.awt.image.BufferedImage;
#java.io.IOException;
#java.io.UncheckedIOException;
#java.nio.file.Path;

public Image loadImage(Path imagePath) {
    try {
        BufferedImage imageData = ImageIO.read(imagePath.toFile());
        return new Image(imagePath.getFileName().toString(), imageData);
    } catch (IOException e) {
        throw new UncheckedIOException(String.format("Failed to load image %s", imagePath.toString()), e);
    }
}

#
uses java.awt.image.BufferedImage;

private Image convertToBlackAndWhite(Image image) {
    BufferedImage processedData =
        new BufferedImage(image.data.getWidth(), image.data.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
    processedData.getGraphics().drawImage(image.data, 0, 0, null);

    return new Image(image.name, processedData);
}