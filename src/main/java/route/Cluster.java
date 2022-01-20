package route;

import GUItil.GUItil;
import UAVs.UAV;
import UAVs.network.NodeIP;
import org.apache.log4j.Logger;
import scene.PlaneWars;

import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class Cluster {

    Logger logger = Logger.getLogger(Cluster.class);
    //本簇的成员
    private CopyOnWriteArrayList<Integer> memberList;
    private CopyOnWriteArrayList<UAV> gatewayList;
    //簇头
    public UAV clusterHead;
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
        gatewayList = new CopyOnWriteArrayList<>();
        //clusterHead.ip = new NodeIP("192.168."+clusterID+"."+clusterHead.getSerialID());

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
            member.communication = new CopyOnWriteArrayList<>();
            member.routeTable = new CopyOnWriteArrayList<>();
            member.routeCache = new ConcurrentHashMap<>();
            if(firstCluster.gatewayList.size() >= 1){
                Iterator<UAV> uavIterator = firstCluster.gatewayList.iterator();
                while (uavIterator.hasNext()){
                    UAV uav = uavIterator.next();
                    //member.routeTable.add(new RouteTableItem(uav.getSerialID(), uav.getSerialID(), uav.calculateDistance(member)));
                    member.routeTable.add(new RouteTableItem(uav.getSerialID(), uav.getSerialID(), 1));
                    member.communication.add(uav);
                }
            }
            member.setGateWay();
            firstCluster.gatewayList.add(member);
            this.gatewayList.add(member);
            logger.info("At " + PlaneWars.currentTime + ": 第" + member.getSerialID() + "号无人机成为" + this.getClusterID() + "和" + firstCluster.clusterID + "簇的网关");
            Route.routes.add(member);
            clusterHead.communication.add(member);
            firstCluster.clusterHead.communication.add(member);
            member.communication.add(this.clusterHead);
            member.communication.add(firstCluster.clusterHead);
//            member.routeTable.add(new RouteTableItem(this.clusterHead.getSerialID(), this.clusterHead.getSerialID(), clusterHead.calculateDistance(member)));
//            member.routeTable.add(new RouteTableItem(firstCluster.clusterHead.getSerialID(), firstCluster.clusterHead.getSerialID(), firstCluster.clusterHead.calculateDistance(member)));
            member.routeTable.add(new RouteTableItem(this.clusterHead.getSerialID(), this.clusterHead.getSerialID(), 1));
            member.routeTable.add(new RouteTableItem(firstCluster.clusterHead.getSerialID(), firstCluster.clusterHead.getSerialID(), 1));
            this.gateWayNumber++;
            this.memberList.add(member.getSerialID());
            return true;
        } else if (member.clusters.size() == 0 && member.cluster == null) {
            logger.info("At " + PlaneWars.currentTime + ": 第" + member.getSerialID() + "号无人机加入" + this.getClusterID() + "簇");
            RouteTableItem routeTableItem = new RouteTableItem(member.getSerialID(), member.getSerialID(), clusterHead.calculateDistance(member));
            clusterHead.routeTable.add(routeTableItem);
            member.clusterStatus = ClusterStatus.ClusterMember;
            //member.ip = new NodeIP("192.168."+clusterID+"."+member.getSerialID());
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
