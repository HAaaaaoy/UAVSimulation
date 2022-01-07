package scene;

import UAVs.NRUAV;
import UAVs.RUAV;

import javax.swing.*;
import java.awt.*;
import java.util.Iterator;

public class PlaneWars extends JPanel {

    //仿真实现
    public UAVNetwork uavNetwork;

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
                while (true) {
                    if (uavNetwork.status.equals(SimulationStatus.RUNNING) && (uavNetwork.getnRUAVs().size() > 0)) {
                        uavNetwork.UAVsMoving();
                        uavNetwork.infect();
                        try {
                            Thread.sleep(40);
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


}
