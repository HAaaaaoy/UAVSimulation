package GUI;

import Curve.XYLineChart_AWT;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import org.jfree.ui.RefineryUtilities;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;

public class ControlPanel {
    private JPanel panel;
    private JTextField 面积textField1;
    private JButton 网络生成时间图Button;
    private JButton 连通节点图Button;
    private JCheckBox 显示轨迹CheckBox;
    private JCheckBox 备份CheckBox;
    private JRadioButton 编队飞行RadioButton;
    private JRadioButton 智能恢复RadioButton;
    private JButton 开始仿真Button;
    private JSlider 线程速度slider1;
    private JTextField 个数textField2;
    private JTextField 速度textField3;
    private JTextField 通信范围textField4;
    private JButton 暂停Button;
    private JRadioButton 无恢复RadioButton;
    private JRadioButton 自定义RadioButton;
    private JRadioButton 常规恢复RadioButton;
    private JTabbedPane tabbedPane1;
    private JTabbedPane tabbedPane2;
    private JButton 编队时间比较Button;
    private JButton 巡航状态Button;
    private JButton 重构时间Button;
    private JButton 巡航状态对比Button;


    private NotificationForm notificationForm;

    public ControlPanel(NotificationForm notificationForm) {
        this.notificationForm = notificationForm;

        面积textField1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        个数textField2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        速度textField3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        通信范围textField4.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        线程速度slider1.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentMoved(ComponentEvent e) {
                super.componentMoved(e);
            }
        });
        开始仿真Button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
            }
        });
        暂停Button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
            }
        });
        网络生成时间图Button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedIndex = notificationForm.current;
                if (selectedIndex == 1) {
                    XYLineChart_AWT chart = new XYLineChart_AWT("连通节点图", "连通节点图", "连通节点数", "组网时间/ms", "随机拓扑", notificationForm.pw.randomMeshTime);
                    chart.pack();
                    RefineryUtilities.centerFrameOnScreen(chart);
                    chart.setVisible(true);
                } else if (selectedIndex == 2) {
                    XYLineChart_AWT chart = new XYLineChart_AWT("连通节点图", "连通节点图", "连通节点数", "组网时间/ms", "格状网络拓扑", notificationForm.pw.gridMeshTime);
                    chart.pack();
                    RefineryUtilities.centerFrameOnScreen(chart);
                    chart.setVisible(true);
                } else if (selectedIndex == 3) {
                    XYLineChart_AWT chart = new XYLineChart_AWT("连通节点图", "连通节点图", "连通节点数", "组网时间/ms", "球状网络拓扑", notificationForm.pw.circleMeshTime);
                    chart.pack();
                    RefineryUtilities.centerFrameOnScreen(chart);
                    chart.setVisible(true);
                } else if (selectedIndex == 4) {
                    XYLineChart_AWT chart = new XYLineChart_AWT("连通节点图", "连通节点图", "连通节点数", "组网时间/ms", "链状网络拓扑", notificationForm.pw.lineMeshTime);
                    chart.pack();
                    RefineryUtilities.centerFrameOnScreen(chart);
                    chart.setVisible(true);
                }
            }
        });
        连通节点图Button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedIndex = notificationForm.current;
                if (selectedIndex == 2) {
                    XYLineChart_AWT chart = new XYLineChart_AWT("编队状态图", "编队状态图", "编队完成度%", "编队时间/ms", "格状网络拓扑", notificationForm.pw.formationGridTime);
                    chart.pack();
                    RefineryUtilities.centerFrameOnScreen(chart);
                    chart.setVisible(true);
                } else if (selectedIndex == 3) {
                    XYLineChart_AWT chart = new XYLineChart_AWT("编队状态图", "编队状态图", "编队完成度%", "编队时间/ms", "球状网络拓扑", notificationForm.pw.formationCircleTime);
                    chart.pack();
                    RefineryUtilities.centerFrameOnScreen(chart);
                    chart.setVisible(true);
                } else if (selectedIndex == 4) {
                    XYLineChart_AWT chart = new XYLineChart_AWT("编队状态图", "编队状态图", "编队完成度%", "编队时间/ms", "链状网络拓扑", notificationForm.pw.formationLineTime);
                    chart.pack();
                    RefineryUtilities.centerFrameOnScreen(chart);
                    chart.setVisible(true);
                } else if (selectedIndex == 5) {
                    XYLineChart_AWT chart = new XYLineChart_AWT("编队状态图", "编队状态图", "编队完成度%", "编队时间/ms", "拓扑切换", notificationForm.pw.formationTransferTime);
                    chart.pack();
                    RefineryUtilities.centerFrameOnScreen(chart);
                    chart.setVisible(true);
                } else {

                }
            }
        });

        编队时间比较Button.addMouseListener(new MouseAdapter() {
            /**
             * {@inheritDoc}
             *
             * @param e
             */
            @Override
            public void mouseClicked(MouseEvent e) {
                XYLineChart_AWT chart = new XYLineChart_AWT("编队状态图", "编队状态图", "编队完成度%", "编队时间/ms", "格状拓扑", "球状拓扑","链状拓扑",notificationForm.pw.formationGridTime,notificationForm.pw.formationCircleTime,notificationForm.pw.formationLineTime);
                chart.pack();
                RefineryUtilities.centerFrameOnScreen(chart);
                chart.setVisible(true);
            }
        });

        巡航状态Button.addMouseListener(new MouseAdapter() {
            /**
             * {@inheritDoc}
             *
             * @param e
             */
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedIndex = notificationForm.current;
                if (selectedIndex == 6) {
                    XYLineChart_AWT chart = new XYLineChart_AWT("编队状态图", "编队状态图", "巡航时间s", "巡航完成度%", "格状网络拓扑", notificationForm.pw.cruiseTime, true);
                    chart.pack();
                    RefineryUtilities.centerFrameOnScreen(chart);
                    chart.setVisible(true);
                } else if (selectedIndex == 7) {
                    XYLineChart_AWT chart = new XYLineChart_AWT("编队状态图", "编队状态图", "巡航时间s", "巡航完成度%", "球状网络拓扑", notificationForm.pw.cruiseTime, true);
                    chart.pack();
                    RefineryUtilities.centerFrameOnScreen(chart);
                    chart.setVisible(true);
                } else if (selectedIndex == 8) {
                    XYLineChart_AWT chart = new XYLineChart_AWT("编队状态图", "编队状态图", "巡航时间s", "巡航完成度%", "链状网络拓扑", notificationForm.pw.cruiseTime, true);
                    chart.pack();
                    RefineryUtilities.centerFrameOnScreen(chart);
                    chart.setVisible(true);
                }
            }
        });

        重构时间Button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                XYLineChart_AWT chart = new XYLineChart_AWT("重构状态图", "重构状态图", "重构完成度%", "重构时间/ms", "拓扑切换", notificationForm.pw.reTime);
                chart.pack();
                RefineryUtilities.centerFrameOnScreen(chart);
                chart.setVisible(true);
            }
        });

        巡航状态对比Button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedIndex = notificationForm.current;
                int re = notificationForm.reButton;
                if (selectedIndex == 6 && re==1) {
                    XYLineChart_AWT chart = new XYLineChart_AWT("编队状态图", "编队状态图", "巡航时间s", "巡航完成度%", "格状网络拓扑","重构格状网络拓扑", notificationForm.pw.cruiseTime, notificationForm.pw.reCruiseTime);
                    chart.pack();
                    RefineryUtilities.centerFrameOnScreen(chart);
                    chart.setVisible(true);
                } else if (selectedIndex == 7 && re==2) {
                    XYLineChart_AWT chart = new XYLineChart_AWT("编队状态图", "编队状态图", "巡航时间s", "巡航完成度%", "球状网络拓扑","重构球状网络拓扑", notificationForm.pw.cruiseTime, notificationForm.pw.reCruiseTime );
                    chart.pack();
                    RefineryUtilities.centerFrameOnScreen(chart);
                    chart.setVisible(true);
                } else if (selectedIndex == 8 && re==3) {
                    XYLineChart_AWT chart = new XYLineChart_AWT("编队状态图", "编队状态图", "巡航时间s", "巡航完成度%", "链状网络拓扑","重构链状网络拓扑",notificationForm.pw.cruiseTime, notificationForm.pw.reCruiseTime);
                    chart.pack();
                    RefineryUtilities.centerFrameOnScreen(chart);
                    chart.setVisible(true);
                }
            }
        });
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }


    public JPanel getPanel() {
        return panel;
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
        panel = new JPanel();
        panel.setLayout(new GridLayoutManager(4, 3, new Insets(0, 0, 0, 0), -1, -1));
        tabbedPane2 = new JTabbedPane();
        panel.add(tabbedPane2, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(200, 200), null, 0, false));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(5, 1, new Insets(0, 0, 0, 0), -1, -1));
        tabbedPane2.addTab("控制面板", panel1);
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(5, 3, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel2, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        面积textField1 = new JTextField();
        面积textField1.setText("64");
        panel2.add(面积textField1, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        个数textField2 = new JTextField();
        个数textField2.setText("64");
        panel2.add(个数textField2, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        速度textField3 = new JTextField();
        速度textField3.setText("30");
        panel2.add(速度textField3, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label1 = new JLabel();
        label1.setText("区域面积");
        panel2.add(label1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("节点个数");
        panel2.add(label2, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label3 = new JLabel();
        label3.setText("节点速度");
        panel2.add(label3, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label4 = new JLabel();
        label4.setText("通信范围");
        panel2.add(label4, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        通信范围textField4 = new JTextField();
        通信范围textField4.setText("5");
        panel2.add(通信范围textField4, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label5 = new JLabel();
        label5.setText("km^2");
        panel2.add(label5, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label6 = new JLabel();
        label6.setText("个");
        panel2.add(label6, new GridConstraints(1, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label7 = new JLabel();
        label7.setText("m/s");
        panel2.add(label7, new GridConstraints(2, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label8 = new JLabel();
        label8.setText("km");
        panel2.add(label8, new GridConstraints(3, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        线程速度slider1 = new JSlider();
        panel2.add(线程速度slider1, new GridConstraints(4, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label9 = new JLabel();
        label9.setText("慢");
        panel2.add(label9, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label10 = new JLabel();
        label10.setText("快");
        panel2.add(label10, new GridConstraints(4, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel3, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        网络生成时间图Button = new JButton();
        网络生成时间图Button.setEnabled(true);
        网络生成时间图Button.setText("生成时间图");
        panel3.add(网络生成时间图Button, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        连通节点图Button = new JButton();
        连通节点图Button.setText("连通节点图");
        panel3.add(连通节点图Button, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new GridLayoutManager(4, 2, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel4, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        显示轨迹CheckBox = new JCheckBox();
        显示轨迹CheckBox.setSelected(true);
        显示轨迹CheckBox.setText("显示轨迹");
        panel4.add(显示轨迹CheckBox, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        备份CheckBox = new JCheckBox();
        备份CheckBox.setText("备份");
        panel4.add(备份CheckBox, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        编队飞行RadioButton = new JRadioButton();
        编队飞行RadioButton.setEnabled(true);
        编队飞行RadioButton.setSelected(true);
        编队飞行RadioButton.setText("编队飞行");
        panel4.add(编队飞行RadioButton, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        智能恢复RadioButton = new JRadioButton();
        智能恢复RadioButton.setText("智能恢复");
        panel4.add(智能恢复RadioButton, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        无恢复RadioButton = new JRadioButton();
        无恢复RadioButton.setText("无恢复");
        panel4.add(无恢复RadioButton, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        自定义RadioButton = new JRadioButton();
        自定义RadioButton.setText("自定义");
        panel4.add(自定义RadioButton, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        常规恢复RadioButton = new JRadioButton();
        常规恢复RadioButton.setText("常规恢复");
        panel4.add(常规恢复RadioButton, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JRadioButton radioButton1 = new JRadioButton();
        radioButton1.setText("其它");
        panel4.add(radioButton1, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel5 = new JPanel();
        panel5.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel5, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        开始仿真Button = new JButton();
        开始仿真Button.setText("开始仿真");
        panel5.add(开始仿真Button, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        暂停Button = new JButton();
        暂停Button.setText("暂停仿真");
        panel5.add(暂停Button, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        tabbedPane1 = new JTabbedPane();
        panel.add(tabbedPane1, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(200, 200), null, 0, false));
        final JPanel panel6 = new JPanel();
        panel6.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        tabbedPane1.addTab("日志", panel6);
        final Spacer spacer1 = new Spacer();
        panel.add(spacer1, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(-1, 10), null, 0, false));
        final Spacer spacer2 = new Spacer();
        panel.add(spacer2, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(-1, 10), null, 0, false));
        final Spacer spacer3 = new Spacer();
        panel.add(spacer3, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_FIXED, 1, null, new Dimension(10, -1), null, 0, false));
        final Spacer spacer4 = new Spacer();
        panel.add(spacer4, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_FIXED, 1, null, new Dimension(10, -1), null, 0, false));
        final Spacer spacer5 = new Spacer();
        panel.add(spacer5, new GridConstraints(2, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_FIXED, 1, null, new Dimension(10, -1), null, 0, false));
        final Spacer spacer6 = new Spacer();
        panel.add(spacer6, new GridConstraints(1, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_FIXED, 1, null, new Dimension(10, -1), null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return panel;
    }

}
