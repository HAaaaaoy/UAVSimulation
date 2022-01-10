package route;

import GUItil.GUItil;
import UAVs.UAV;
import org.apache.log4j.Logger;
import scene.PlaneWars;

import java.util.concurrent.CopyOnWriteArrayList;

public class Cluster {

    Logger logger = Logger.getLogger(Cluster.class);
    //本簇的成员
    private CopyOnWriteArrayList<Integer> memberList;
    //簇头
    private UAV clusterHead;
    private static int clusterNumber = 1;
    public final int memberNumber = 10;
    private int clusterID;
    //簇半径
    public int clusterRadius;

    public Cluster(UAV clusterHead){
        this.clusterHead = clusterHead;
        this.clusterID = clusterNumber++;
        memberList = new CopyOnWriteArrayList<>();
        clusterRadius = (int) (GUItil.getBounds().height/3);
    }

    public CopyOnWriteArrayList<Integer> getMemberList() {
        return memberList;
    }

    public void setMemberList(CopyOnWriteArrayList<Integer> memberList) {
        this.memberList = memberList;
    }

    public void addClusterMember(UAV member){
        logger.info("At "+ PlaneWars.currentTime+": 第"+ member.getSerialID() + "号无人机加入"+this.getClusterID()+"簇");
        this.memberList.add(member.getSerialID());
    }

    public int getClusterID() {
        return clusterID;
    }




}
