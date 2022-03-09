package GUI;

import UAVs.network.Topology;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import scene.PlaneWars;
import scene.SimulationStatus;
import scene.UAVNetwork;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class NotificationForm {
    private JPanel panel1;
    private JButton 退出Button;
    private JButton 系统Button;
    private JButton 帮助Button;
    private JButton 放大Button;
    private JButton 缩小Button;
    private JButton 返回Button;
    private JButton 导入Button;
    private JButton 自动调整画布位置Button;
    private JButton 还原Button;
    private JButton 随机拓扑生成Button;
    private JButton 格状网络拓扑Button;
    private JButton 球状网络拓扑Button;
    private JButton 链状网络拓扑Button;
    private JButton 巡航拓扑Button;
    private JButton 启动仿真Button;
    private JButton 切换拓扑Button;
    private JButton 弹性重构巡航Button;

    public PlaneWars pw;
    public int current = 0;

    public int reButton = 0;

    public NotificationForm(PlaneWars pw) {
        this.pw = pw;
        退出Button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
            }
        });

        随机拓扑生成Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                current = 1;
                Rectangle bounds = pw.getBounds();
                pw.setNetwork(new UAVNetwork(64));
                pw.setSize(bounds.width, bounds.height);
                pw.topologyStatus = Topology.Random;
                pw.uavNetwork.status = SimulationStatus.FindCluster;
                pw.initThread();
            }
        });

        格状网络拓扑Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                current = 2;
                Rectangle bounds = pw.getBounds();
                pw.setNetwork(new UAVNetwork(64));
                pw.setSize(bounds.width, bounds.height);
                pw.topologyStatus = Topology.Grid;
                pw.uavNetwork.status = SimulationStatus.FindCluster;
                pw.initThread();
            }
        });

        球状网络拓扑Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                current = 3;
                Rectangle bounds = pw.getBounds();
                pw.setNetwork(new UAVNetwork(64));
                pw.setSize(bounds.width, bounds.height);
                pw.topologyStatus = Topology.Circle;
                pw.uavNetwork.status = SimulationStatus.FindCluster;
                pw.initThread();
            }
        });

        链状网络拓扑Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                current = 4;
                Rectangle bounds = pw.getBounds();
                pw.setNetwork(new UAVNetwork(64));
                pw.setSize(bounds.width, bounds.height);
                pw.topologyStatus = Topology.Line;
                pw.uavNetwork.status = SimulationStatus.FindCluster;
                pw.initThread();
            }
        });

        还原Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pw.runningFlag = false;
                pw.uavNetwork = null;
                pw.removeAll();
            }
        });

        启动仿真Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pw.startSimulation();
            }
        });

        切换拓扑Button.addMouseListener(new MouseAdapter() {
            /**
             * {@inheritDoc}
             *
             * @param e
             */
            @Override
            public void mouseClicked(MouseEvent e) {
                current = 5;
                JPopupMenu pop = new JPopupMenu();//你的弹出菜单
                JMenuItem menu1 = new JMenuItem("格状->球状");
                JMenuItem menu2 = new JMenuItem("球状->格状");
                JMenuItem menu3 = new JMenuItem("链状->球状");
                JMenuItem menu4 = new JMenuItem("球状->链状");
                JMenuItem menu5 = new JMenuItem("链状->格状");
                JMenuItem menu6 = new JMenuItem("格状->链状");

                menu1.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        Rectangle bounds = pw.getBounds();
                        pw.setNetwork(new UAVNetwork(64));
                        pw.setSize(bounds.width, bounds.height);
                        pw.topologyStatus = Topology.Circle;
                        pw.uavNetwork.status = SimulationStatus.FindCluster;
                        pw.xToY(Topology.Grid);
                        pw.repaint();
                    }
                });

                menu2.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        Rectangle bounds = pw.getBounds();
                        pw.setNetwork(new UAVNetwork(64));
                        pw.setSize(bounds.width, bounds.height);
                        pw.topologyStatus = Topology.Grid;
                        pw.uavNetwork.status = SimulationStatus.FindCluster;
                        pw.xToY(Topology.Circle);
                        pw.repaint();
                    }
                });

                menu3.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        Rectangle bounds = pw.getBounds();
                        pw.setNetwork(new UAVNetwork(64));
                        pw.setSize(bounds.width, bounds.height);
                        pw.topologyStatus = Topology.Circle;
                        pw.uavNetwork.status = SimulationStatus.FindCluster;
                        pw.xToY(Topology.Line);
                        pw.repaint();
                    }
                });

                menu4.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        Rectangle bounds = pw.getBounds();
                        pw.setNetwork(new UAVNetwork(64));
                        pw.setSize(bounds.width, bounds.height);
                        pw.topologyStatus = Topology.Line;
                        pw.uavNetwork.status = SimulationStatus.FindCluster;
                        pw.xToY(Topology.Circle);
                        pw.repaint();
                    }
                });

                menu5.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        Rectangle bounds = pw.getBounds();
                        pw.setNetwork(new UAVNetwork(64));
                        pw.setSize(bounds.width, bounds.height);
                        pw.topologyStatus = Topology.Grid;
                        pw.uavNetwork.status = SimulationStatus.FindCluster;
                        pw.xToY(Topology.Line);
                        pw.repaint();
                    }
                });

                menu6.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        Rectangle bounds = pw.getBounds();
                        pw.setNetwork(new UAVNetwork(64));
                        pw.setSize(bounds.width, bounds.height);
                        pw.topologyStatus = Topology.Line;
                        pw.uavNetwork.status = SimulationStatus.FindCluster;
                        pw.xToY(Topology.Grid);
                        pw.repaint();
                    }
                });

                pop.add(menu1);
                pop.add(menu2);
                pop.add(menu3);
                pop.add(menu4);
                pop.add(menu5);
                pop.add(menu6);
                pop.show(切换拓扑Button, e.getX(), e.getY());
            }
        });

        巡航拓扑Button.addMouseListener(new MouseAdapter() {
            /**
             * {@inheritDoc}
             *
             * @param e
             */
            @Override
            public void mouseClicked(MouseEvent e) {
                JPopupMenu pop = new JPopupMenu();//你的弹出菜单
                JMenuItem menu1 = new JMenuItem("格状拓扑巡航");
                JMenuItem menu2 = new JMenuItem("球状拓扑巡航");
                JMenuItem menu3 = new JMenuItem("链状拓扑巡航");

                menu1.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        current = 6;
                        Rectangle bounds = pw.getBounds();
                        pw.setNetwork(new UAVNetwork(64));
                        pw.setSize(bounds.width, bounds.height);
                        pw.topologyStatus = Topology.Grid;
                        pw.uavNetwork.status = SimulationStatus.FindCluster;
                        pw.initThreads();
                    }
                });

                menu2.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        current = 7;
                        Rectangle bounds = pw.getBounds();
                        pw.setNetwork(new UAVNetwork(64));
                        pw.setSize(bounds.width, bounds.height);
                        pw.topologyStatus = Topology.Circle;
                        pw.uavNetwork.status = SimulationStatus.FindCluster;
                        pw.initThreads();
                    }
                });

                menu3.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        current = 8;
                        Rectangle bounds = pw.getBounds();
                        pw.setNetwork(new UAVNetwork(64));
                        pw.setSize(bounds.width, bounds.height);
                        pw.topologyStatus = Topology.Line;
                        pw.uavNetwork.status = SimulationStatus.FindCluster;
                        pw.initThreads();
                    }
                });
                pop.add(menu1);
                pop.add(menu2);
                pop.add(menu3);
                pop.show(巡航拓扑Button, e.getX(), e.getY());
            }
        });

        弹性重构巡航Button.addMouseListener(new MouseAdapter() {
            /**
             * {@inheritDoc}
             *
             * @param e
             */
            @Override
            public void mouseClicked(MouseEvent e) {
                JPopupMenu pop = new JPopupMenu();//你的弹出菜单
                JMenuItem menu1 = new JMenuItem("格状拓扑弹性重构巡航");
                JMenuItem menu2 = new JMenuItem("球状拓扑弹性重构巡航");
                JMenuItem menu3 = new JMenuItem("链状拓扑弹性重构巡航");
                menu1.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        reButton = 1;
                        Rectangle bounds = pw.getBounds();
                        pw.setNetwork(new UAVNetwork(51));
                        pw.uavNetwork.setClusterMemberNumber(6);
                        pw.setSize(bounds.width, bounds.height);
                        pw.topologyStatus = Topology.Grid;
                        pw.uavNetwork.status = SimulationStatus.FindCluster;
                        pw.reInitThreads();
                    }
                });

                menu2.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        reButton = 2;
                        Rectangle bounds = pw.getBounds();
                        pw.setNetwork(new UAVNetwork(51));
                        pw.uavNetwork.setClusterMemberNumber(6);
                        pw.setSize(bounds.width, bounds.height);
                        pw.topologyStatus = Topology.Circle;
                        pw.uavNetwork.status = SimulationStatus.FindCluster;
                        pw.reInitThreads();
                    }
                });

                menu3.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        reButton = 3;
                        Rectangle bounds = pw.getBounds();
                        pw.setNetwork(new UAVNetwork(51));
                        pw.uavNetwork.setClusterMemberNumber(6);
                        pw.setSize(bounds.width, bounds.height);
                        pw.topologyStatus = Topology.Line;
                        pw.uavNetwork.status = SimulationStatus.FindCluster;
                        pw.reInitThreads();
                    }
                });
                pop.add(menu1);
                pop.add(menu2);
                pop.add(menu3);
                pop.show(弹性重构巡航Button, e.getX(), e.getY());
            }
        });
    }

    public JPanel getPanel1() {
        return panel1;
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(2, 3, new Insets(0, 0, 0, 0), -1, -1));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(1, 17, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel2, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        退出Button = new JButton();
        退出Button.setText("退出");
        panel2.add(退出Button, new GridConstraints(0, 16, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        自动调整画布位置Button = new JButton();
        自动调整画布位置Button.setText("自动调整画布位置");
        panel2.add(自动调整画布位置Button, new GridConstraints(0, 15, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        导入Button = new JButton();
        导入Button.setText("导入");
        panel2.add(导入Button, new GridConstraints(0, 5, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        返回Button = new JButton();
        返回Button.setText("返回");
        panel2.add(返回Button, new GridConstraints(0, 4, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        缩小Button = new JButton();
        缩小Button.setText("缩小");
        panel2.add(缩小Button, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        放大Button = new JButton();
        放大Button.setText("放大");
        panel2.add(放大Button, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        系统Button = new JButton();
        系统Button.setText("系统");
        panel2.add(系统Button, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        帮助Button = new JButton();
        帮助Button.setText("帮助");
        panel2.add(帮助Button, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        随机拓扑生成Button = new JButton();
        随机拓扑生成Button.setText("随机拓扑生成");
        panel2.add(随机拓扑生成Button, new GridConstraints(0, 6, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        格状网络拓扑Button = new JButton();
        格状网络拓扑Button.setText("格状网络拓扑");
        panel2.add(格状网络拓扑Button, new GridConstraints(0, 7, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        球状网络拓扑Button = new JButton();
        球状网络拓扑Button.setText("球状网络拓扑");
        panel2.add(球状网络拓扑Button, new GridConstraints(0, 8, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        链状网络拓扑Button = new JButton();
        链状网络拓扑Button.setText("链状网络拓扑");
        panel2.add(链状网络拓扑Button, new GridConstraints(0, 9, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        巡航拓扑Button = new JButton();
        巡航拓扑Button.setText("巡航拓扑");
        panel2.add(巡航拓扑Button, new GridConstraints(0, 11, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        切换拓扑Button = new JButton();
        切换拓扑Button.setText("切换拓扑");
        panel2.add(切换拓扑Button, new GridConstraints(0, 10, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        弹性重构巡航Button = new JButton();
        弹性重构巡航Button.setText("弹性重构巡航");
        panel2.add(弹性重构巡航Button, new GridConstraints(0, 12, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        还原Button = new JButton();
        还原Button.setText("还原/停止");
        panel2.add(还原Button, new GridConstraints(0, 14, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        启动仿真Button = new JButton();
        启动仿真Button.setText("启动仿真");
        panel2.add(启动仿真Button, new GridConstraints(0, 13, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        panel1.add(spacer1, new GridConstraints(1, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final Spacer spacer2 = new Spacer();
        panel1.add(spacer2, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_FIXED, 1, null, new Dimension(10, -1), null, 0, false));
        final Spacer spacer3 = new Spacer();
        panel1.add(spacer3, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(-1, 10), null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return panel1;
    }

}
