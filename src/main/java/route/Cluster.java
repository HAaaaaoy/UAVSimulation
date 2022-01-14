package route;

import GUItil.GUItil;
import UAVs.UAV;
import org.apache.log4j.Logger;
import scene.PlaneWars;

import java.util.concurrent.ConcurrentHashMap;
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
    public static int clusterRadius =  (int) (3*GUItil.getBounds().height/8);
    //本簇的网关数量
    private int gateWayNumber = 0;

    public Cluster(UAV clusterHead){
        this.clusterHead = clusterHead;
        this.clusterID = clusterNumber++;
        memberList = new CopyOnWriteArrayList<>();

    }

    public CopyOnWriteArrayList<Integer> getMemberList() {
        return memberList;
    }

    public void setMemberList(CopyOnWriteArrayList<Integer> memberList) {
        this.memberList = memberList;
    }

    public void addClusterMember(UAV member){
        logger.info("At "+ PlaneWars.currentTime+": 第"+ member.getSerialID() + "号无人机加入"+this.getClusterID()+"簇");
        clusterHead.communication.add(member);
        RouteTableItem routeTableItem = new RouteTableItem(member.getSerialID(), member.getSerialID(), clusterHead.calculateDistance(member));
        clusterHead.routes.add(routeTableItem);
        member.clusterStatus = ClusterStatus.ClusterMember;
        member.joinCluster(this);
        if(member.clusters.size() >=2 && gateWayNumber <=1){
            member.setGateWay();
            member.routes = new CopyOnWriteArrayList<>();
            member.routeMaps = new ConcurrentHashMap<>();
            member.communication = new CopyOnWriteArrayList<>();
            //member.routeMaps.add(new RouteTableItem(this.clusterHead.getSerialID(), this.clusterHead.getSerialID()));
            this.gateWayNumber ++ ;
        }
        this.memberList.add(member.getSerialID());
    }

    public int getClusterID() {
        return clusterID;
    }




}
