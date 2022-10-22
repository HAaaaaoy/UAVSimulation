package scene;

import UAVs.UAV;
import UAVs.network.Transmission;
import org.apache.log4j.Logger;

import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class WirelessChannel extends Thread {

    //信道占用状态
    //表明传输数据
    private boolean data;
    //表明传输控制包
    private boolean control;
    //表明碰撞冲突
    private boolean collision;
    //表明空闲
    private boolean idle;
    //在这个时隙传输的比特数量
    private double bitsInSlot;
    //Slot speed
    private double slotSpeed;
    //wifi无线信道的状态
    public WirelessChannelStatus wifistatus;


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


    // 无线信道的实现
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
//                        .getUavHashMap().get(t.getDst()).linkLayer.addReceiveQueue(t.getPacket());
//                        t.getPacket().setReceiveTime(Math.toIntExact(PlaneWars.currentTime));
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
//        UAV src = UAVNetwork.uavHashMap.get(transmission.getSrc());
//        UAV dst = UAVNetwork.uavHashMap.get(transmission.getDst());
//        if ((PlaneWars.currentTime - transmission.getCreatTime()) >= delay(src.calculateDistance(dst))) {
//            return true;
//        }
        return false;
    }

    public int delay(int distance) {
        return (int) Math.floor(0.5 * distance);
    }

    public boolean isData() {
        return data;
    }

    public void setData(boolean data) {
        this.data = data;
    }

    public boolean isControl() {
        return control;
    }

    public void setControl(boolean control) {
        this.control = control;
    }

    public boolean isCollision() {
        return collision;
    }

    public void setCollision(boolean collision) {
        this.collision = collision;
    }

    public boolean isIdle() {
        return idle;
    }

    public void setIdle(boolean idle) {
        this.idle = idle;
    }

    public double getBitsInSlot() {
        return bitsInSlot;
    }

    public void setBitsInSlot(double bitsInSlot) {
        this.bitsInSlot = bitsInSlot;
    }

    public double getSlotSpeed() {
        return slotSpeed;
    }

    public void setSlotSpeed(double slotSpeed) {
        this.slotSpeed = slotSpeed;
    }
}
