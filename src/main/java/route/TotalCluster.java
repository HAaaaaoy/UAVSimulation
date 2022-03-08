package route;

import GUItil.GUItil;
import UAVs.UAV;
import UAVs.network.Topology;
import image.ImageRead;
import scene.UAVNetwork;

import java.util.concurrent.CopyOnWriteArrayList;

public class TotalCluster {

    public CopyOnWriteArrayList<Cluster> clusters;
    public int left, right, top, bottom;
    public ClusterMoveStatus moveStatus = ClusterMoveStatus.Right;
    public ClusterMoveStatus lastMoveStatus;
    public Topology topology;

    public UAVNetwork uavNetwork;


    public TotalCluster(UAVNetwork uavNetwork) {
        this.uavNetwork = uavNetwork;
        clusters = new CopyOnWriteArrayList<>();
    }

    public void initStartPosition(Topology topology) {
        if (topology == Topology.Grid || topology==Topology.Circle) {
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
            if (right >= uavNetwork.width && bottom >= uavNetwork.height) {
                moveStatus = ClusterMoveStatus.Stop;
            } else if (right >= uavNetwork.width) {
                lastMoveStatus = ClusterMoveStatus.Right;
                moveStatus = ClusterMoveStatus.Down;
            }
        } else if (moveStatus == ClusterMoveStatus.Down) {
            downMove();
            if (topology == Topology.Grid) {
                if (((bottom <= uavNetwork.uavHeight * 9 * 2.5 && bottom >= uavNetwork.uavHeight * 9 * 2) || bottom >= uavNetwork.height) && lastMoveStatus == ClusterMoveStatus.Right) {
                    lastMoveStatus = ClusterMoveStatus.Down;
                    moveStatus = ClusterMoveStatus.Left;
                } else if ((bottom >= uavNetwork.uavHeight * 9 * 3 || bottom >= uavNetwork.height) && lastMoveStatus == ClusterMoveStatus.Left) {
                    moveStatus = ClusterMoveStatus.Right;
                    lastMoveStatus = ClusterMoveStatus.Down;
                }
            } else if (topology == Topology.Line) {
                if (((bottom <= uavNetwork.uavHeight * 8 * 2.5 && bottom >= uavNetwork.uavHeight * 8 * 2) || bottom >= uavNetwork.height) && lastMoveStatus == ClusterMoveStatus.Right) {
                    lastMoveStatus = ClusterMoveStatus.Down;
                    moveStatus = ClusterMoveStatus.Left;
                } else if ((bottom >= uavNetwork.uavHeight * 8 * 3 || bottom >= uavNetwork.height) && lastMoveStatus == ClusterMoveStatus.Left) {
                    moveStatus = ClusterMoveStatus.Right;
                    lastMoveStatus = ClusterMoveStatus.Down;
                }
            }

        } else if (moveStatus == ClusterMoveStatus.Left) {
            leftMove();
            if (left<=0 && bottom >= uavNetwork.height) {
                moveStatus = ClusterMoveStatus.Stop;
            }
            else if(left <= 0) {
                lastMoveStatus = ClusterMoveStatus.Left;
                moveStatus = ClusterMoveStatus.Down;
            }
        } else if (moveStatus == ClusterMoveStatus.Stop) {
            System.out.println("巡航仿真结束");
        }
    }


    public void drawCloud() {
        int lefts, rights, tops, bottoms;
        lefts = left / uavNetwork.areaWidth;
        rights = right / uavNetwork.areaWidth;
        tops = top / uavNetwork.areaHeight;
        bottoms = bottom / uavNetwork.areaHeight;
        for (int i = tops; i <= bottoms; i++) {
            for (int j = lefts; j <= rights; j++) {
                int index = i * 26 + j;
                try {
                    uavNetwork.cloudDraws.get(index).image = ImageRead.cloudBlue;
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }
    }
}
