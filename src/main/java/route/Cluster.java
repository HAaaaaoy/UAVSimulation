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
    public final int memberNumber = 8;
    private int clusterID;
    //簇半径
    public static int clusterRadius = (int) (GUItil.getBounds().height / 2);
    //本簇的网关数量
    private int gateWayNumber = 0;

    public Cluster(UAV clusterHead) {
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

    public Boolean addClusterMember(UAV member) {
        if (member.clusters.size() == 1 && this.gateWayNumber == 0 && member.cluster == null) {
            logger.info("At " + PlaneWars.currentTime + ": 第" + member.getSerialID() + "号无人机加入" + this.getClusterID() + "簇");
            RouteTableItem routeTableItem = new RouteTableItem(member.getSerialID(), member.getSerialID(), clusterHead.calculateDistance(member));
            clusterHead.routeTable.add(routeTableItem);
            member.clusterStatus = ClusterStatus.ClusterMember;
            member.joinCluster(this);
            Cluster firstCluster = member.clusters.get(0);
            member.setGateWay();
            logger.info("At " + PlaneWars.currentTime + ": 第" + member.getSerialID() + "号无人机成为" + this.getClusterID() + "和" + firstCluster.clusterID + "簇的网关");
            Route.routes.add(member);
            clusterHead.communication.add(member);
            firstCluster.clusterHead.communication.add(member);
            member.routeTable = new CopyOnWriteArrayList<>();
            member.routeCache = new ConcurrentHashMap<>();
            member.communication = new CopyOnWriteArrayList<>();
            member.communication.add(this.clusterHead);
            member.communication.add(firstCluster.clusterHead);
            member.routeTable.add(new RouteTableItem(this.clusterHead.getSerialID(), this.clusterHead.getSerialID(), clusterHead.calculateDistance(member)));
            member.routeTable.add(new RouteTableItem(firstCluster.clusterHead.getSerialID(), firstCluster.clusterHead.getSerialID(), firstCluster.clusterHead.calculateDistance(member)));
            this.gateWayNumber++;
            this.memberList.add(member.getSerialID());
            return true;
        } else if (member.clusters.size() == 0 && member.cluster == null) {
            logger.info("At " + PlaneWars.currentTime + ": 第" + member.getSerialID() + "号无人机加入" + this.getClusterID() + "簇");
            RouteTableItem routeTableItem = new RouteTableItem(member.getSerialID(), member.getSerialID(), clusterHead.calculateDistance(member));
            clusterHead.routeTable.add(routeTableItem);
            member.clusterStatus = ClusterStatus.ClusterMember;
            member.joinCluster(this);
            this.memberList.add(member.getSerialID());
            return true;
        }
        return false;
    }

    public int getClusterID() {
        return clusterID;
    }


}
