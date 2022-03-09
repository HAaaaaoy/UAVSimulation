package image;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;

public class ImageRead {

    public static BufferedImage NRUAVs;
    public static BufferedImage RUAVs;
    public static BufferedImage GateWays;
    public static BufferedImage map;
    public static BufferedImage cloudOriginal;
    public static BufferedImage cloudBlue;
    public static BufferedImage cloudCopy;
    static {
        try {
            NRUAVs = ImageIO.read(new FileInputStream("./image/NRUAVs.png"));
            RUAVs = ImageIO.read(new FileInputStream("./image/RUAVs.png"));
            GateWays = ImageIO.read(new FileInputStream("./image/GateWayUAV.png"));
            map = ImageIO.read(new FileInputStream("./image/map.jpeg"));
            cloudOriginal = ImageIO.read(new FileInputStream("./image/cloud.png"));
            cloudBlue = ImageIO.read(new FileInputStream("./image/cloud_blue.png"));
            cloudCopy = ImageIO.read(new FileInputStream("./image/cloud_copy.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
