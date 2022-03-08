import GUI.MainFrame;

import javax.swing.*;

public class Start {
    public static void main(String[] args) {

        try {
            String lookAndFeel = "com.sun.java.swing.plaf.windows.WindowsLookAndFeel";
            UIManager.setLookAndFeel(lookAndFeel);
        } catch (Exception e) {
            e.printStackTrace();
        }
        MainFrame mainFrame = new MainFrame();
        mainFrame.load();
    }
}
