package image;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;

public class ImageRead {

    public static BufferedImage NRUAVs;
    public static BufferedImage RUAVs;
    public static BufferedImage map;
    static {
        try {
            NRUAVs = ImageIO.read(new FileInputStream("./image/NRUAVs.png"));
            RUAVs = ImageIO.read(new FileInputStream("./image/RUAVs.png"));
            map = ImageIO.read(new FileInputStream("./image/map.jpeg"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
