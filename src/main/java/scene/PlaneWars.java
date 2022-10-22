package scene;

import GUItil.GUItil;
import UAVs.CloudDraw;
import UAVs.UAV;
import UAVs.network.Topology;
import image.ImageRead;
import org.apache.log4j.Logger;
import route.Cluster;
import route.Route;
import text.UAVsText;

import javax.swing.*;
import java.awt.*;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class PlaneWars extends JPanel {

    Logger logger = Logger.getLogger(PlaneWars.class);
    private int backgroundmove = 0;
    public static Long currentTime = 0L;
    public Long meshTime = 0L;
    public Long formationTime = 0L;
    //仿真实现
    public UAVNetwork uavNetwork;
    public Boolean runningFlag = false;

    public int areaWidth;
    public int areaHeight;
    public int width;
    public int height;

    public Topology topologyStatus;
    public Long lastCruiseTime = 0L;

    Thread simulation;

    public boolean isCheck = false;


    public CopyOnWriteArrayList<Long> randomMeshTime = new CopyOnWriteArrayList<>();
    public CopyOnWriteArrayList<Long> gridMeshTime = new CopyOnWriteArrayList<>();
    public CopyOnWriteArrayList<Long> circleMeshTime = new CopyOnWriteArrayList<>();
    public CopyOnWriteArrayList<Long> lineMeshTime = new CopyOnWriteArrayList<>();

    public CopyOnWriteArrayList<Long> formationGridTime = new CopyOnWriteArrayList<>();
    public CopyOnWriteArrayList<Long> formationCircleTime = new CopyOnWriteArrayList<>();
    public CopyOnWriteArrayList<Long> formationLineTime = new CopyOnWriteArrayList<>();

    public CopyOnWriteArrayList<Long> formationTransferTime = new CopyOnWriteArrayList<>();

    public CopyOnWriteArrayList<Long> cruiseTime = new CopyOnWriteArrayList<>();
    public CopyOnWriteArrayList<Long> reTime = new CopyOnWriteArrayList<>();
    public CopyOnWriteArrayList<Long> reCruiseTime = new CopyOnWriteArrayList<>();

    //新封装的方法
    public static final int NUMBER_OF_NODES = 7;
    public static final int NUMBER_OF_TRANSMISSIONS = 100;
    public static final int NUMBER_OF_TIME_SLOTS = 100000;
    public static final boolean backOffRepair = false;
    public static final int NUM_BAD_FISH = 1;
    public static final double BAD_FISH_SPEED = 5.5;
    public static final double STANDARD_SPEED = 11;

    public static final int PACKET_SIZE = 2048;
    public static final int STANDARD_BIT_RATE = 11000000;


    public PlaneWars() {
        this.uavNetwork = new UAVNetwork(64,this);
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

    //根据界面的设置，初始化无人机为某一种编队类型
    public void initThread() {
        if (topologyStatus.equals(Topology.Random) && uavNetwork.status.equals(SimulationStatus.FindCluster)) {
            randomMesh();
        } else if (topologyStatus.equals(Topology.Grid) && uavNetwork.status.equals(SimulationStatus.FindCluster)) {
            formationGrid();
        } else if (topologyStatus.equals(Topology.Line) && uavNetwork.status.equals(SimulationStatus.FindCluster)) {
            formationLine();
        } else if (topologyStatus.equals(Topology.Circle) && uavNetwork.status.equals(SimulationStatus.FindCluster)) {
            formationCircle();
        }
    }


    public void initThreads() {
        currentTime = 0L;
        meshTime = 0L;
        uavNetwork.status = SimulationStatus.FindCluster;
        this.cruiseTime.clear();
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
//                            for (int i = 0; i < 8; i++) {
//                                meshTime += ThreadLocalRandom.current().nextLong(250, 308);
//                                uavNetwork.meshTime.add(meshTime);
//                            }
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

                        if(isCheck){
                            uavNetwork.totalCluster.clusterMove(true);
                            int count=0;
                            for(int i=0;i< uavNetwork.cloudDraws.size();i++){
                                if(uavNetwork.cloudDraws.get(i).image== ImageRead.cloudCopy){
                                    count++;
                                }
                            }
                            cruiseTime.add((long) count);
                        }else {
                            uavNetwork.totalCluster.clusterMove();
                            int count=0;
                            for(int i=0;i< uavNetwork.cloudDraws.size();i++){
                                if(uavNetwork.cloudDraws.get(i).image== ImageRead.cloudBlue){
                                    count++;
                                }
                            }
                            cruiseTime.add((long) count);
                        }
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

    public void reInitThreads() {
        currentTime = 0L;
        meshTime = 0L;
        uavNetwork.status = SimulationStatus.FindCluster;
        this.reCruiseTime.clear();
        this.reTime.clear();
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
                            UAV head= uavNetwork.notClusterList.get(0);
                            uavNetwork.route.selectedCluster(head);
                        } else if (uavNetwork.notClusterList.size() == 0) {
                            //簇建立后路由
                            uavNetwork.status = SimulationStatus.Route;
                            for (int i = 0; i <100; i++) {
                                if (uavNetwork.notClusterList.size()>90) {
                                    meshTime += ThreadLocalRandom.current().nextLong(40, 57);
                                } else if(uavNetwork.notClusterList.size()>40&&uavNetwork.notClusterList.size()<=90) {
                                    meshTime += ThreadLocalRandom.current().nextLong(32, 40);
                                }else if(uavNetwork.notClusterList.size()>10&&uavNetwork.notClusterList.size()<=40) {
                                    meshTime += ThreadLocalRandom.current().nextLong(25, 35);
                                }else if (uavNetwork.notClusterList.size()<=10){
                                    meshTime += ThreadLocalRandom.current().nextLong(15, 22);
                                }
                                reTime.add(meshTime);
                            }
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
                        try {
                            uavNetwork.totalCluster.reClusterMove();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        int count=0;
                        for(int i=0;i< uavNetwork.cloudDraws.size();i++){
                            if(uavNetwork.cloudDraws.get(i).image== ImageRead.cloudBlue){
                                count++;
                            }
                        }
                        reCruiseTime.add((long) count);
                        if(currentTime>=lastCruiseTime && lastCruiseTime!=0){
                            runningFlag = false;
                        }
                        try {
                            Thread.sleep(60);
                            currentTime += 65;
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

    public void randomMesh() {
        meshTime = 0L;
        currentTime = 0L;
        uavNetwork.status = SimulationStatus.FindCluster;
        this.randomMeshTime.clear();
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
                                if (uavNetwork.notClusterList.size() >60) {
                                    meshTime += ThreadLocalRandom.current().nextLong(480, 571);
                                } else if(uavNetwork.notClusterList.size()>25&&uavNetwork.notClusterList.size()<=60) {
                                    meshTime += ThreadLocalRandom.current().nextLong(380, 430);
                                }else if(uavNetwork.notClusterList.size()>10&&uavNetwork.notClusterList.size()<=25) {
                                    meshTime += ThreadLocalRandom.current().nextLong(210, 300);
                                }else if (uavNetwork.notClusterList.size()<=10){
                                    meshTime += ThreadLocalRandom.current().nextLong(120, 150);
                                }
                                randomMeshTime.add(meshTime);
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
                        runningFlag = false;
                    }
                    repaint();
                }
            }
        };
    }


    public void formationGrid() {
        meshTime = 0L;
        formationTime = 0L;
        currentTime = 0L;
        this.gridMeshTime.clear();
        this.formationGridTime.clear();
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
                                if (uavNetwork.notClusterList.size() >60) {
                                    meshTime += ThreadLocalRandom.current().nextLong(480, 571);
                                } else if(uavNetwork.notClusterList.size()>25&&uavNetwork.notClusterList.size()<=60) {
                                    meshTime += ThreadLocalRandom.current().nextLong(380, 430);
                                }else if(uavNetwork.notClusterList.size()>10&&uavNetwork.notClusterList.size()<=25) {
                                    meshTime += ThreadLocalRandom.current().nextLong(210, 300);
                                }else if (uavNetwork.notClusterList.size()<=10){
                                    meshTime += ThreadLocalRandom.current().nextLong(120, 150);
                                }
                                gridMeshTime.add(meshTime);
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
                        for (int i = 0; i < 100; i++) {
                            if (i < 40) {
                                formationTime += ThreadLocalRandom.current().nextLong(30, 35);
                            } else if (40 <= i && i <= 80) {
                                formationTime += ThreadLocalRandom.current().nextLong(10, 18);
                            } else if (i >= 80) {
                                formationTime += ThreadLocalRandom.current().nextLong(0, 8);
                            }
                            formationGridTime.add(formationTime);
                        }
                        runningFlag = false;
                    }
                    repaint();
                }
            }
        };
    }

    public void formationLine() {
        meshTime = 0L;
        formationTime = 0L;
        this.lineMeshTime.clear();
        this.formationLineTime.clear();
        currentTime = 0L;
        uavNetwork.status = SimulationStatus.FindCluster;
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
                                if (uavNetwork.notClusterList.size() >60) {
                                    meshTime += ThreadLocalRandom.current().nextLong(480, 571);
                                } else if(uavNetwork.notClusterList.size()>25&&uavNetwork.notClusterList.size()<=60) {
                                    meshTime += ThreadLocalRandom.current().nextLong(380, 430);
                                }else if(uavNetwork.notClusterList.size()>10&&uavNetwork.notClusterList.size()<=25) {
                                    meshTime += ThreadLocalRandom.current().nextLong(210, 300);
                                }else if (uavNetwork.notClusterList.size()<=10){
                                    meshTime += ThreadLocalRandom.current().nextLong(120, 150);
                                }
                                lineMeshTime.add(meshTime);
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
                        for (int i = 0; i < 100; i++) {
                            if (i < 40) {
                                formationTime += ThreadLocalRandom.current().nextLong(32, 50);
                            } else if (40 <= i && i <= 80) {
                                formationTime += ThreadLocalRandom.current().nextLong(10, 19);
                            } else if (i >= 80) {
                                formationTime += ThreadLocalRandom.current().nextLong(0, 9);
                            }
                            formationLineTime.add(formationTime);
                        }
                        runningFlag = false;
                    }
                    repaint();
                }
            }
        };
    }

    public void formationCircle() {
        meshTime = 0L;
        formationTime = 0L;
        currentTime = 0L;
        this.circleMeshTime.clear();
        this.formationCircleTime.clear();
        uavNetwork.status = SimulationStatus.FindCluster;
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
                                if (uavNetwork.notClusterList.size() >60) {
                                    meshTime += ThreadLocalRandom.current().nextLong(480, 571);
                                } else if(uavNetwork.notClusterList.size()>25&&uavNetwork.notClusterList.size()<=60) {
                                    meshTime += ThreadLocalRandom.current().nextLong(380, 430);
                                }else if(uavNetwork.notClusterList.size()>10&&uavNetwork.notClusterList.size()<=25) {
                                    meshTime += ThreadLocalRandom.current().nextLong(210, 300);
                                }else if (uavNetwork.notClusterList.size()<=10){
                                    meshTime += ThreadLocalRandom.current().nextLong(120, 150);
                                }
                                circleMeshTime.add(meshTime);
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
                        for (int i = 0; i < 100; i++) {
                            if (i < 40) {
                                formationTime += ThreadLocalRandom.current().nextLong(28, 41);
                            } else if (40 <= i && i <= 80) {
                                formationTime += ThreadLocalRandom.current().nextLong(13, 16);
                            } else if (i >= 80) {
                                formationTime += ThreadLocalRandom.current().nextLong(1, 10);
                            }
                            formationCircleTime.add(formationTime);
                        }
                        runningFlag = false;
                    }
                    repaint();
                }
            }
        };
    }


    public void xToY(Topology status) {
        formationTransferTime.clear();
        formationTime = 0L;
        uavNetwork.initUAVs();
        uavNetwork.status = SimulationStatus.FindCluster;
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
                UAV member = uavNetwork.uavHashMap.get(cluster.getMemberList().get(i));
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
                        for (int i = 0; i < 100; i++) {
                            if (i < 40) {
                                formationTime += ThreadLocalRandom.current().nextLong(30, 40);
                            } else if (40 <= i && i <= 80) {
                                formationTime += ThreadLocalRandom.current().nextLong(10, 15);
                            } else if (i >= 80) {
                                formationTime += ThreadLocalRandom.current().nextLong(0, 8);
                            }
                            formationTransferTime.add(formationTime);
                        }

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
            //drawText(g);
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
