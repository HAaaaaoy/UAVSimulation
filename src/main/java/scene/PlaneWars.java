package scene;

import UAVs.NRUAV;
import UAVs.RUAV;
import org.apache.log4j.Logger;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;

public class PlaneWars extends JPanel {

    Logger logger = Logger.getLogger(PlaneWars.class);
    private int backgroundmove = 0;
    public static Long currentTime = 0L;
    //仿真实现
    public UAVNetwork uavNetwork;
    private Boolean runningFlag = true;

    public PlaneWars() {
        uavNetwork = new UAVNetwork(1, 50);
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
                while (runningFlag) {
                    //入网过程
                    if (uavNetwork.status.equals(SimulationStatus.RUNNING) && (uavNetwork.getnRUAVs().size() > 0)) {
                        uavNetwork.UAVsMoving();
                        uavNetwork.infect();
                        try {
                            Thread.sleep(40);
                            currentTime += 40;
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }else if(uavNetwork.status.equals(SimulationStatus.RUNNING) && (uavNetwork.getnRUAVs().size() == 0) ){
                        logger.info("开始路由");
                        while (uavNetwork.notClusterList.size() > 0){
                            uavNetwork.route.selectedCluster(uavNetwork.notClusterList.get(0));
                        }

                        try {
                            Thread.sleep(100);
                            currentTime += 100;
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        setRunningFlag(false);
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
        paintUAVs(g);
    }

    public void paintUAVs(Graphics g) {
        Iterator<NRUAV> nruavIterator = uavNetwork.getnRUAVs().iterator();
        Iterator<RUAV> ruavIterator = uavNetwork.getrUAVs().iterator();
        while (nruavIterator.hasNext()){
            nruavIterator.next().drawUAVs(g);
        }
        while (ruavIterator.hasNext()){
            ruavIterator.next().drawUAVs(g);
        }
    }

    private void setRunningFlag(Boolean flag){
        this.runningFlag = flag;
    }

}
