package UAVs.network;

import UAVs.UAV;

import java.util.concurrent.CopyOnWriteArrayList;

public class Packet {

    private static int sequenceNumber = 1;
    private int src;
    private int dst;
    private int next;
    private int creatTime;
    private int sentTme;
    private int receiveTime;
    public int packetID;
    public CopyOnWriteArrayList<UAV> routeList ;


    private short ackFlag;

    public Packet(int src, int dst, int creatTime) {
        this.src = src;
        this.dst = dst;
        this.packetID = sequenceNumber++;
        this.creatTime = creatTime;
        this.routeList = new CopyOnWriteArrayList<>();
    }

    public int getSrc() {
        return src;
    }

    public void setSrc(int src) {
        this.src = src;
    }

    public int getDst() {
        return dst;
    }

    public void setDst(int dst) {
        this.dst = dst;
    }

    public int getCreatTime() {
        return creatTime;
    }

    public void setCreatTime(int creatTime) {
        this.creatTime = creatTime;
    }

    public int getSentTme() {
        return sentTme;
    }

    public void setSentTme(int sentTme) {
        this.sentTme = sentTme;
    }

    public int getReceiveTime() {
        return receiveTime;
    }

    public void setReceiveTime(int receiveTime) {
        this.receiveTime = receiveTime;
    }

    public short getAckFlag() {
        return ackFlag;
    }

    public void setAckFlag(short ackFlag) {
        this.ackFlag = ackFlag;
    }

    public int getNext() {
        return next;
    }

    public void setNext(int next) {
        this.next = next;
    }
}
