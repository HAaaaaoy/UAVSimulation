package text;

public class NRText extends UAVsText {

    public NRText(int position_x, int position_y, int UAV_Height, int id) {
        super(position_x, position_y, UAV_Height, id);
    }

    public NRText(String s, int position_x, int position_y) {
        super(s, position_x, position_y);
    }

    public NRText(double v, double v1, String s, int position_x, int position_y) {
        super(v, v1, s, position_x, position_y);
    }
}
