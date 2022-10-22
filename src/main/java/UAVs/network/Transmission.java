package UAVs.network;

public class Transmission {

    private int src;
    private int dst;
    private Packet packet;
    private Long creatTime;



    public Transmission(int src, int dst, Packet p, Long creatTime) {
        this.src = src;
        this.dst = dst;
        this.packet = p;
        this.creatTime = creatTime;
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

    public Packet getPacket() {
        return packet;
    }

    public void setPacket(Packet packet) {
        this.packet = packet;
    }

    public Long getCreatTime() {
        return creatTime;
    }

    public void setCreatTime(Long creatTime) {
        this.creatTime = creatTime;
    }
}
