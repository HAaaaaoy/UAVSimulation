package route;

import GUItil.GUItil;
import UAVs.UAV;
import UAVs.network.Topology;
import image.ImageRead;
import scene.UAVNetwork;

import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

public class TotalCluster {

    public CopyOnWriteArrayList<Cluster> clusters;
    public int left, right, top, bottom;
    public ClusterMoveStatus moveStatus = ClusterMoveStatus.Right;
    public Topology topology;

    public UAVNetwork uavNetwork;

    public int areaWidth;
    public int areaHeight;


    public TotalCluster(UAVNetwork uavNetwork) {
        this.uavNetwork = uavNetwork;
        this.areaWidth = uavNetwork.areaWidth;
        this.areaHeight = uavNetwork.areaHeight;
        clusters = new CopyOnWriteArrayList<>();
    }

    public void initStartPosition(Topology topology) {
        if (topology == Topology.Grid) {
            this.topology = topology;
            left = 0;
            top = 0;
            bottom = 450;
            right = 450;
            for (int i = 0; i < clusters.size(); i++) {
                switch (i) {
                    case 0:
                        setTargetPosition(clusters.get(i).clusterHead, 50, 50);
                        break;
                    case 1:
                        setTargetPosition(clusters.get(i).clusterHead, 200, 50);
                        break;
                    case 2:
                        setTargetPosition(clusters.get(i).clusterHead, 350, 50);
                        break;
                    case 3:
                        setTargetPosition(clusters.get(i).clusterHead, 50, 200);
                        break;
                    case 4:
                        setTargetPosition(clusters.get(i).clusterHead, 200, 200);
                        break;
                    case 5:
                        setTargetPosition(clusters.get(i).clusterHead, 350, 200);
                        break;
                    case 6:
                        setTargetPosition(clusters.get(i).clusterHead, 50, 350);
                        break;
                    case 7:
                        setTargetPosition(clusters.get(i).clusterHead, 200, 350);
                        break;
                }
            }
        } else if (topology == Topology.Line) {
            this.topology = topology;
            left = 0;
            top = 0;
            bottom = 400;
            right = 320;
            for (int i = 0; i < clusters.size(); i++) {
                switch (i) {
                    case 0:
                        setTargetPosition(clusters.get(i).clusterHead, 0, 0);
                        break;
                    case 1:
                        setTargetPosition(clusters.get(i).clusterHead, 0, 50);
                        break;
                    case 2:
                        setTargetPosition(clusters.get(i).clusterHead, 0, 100);
                        break;
                    case 3:
                        setTargetPosition(clusters.get(i).clusterHead, 0, 150);
                        break;
                    case 4:
                        setTargetPosition(clusters.get(i).clusterHead, 0, 200);
                        break;
                    case 5:
                        setTargetPosition(clusters.get(i).clusterHead, 0, 250);
                        break;
                    case 6:
                        setTargetPosition(clusters.get(i).clusterHead, 0, 300);
                        break;
                    case 7:
                        setTargetPosition(clusters.get(i).clusterHead, 0, 350);
                        break;
                }
            }
        }

    }

    public void setTargetPosition(UAV uav, int x, int y) {
        uav.targetX = x;
        uav.targetY = y;
    }

    public void rightMove() {
        drawCloud();
        for (int i = 0; i < clusters.size(); i++) {
            UAV uav = clusters.get(i).clusterHead;
            uav.targetX = uav.targetX + 10;
        }
        left += 10;
        right += 10;

    }

    public void downMove() {
        drawCloud();
        for (int i = 0; i < clusters.size(); i++) {
            UAV uav = clusters.get(i).clusterHead;
            uav.targetY = uav.targetY + 10;
        }
        top += 10;
        bottom += 10;
    }

    public void leftMove() {
        drawCloud();
        for (int i = 0; i < clusters.size(); i++) {
            UAV uav = clusters.get(i).clusterHead;
            uav.targetX = uav.targetX - 10;
        }
        left -= 10;
        right -= 10;

    }

    public void clusterMove() {
        if (moveStatus == ClusterMoveStatus.Right) {
            rightMove();
            if (right >= GUItil.getBounds().width) {
                moveStatus = ClusterMoveStatus.Down;
            }
        } else if (moveStatus == ClusterMoveStatus.Down) {
            downMove();
            if (topology == Topology.Grid) {
                if (bottom >= 1300 && right < GUItil.getBounds().width) {
                    moveStatus = ClusterMoveStatus.Right;
                } else if (bottom >= 850 && left > 0) {
                    moveStatus = ClusterMoveStatus.Left;
                }
            } else if (topology == Topology.Line) {
                if (bottom >= 1150 && right < GUItil.getBounds().width) {
                    moveStatus = ClusterMoveStatus.Right;
                } else if (bottom >= 770 && left > 0) {
                    moveStatus = ClusterMoveStatus.Left;
                }
            }

        } else if (moveStatus == ClusterMoveStatus.Left) {
            leftMove();
            if (left <= 0) {
                moveStatus = ClusterMoveStatus.Down;
            }
        }
    }


    public void drawCloud() {
        int lefts, rights, tops, bottoms;
        lefts = left/areaWidth;
        rights= right/areaWidth;
        tops = top/areaHeight;
        bottoms = bottom/areaHeight;
        for(int i=tops;i<=bottoms;i++){
            for(int j=lefts;j<=rights;j++){
                int index = i*26+j;
                try{
                    uavNetwork.cloudDraws.get(index).image = ImageRead.cloudBlue;
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

        }



    }

}
