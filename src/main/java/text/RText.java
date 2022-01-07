package text;

public class RText extends UAVsText {


    private static long time;
    public RText(int position_x, int position_y, int UAV_Height, int id) {
        super(position_x, position_y, UAV_Height, id);
    }

    public RText(String s, int position_x, int position_y) {
        super(s, position_x, position_y);
    }

    public RText(double v, double v1, String s, int position_x, int position_y) {
        super(v, v1, s, position_x, position_y);
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
