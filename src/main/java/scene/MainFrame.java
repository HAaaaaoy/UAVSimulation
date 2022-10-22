package scene;

import GUItil.GUItil;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class MainFrame extends JFrame {
    //创建Jpanle的对象
    PlaneWars pw = new PlaneWars();

    //创建MainFrame的构造器，初始化对象
    public MainFrame() throws IOException {
        pw.setFocusable(true);
        pw.requestFocus();
        this.add(pw, BorderLayout.CENTER);
    }

    public void start() {
        pw.startSimulation();
    }

    public static void main(String[] args) throws IOException {
        MainFrame mf = new MainFrame();
        mf.start();
        mf.setTitle("无人机蜂群仿真系统V1.0");
        Rectangle bounds = GUItil.getBounds();
        mf.setBounds(bounds);
        mf.setExtendedState(JFrame.MAXIMIZED_BOTH);
        mf.setLocationRelativeTo(null);
        mf.setDefaultCloseOperation(EXIT_ON_CLOSE);
        //窗体可拉伸
        mf.setResizable(true);
        mf.setVisible(true);
    }
}
