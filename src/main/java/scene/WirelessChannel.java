package scene;

import UAVs.UAV;
import UAVs.network.Transmission;
import org.apache.log4j.Logger;

import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

public class WirelessChannel extends Thread {
    /**
     * 无线信道
     */
    Logger logger = Logger.getLogger(WirelessChannel.class);
    //
    public CopyOnWriteArrayList<Transmission> transmissions;

    public WirelessChannel(){
        transmissions = new CopyOnWriteArrayList<>();
    }

    public synchronized void addTransmission(Transmission transmission){
        this.transmissions.add(transmission);
        logger.info("WLAN通信环境已创建");
        this.start();
    }

    public void run(){
        Transmission t;
        while (true){
            Iterator<Transmission> iterator = transmissions.iterator();
            while (iterator.hasNext()){
                t = iterator.next();
                if(isAchieved(t)){
                    //到达操作
                    transmissions.remove(t);
                }
            }
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public Boolean isAchieved(Transmission transmission){
        UAV src = UAVNetwork.getUavHashMap().get(transmission.getSrc());
        UAV dst = UAVNetwork.getUavHashMap().get(transmission.getDst());
        if((PlaneWars.currentTime-transmission.getCreatTime())>= delay(src.calculateDistance(dst)) ){
            return true;
        }
        return false;
    }

    private int delay(int distance){
        return (int) Math.floor(0.1*distance);
    }


}
