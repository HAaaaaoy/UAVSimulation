package text;

import javafx.scene.text.Text;

import java.awt.*;

public abstract class UAVsText extends Text {
    //跟随无人机的字体
    private int Reposition_x;
    private int Reposition_y;
    private int id;
    private long time;


    public UAVsText(int position_x, int position_y, int UAV_Height, int id) {
        this.Reposition_x = position_x - UAV_Height;
        this.Reposition_y = position_y + UAV_Height/2 + UAV_Height + 10;
        this.id = id;
    }

    public UAVsText(String s, int position_x, int position_y) {
        super(s);
        Reposition_x = position_x;
        Reposition_y = position_y;
    }

    public UAVsText(double v, double v1, String s, int position_x, int position_y) {
        super(v, v1, s);
        Reposition_x = position_x;
        Reposition_y = position_y;
    }

    @Override
    public String toString() {
        return
                "无人机编号=" + id +
                ", 感染时间花费" + time + "毫秒";
    }

    public int getReposition_x() {
        return Reposition_x;
    }

    public void setReposition_x(int reposition_x) {
        Reposition_x = reposition_x;
    }

    public int getReposition_y() {
        return Reposition_y;
    }

    public void setReposition_y(int reposition_y) {
        Reposition_y = reposition_y;
    }


    public int getid() {
        return id;
    }

    public void setid(int id) {
        this.id = id;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public void drawText(Graphics g){
        g.drawString(new RText(getReposition_x(),getReposition_y(),20, getid()).toString() , Reposition_x, Reposition_y);
    }

}
