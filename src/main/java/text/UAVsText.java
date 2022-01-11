package text;

import UAVs.UAV;
import javafx.scene.text.Text;
import org.apache.log4j.Logger;
import scene.UAVNetwork;

import java.awt.*;

public class UAVsText extends Text {
    //跟随无人机的字体
    private int Reposition_x;
    private int Reposition_y;
    private int id;
    private long time;
    private TextStatus textStatus;
    private UAV uav;
    Logger logger = Logger.getLogger(UAVsText.class);


    public UAVsText(int position_x, int position_y, int UAV_Height, int id, UAV uav) {
        this.Reposition_x = position_x - UAV_Height;
        this.Reposition_y = position_y + UAV_Height / 2 + UAV_Height + 10;
        this.id = id;
        this.uav = uav;
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

        if (textStatus.equals(TextStatus.ClusterHeader)) {
            return
                    "At" + time + ": 无人机" + id + "成为簇头";
        } else if (textStatus.equals(TextStatus.GateWay)) {
            return
                    "At" + time + ": 无人机" + id + "成为网关";
        }
        return
                "无人机编号-" + id + ", 感染时间花费" + time + "毫秒";
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

    public void updatePosition(){
        logger.info("开始绘制" +id + "-" + uav.position_index_y + "-" + uav.position_index_y );
        this.Reposition_x = uav.position_index_x;
        this.Reposition_y = uav.position_index_y -10;
    }

    public void setid(int id) {
        this.id = id;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public TextStatus getTextStatus() {
        return textStatus;
    }

    public void setTextStatus(TextStatus textStatus) {
        this.textStatus = textStatus;
    }

    public void drawText(Graphics g){
        updatePosition();

        g.drawString(this.toString() , Reposition_x, Reposition_y);
    }




}
