package scene;

import UAVs.UAV;
import org.apache.log4j.Logger;
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

    public PlaneWars() {
        uavNetwork = new UAVNetwork(50);
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
                    if (uavNetwork.status.equals(SimulationStatus.FindCluster)) {
                        uavNetwork.UAVsMoving();
                        if (uavNetwork.notClusterList.size() > 0 && currentTime % 2000 == 0) {
                            uavNetwork.route.selectedCluster(uavNetwork.notClusterList.get(0));
                        } else if (uavNetwork.notClusterList.size() == 0) {
                            //簇建立后路由
                            uavNetwork.status = SimulationStatus.Route;
                        }
                        try {
                            Thread.sleep(40);
                            currentTime += 40;
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }else if(uavNetwork.status.equals(SimulationStatus.Route)){
                        uavNetwork.UAVsMoving();
                        try {
                            Thread.sleep(40);
                            currentTime += 40;
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

    private void setRunningFlag(Boolean flag) {
        this.runningFlag = flag;
    }

}
