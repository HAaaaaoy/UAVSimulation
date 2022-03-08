package scene;

import GUItil.GUItil;
import UAVs.CloudDraw;
import UAVs.UAV;
import UAVs.network.Topology;
import org.apache.log4j.Logger;
import route.Cluster;
import route.Route;
import text.UAVsText;

import javax.swing.*;
import java.awt.*;
import java.util.Iterator;
import java.util.concurrent.ThreadLocalRandom;

public class PlaneWars extends JPanel {

    Logger logger = Logger.getLogger(PlaneWars.class);
    private int backgroundmove = 0;
    public static Long currentTime = 0L;
    public Long meshTime = 0L;
    //仿真实现
    public UAVNetwork uavNetwork;
    public Boolean runningFlag = false;

    public int areaWidth;
    public int areaHeight;
    public int width;
    public int height;

    public Topology topologyStatus;

    Thread simulation;


    public PlaneWars() {
        this.uavNetwork = new UAVNetwork(64);
    }

    public void setNetwork(UAVNetwork uavNetwork) {
        this.uavNetwork = uavNetwork;
    }

    public void board() {
        this.removeAll();
        areaWidth = width / 25;
        areaHeight = height / 15;
        uavNetwork.areaWidth = width / 25;
        uavNetwork.areaHeight = height / 15;
    }

    public void setSize(int x, int y) {
        width = x;
        height = y;
        uavNetwork.width = x;
        uavNetwork.height = y;
    }

    public void startSimulation() {
        board();
        runningFlag = true;
        simulation.start();
    }

    public void stopSimulation() {
        simulation.interrupt();
    }

    public void initThread() {
        if (topologyStatus.equals(Topology.Grid) && uavNetwork.status.equals(SimulationStatus.FindCluster)) {
            formationGrid();
        } else if (topologyStatus.equals(Topology.Line) && uavNetwork.status.equals(SimulationStatus.FindCluster)) {
            formationLine();
        } else if (topologyStatus.equals(Topology.Circle) && uavNetwork.status.equals(SimulationStatus.FindCluster)) {
            formationCircle();
        }
    }


    public void initThreads() {
        simulation = new Thread() {
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
                            for (int i = 0; i < 8; i++) {
                                meshTime += ThreadLocalRandom.current().nextLong(250, 308);
                                uavNetwork.meshTime.add(meshTime);
                            }
                        } else if (uavNetwork.notClusterList.size() == 0) {
                            //簇建立后路由
                            uavNetwork.status = SimulationStatus.Route;
                            System.out.println(uavNetwork.meshTime);
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
                        uavNetwork.topologyStatus = topologyStatus;
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
                    } else if (uavNetwork.status.equals(SimulationStatus.ForCruise)) {
                        uavNetwork.UAVsMoving();
                        try {
                            Thread.sleep(50);
                            currentTime += 50;
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    } else if (uavNetwork.status.equals(SimulationStatus.Cruise)) {
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
    }


    //仿真的多种实现
    // 生成随机拓扑，然后编队
    public void formationGrid() {
        simulation = new Thread() {
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
                            for (int i = 0; i < 8; i++) {
                                meshTime += ThreadLocalRandom.current().nextLong(250, 308);
                                uavNetwork.meshTime.add(meshTime);
                            }
                        } else if (uavNetwork.notClusterList.size() == 0) {
                            //簇建立后路由
                            uavNetwork.status = SimulationStatus.Route;
                            System.out.println(uavNetwork.meshTime);
                        }
                        try {
                            Thread.sleep(50);
                            currentTime += 50;
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    } else if (uavNetwork.status.equals(SimulationStatus.Route)) {
                        uavNetwork.UAVsMoving();
                        uavNetwork.status = SimulationStatus.Communicate;
                        uavNetwork.topologyStatus = topologyStatus;
                        try {
                            Thread.sleep(80);
                            currentTime += 80;
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    } else if (uavNetwork.status.equals(SimulationStatus.Communicate)) {
                        uavNetwork.UAVsMoving();
                        try {
                            Thread.sleep(50);
                            currentTime += 50;
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    } else if (uavNetwork.status.equals(SimulationStatus.ForCruise)) {
                        runningFlag = false;
                    }
                    repaint();
                }
            }
        };
    }

    public void formationLine() {
        simulation = new Thread() {
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
                            for (int i = 0; i < 8; i++) {
                                meshTime += ThreadLocalRandom.current().nextLong(250, 308);
                                uavNetwork.meshTime.add(meshTime);
                            }
                        } else if (uavNetwork.notClusterList.size() == 0) {
                            //簇建立后路由
                            uavNetwork.status = SimulationStatus.Route;
                            System.out.println(uavNetwork.meshTime);
                        }
                        try {
                            Thread.sleep(50);
                            currentTime += 50;
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    } else if (uavNetwork.status.equals(SimulationStatus.Route)) {
                        uavNetwork.UAVsMoving();
                        uavNetwork.status = SimulationStatus.Communicate;
                        uavNetwork.topologyStatus = topologyStatus;
                        try {
                            Thread.sleep(80);
                            currentTime += 80;
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    } else if (uavNetwork.status.equals(SimulationStatus.Communicate)) {
                        uavNetwork.UAVsMoving();
                        try {
                            Thread.sleep(50);
                            currentTime += 50;
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    } else if (uavNetwork.status.equals(SimulationStatus.ForCruise)) {
                        runningFlag = false;
                    }
                    repaint();
                }
            }
        };
    }

