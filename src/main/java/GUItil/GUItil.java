package GUItil;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

public class GUItil {

    public static Rectangle getBounds() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        //保证主界面不会覆盖电脑屏幕的任务栏
        Insets screenInsets = Toolkit.getDefaultToolkit().getScreenInsets(new JFrame().getGraphicsConfiguration());
        Rectangle rectangle = new Rectangle(screenInsets.left, screenInsets.top, screenSize.width - screenInsets.left - screenInsets.right,
                screenSize.height - screenInsets.top - screenInsets.bottom);
        return rectangle;
    }

    public static ArrayList<Integer> getCenter() {
            int x = getBounds().width/2;
            int y = getBounds().height/2;
            ArrayList<Integer> center = new ArrayList<>();
            center.add(x);
            center.add(y);
            return center;
    }

    //符合这个项目要求的版本
    public static Integer getKeyByValue2(Map map, Object value) {
        Integer keys = null;
        Iterator it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            ArrayList list = (ArrayList) entry.getValue();
            Iterator iterator = list.iterator();
            while (iterator.hasNext()) {
                if (iterator.next().equals(value)) {
                    keys = (Integer) entry.getKey();
                }
            }
        }
        return keys;
    }


}
