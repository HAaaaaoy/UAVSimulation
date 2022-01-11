package route;

public class RouteTableItem {

    private int dst;
    private int next;

    public RouteTableItem(int dst, int next){
        this.dst = dst;
        this.next = next;
    }

    public RouteTableItem(){

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
}
