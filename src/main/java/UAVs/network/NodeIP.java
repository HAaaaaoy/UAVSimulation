package UAVs.network;

public class NodeIP {
    private String ip;
    //对应ip四个字段
    private int ipA;
    private int ipB;
    private int ipC;
    private int ipD;
    private String[] ips;

    public NodeIP(String i){
        this.ip = i.trim();
        ips = i.split("\\.");
        ipA = Integer.parseInt(ips[0].trim());
        ipB = Integer.parseInt(ips[1].trim());
        ipC = Integer.parseInt(ips[2].trim());
        ipD = Integer.parseInt(ips[3].trim());
    }

    public NodeIP(){

    }

    public void setIp(String i){
        this.ip = i.trim();
        ips = i.split("\\.");
        ipA = Integer.parseInt(ips[0].trim());
        ipB = Integer.parseInt(ips[1].trim());
        ipC = Integer.parseInt(ips[2].trim());
        ipD = Integer.parseInt(ips[3].trim());
    }

    public String getGateway(){
        return getIpA()+"."+getIpB()+"."+getIpC()+".0";
    }

    public int getIpA(){
        return ipA;
    }

    public int getIpB(){
        return ipB;
    }

    public int getIpC(){
        return ipC;
    }

    public int getIpD(){
        return ipD;
    }

    public String getIp(){
        return ip;
    }
}
