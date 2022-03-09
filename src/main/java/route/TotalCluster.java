package route;

import GUItil.GUItil;
import UAVs.UAV;
import UAVs.network.Topology;
import image.ImageRead;
import scene.PlaneWars;
import scene.UAVNetwork;

import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class TotalCluster {

    public CopyOnWriteArrayList<Cluster> clusters;
    public CopyOnWriteArrayList<Cluster> secondClusters;


    public int left, right, top, bottom;
    public int bottom2;
    public ClusterMoveStatus moveStatus = ClusterMoveStatus.Right;
    public ClusterMoveStatus lastMoveStatus;
    public Topology topology;

    public UAVNetwork uavNetwork;

    public boolean isSpilt = false;

    public PlaneWars pw;

    public TotalCluster(UAVNetwork uavNetwork, PlaneWars pw) {
        this.uavNetwork = uavNetwork;
        this.pw = pw;
        clusters = new CopyOnWriteArrayList<>();
        secondClusters = new CopyOnWriteArrayList<>();
    }

    public void initStartPosition(Topology topology) {
        if (topology == Topology.Grid || topology == Topology.Circle) {
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


    public void rightMoves() {
        if (bottom2 >= uavNetwork.height) {
        } else {
            for (int i = 0; i < secondClusters.size(); i++) {
                UAV uav = secondClusters.get(i).clusterHead;
                uav.targetY = uav.targetY + 4;
            }
            bottom2 += 4;
        }
    }

    public void downMoves() {
        if (bottom2 >= uavNetwork.height) {
        } else {
            for (int i = 0; i < secondClusters.size(); i++) {
                UAV uav = secondClusters.get(i).clusterHead;
                uav.targetY = uav.targetY + 4;
            }
            bottom2 += 4;
        }
    }

    public void leftMoves() {
        if (bottom2 >= uavNetwork.height) {
        } else {
            for (int i = 0; i < secondClusters.size(); i++) {
                UAV uav = secondClusters.get(i).clusterHead;
                uav.targetY = uav.targetY + 4;
            }
            bottom2 += 4;
        }
    }


    public void clusterMove() {
        if (moveStatus == ClusterMoveStatus.Right) {
            rightMove();
            if (right >= uavNetwork.width && bottom >= uavNetwork.height) {
                moveStatus = ClusterMoveStatus.Stop;
                pw.runningFlag = false;
                pw.lastCruiseTime = PlaneWars.currentTime;
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
            } else if (topology == Topology.Circle) {
                if (((bottom <= uavNetwork.uavHeight * 9.5 * 2.5 && bottom >= uavNetwork.uavHeight * 9.5 * 2) || bottom >= uavNetwork.height) && lastMoveStatus == ClusterMoveStatus.Right) {
                    lastMoveStatus = ClusterMoveStatus.Down;
                    moveStatus = ClusterMoveStatus.Left;
                } else if ((bottom >= uavNetwork.uavHeight * 9.5 * 3 || bottom >= uavNetwork.height) && lastMoveStatus == ClusterMoveStatus.Left) {
                    moveStatus = ClusterMoveStatus.Right;
                    lastMoveStatus = ClusterMoveStatus.Down;
                }
            }

        } else if (moveStatus == ClusterMoveStatus.Left) {
            leftMove();
            if (left <= 0 && bottom >= uavNetwork.height) {
                moveStatus = ClusterMoveStatus.Stop;
            } else if (left <= 0) {
                lastMoveStatus = ClusterMoveStatus.Left;
                moveStatus = ClusterMoveStatus.Down;
            }
        } else if (moveStatus == ClusterMoveStatus.Stop) {
            pw.lastCruiseTime = PlaneWars.currentTime;
            System.out.println("巡航仿真结束");
        }
    }

    public void clusterMove(boolean b) {
        if (moveStatus == ClusterMoveStatus.Right) {
            rightMove();
            rightMoves();
            if (right >= uavNetwork.width && bottom >= uavNetwork.height) {
                moveStatus = ClusterMoveStatus.Stop;
                pw.runningFlag = false;
                pw.lastCruiseTime = PlaneWars.currentTime;
            } else if (right >= uavNetwork.width) {
                secondClusters.add(clusters.remove(clusters.size() - 1));
                secondClusters.add(clusters.remove(clusters.size() - 1));
                bottom2 = bottom;
                bottom = bottom-150;
                lastMoveStatus = ClusterMoveStatus.Right;
                moveStatus = ClusterMoveStatus.Down;
            }
        } else if (moveStatus == ClusterMoveStatus.Down) {
            downMove();
            downMoves();
            if (topology == Topology.Grid) {
                if (((bottom <= 1.7*uavNetwork.width/3 && bottom >= uavNetwork.width/2) || bottom >= uavNetwork.height) && lastMoveStatus == ClusterMoveStatus.Right) {
                    lastMoveStatus = ClusterMoveStatus.Down;
                    moveStatus = ClusterMoveStatus.Left;
                } else if (( bottom >= uavNetwork.height) && lastMoveStatus == ClusterMoveStatus.Left) {
                    moveStatus = ClusterMoveStatus.Right;
                    lastMoveStatus = ClusterMoveStatus.Down;
                }
            } else if (topology == Topology.Line) {
                if (((bottom <= 1.7*uavNetwork.width/3 && bottom >= uavNetwork.width/2) || bottom >= uavNetwork.height) && lastMoveStatus == ClusterMoveStatus.Right) {
                    lastMoveStatus = ClusterMoveStatus.Down;
                    moveStatus = ClusterMoveStatus.Left;
                } else if (( bottom >= uavNetwork.height) && lastMoveStatus == ClusterMoveStatus.Left) {
                    moveStatus = ClusterMoveStatus.Right;
                    lastMoveStatus = ClusterMoveStatus.Down;
                }
            } else if (topology == Topology.Circle) {
                if (((bottom <= 1.7*uavNetwork.width/3 && bottom >= uavNetwork.width/2) || bottom >= uavNetwork.height) && lastMoveStatus == ClusterMoveStatus.Right) {
                    lastMoveStatus = ClusterMoveStatus.Down;
                    moveStatus = ClusterMoveStatus.Left;
                } else if ((bottom >= uavNetwork.height) && lastMoveStatus == ClusterMoveStatus.Left) {
                    moveStatus = ClusterMoveStatus.Right;
                    lastMoveStatus = ClusterMoveStatus.Down;
                }
            }

        } else if (moveStatus == ClusterMoveStatus.Left) {
            leftMove();
            leftMoves();
            if (left <= 0 && bottom >= uavNetwork.height) {
                moveStatus = ClusterMoveStatus.Stop;
            } else if (left <= 0) {
                lastMoveStatus = ClusterMoveStatus.Left;
                moveStatus = ClusterMoveStatus.Down;
            }
        } else if (moveStatus == ClusterMoveStatus.Stop) {
            pw.lastCruiseTime = PlaneWars.currentTime;
            System.out.println("巡航仿真结束");
        }


    }


    public void reClusterMove() throws InterruptedException {
        if (moveStatus == ClusterMoveStatus.Right) {
            rightMove();
            if (right >= uavNetwork.width && bottom >= uavNetwork.height) {
                moveStatus = ClusterMoveStatus.Stop;
            } else if (right >= uavNetwork.width) {
                lastMoveStatus = ClusterMoveStatus.Right;
                moveStatus = ClusterMoveStatus.Down;
            } else if (left > uavNetwork.uavWidth * 9 * 1.25 && top < uavNetwork.uavHeight * 9 * 0.5) {
                //弹性损失节点
                if (!isSpilt) {
                    int[] random = new int[8];
                    random[0] = new Random().nextInt(2, 7);
                    random[1] = new Random().nextInt(2, 7);
                    random[2] = new Random().nextInt(2, 7);
                    random[3] = new Random().nextInt(2, 7);
                    random[4] = new Random().nextInt(2, 7);
                    random[5] = new Random().nextInt(2, 7);
                    random[6] = new Random().nextInt(2, 7);
                    random[7] = new Random().nextInt(2, 7);
                    for (int i = 0; i < clusters.size(); i++) {
                        UAV uav = clusters.get(i).clusterHead;
                        //或者使用unchecked((int)tick)也可
                        int index = uav.cluster.getMemberList().remove(random[i]);
                        uavNetwork.movingList.remove(uavNetwork.uavHashMap.get(index));
                    }
                    uavNetwork.clusterCount = 160;
                    isSpilt = true;
                }
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
            } else if (topology == Topology.Circle) {
                if (((bottom <= uavNetwork.uavHeight * 9.5 * 2.5 && bottom >= uavNetwork.uavHeight * 9.5 * 2) || bottom >= uavNetwork.height) && lastMoveStatus == ClusterMoveStatus.Right) {
                    lastMoveStatus = ClusterMoveStatus.Down;
                    moveStatus = ClusterMoveStatus.Left;
                } else if ((bottom >= uavNetwork.uavHeight * 9.5 * 3 || bottom >= uavNetwork.height) && lastMoveStatus == ClusterMoveStatus.Left) {
                    moveStatus = ClusterMoveStatus.Right;
                    lastMoveStatus = ClusterMoveStatus.Down;
                }
            }

        } else if (moveStatus == ClusterMoveStatus.Left) {
            leftMove();
            if (left <= 0 && bottom >= uavNetwork.height) {
                moveStatus = ClusterMoveStatus.Stop;
            } else if (left <= 0) {
                lastMoveStatus = ClusterMoveStatus.Left;
                moveStatus = ClusterMoveStatus.Down;
            }
        } else if (moveStatus == ClusterMoveStatus.Stop) {

            System.out.println("巡航仿真结束");
        }
    }


    public void drawCloud() {
        if (!pw.isCheck) {
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
        }else {
            int lefts, rights, tops, bottoms;
            lefts = left / uavNetwork.areaWidth;
            rights = right / uavNetwork.areaWidth;
            tops = top / uavNetwork.areaHeight;
            bottoms = bottom / uavNetwork.areaHeight;
            for (int i = tops; i <= bottoms; i++) {
                for (int j = lefts; j <= rights; j++) {
                    int index = i * 26 + j;
                    try {
                        uavNetwork.cloudDraws.get(index).image = ImageRead.cloudCopy;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }
        }
    }

}
