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
import java.util.LinkedList;
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
    public SimulationStatus status = SimulationStatus.FindCluster;
    private int serialID = 1;

    public WirelessChannel wifi = new WirelessChannel();
    private int RIPNumber = 0;
    public static CopyOnWriteArrayList<UAV> communicationList = new CopyOnWriteArrayList<>();

    public int gridCount = 0;
    public int lineCount = 0;


    public final int areaWidth = GUItil.getBounds().width/25;
    public final int areaHeight = GUItil.getBounds().height/15;


    public ArrayList<CloudDraw> cloudDraws = new ArrayList<>();


    public TotalCluster totalCluster;

    public UAVNetwork(int uavNumber) {
        this.uavNumber = uavNumber;
        this.route = new Route();
        this.totalCluster = new TotalCluster(this);
    }


    public void initUAVs() {
        //无人机高度设为120
        int height = 50;
        //无人机宽度设为120
        int width = 50;
        for (int i = 0; i < uavNumber; i++) {
            //在开始划分的时候随机平均
            int m = i % 6;
            logger.info("第" + serialID + "号无人机初始化--IP地址：" + "192.168.192." + serialID);
            UAV uav = new UAV(ThreadLocalRandom.current().nextInt(m * GUItil.getBounds().width / 7, (m + 1) * GUItil.getBounds().width / 7),
                    ThreadLocalRandom.current().nextInt(GUItil.getBounds().height),
                    height, width, ImageRead.NRUAVs, serialID, this);

            notClusterList.add(uav);
            movingList.add(uav);
            serialID++;
            uavHashMap.put(uav.getSerialID(), uav);
        }
    }

    public void initCloud(){
        BufferedImage cloudOriginal = ImageRead.cloudOriginal;
        int x = 0, y = 0;
        while (true) {
            if (x > GUItil.getBounds().width && y>GUItil.getBounds().height) break;
            if (x > GUItil.getBounds().width) {
//                CloudDraw cloudDraw = new CloudDraw(cloudOriginal,x,y);
//                cloudDraws.add(cloudDraw);
                x = 0;
                y += areaHeight;
                if(y>GUItil.getBounds().height) break;
            } else {
                CloudDraw cloudDraw = new CloudDraw(cloudOriginal,x,y);
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
        if(topologyStatus==Topology.Grid){
            if(gridCount<=90)  gridCount++;
            if(gridCount>=90){
                status=SimulationStatus.Cruise;
            }
            else if(gridCount>45) {
                status=SimulationStatus.ForCruise;
                totalCluster.initStartPosition(topologyStatus);
            }
        }
        if(topologyStatus==Topology.Line){
            if(lineCount<=90)  lineCount++;
            if(lineCount>=90){
                status=SimulationStatus.Cruise;
            }
            else if(lineCount>45) {
                status=SimulationStatus.ForCruise;
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
