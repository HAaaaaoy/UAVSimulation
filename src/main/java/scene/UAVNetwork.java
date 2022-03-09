package scene;

import GUItil.GUItil;
import UAVs.CloudDraw;
import UAVs.UAV;
import UAVs.network.Topology;
import image.ImageRead;
import org.apache.log4j.Logger;
import route.Route;
import route.TotalCluster;
import text.UAVsText;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class UAVNetwork {

    private int uavNumber;
    //保存网络所有无人机的map表
    public static HashMap<Integer, UAV> uavHashMap = new HashMap<>();
    public CopyOnWriteArrayList<UAV> notClusterList = new CopyOnWriteArrayList<>();
    public CopyOnWriteArrayList<UAV> movingList = new CopyOnWriteArrayList<>();
    //仿真时无人机的标注信息
    private CopyOnWriteArrayList<UAVsText> texts = new CopyOnWriteArrayList<>();
    //记录无人机入网时间
    public Topology topologyStatus = Topology.Random;
    public Route route;
    Logger logger = Logger.getLogger(UAVNetwork.class);

    //记录无人机感染的时间
    public HashMap<Integer, Long> timeRecord = new HashMap<>();
    //仿真状态
    public SimulationStatus status = SimulationStatus.OffLine;
    private int serialID = 1;

    public WirelessChannel wifi = new WirelessChannel();
    private int RIPNumber = 0;
    public static CopyOnWriteArrayList<UAV> communicationList = new CopyOnWriteArrayList<>();

    public int gridCount = 0;
    public int lineCount = 0;
    public int circleCount = 0;

    //画图用
    public int meshUavCount = 0;
    public ArrayList<Long> meshTime = new ArrayList<>();


    public int areaWidth;
    public int areaHeight;
    public int width;
    public int height;

    public final int uavHeight = 50;
    public final int uavWidth = 50;

    public ArrayList<CloudDraw> cloudDraws = new ArrayList<>();

    public void setClusterMemberNumber(int clusterMemberNumber) {
        this.clusterMemberNumber = clusterMemberNumber;
    }

    public int clusterMemberNumber = 0;




    public TotalCluster totalCluster;

    public UAVNetwork(int uavNumber) {
        this.uavNumber = uavNumber;
        this.route = new Route();
        this.totalCluster = new TotalCluster(this);
    }


    public void initUAVs() {
        for (int i = 0; i < uavNumber; i++) {
            //在开始划分的时候随机平均
            int m = i % 6;
            logger.info("第" + serialID + "号无人机初始化--IP地址：" + "192.168.192." + serialID);
            UAV uav = new UAV(ThreadLocalRandom.current().nextInt(m * width / 7, (m + 1) * width / 7),
                    ThreadLocalRandom.current().nextInt(height),
                    uavHeight, uavWidth, ImageRead.NRUAVs, serialID, this);

            notClusterList.add(uav);
            movingList.add(uav);
            serialID++;
            uavHashMap.put(uav.getSerialID(), uav);
        }
    }

    public void initCloud() {
        BufferedImage cloudOriginal = ImageRead.cloudOriginal;
        int x = 0, y = 0;
        while (true) {
            if (x > width && y > height) break;
            if (x > width) {
                x = 0;
                y += areaHeight;
                if (y > height) break;
            } else {
                CloudDraw cloudDraw = new CloudDraw(cloudOriginal, x, y);
                cloudDraws.add(cloudDraw);
                x += areaWidth;
            }
        }
        System.out.println(cloudDraws.size());

    }

    //无人机移动
    public void UAVsMoving() {
        Iterator<UAV> iterator = movingList.iterator();
        while (iterator.hasNext()) {
            iterator.next().move(topologyStatus);
        }
        if (topologyStatus == Topology.Grid) {
            if (gridCount <= 550) gridCount++;
            if (gridCount >= 550) {
                status = SimulationStatus.Cruise;
            } else if (gridCount > 450) {
                status = SimulationStatus.ForCruise;
                totalCluster.initStartPosition(topologyStatus);
            }
        }
        if (topologyStatus == Topology.Line) {
            if (lineCount <= 550) lineCount++;
            if (lineCount >= 550) {
                status = SimulationStatus.Cruise;
            } else if (lineCount > 450) {
                status = SimulationStatus.ForCruise;
                totalCluster.initStartPosition(topologyStatus);
            }
        }
        if (topologyStatus == Topology.Circle) {
            if (circleCount <= 550) circleCount++;
            if (circleCount >= 550) {
                status = SimulationStatus.Cruise;
            } else if (circleCount > 450) {
                status = SimulationStatus.ForCruise;
                totalCluster.initStartPosition(topologyStatus);
            }
        }

    }

    public void communicationSim() {
        Iterator<UAV> uavIterator = movingList.iterator();
        while (uavIterator.hasNext()) {
            uavIterator.next().start();
        }
    }

    //生成网络的路由
    public void RIP() {
        while (RIPNumber < 2000) {
            Iterator<UAV> uavIterator = Route.routes.iterator();
            while (uavIterator.hasNext()) {
                UAV uav = uavIterator.next();
                uav.sentSelfRIP();
                uav.D_V();
                RIPNumber++;
            }
        }
        logger.info("网关和簇头" + Route.routes.size());
    }

    public void reRoute() {
        this.notClusterList = new CopyOnWriteArrayList<>();
        this.notClusterList.addAll(movingList);
        Iterator<UAV> uavIterator = notClusterList.iterator();
        while (uavIterator.hasNext()) {
            uavIterator.next().cacheClear();
        }
        RIPNumber = 0;
        this.status = SimulationStatus.FindCluster;

    }


    public CopyOnWriteArrayList<UAVsText> getTexts() {
        return texts;
    }

    public static HashMap<Integer, UAV> getUavHashMap() {
        return uavHashMap;
    }


}