    public void formationCircle() {
        simulation = new Thread() {
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
                            for (int i = 0; i < 8; i++) {
                                meshTime += ThreadLocalRandom.current().nextLong(250, 308);
                                uavNetwork.meshTime.add(meshTime);
                            }
                        } else if (uavNetwork.notClusterList.size() == 0) {
                            //簇建立后路由
                            uavNetwork.status = SimulationStatus.Route;
                            System.out.println(uavNetwork.meshTime);
                        }
                        try {
                            Thread.sleep(50);
                            currentTime += 50;
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    } else if (uavNetwork.status.equals(SimulationStatus.Route)) {
                        uavNetwork.UAVsMoving();
                        uavNetwork.status = SimulationStatus.Communicate;
                        uavNetwork.topologyStatus = topologyStatus;
                        try {
                            Thread.sleep(80);
                            currentTime += 80;
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    } else if (uavNetwork.status.equals(SimulationStatus.Communicate)) {
                        uavNetwork.UAVsMoving();
                        try {
                            Thread.sleep(50);
                            currentTime += 50;
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    } else if (uavNetwork.status.equals(SimulationStatus.ForCruise)) {
                        runningFlag = false;
                    }
                    repaint();
                }
            }
        };
    }


    public void xToY(Topology status) {
        uavNetwork.initUAVs();
        if (uavNetwork.status.equals(SimulationStatus.FindCluster)) {
            while (uavNetwork.notClusterList.size() > 0) {
                uavNetwork.route.selectedCluster(uavNetwork.notClusterList.get(0));
            }
            if (uavNetwork.notClusterList.size() == 0) {
                //簇建立后路由
                uavNetwork.status = SimulationStatus.Route;
            }
        }
        for (Cluster cluster : uavNetwork.totalCluster.clusters) {
            UAV head = cluster.clusterHead;
            head.moveRandomly();
            for (int i = 0; i < cluster.getMemberList().size(); i++) {
                UAV member = UAVNetwork.uavHashMap.get(cluster.getMemberList().get(i));
                if(status==Topology.Grid){
                    member.memberMoveGridly();
                }else if(status==Topology.Line){
                    member.memberMoveLinely();
                }else if(status==Topology.Circle){
                    member.memberMoveCircly();
                }
            }
        }
        runningFlag = true;
        repaint();
        simulation = new Thread() {
            @Override
            public void run() {
                while (runningFlag) {
                    //入网过程
                    if (uavNetwork.status.equals(SimulationStatus.Route)) {
                        uavNetwork.UAVsMoving();
                        uavNetwork.status = SimulationStatus.Communicate;
                        uavNetwork.topologyStatus = topologyStatus;
                        try {
                            Thread.sleep(80);
                            currentTime += 80;
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    } else if (uavNetwork.status.equals(SimulationStatus.Communicate)) {
                        uavNetwork.UAVsMoving();
                        try {
                            Thread.sleep(50);
                            currentTime += 50;
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    } else if(uavNetwork.status.equals(SimulationStatus.ForCruise)){
                        runningFlag = false;
                    }
                    repaint();
                }
            }
        };
    }













    //绘制所有无人机
    @Override
    public void paint(Graphics g) {
        if (runningFlag) {
            super.paint(g);
            drawBackGround(g);
            paintUAVs(g);
            drawText(g);
        }

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
            g.drawImage(cloudDraw.image, cloudDraw.x, cloudDraw.y, areaWidth, areaHeight, this);
        }


    }

    private void setRunningFlag(Boolean flag) {
        this.runningFlag = flag;
    }

}
