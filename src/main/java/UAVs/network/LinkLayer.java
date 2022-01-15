package UAVs.network;

import UAVs.UAV;
import scene.PlaneWars;
import scene.UAVNetwork;
import scene.WirelessChannel;

import java.util.LinkedList;

public class LinkLayer {

    //链路层

    //存储发送报文的队列
    private LinkedList<Packet> sentQueue;
    //存储接受报文的队列
    public LinkedList<Packet> receiveQueue;
    private WirelessChannel wirelessChannel;

    public LinkLayer(WirelessChannel wirelessChannel){
        this.sentQueue = new LinkedList<>();
        this.receiveQueue = new LinkedList<>();
        this.wirelessChannel = wirelessChannel;
    }

    public void addSentQueue(Packet packet){
        this.sentQueue.add(packet);
    }

    public void addReceiveQueue(Packet packet){
        this.receiveQueue.add(packet);
    }

    public void send(Packet packet){
        Transmission transmission = new Transmission(packet.getSrc(), packet.getNext(), packet, PlaneWars.currentTime);
        wirelessChannel.addTransmission(transmission);
    }


}
