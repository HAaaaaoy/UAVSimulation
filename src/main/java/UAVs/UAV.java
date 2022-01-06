package UAVs;

import GUItil.GUItil;
import UAVs.network.NodeIP;
import UAVs.network.Packet;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ThreadLocalRandom;

import org.apache.log4j.Logger;


public class UAV extends Thread {

    Logger logger = Logger.getLogger(UAV.class);
    /**
     * 封装无人机的方法和属性
     */
    private final int circle_radius = 30;
    //坐标属性
    private int position_index_x;
    private int position_index_y;
    //UAV大小
    private int UAV_Width;
    private int UAV_Height;
    //每个UAV唯一的编号
    private int serialID;
    //无人机的IP
    private NodeIP ip;

    private int moveStep = 7;
    private int random_move;
    //无人机图标
    BufferedImage UAV_image;
    //虚拟坐标
    private Point virtualPosition = new Point();
    private int ack[] = {1};

    //发送与接收packet的队列
    private CopyOnWriteArrayList<Packet> sentList = new CopyOnWriteArrayList<>();
    private CopyOnWriteArrayList<Packet> receivedList = new CopyOnWriteArrayList<>();

    public static int totalUavNumber = 0;

    public UAV(int position_index_x, int position_index_y, int UAV_Height, int UAV_Width, BufferedImage UAV_image, int serialID, String ip) {
        this.position_index_x = position_index_x;
        this.position_index_y = position_index_y;
        this.UAV_Height = UAV_Height;
        this.UAV_Width = UAV_Width;
        this.UAV_image = UAV_image;
        this.serialID = serialID;
        this.ip = new NodeIP(ip);
    }

    public void move() {
        random_move = ThreadLocalRandom.current().nextInt(1,9);
        try {
            switch (random_move) {
                case 1:
                    this.move_left();
                    break;
                case 2:
                    this.move_right();
                    break;
                case 3:
                    this.move_up();
                    break;
                case 4:
                    this.move_down();
                    break;
                case 5:
                    this.move_left_up();
                    break;
                case 6:
                    this.move_left_down();
                    break;
                case 7:
                    this.move_right_up();
                    break;
                default:
                    this.move_right_down();
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 无人机的随机移动方式
     */
    private void move_right_down() {
        if (!(position_index_x >= GUItil.getBounds().width - UAV_Width || position_index_y >= GUItil.getBounds().height - UAV_Width)) {
            this.position_index_x = position_index_x + moveStep;
            this.position_index_y = position_index_y + moveStep;
        } else {
            this.position_index_x = position_index_x - moveStep;
            this.position_index_y = position_index_y - moveStep;
        }
    }

    private void move_right_up() {
        if (!(position_index_x >= GUItil.getBounds().width - UAV_Width || position_index_y <= 0 + UAV_Width)) {
            this.position_index_x = position_index_x + moveStep;
            this.position_index_y = position_index_y - moveStep;
        } else {
            this.position_index_x = position_index_x - moveStep;
            this.position_index_y = position_index_y + moveStep;
        }
    }

    private void move_left_down() {
        if (!(position_index_x <= 0 + UAV_Width || position_index_y >= GUItil.getBounds().height - UAV_Width)) {
            this.position_index_x = position_index_x - moveStep;
            this.position_index_y = position_index_y + moveStep;
        } else {
            this.position_index_x = position_index_x + moveStep;
            this.position_index_y = position_index_y - moveStep;
        }
    }

    private void move_left_up() {
        if (!(position_index_x <= 0 + UAV_Width || position_index_y <= 0 + UAV_Width)) {
            this.position_index_x = position_index_x - moveStep;
            this.position_index_y = position_index_y - moveStep;
        } else {
            this.position_index_x = position_index_x + moveStep;
            this.position_index_y = position_index_y + moveStep;
        }
    }

    private void move_down() {
        if (!(position_index_y >= GUItil.getBounds().height - UAV_Width)) {
            this.position_index_y = position_index_y + moveStep;
        } else {
            this.position_index_y = position_index_y - moveStep;
        }
    }

    private void move_up() {
        if (!(position_index_y <= 0 + UAV_Width)) {
            this.position_index_y = position_index_y - moveStep;
        } else {
            this.position_index_y = position_index_y + moveStep;
        }
    }

    private void move_right() {
        if (!(position_index_y >= GUItil.getBounds().width - UAV_Width)) {
            this.position_index_x = position_index_x + moveStep;
        } else {
            this.position_index_x = position_index_x - moveStep;
        }
    }

    private void move_left() {
        if (!(position_index_x <= 0 + UAV_Width)) {
            this.position_index_x = position_index_x - moveStep;
        } else {
            this.position_index_x = position_index_x + moveStep;
        }
    }

    /**
     * 获取当前无人机的真实坐标
     */
    public int getPosition_index_x() {
        return position_index_x;
    }

    public void setPosition_index_x(int position_index_x) {
        this.position_index_x = position_index_x;
    }

    public int getPosition_index_y() {
        return position_index_y;
    }

    public void setPosition_index_y(int position_index_y) {
        this.position_index_y = position_index_y;
    }

    public int getUAV_Width() {
        return UAV_Width;
    }

    public void setUAV_Width(int UAV_Width) {
        this.UAV_Width = UAV_Width;
    }

    public int getUAV_Height() {
        return UAV_Height;
    }

    public void setUAV_Height(int UAV_Height) {
        this.UAV_Height = UAV_Height;
    }

    public BufferedImage getUAV_image() {
        return UAV_image;
    }

    public void setUAV_image(BufferedImage UAV_image) {
        this.UAV_image = UAV_image;
    }
}
