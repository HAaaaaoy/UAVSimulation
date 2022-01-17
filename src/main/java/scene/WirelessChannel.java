package scene;

import UAVs.UAV;
import UAVs.network.Transmission;
import org.apache.log4j.Logger;

import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class WirelessChannel extends Thread {

    private int random ;
    /**
     * 无线信道
     */
    Logger logger = Logger.getLogger(WirelessChannel.class);
    //
    public CopyOnWriteArrayList<Transmission> transmissions;

    public WirelessChannel() {
        transmissions = new CopyOnWriteArrayList<>();
        this.start();
    }

    public synchronized void addTransmission(Transmission transmission) {
        this.transmissions.add(transmission);

    }

    public void run() {
        Transmission t;
        while (true) {
            Iterator<Transmission> iterator = transmissions.iterator();
            while (iterator.hasNext()) {
                t = iterator.next();
                if (isAchieved(t)) {
                    //到达操作
                    transmissions.remove(t);
                    random = ThreadLocalRandom.current().nextInt(0,100);
                    if(random <=90){
                        UAVNetwork.getUavHashMap().get(t.getDst()).linkLayer.addReceiveQueue(t.getPacket());
                        t.getPacket().setReceiveTime(Math.toIntExact(PlaneWars.currentTime));
                    }
                }
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public Boolean isAchieved(Transmission transmission) {
        UAV src = UAVNetwork.uavHashMap.get(transmission.getSrc());
        UAV dst = UAVNetwork.uavHashMap.get(transmission.getDst());
        if ((PlaneWars.currentTime - transmission.getCreatTime()) >= delay(src.calculateDistance(dst))) {
            return true;
        }
        return false;
    }

    private int delay(int distance) {
        return (int) Math.floor(0.5 * distance);
    }


}
