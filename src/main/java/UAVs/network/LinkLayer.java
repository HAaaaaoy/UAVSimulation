package UAVs.network;

import scene.WirelessChannel;

import java.util.LinkedList;

public class LinkLayer {

    //链路层

    //存储发送报文的队列
    private LinkedList<Packet> sentQueue;
    //存储接受报文的队列
    private LinkedList<Packet> receiveQueue;
    private WirelessChannel wirelessChannel;

    public LinkLayer(WirelessChannel wirelessChannel){
        this.sentQueue = new LinkedList<>();
        this.receiveQueue = new LinkedList<>();
        this.wirelessChannel = wirelessChannel;
    }



}
