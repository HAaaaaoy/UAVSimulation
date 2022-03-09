package Curve;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class TwoTimeChart extends JFrame {


    public TwoTimeChart(String applicationTitle, String chartTitle, String xian, CopyOnWriteArrayList<Long> data) {
        super(applicationTitle);
        JFreeChart xylineChart = ChartFactory.createXYLineChart(
                "网络生成图",
                "组网时间/ms",
                "最大连通集(百分比）%",
                createDataset1(xian, data),
                PlotOrientation.VERTICAL,
                true, true, false);

        //改的标题的字体
        xylineChart.getLegend().setItemFont(new Font("微软雅黑", Font.BOLD, 12));
        xylineChart.getTitle().setFont(new Font("微软雅黑", Font.BOLD, 18));

        ChartPanel chartPanel = new ChartPanel(xylineChart);
        chartPanel.setPreferredSize(new Dimension(846, 567));




        JFreeChart xylineChart2 = ChartFactory.createXYLineChart(
                chartTitle,
                "连通节点数/个",
                "组网时间/ms",
                createDataset2(xian, data),
                PlotOrientation.VERTICAL,
                true, true, false);

        //改的标题的字体
        xylineChart2.getLegend().setItemFont(new Font("微软雅黑", Font.BOLD, 12));
        xylineChart2.getTitle().setFont(new Font("微软雅黑", Font.BOLD, 18));

        ChartPanel chartPanel2 = new ChartPanel(xylineChart2);
        chartPanel2.setPreferredSize(new Dimension(846, 567));

        final XYPlot plot = xylineChart.getXYPlot();
        // 设置网格背景颜色
        plot.setBackgroundPaint(Color.white);
        // 设置网格竖线颜色
        plot.setDomainGridlinePaint(Color.pink);
        // 设置网格横线颜色
        plot.setRangeGridlinePaint(Color.pink);




        final XYPlot plot2 = xylineChart2.getXYPlot();
        // 设置网格背景颜色
        plot2.setBackgroundPaint(Color.white);
        // 设置网格竖线颜色
        plot2.setDomainGridlinePaint(Color.pink);
        // 设置网格横线颜色
        plot2.setRangeGridlinePaint(Color.pink);


        //一张图上目前最多三条线，先设置好
        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        renderer.setSeriesPaint(0, Color.RED);
        renderer.setSeriesPaint(1, Color.BLACK);
        renderer.setSeriesPaint(2, Color.BLUE);

        renderer.setSeriesStroke(0, new BasicStroke(1));
        renderer.setSeriesStroke(1, new BasicStroke(1));
        renderer.setSeriesStroke(2, new BasicStroke(1));

//      renderer.setSeriesShape(0,star);//设置第一条曲线数据点的图形
        renderer.setBaseShapesVisible(true);//坐标点的形状是否可见
        renderer.setBaseShapesFilled(true);

        //一张图上目前最多三条线，先设置好
        XYLineAndShapeRenderer renderer1 = new XYLineAndShapeRenderer();
        renderer1.setSeriesPaint(0, Color.RED);
        renderer1.setSeriesPaint(1, Color.BLACK);
        renderer1.setSeriesPaint(2, Color.BLUE);

        renderer1.setSeriesStroke(0, new BasicStroke(1));
        renderer1.setSeriesStroke(1, new BasicStroke(1));
        renderer1.setSeriesStroke(2, new BasicStroke(1));

//      renderer.setSeriesShape(0,star);//设置第一条曲线数据点的图形
        renderer1.setBaseShapesVisible(true);//坐标点的形状是否可见
        renderer1.setBaseShapesFilled(true);

        plot.setRenderer(renderer);
        plot2.setRenderer(renderer1);
        this.add(chartPanel,BorderLayout.WEST);
        this.add(chartPanel2,BorderLayout.EAST);

        //解决中文乱码问题
        Font font1 = new Font("微软雅黑", Font.BOLD, 18);
        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        rangeAxis.setUpperMargin(0.15);
        rangeAxis.setLabelFont(font1);
        rangeAxis.setTickLabelFont(font1);
        NumberAxis domainAxis = (NumberAxis) plot.getDomainAxis();
        domainAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        domainAxis.setUpperMargin(0.15);
        domainAxis.setLabelFont(font1);
        domainAxis.setTickLabelFont(font1);

        Font font2 = new Font("微软雅黑", Font.BOLD, 18);
        NumberAxis rangeAxis2 = (NumberAxis) plot2.getRangeAxis();
        rangeAxis2.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        rangeAxis2.setUpperMargin(0.15);
        rangeAxis2.setLabelFont(font2);
        rangeAxis2.setTickLabelFont(font2);
        NumberAxis domainAxis2 = (NumberAxis) plot2.getDomainAxis();
        domainAxis2.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        domainAxis2.setUpperMargin(0.15);
        domainAxis2.setLabelFont(font2);
        domainAxis2.setTickLabelFont(font2);



        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }


    private XYDataset createDataset1(String name, CopyOnWriteArrayList<Long> data) {
        final XYSeries xian = new XYSeries(name);
        for (int i = 0; i < data.size(); i++) {
            xian.add((double)data.get(i),100*(i+1)/data.size());
        }
        final XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(xian);
        return dataset;
    }


    private XYDataset createDataset2(String name, CopyOnWriteArrayList<Long> data) {
        final XYSeries xian = new XYSeries(name);
        for (int i = 0; i < data.size(); i++) {
            xian.add(i + 1, data.get(i));
        }
        final XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(xian);
        return dataset;
    }
}
