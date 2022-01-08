package UAVs;

import GUItil.GUItil;
import UAVs.network.LinkLayer;
import UAVs.network.NodeIP;
import UAVs.network.Packet;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ThreadLocalRandom;

import org.apache.log4j.Logger;
import scene.PlaneWars;
import scene.WirelessChannel;


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
    //链路层
    private LinkLayer linkLayer;

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
    public WirelessChannel wifi;

    public UAV(int position_index_x, int position_index_y, int UAV_Height, int UAV_Width, BufferedImage UAV_image, int serialID, WirelessChannel wifi) {
        this.position_index_x = position_index_x;
        this.position_index_y = position_index_y;
        this.UAV_Height = UAV_Height;
        this.UAV_Width = UAV_Width;
        this.UAV_image = UAV_image;
        this.serialID = serialID;
//        this.ip = new NodeIP(ip);
        this.wifi = wifi;
        this.linkLayer = new LinkLayer(wifi);
    }

    public void move() {
        random_move = ThreadLocalRandom.current().nextInt(1, 9);
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
     * 绘图
     */
    public void drawUAVs(Graphics g) {
        g.drawImage(UAV_image, position_index_x, position_index_y, UAV_Height, UAV_Width, null);
        //绘制线条
//        if (PlaneWars.Linkinfom.get(serialID) != null) {
//            for (int i = 0; i < PlaneWars.Linkinfom.get(serialID).size(); i++) {
//                g.drawLine(position_index_x + UAV_Width / 2, position_index_y + UAV_Width / 2,
//                        PlaneWars.getRuaVs().get(PlaneWars.Linkinfom.get(serialID).get(i)).getPosition_index_x() + UAV_Width / 2,
//                        PlaneWars.getRuaVs().get(PlaneWars.Linkinfom.get(serialID).get(i)).getPosition_index_y() + UAV_Width / 2);
//            }
//        }

        g.drawOval(position_index_x, position_index_y, 120, 120);
    }

    //是否能够通信
    public Boolean isGetMessage(UAV uav) {
        //假设无人机的通信距离是80m;
        int UAV_MAX_recieved = 120;
        //求两无人机的中心点
        int u1x = this.position_index_x + this.UAV_Width / 2;
        int u1y = this.position_index_y + this.UAV_Height / 2;
        int u2x = uav.position_index_x + uav.UAV_Width / 2;
        int u2y = uav.position_index_y + uav.UAV_Height / 2;
        //横、纵、斜距离检测
        return Math.sqrt(Math.abs(u1x - u2x) * Math.abs(u1x - u2x) + Math.abs(u1y - u2y) * Math.abs(u1y - u2y)) <= UAV_MAX_recieved;

    }

    public int calculateDistance(UAV uav){
        //求两无人机的中心点
        int u1x = this.position_index_x + this.UAV_Width / 2;
        int u1y = this.position_index_y + this.UAV_Height / 2;
        int u2x = uav.position_index_x + uav.UAV_Width / 2;
        int u2y = uav.position_index_y + uav.UAV_Height / 2;
        return (int) Math.sqrt(Math.abs(u1x - u2x) * Math.abs(u1x - u2x) + Math.abs(u1y - u2y) * Math.abs(u1y - u2y));
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

    public int getSerialID() {
        return serialID;
    }

    public NodeIP getIp() {
        return ip;
    }
}
