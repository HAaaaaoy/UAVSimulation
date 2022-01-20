package UAVs.network;

import UAVs.UAV;
import org.apache.log4j.Logger;
import scene.PlaneWars;
import scene.UAVNetwork;
import scene.WirelessChannel;

import java.util.LinkedList;

public class LinkLayer {

    //链路层
    Logger logger = Logger.getLogger(UAV.class);
    //存储发送报文的队列
    public LinkedList<Packet> sentQueue;
    //存储接受报文的队列
    public LinkedList<Packet> receiveQueue;
    private WirelessChannel wirelessChannel;
    //所属的uav
    private UAV uav;

    public final int windowSize = 5;

    public LinkLayer(WirelessChannel wirelessChannel, UAV uav){
        this.sentQueue = new LinkedList<>();
        this.receiveQueue = new LinkedList<>();
        this.wirelessChannel = wirelessChannel;
        this.uav = uav;
    }

    public synchronized void addSentQueue(Packet packet){
        if(sentQueue.size() <= windowSize-1){
            this.sentQueue.add(packet);
        }
    }

    public void addReceiveQueue(Packet packet){
        if(receiveQueue.size() <= windowSize-1){
            this.receiveQueue.add(packet);
        }
    }

    public void send(Packet packet, int next){
        packet.routeList.add(uav);
        Transmission transmission = new Transmission(packet.getSrc(), next, packet, PlaneWars.currentTime);
        wirelessChannel.addTransmission(transmission);
    }


}
