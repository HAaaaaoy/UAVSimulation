package UAVs;

import GUItil.GUItil;
import UAVs.network.LinkLayer;
import UAVs.network.NodeIP;
import UAVs.network.Packet;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ThreadLocalRandom;

import UAVs.network.Topology;
import image.ImageRead;
import org.apache.log4j.Logger;
import route.Cluster;
import route.ClusterStatus;
import route.Route;
import route.RouteTableItem;
import scene.PlaneWars;
import scene.SimulationStatus;
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
    public NodeIP ip;
    //链路层
    public LinkLayer linkLayer;

    private int moveStep = 5;
    private int random_move;
    //无人机图标
    BufferedImage UAV_image;
    public int gridDistance = 50;
    public int lineDistance = 40;
    public int circleDistance = 75;
    public int moveSpeed = 30;
    //虚拟坐标
    private Point virtualPosition;
    private int ack[] = {1};

    public Cluster cluster;
    //所加入的cluster
    public CopyOnWriteArrayList<Cluster> clusters = new CopyOnWriteArrayList();
    public ClusterStatus clusterStatus;

    public static int totalUavNumber = 0;
    public WirelessChannel wifi;
    public UAVNetwork uavNetwork;
    //无人机显示文本
    private UAVsText uaVsText;
    private String packetText = "";

    //网关或者簇头所用的路由表
    public CopyOnWriteArrayList<RouteTableItem> routeTable;
    public Map<UAV, CopyOnWriteArrayList<RouteTableItem>> routeCache = new HashMap<>();
    //与本UAV相连的其他簇头或网关
    public CopyOnWriteArrayList<UAV> communication = new CopyOnWriteArrayList<>();
    public CopyOnWriteArrayList<UAV> routeList = new CopyOnWriteArrayList<>();

    private int countNumber = 0;
    private int receivedUavID = 0;

    public int targetX, targetY;

    public void setClusterNumber(int clusterNumber) {
        this.clusterNumber = clusterNumber;
    }

    public int clusterNumber;




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
        this.linkLayer = new LinkLayer(this.wifi, this);
        this.uaVsText = new UAVsText(position_index_x, position_index_y, UAV_Height, serialID, this);
    }

    public void run() {
        while (true) {
            RouteAndForward();
            try {
                Thread.sleep(150);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (PlaneWars.currentTime % 1000 == 0) {
                if (!this.linkLayer.sentQueue.isEmpty()) {
                    Packet packet = linkLayer.sentQueue.remove();
                    this.linkLayer.send(packet, packet.getNext());
                    logger.info("At " + PlaneWars.currentTime + ": 发送报文- " + packet.packetID);
                }
            }
        }
    }

    public void move(Topology topologyStatus) {
        if (topologyStatus == Topology.Random) {
            moveRandomly();
        } else if (topologyStatus == Topology.Grid) {
            moveGridly();
        } else if (topologyStatus == Topology.Line) {
            moveLinely();
        } else if (topologyStatus == Topology.Circle) {
            moveCircly();
        }
    }


    public void moveRandomly() {
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

    public void moveGridly() {
        if (clusterStatus == ClusterStatus.ClusterHead) {
            if (uavNetwork.status == SimulationStatus.Communicate) {
                moveRandomly();
                if (uavNetwork.gridCount >= 300) {
                    for (int i = 0; i < this.cluster.getMemberList().size(); i++) {
                        UAV member = UAVNetwork.uavHashMap.get(cluster.getMemberList().get(i));
                        member.memberMoveGridly();
                    }
                }
            } else if (uavNetwork.status == SimulationStatus.ForCruise) {
                position_index_x = position_index_x + (targetX - position_index_x) / moveSpeed;
                position_index_y = position_index_y + (targetY - position_index_y) / moveSpeed;
                for (int i = 0; i < this.cluster.getMemberList().size(); i++) {
                    UAV member = UAVNetwork.uavHashMap.get(cluster.getMemberList().get(i));
                    member.memberMoveGridly();
                }
            } else if (uavNetwork.status == SimulationStatus.Cruise) {
                position_index_x = targetX;
                position_index_y = targetY;
                for (int i = 0; i < this.cluster.getMemberList().size(); i++) {
                    UAV member = UAVNetwork.uavHashMap.get(cluster.getMemberList().get(i));
                    member.memberMoveGridly();
                }
            }
        } else {
            if (uavNetwork.gridCount <= 300) {
                int position = clusters.get(0).getMemberList().indexOf((Integer) this.serialID);
                UAV head = clusters.get(0).clusterHead;
                switch (position) {
                    case 0:
                        if (calculateDistance(head) <= gridDistance) {
                            position_index_x = head.position_index_x;
                            position_index_y = head.position_index_y - gridDistance;
                        } else {
                            position_index_x = position_index_x + (head.position_index_x - position_index_x) / moveSpeed;
                            position_index_y = position_index_y + (head.position_index_y - gridDistance - position_index_y) / moveSpeed;
                        }
                        break;
                    case 1:
                        if (calculateDistance(head) <= gridDistance) {
                            position_index_x = head.position_index_x;
                            position_index_y = head.position_index_y + gridDistance;
                        } else {
                            position_index_x = position_index_x + (head.position_index_x - position_index_x) / moveSpeed;
                            position_index_y = position_index_y + (head.position_index_y + gridDistance - position_index_y) / moveSpeed;
                        }
                        break;
                    case 2:
                        if (calculateDistance(head) <= gridDistance) {
                            position_index_x = head.position_index_x - gridDistance;
                            position_index_y = head.position_index_y;
                        } else {
                            position_index_x = position_index_x + (head.position_index_x - gridDistance - position_index_x) / moveSpeed;
                            position_index_y = position_index_y + (head.position_index_y - position_index_y) / moveSpeed;
                        }
                        break;
                    case 3:
                        if (calculateDistance(head) <= gridDistance) {
                            position_index_x = head.position_index_x + gridDistance;
                            position_index_y = head.position_index_y;
                        } else {
                            position_index_x = position_index_x + (head.position_index_x + gridDistance - position_index_x) / moveSpeed;
                            position_index_y = position_index_y + (head.position_index_y - position_index_y) / moveSpeed;
                        }
                        break;
                    case 4:
                        if (calculateDistance(head) <= gridDistance) {
                            position_index_x = head.position_index_x - gridDistance;
                            position_index_y = head.position_index_y - gridDistance;
                        } else {
                            position_index_x = position_index_x + (head.position_index_x - gridDistance - position_index_x) / moveSpeed;
                            position_index_y = position_index_y + (head.position_index_y - gridDistance - position_index_y) / moveSpeed;
                        }
                        break;
                    case 5:
                        if (calculateDistance(head) <= gridDistance) {
                            position_index_x = head.position_index_x + gridDistance;
                            position_index_y = head.position_index_y - gridDistance;
                        } else {
                            position_index_x = position_index_x + (head.position_index_x + gridDistance - position_index_x) / moveSpeed;
                            position_index_y = position_index_y + (head.position_index_y - gridDistance - position_index_y) / moveSpeed;
                        }
                        break;
                    case 6:
                        if (calculateDistance(head) <= gridDistance) {
                            position_index_x = head.position_index_x - gridDistance;
                            position_index_y = head.position_index_y + gridDistance;
                        } else {
                            position_index_x = position_index_x + (head.position_index_x - gridDistance - position_index_x) / moveSpeed;
                            position_index_y = position_index_y + (head.position_index_y + gridDistance - position_index_y) / moveSpeed;
                        }
                        break;

                    case 7:
                        if (calculateDistance(head) <= gridDistance) {
                            position_index_x = head.position_index_x + gridDistance;
                            position_index_y = head.position_index_y + gridDistance;
                        } else {
                            position_index_x = position_index_x + (head.position_index_x + gridDistance - position_index_x) / moveSpeed;
                            position_index_y = position_index_y + (head.position_index_y + gridDistance - position_index_y) / moveSpeed;
                        }
                        break;
                }
            }
        }
    }

    public void memberMoveGridly() {
        int position = clusters.get(0).getMemberList().indexOf((Integer) this.serialID);
        UAV head = clusters.get(0).clusterHead;
        switch (position) {
            case 0:
                position_index_x = head.position_index_x;
                position_index_y = head.position_index_y - gridDistance;
                break;
            case 1:
                position_index_x = head.position_index_x;
                position_index_y = head.position_index_y + gridDistance;
                break;
            case 2:
                position_index_x = head.position_index_x - gridDistance;
                position_index_y = head.position_index_y;
                break;
            case 3:
                position_index_x = head.position_index_x + gridDistance;
                position_index_y = head.position_index_y;
                break;
            case 4:
                position_index_x = head.position_index_x - gridDistance;
                position_index_y = head.position_index_y - gridDistance;
                break;
            case 5:
                position_index_x = head.position_index_x + gridDistance;
                position_index_y = head.position_index_y - gridDistance;
                break;
            case 6:
                position_index_x = head.position_index_x - gridDistance;
                position_index_y = head.position_index_y + gridDistance;
                break;
            case 7:
                position_index_x = head.position_index_x + gridDistance;
                position_index_y = head.position_index_y + gridDistance;
                break;
        }
    }

    public void moveLinely() {
        if (clusterStatus == ClusterStatus.ClusterHead) {
            if (uavNetwork.status == SimulationStatus.Communicate) {
                moveRandomly();
                if (uavNetwork.lineCount >= 300) {
                    for (int i = 0; i < this.cluster.getMemberList().size(); i++) {
                        UAV member = UAVNetwork.uavHashMap.get(cluster.getMemberList().get(i));
                        member.memberMoveLinely();
                    }
                }
            } else if (uavNetwork.status == SimulationStatus.ForCruise) {
                position_index_x = position_index_x + (targetX - position_index_x) / moveSpeed;
                position_index_y = position_index_y + (targetY - position_index_y) / moveSpeed;
                for (int i = 0; i < this.cluster.getMemberList().size(); i++) {
                    UAV member = UAVNetwork.uavHashMap.get(cluster.getMemberList().get(i));
                    member.memberMoveLinely();
                }
            } else if (uavNetwork.status == SimulationStatus.Cruise) {
                position_index_x = targetX;
                position_index_y = targetY;
                for (int i = 0; i < this.cluster.getMemberList().size(); i++) {
                    UAV member = UAVNetwork.uavHashMap.get(cluster.getMemberList().get(i));
                    member.memberMoveLinely();
                }
            }
        } else {
            if (uavNetwork.lineCount <= 300) {
                int position = clusters.get(0).getMemberList().indexOf((Integer) this.serialID);
                UAV head = clusters.get(0).clusterHead;
                if (calculateDistance(head.position_index_x + (position + 1) * lineDistance, head.position_index_y) <= lineDistance) {
                    position_index_x = head.position_index_x + (position + 1) * lineDistance;
                    position_index_y = head.position_index_y;
                } else {
                    position_index_x = position_index_x + (head.position_index_x + lineDistance - position_index_x) / moveSpeed;
                    position_index_y = position_index_y + (head.position_index_y - lineDistance - position_index_y) / moveSpeed;
                }
            }
        }
    }

    public void memberMoveLinely() {
        int position = clusters.get(0).getMemberList().indexOf((Integer) this.serialID);
        UAV head = clusters.get(0).clusterHead;
        position_index_x = head.position_index_x + (position + 1) * lineDistance;
        position_index_y = head.position_index_y;
    }


    public void moveCircly() {
        if (clusterStatus == ClusterStatus.ClusterHead) {
            if (uavNetwork.status == SimulationStatus.Communicate) {
                moveRandomly();
                if (uavNetwork.circleCount >= 300) {
                    for (int i = 0; i < this.cluster.getMemberList().size(); i++) {
                        UAV member = UAVNetwork.uavHashMap.get(cluster.getMemberList().get(i));
                        member.memberMoveCircly();
                    }
                }
            } else if (uavNetwork.status == SimulationStatus.ForCruise) {
                position_index_x = position_index_x + (targetX - position_index_x) / moveSpeed;
                position_index_y = position_index_y + (targetY - position_index_y) / moveSpeed;
                for (int i = 0; i < this.cluster.getMemberList().size(); i++) {
                    UAV member = UAVNetwork.uavHashMap.get(cluster.getMemberList().get(i));
                    member.memberMoveCircly();
                }
            } else if (uavNetwork.status == SimulationStatus.Cruise) {
                position_index_x = targetX;
                position_index_y = targetY;
                for (int i = 0; i < this.cluster.getMemberList().size(); i++) {
                    UAV member = UAVNetwork.uavHashMap.get(cluster.getMemberList().get(i));
                    member.memberMoveCircly();
                }
            }
        } else {
            if (uavNetwork.circleCount <= 300) {
                int position = clusters.get(0).getMemberList().indexOf((Integer) this.serialID);
                UAV head = clusters.get(0).clusterHead;
                switch (position) {
                    case 0:
                        if (calculateDistance(head) <= circleDistance) {
                            position_index_x = head.position_index_x;
                            position_index_y = head.position_index_y - circleDistance;
                        } else {
                            position_index_x = position_index_x + (head.position_index_x - position_index_x) / moveSpeed;
                            position_index_y = position_index_y + (head.position_index_y - circleDistance - position_index_y) / moveSpeed;
                        }
                        break;
                    case 1:
                        if (calculateDistance(head) <= circleDistance) {
                            position_index_x = head.position_index_x;
                            position_index_y = head.position_index_y + circleDistance;
                        } else {
                            position_index_x = position_index_x + (head.position_index_x - position_index_x) / moveSpeed;
                            position_index_y = position_index_y + (head.position_index_y + circleDistance - position_index_y) / moveSpeed;
                        }
                        break;
                    case 2:
                        if (calculateDistance(head) <= circleDistance) {
                            position_index_x = head.position_index_x - circleDistance;
                            position_index_y = head.position_index_y;
                        } else {
                            position_index_x = position_index_x + (head.position_index_x - circleDistance - position_index_x) / moveSpeed;
                            position_index_y = position_index_y + (head.position_index_y - position_index_y) / moveSpeed;
                        }
                        break;
                    case 3:
                        if (calculateDistance(head) <= circleDistance) {
                            position_index_x = head.position_index_x + circleDistance;
                            position_index_y = head.position_index_y;
                        } else {
                            position_index_x = position_index_x + (head.position_index_x + circleDistance - position_index_x) / moveSpeed;
                            position_index_y = position_index_y + (head.position_index_y - position_index_y) / moveSpeed;
                        }
                        break;
                    case 4:
                        if (calculateDistance(head) <= circleDistance) {
                            position_index_x = head.position_index_x - (int) (circleDistance / 1.5);
                            position_index_y = head.position_index_y - (int) (circleDistance / 1.5);
                        } else {
                            position_index_x = position_index_x + (head.position_index_x - (int) (circleDistance / 1.5) - position_index_x) / moveSpeed;
                            position_index_y = position_index_y + (head.position_index_y - (int) (circleDistance / 1.5) - position_index_y) / moveSpeed;
                        }
                        break;
                    case 5:
                        if (calculateDistance(head) <= circleDistance) {
                            position_index_x = head.position_index_x + (int) (circleDistance / 1.5);
                            position_index_y = head.position_index_y - (int) (circleDistance / 1.5);
                        } else {
                            position_index_x = position_index_x + (head.position_index_x + (int) (circleDistance / 1.5) - position_index_x) / moveSpeed;
                            position_index_y = position_index_y + (head.position_index_y - (int) (circleDistance / 1.5) - position_index_y) / moveSpeed;
                        }
                        break;
                    case 6:
                        if (calculateDistance(head) <= circleDistance) {
                            position_index_x = head.position_index_x - (int) (circleDistance / 1.5);
                            position_index_y = head.position_index_y + (int) (circleDistance / 1.5);
                        } else {
                            position_index_x = position_index_x + (head.position_index_x - (int) (circleDistance / 1.5) - position_index_x) / moveSpeed;
                            position_index_y = position_index_y + (head.position_index_y + (int) (circleDistance / 1.5) - position_index_y) / moveSpeed;
                        }
                        break;

                    case 7:
                        if (calculateDistance(head) <= circleDistance) {
                            position_index_x = head.position_index_x + (int) (circleDistance / 1.5);
                            position_index_y = head.position_index_y + (int) (circleDistance / 1.5);
                        } else {
                            position_index_x = position_index_x + (head.position_index_x + (int) (circleDistance / 1.5) - position_index_x) / moveSpeed;
                            position_index_y = position_index_y + (head.position_index_y + (int) (circleDistance / 1.5) - position_index_y) / moveSpeed;
                        }
                        break;
                }
            }
        }
    }

    public void memberMoveCircly() {
        int position = clusters.get(0).getMemberList().indexOf((Integer) this.serialID);
        UAV head = clusters.get(0).clusterHead;
        switch (position) {
            case 0:
                position_index_x = head.position_index_x;
                position_index_y = head.position_index_y - circleDistance;
                break;
            case 1:
                position_index_x = head.position_index_x;
                position_index_y = head.position_index_y + circleDistance;
                break;
            case 2:
                position_index_x = head.position_index_x - circleDistance;
                position_index_y = head.position_index_y;
                break;
            case 3:
                position_index_x = head.position_index_x + circleDistance;
                position_index_y = head.position_index_y;
                break;
            case 4:
                position_index_x = head.position_index_x - (int) (circleDistance / 1.5);
                position_index_y = head.position_index_y - (int) (circleDistance / 1.5);
                break;
            case 5:
                position_index_x = head.position_index_x + (int) (circleDistance / 1.5);
                position_index_y = head.position_index_y - (int) (circleDistance / 1.5);
                break;
            case 6:
                position_index_x = head.position_index_x - (int) (circleDistance / 1.5);
                position_index_y = head.position_index_y + (int) (circleDistance / 1.5);
                break;
            case 7:
                position_index_x = head.position_index_x + (int) (circleDistance / 1.5);
                position_index_y = head.position_index_y + (int) (circleDistance / 1.5);
                break;
        }
    }


    /**
     * 绘图
     */
    public void drawUAVs(Graphics g) {
        g.drawImage(UAV_image, position_index_x, position_index_y, UAV_Height, UAV_Width, null);
        if (this.cluster != null) {
            if (uavNetwork.status.equals(SimulationStatus.FindCluster) || uavNetwork.status.equals(SimulationStatus.Route)) {
                g.drawOval(position_index_x + UAV_Height / 2 - GUItil.getBounds().height / 2, position_index_y + UAV_Width / 2 - GUItil.getBounds().height / 2, 2 * Cluster.clusterRadius, 2 * Cluster.clusterRadius);
            }
        }
        if (countNumber > 0) {
            showReceivePacket(g);
            countNumber--;
            if (countNumber < 0) {
                this.routeList.clear();
                countNumber = 0;
            }
        }
    }

    public void showReceivePacket(Graphics g) {
        try {
            g.drawString(packetText, position_index_x - 10, position_index_y - 5);
            for (int i = 0; i < routeList.size() - 1; i++) {
                UAV src = routeList.get(i);
                UAV dst = routeList.get(i + 1);
                g.drawLine(src.position_index_x + UAV_Width / 2, src.position_index_y + UAV_Width / 2,
                        dst.position_index_x + UAV_Width / 2,
                        dst.position_index_y + UAV_Width / 2);
            }
        } catch (Exception e) {
            e.printStackTrace();
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


    public int calculateDistance(int x, int y) {
        //求两无人机的中心点
        int u1x = this.position_index_x;
        int u1y = this.position_index_y;
        int u2x = x;
        int u2y = y;
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
            //修正误差
            if (this.calculateDistance(uav) <= cluster.clusterRadius + 100 && cluster.getMemberList().size() < cluster.memberNumber) {
                if (this.cluster.addClusterMember(uav)) {
                    uavNetwork.notClusterList.remove(uav);
                }
            } else if (this.calculateDistance(uav) <= cluster.clusterRadius + 100 && cluster.getMemberList().size() >= cluster.memberNumber) {
                break;
            }
        }
    }

    //成为网关
    public void setGateWay() {
        this.clusterStatus = ClusterStatus.GateWay;
        uaVsText.setTextStatus(TextStatus.GateWay);
        uaVsText.setTime(PlaneWars.currentTime);
        uaVsText.setReposition_x(this.position_index_x);
        uaVsText.setReposition_y(this.position_index_y);
        uavNetwork.getTexts().add(uaVsText);
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
        if(uavNetwork.clusterMemberNumber!=0){
            this.cluster.memberNumber = uavNetwork.clusterMemberNumber;
        }
        uavNetwork.totalCluster.clusters.add(this.cluster);
        logger.info("At " + PlaneWars.currentTime + ": 第" + serialID + "号无人机成为簇头，簇编号--" + this.cluster.getClusterID());
        this.clusterStatus = ClusterStatus.ClusterHead;
        this.UAV_image = ImageRead.RUAVs;
        this.routeTable = new CopyOnWriteArrayList<>();
        this.routeCache = new ConcurrentHashMap<>();
        this.communication = new CopyOnWriteArrayList<>();
        Route.routes.add(this);
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

    /**
     * 路由相关
     * */

    /**
     * 添加从网络获取的由邻接路由器发送的RIP报文集合
     *
     * @param nRIPCache
     */
    public synchronized void putRIPCache(Map<UAV, CopyOnWriteArrayList<RouteTableItem>> nRIPCache) {
        //此处不可直接对nRIPCache使用进行RIPCache.putAll拷贝，此时拷贝的是由各个路由器发送路由表的实体项
        //D_V算法中的表项修改操作会直接修改原路由器路由表项对象导致莫名其妙的错误
        for (Map.Entry<UAV, CopyOnWriteArrayList<RouteTableItem>> entry : nRIPCache.entrySet()) {
            //Key--路由器名
            //Value--对应路由表
            CopyOnWriteArrayList<RouteTableItem> sentRoutingList = entry.getValue();
            //复制路由表
            CopyOnWriteArrayList<RouteTableItem> copyRoutingList = new CopyOnWriteArrayList<RouteTableItem>();
            for (RouteTableItem sitem : sentRoutingList) {
                copyRoutingList.add(new RouteTableItem(sitem));
            }
            routeCache.put(entry.getKey(), copyRoutingList);
        }

    }

    /**
     * 发送获取RIP报文至邻接网络的RIPCache
     * 注：此方法调用网络类的putRIPCache方法，将本路由器路由表封装成RIP报文发送至本路由器邻接的路由器
     */
    public void sentSelfRIP() {
        //遍历直连网络列表
        for (UAV uav : communication) {
            //发送本路由器routingList至临接的路由器
            //此处应该发送复制后的routerTable
            //复制路由表
            CopyOnWriteArrayList<RouteTableItem> copyRouterTable = new CopyOnWriteArrayList<RouteTableItem>();
            for (RouteTableItem item : routeTable) {
                copyRouterTable.add(new RouteTableItem(item));
            }
            Map<UAV, CopyOnWriteArrayList<RouteTableItem>> map = new ConcurrentHashMap<>();
            map.put(this, copyRouterTable);
            uav.putRIPCache(map);
        }
    }

    /**
     * RIP距离向量算法
     * 注：需将RIPCache中所有RIP报文进行算法处理，D_V算法需独占RIPCache,routingList
     */
    public synchronized void D_V() {
        //遍历未处理RIP报文集合
        for (Map.Entry<UAV, CopyOnWriteArrayList<RouteTableItem>> entry : routeCache.entrySet()) {
            //Key--路由器
            UAV sentUav = entry.getKey();
            //Value--对应路由器的路由表
            CopyOnWriteArrayList<RouteTableItem> sentRouteTable = entry.getValue();
            for (RouteTableItem sitem : sentRouteTable) {
                //路由表处理
                //置下一跳路由器为发送路由其
                sitem.setNext(sentUav.getSerialID());
                //跳数加1
                //sitem.increaseHopNum(this.calculateDistance(sentUav));
                sitem.increaseHopNum();
                //存储目的网络相同的routingListItem'Index
                int index = -1;
                for (int i = 0; i < routeTable.size(); i++) {
                    RouteTableItem titem = routeTable.get(i);
                    //判断routingList是否存在与该项目的网络相同的项，有则置index为i并跳出循环
                    if (titem.equalsDst(sitem)) {
                        index = i;
                        break;
                    }
                }
                //routingList存在与该项目的网络相同的项
                if (index != -1) {
                    RouteTableItem titem = routeTable.get(index);
                    //判断是否有相同下一跳路由器
                    if (titem.equalsNextRoute(sitem)) {
                        //有相同下一跳路由器
                        //替换原有Item
                        if (communication.contains(UAVNetwork.uavHashMap.get(titem.getNext()))) {

                            //直连路由器不更新
                        } else {
                            routeTable.remove(index);
                            routeTable.add(sitem);
                        }
                    } else {
                        //比较跳数
                        if (sitem.getHopNum() < titem.getHopNum()) {
                            //跳数小于原有项
                            //替换原有Item
                            routeTable.remove(index);
                            routeTable.add(sitem);
                        }
                    }
                }
                //不存在--添加该项目
                else {
                    routeTable.add(sitem);
                }
            }
        }
        //处理完成后清空RIPCache
        countNumber++;
        routeCache.clear();
    }

    public void printRouterTable() {
        logger.info("开始打印第" + this.serialID + "号无人机的路由表，长度： " + routeTable.size());
        for (RouteTableItem item : routeTable) {
            logger.info(item.getDst() + "---" + item.getNext() + "---" + item.getHopNum());
        }
    }

    public void RouteAndForward() {
        if (!this.linkLayer.receiveQueue.isEmpty()) {
            Packet packet = linkLayer.receiveQueue.remove();
            if (packet.getDst() == this.serialID) {
                receivedUavID = packet.getSrc();
                countNumber = 100;
                packetText = "At" + PlaneWars.currentTime + this.serialID + "收到来自" + packet.getSrc() + "的报文，ID为" + packet.packetID + "时延" + (PlaneWars.currentTime - packet.getCreatTime());
                packet.routeList.add(this);
                routeList = packet.routeList;
                logger.info("At" + PlaneWars.currentTime + this.serialID + "收到来自" + packet.getSrc() + "的报文，ID为" + packet.packetID + "时延" + (PlaneWars.currentTime - packet.getCreatTime()));
                logger.info("此报文经过的路由为： " + this.routeList);
            } else {
                Boolean isRouted = true;
                Iterator<UAV> uavIterator = communication.iterator();
                while (uavIterator.hasNext()) {
                    UAV uav = uavIterator.next();
                    if (uav.serialID == packet.getDst()) {
                        packet.setNext(uav.serialID);
                        this.linkLayer.addSentQueue(packet);
                        isRouted = false;
                        break;
                    }
                }

                if (isRouted) {
                    int nextRouter = 0;
                    //true说明需要路由转发,遍历本路由器的路由表routerTable
                    for (RouteTableItem routeTableItem : this.routeTable) {
                        if (packet.getDst() == routeTableItem.getDst()) {
                            //目的网段相同
                            nextRouter = routeTableItem.getNext();
                            break;
                        }
                    }
                    if (nextRouter != 0) {
                        packet.setNext(nextRouter);
                        logger.info("At " + PlaneWars.currentTime + "-" + this.serialID + ": 转发报文--目的地址 " + nextRouter + " 报文ID" + packet.packetID);
                        this.linkLayer.addSentQueue(packet);
                        //this.linkLayer.send(packet);
                    }

                }
            }
        }
    }


    public void generatePacket(int dst) {
        Packet packet = new Packet(this.serialID, dst, Math.toIntExact(PlaneWars.currentTime));
        logger.info("At " + PlaneWars.currentTime + "-" + this.serialID + ": 生成报文--目的地址 " + dst + " 报文ID" + packet.packetID);
        if (this.clusterStatus == ClusterStatus.ClusterMember) {
            packet.setNext(this.clusters.get(0).clusterHead.serialID);
        } else {
            this.linkLayer.addReceiveQueue(packet);
        }

    }

    public void cacheClear() {
        routeTable.clear();
        routeCache.clear();
        communication.clear();
        routeList.clear();
        clusters.clear();
        cluster = null;
        clusterStatus = null;

    }


}
