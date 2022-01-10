package UAVs;

import scene.UAVNetwork;
import scene.WirelessChannel;

import java.awt.image.BufferedImage;

public class RUAV extends UAV{

    //入网（感染）的无人机

    public RUAV(int position_index_x, int position_index_y, int UAV_Height, int UAV_Width, BufferedImage UAV_image, int serialID, UAVNetwork uavNetwork) {
        super(position_index_x, position_index_y, UAV_Height, UAV_Width, UAV_image, serialID, uavNetwork);
    }
}
