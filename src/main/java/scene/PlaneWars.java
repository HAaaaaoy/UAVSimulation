package scene;

import GUItil.GUItil;
import UAVs.CloudDraw;
import UAVs.UAV;
import UAVs.network.Topology;
import org.apache.log4j.Logger;
import route.Route;
import text.UAVsText;

import javax.swing.*;
import java.awt.*;
import java.util.Iterator;

public class PlaneWars extends JPanel {

    Logger logger = Logger.getLogger(PlaneWars.class);
    private int backgroundmove = 0;
    public static Long currentTime = 0L;
    //仿真实现
    public UAVNetwork uavNetwork;
    private Boolean runningFlag = true;


    public final int areaWidth = GUItil.getBounds().width/25;
    public final int areaHeight = GUItil.getBounds().height/15;


    public PlaneWars() {
        uavNetwork = new UAVNetwork(64);
    }


    public void board() {
        this.removeAll();
    }

    public void startSimulation() {
        board();
        Thread Simulation = new Thread() {
            @Override
            public void run() {
                uavNetwork.initUAVs();
                uavNetwork.initCloud();
                while (runningFlag) {
                    //入网过程
                    if (uavNetwork.status.equals(SimulationStatus.FindCluster)) {
                        uavNetwork.UAVsMoving();
                        if (uavNetwork.notClusterList.size() > 0 && currentTime % 2000 == 0) {
                            uavNetwork.route.selectedCluster(uavNetwork.notClusterList.get(0));
                        } else if (uavNetwork.notClusterList.size() == 0) {
                            //簇建立后路由
                            uavNetwork.status = SimulationStatus.Route;
                        }
                        try {
                            Thread.sleep(50);
                            currentTime += 50;
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    } else if (uavNetwork.status.equals(SimulationStatus.Route)) {
                        uavNetwork.UAVsMoving();
                        uavNetwork.RIP();
                        for (UAV uav : Route.routes) {
                            uav.printRouterTable();
                        }
                        uavNetwork.communicationSim();
                        uavNetwork.status = SimulationStatus.Communicate;
                        uavNetwork.topologyStatus = Topology.Line;
                        try {
                            Thread.sleep(80);
                            currentTime += 80;
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    } else if (uavNetwork.status.equals(SimulationStatus.Communicate)) {
                        uavNetwork.UAVsMoving();
                        /**
                         * 暂时不产生随机报文
                         * */
//                        if(currentTime % 1000 == 0){
//                            int i = ThreadLocalRandom.current().nextInt(1,50);
//                            int j = ThreadLocalRandom.current().nextInt(1,50);
//                            if (!(i == j)){
//                                UAVNetwork.getUavHashMap().get(i).generatePacket(j);
//                            }
//                        }

                        try {
                            Thread.sleep(50);
                            currentTime += 50;
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }else if(uavNetwork.status.equals(SimulationStatus.ForCruise)){
                        uavNetwork.UAVsMoving();
                        try {
                            Thread.sleep(50);
                            currentTime += 50;
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }else if(uavNetwork.status.equals(SimulationStatus.Cruise)){
                        uavNetwork.UAVsMoving();
                        uavNetwork.totalCluster.clusterMove();
                        try {
                            Thread.sleep(60);
                            currentTime += 60;
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                    }
                    repaint();
                }
            }
        };
        Simulation.start();
    }


    //绘制所有无人机
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        drawBackGround(g);
        paintUAVs(g);
        drawText(g);
    }

    public void paintUAVs(Graphics g) {
        Iterator<UAV> uavIterator = uavNetwork.movingList.iterator();
        while (uavIterator.hasNext()) {
            uavIterator.next().drawUAVs(g);
        }

    }

    public void drawText(Graphics g) {
        Iterator<UAVsText> iterator = uavNetwork.getTexts().iterator();
        while (iterator.hasNext()) {
            iterator.next().drawText(g);
        }

    }

    public void drawBackGround(Graphics g) {
        Iterator<CloudDraw> iterator = uavNetwork.cloudDraws.iterator();
        while (iterator.hasNext()) {
            CloudDraw cloudDraw = iterator.next();
            g.drawImage(cloudDraw.image,cloudDraw.x, cloudDraw.y,areaWidth,areaHeight,this);
        }


    }

    private void setRunningFlag(Boolean flag) {
        this.runningFlag = flag;
    }

}
