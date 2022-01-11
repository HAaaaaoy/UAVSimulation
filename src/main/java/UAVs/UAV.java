package UAVs;

import GUItil.GUItil;
import UAVs.network.LinkLayer;
import UAVs.network.NodeIP;
import UAVs.network.Packet;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ThreadLocalRandom;

import image.ImageRead;
import org.apache.log4j.Logger;
import route.Cluster;
import route.ClusterStatus;
import route.RouteTableItem;
import scene.PlaneWars;
import scene.UAVNetwork;
import scene.WirelessChannel;
import text.TextStatus;
import text.UAVsText;


public class UAV extends Thread {

    Logger logger = Logger.getLogger(UAV.class);
    /**
     * 封装无人机的方法和属性
     */
    private final int circle_radius = 30;
    //坐标属性
    public int position_index_x;
    public int position_index_y;
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
    private Point virtualPosition;
    private int ack[] = {1};


    //发送与接收packet的队列
    private CopyOnWriteArrayList<Packet> sentList = new CopyOnWriteArrayList<>();
    private CopyOnWriteArrayList<Packet> receivedList = new CopyOnWriteArrayList<>();


    public Cluster cluster;
    //所加入的cluster
    public CopyOnWriteArrayList clusters = new CopyOnWriteArrayList();
    public ClusterStatus clusterStatus;

    public static int totalUavNumber = 0;
    public WirelessChannel wifi;
    public UAVNetwork uavNetwork;
    //无人机显示文本
    private UAVsText uaVsText;

    //网关或者簇头所用的路由表
    public CopyOnWriteArrayList<RouteTableItem> routeMaps;

    public UAV(int position_index_x, int position_index_y, int UAV_Height, int UAV_Width, BufferedImage UAV_image, int serialID, UAVNetwork uavNetwork) {
        this.position_index_x = position_index_x;
        this.position_index_y = position_index_y;
        this.UAV_Height = UAV_Height;
        this.UAV_Width = UAV_Width;
        this.UAV_image = UAV_image;
        this.serialID = serialID;
//        this.ip = new NodeIP(ip);
        this.uavNetwork = uavNetwork;
        this.wifi = this.uavNetwork.wifi;
        this.linkLayer = new LinkLayer(this.wifi);
        this.uaVsText = new UAVsText(position_index_x, position_index_y, UAV_Height, serialID, this);
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
        g.drawImage(UAV_image, position_index_x ,position_index_y, UAV_Height, UAV_Width, null);
        //绘制线条
//        if (PlaneWars.Linkinfom.get(serialID) != null) {
//            for (int i = 0; i < PlaneWars.Linkinfom.get(serialID).size(); i++) {
//                g.drawLine(position_index_x + UAV_Width / 2, position_index_y + UAV_Width / 2,
//                        PlaneWars.getRuaVs().get(PlaneWars.Linkinfom.get(serialID).get(i)).getPosition_index_x() + UAV_Width / 2,
//                        PlaneWars.getRuaVs().get(PlaneWars.Linkinfom.get(serialID).get(i)).getPosition_index_y() + UAV_Width / 2);
//            }
//        }
        //g.drawOval(position_index_x, position_index_y, 120, 120);
        if (this.cluster != null){
            g.drawOval(position_index_x+60-GUItil.getBounds().height/3,position_index_y+60-GUItil.getBounds().height/3,2*Cluster.clusterRadius,2*Cluster.clusterRadius);
        }


    }


    //是否能够通信
    public int calculateDistance(UAV uav) {
        //求两无人机的中心点
        int u1x = this.position_index_x + this.UAV_Width / 2;
        int u1y = this.position_index_y + this.UAV_Height / 2;
        int u2x = uav.position_index_x + uav.UAV_Width / 2;
        int u2y = uav.position_index_y + uav.UAV_Height / 2;
        return (int) Math.sqrt(Math.abs(u1x - u2x) * Math.abs(u1x - u2x) + Math.abs(u1y - u2y) * Math.abs(u1y - u2y));
    }

    //广播
    public void findClusterMember() {
        CopyOnWriteArrayList<UAV> rUAVs = new CopyOnWriteArrayList<>();
        rUAVs.addAll(uavNetwork.movingList);
        rUAVs.remove(this);
        Iterator<UAV> iterator = rUAVs.iterator();
        while (iterator.hasNext()) {
            UAV uav = iterator.next();
            if (this.calculateDistance(uav) <= cluster.clusterRadius && cluster.getMemberList().size() <= cluster.memberNumber) {
                this.cluster.addClusterMember(uav);
                uavNetwork.notClusterList.remove(uav);
            } else if (this.calculateDistance(uav) <= cluster.clusterRadius && cluster.getMemberList().size() > cluster.memberNumber) {
                break;
            }
        }
    }

    //成为网关
    public void setGateWay(){
        this.clusterStatus = ClusterStatus.GateWay;
        setUAV_image(ImageRead.GateWays);
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

    public Cluster setCluster() {
        this.cluster = new Cluster(this);
        logger.info("At " + PlaneWars.currentTime + ": 第" + serialID + "号无人机成为簇头，簇编号--" + this.cluster.getClusterID());
        this.clusterStatus = ClusterStatus.ClusterHead;
        this.UAV_image = ImageRead.RUAVs;
        this.routeMaps = new CopyOnWriteArrayList<>();
        uavNetwork.notClusterList.remove(this);
        uaVsText.setTextStatus(TextStatus.ClusterHeader);
        uaVsText.setTime(PlaneWars.currentTime);
        uaVsText.setReposition_x(this.position_index_x);
        uaVsText.setReposition_y(this.position_index_y);
        uavNetwork.getTexts().add(uaVsText);
        this.findClusterMember();
        return this.cluster;
    }

    public void joinCluster(Cluster cluster) {
        this.clusters.add(cluster);
    }

    public Cluster getCluster() {
        return this.cluster;
    }




}
