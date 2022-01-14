package route;

public class RouteTableItem {

    private int dst;
    private int next;
    private int hopNum;

    public RouteTableItem(int dst, int next, int hopNum) {
        this.dst = dst;
        this.next = next;
        this.hopNum = hopNum;
    }


    /**
     * 复制构造函数
     */
    public RouteTableItem(RouteTableItem copyItem) {
        setDst(copyItem.getDst());
        setNext(copyItem.getNext());
        setHopNum(copyItem.getHopNum());
    }

    //跳数自增
    public void increaseHopNum(int distance) {
        hopNum = hopNum + distance;
    }


    public int getDst() {
        return dst;
    }

    public void setDst(int dst) {
        this.dst = dst;
    }

    public int getNext() {
        return next;
    }

    public void setNext(int next) {
        this.next = next;
    }

    public int getHopNum() {
        return hopNum;
    }

    public void setHopNum(int hopNum) {
        this.hopNum = hopNum;
    }

    /**
     * 判断路由表项是否有相同下一跳路由器
     */
    public boolean equalsNextRoute(RouteTableItem item) {
        if (this.next == item.getNext()) {
            return true;
        } else {
            return false;
        }

        //return this.nextRouter.equals(item.getNextRouter());
    }

    /**
     * 判断路由表项是否有相同目的网络
     */
    public boolean equalsDst(RouteTableItem item) {
        if (this.dst == item.getDst()) {
            return true;
        } else {
            return false;
        }
    }

}
