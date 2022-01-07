package scene;

import GUItil.GUItil;
import UAVs.NRUAV;
import UAVs.RUAV;
import UAVs.UAV;
import image.ImageRead;
import org.apache.log4j.Logger;
import text.RText;

import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class UAVNetwork {

    private int rNumber, nrNumber;
    //未入网（感染）的无人机队列
    private CopyOnWriteArrayList<NRUAV> nRUAVs = new CopyOnWriteArrayList<>();
    //已经入网（感染）的无人机队列
    private CopyOnWriteArrayList<RUAV> rUAVs = new CopyOnWriteArrayList<>();
    //仿真时无人机的标注信息
    private CopyOnWriteArrayList<RText> rTexts = new CopyOnWriteArrayList<>();
    //记录无人机入网时间

    Logger logger = Logger.getLogger(UAVNetwork.class);

    //记录无人机感染的时间
    public HashMap<Integer, Long> timeRecord = new HashMap<>();
    //仿真状态
    public SimulationStatus status = SimulationStatus.RUNNING;
    private int serialID = 1;

    public UAVNetwork(int rNumber, int nrNumber){
        this.rNumber = rNumber;
        this.nrNumber = nrNumber;
    }


    public void initUAVs() {
        //无人机高度设为120
        int height = 120;
        //无人机宽度设为120
        int width = 120;
        for (int i = 0; i < rNumber; i++) {
            logger.info("192.168.192." + serialID);
            RUAV rUAV = new RUAV(ThreadLocalRandom.current().nextInt(GUItil.getBounds().width),
                    ThreadLocalRandom.current().nextInt(GUItil.getBounds().height),
                    height, width, ImageRead.RUAVs, serialID, "192.168.192." + serialID);
            serialID++;
            rUAVs.add(rUAV);
            rUAV.start();
        }
        for (int j = 0; j < nrNumber; j++) {
            logger.info("192.168.192." + serialID);
            NRUAV nrUAV = new NRUAV(ThreadLocalRandom.current().nextInt(GUItil.getBounds().width),
                    ThreadLocalRandom.current().nextInt(GUItil.getBounds().height),
                    height, width, ImageRead.NRUAVs, serialID, "192.168.192." + serialID);
            serialID++;
            nRUAVs.add(nrUAV);
        }
    }

    //无人机移动
    public void UAVsMoving() {
        for (NRUAV nruav : nRUAVs) {
            nruav.move();
        }
        for (RUAV ruav : rUAVs) {
            ruav.move();
        }
    }

    public void infect() {
        for (int i = 0; i < rUAVs.size(); i++) {
            for (int j = 0; j < nRUAVs.size(); j++) {
                if(rUAVs.get(i).isGetMessage(nRUAVs.get(j))){
                    NRUAV temp =nRUAVs.get(j);
                    rUAVs.add(new RUAV(temp.getPosition_index_x(), temp.getPosition_index_y(), temp.getUAV_Height(), temp.getUAV_Width(),
                            ImageRead.RUAVs, temp.getSerialID(), temp.getIp().getIp()));
                    nRUAVs.remove(nRUAVs.get(j));
                }
            }
        }

    }


    public void addText() {


    }

    public CopyOnWriteArrayList<NRUAV> getnRUAVs() {
        return nRUAVs;
    }

    public CopyOnWriteArrayList<RUAV> getrUAVs() {
        return rUAVs;
    }

    public CopyOnWriteArrayList<RText> getrTexts() {
        return rTexts;
    }


}
