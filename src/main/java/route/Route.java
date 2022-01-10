package route;

import GUItil.GUItil;
import UAVs.UAV;
import scene.UAVNetwork;

import java.util.concurrent.CopyOnWriteArrayList;

public class Route {

    //保存网络中所有的簇
    private CopyOnWriteArrayList<Cluster> clusters;


//    private static Route mInstance;

    public Route(){
        clusters = new CopyOnWriteArrayList<>();

    }

//    public synchronized static Route getInstance(){
//        if(mInstance == null){
//            mInstance = new Route();
//        }
//        return mInstance;
//    }

    public CopyOnWriteArrayList<Cluster> getClusters(){
        return clusters;
    }

    public void selectedCluster( UAV uav){
        this.clusters.add(uav.setCluster());
    }



}
