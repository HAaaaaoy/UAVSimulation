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
import org.jfree.ui.RefineryUtilities;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class XYLineChart_AWT extends JFrame
{
   public XYLineChart_AWT(String applicationTitle, String chartTitle, String xAxisLabel, String yAxisLabel, String xian, CopyOnWriteArrayList<Long> data)
   {
      super(applicationTitle);
      JFreeChart xylineChart = ChartFactory.createXYLineChart(
         chartTitle ,
         xAxisLabel ,
         yAxisLabel ,
         createDataset(xian, data) ,
         PlotOrientation.VERTICAL ,
         true , true , false);

      //改的标题的字体
      xylineChart.getLegend().setItemFont(new Font("微软雅黑", Font.BOLD, 12));
      xylineChart.getTitle().setFont(new Font("微软雅黑", Font.BOLD, 18));

      ChartPanel chartPanel = new ChartPanel(xylineChart);
      chartPanel.setPreferredSize(new Dimension(846 ,567));

      final XYPlot plot = xylineChart.getXYPlot();
      // 设置网格背景颜色
      plot.setBackgroundPaint(Color.white);
      // 设置网格竖线颜色
      plot.setDomainGridlinePaint(Color.pink);
      // 设置网格横线颜色
      plot.setRangeGridlinePaint(Color.pink);


      //一张图上目前最多三条线，先设置好
      XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
      renderer.setSeriesPaint(0, Color.RED );
      renderer.setSeriesPaint(1, Color.BLACK );
      renderer.setSeriesPaint(2, Color.BLUE);

      renderer.setSeriesStroke(0, new BasicStroke(1));
      renderer.setSeriesStroke(1, new BasicStroke(1));
      renderer.setSeriesStroke(2, new BasicStroke(1));

//      renderer.setSeriesShape(0,star);//设置第一条曲线数据点的图形
      renderer.setBaseShapesVisible(true);//坐标点的形状是否可见
      renderer.setBaseShapesFilled(true);

      plot.setRenderer(renderer);
      setContentPane(chartPanel);

      //解决中文乱码问题
      Font font1 = new Font("微软雅黑",Font.BOLD,18);
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
      this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
   }

   public XYLineChart_AWT(String applicationTitle, String chartTitle, String xAxisLabel, String yAxisLabel, String xian, CopyOnWriteArrayList<Long> data, int num)
   {
      super(applicationTitle);
      JFreeChart xylineChart = ChartFactory.createXYLineChart(
              chartTitle ,
              xAxisLabel ,
              yAxisLabel ,
              createDataset(xian, data) ,
              PlotOrientation.VERTICAL ,
              true , true , false);

      //改的标题的字体
      xylineChart.getLegend().setItemFont(new Font("微软雅黑", Font.BOLD, 12));
      xylineChart.getTitle().setFont(new Font("微软雅黑", Font.BOLD, 18));

      ChartPanel chartPanel = new ChartPanel(xylineChart);
      chartPanel.setPreferredSize(new Dimension(846 ,567));

      final XYPlot plot = xylineChart.getXYPlot();
      // 设置网格背景颜色
      plot.setBackgroundPaint(Color.white);
      // 设置网格竖线颜色
      plot.setDomainGridlinePaint(Color.pink);
      // 设置网格横线颜色
      plot.setRangeGridlinePaint(Color.pink);

      //一张图上目前最多三条线，先设置好
      XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
      renderer.setSeriesPaint(0, Color.RED );
      renderer.setSeriesPaint(1, Color.BLACK );
      renderer.setSeriesPaint(2, Color.BLUE);

      renderer.setSeriesStroke(0, new BasicStroke(1));
      renderer.setSeriesStroke(1, new BasicStroke(1));
      renderer.setSeriesStroke(2, new BasicStroke(1));

//      renderer.setSeriesShape(0,star);//设置第一条曲线数据点的图形
      renderer.setBaseShapesVisible(true);//坐标点的形状是否可见
      renderer.setBaseShapesFilled(true);

      plot.setRenderer(renderer);
      setContentPane(chartPanel);
      this.add(chartPanel, BorderLayout.WEST);
      this.add(chartPanel, BorderLayout.EAST);

      //解决中文乱码问题
      Font font1 = new Font("微软雅黑",Font.BOLD,18);
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
      this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
   }



   public XYLineChart_AWT(String applicationTitle, String chartTitle, String xAxisLabel, String yAxisLabel, String xian, CopyOnWriteArrayList<Long> data, boolean b)
   {
      super(applicationTitle);
      JFreeChart xylineChart = ChartFactory.createXYLineChart(
              chartTitle ,
              xAxisLabel ,
              yAxisLabel ,
              createDataset(xian, data,b) ,
              PlotOrientation.VERTICAL ,
              true , true , false);

      //改的标题的字体
      xylineChart.getLegend().setItemFont(new Font("微软雅黑", Font.BOLD, 12));
      xylineChart.getTitle().setFont(new Font("微软雅黑", Font.BOLD, 18));

      ChartPanel chartPanel = new ChartPanel(xylineChart);
      chartPanel.setPreferredSize(new Dimension(846 ,567));

      final XYPlot plot = xylineChart.getXYPlot();
      // 设置网格背景颜色
      plot.setBackgroundPaint(Color.white);
      // 设置网格竖线颜色
      plot.setDomainGridlinePaint(Color.pink);
      // 设置网格横线颜色
      plot.setRangeGridlinePaint(Color.pink);


      //一张图上目前最多三条线，先设置好
      XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
      renderer.setSeriesPaint(0, Color.RED );
      renderer.setSeriesPaint(1, Color.BLACK );
      renderer.setSeriesPaint(2, Color.BLUE);

      renderer.setSeriesStroke(0, new BasicStroke(1));
      renderer.setSeriesStroke(1, new BasicStroke(1));
      renderer.setSeriesStroke(2, new BasicStroke(1));

//      renderer.setSeriesShape(0,star);//设置第一条曲线数据点的图形
      renderer.setBaseShapesVisible(true);//坐标点的形状是否可见
      renderer.setBaseShapesFilled(true);

      plot.setRenderer(renderer);
      setContentPane(chartPanel);

      //解决中文乱码问题
      Font font1 = new Font("微软雅黑",Font.BOLD,18);
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
      this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
   }

   public XYLineChart_AWT(String applicationTitle, String chartTitle, String xAxisLabel, String yAxisLabel, String xian1, String xian2, String xian3 , CopyOnWriteArrayList<Long> data1, CopyOnWriteArrayList<Long> data2, CopyOnWriteArrayList<Long> data3)
   {
      super(applicationTitle);
      JFreeChart xylineChart = ChartFactory.createXYLineChart(
              chartTitle ,
              xAxisLabel ,
              yAxisLabel ,
              createDataset(xian1, xian2, xian3, data1,data2,data3) ,
              PlotOrientation.VERTICAL ,
              true , true , false);

      //改的标题的字体
      xylineChart.getLegend().setItemFont(new Font("微软雅黑", Font.BOLD, 12));
      xylineChart.getTitle().setFont(new Font("微软雅黑", Font.BOLD, 18));

      ChartPanel chartPanel = new ChartPanel(xylineChart);
      chartPanel.setPreferredSize(new Dimension(846 ,567));

      final XYPlot plot = xylineChart.getXYPlot();
      // 设置网格背景颜色
      plot.setBackgroundPaint(Color.white);
      // 设置网格竖线颜色
      plot.setDomainGridlinePaint(Color.pink);
      // 设置网格横线颜色
      plot.setRangeGridlinePaint(Color.pink);


      //一张图上目前最多三条线，先设置好
      XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
      renderer.setSeriesPaint(0, Color.RED );
      renderer.setSeriesPaint(1, Color.BLACK );
      renderer.setSeriesPaint(2, Color.BLUE);

      renderer.setSeriesStroke(0, new BasicStroke(1));
      renderer.setSeriesStroke(1, new BasicStroke(1));
      renderer.setSeriesStroke(2, new BasicStroke(1));

//      renderer.setSeriesShape(0,star);//设置第一条曲线数据点的图形
      renderer.setBaseShapesVisible(true);//坐标点的形状是否可见
      renderer.setBaseShapesFilled(true);

      plot.setRenderer(renderer);
      setContentPane(chartPanel);

      //解决中文乱码问题
      Font font1 = new Font("微软雅黑",Font.BOLD,18);

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

      this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
   }


   public XYLineChart_AWT(String applicationTitle, String chartTitle, String xAxisLabel, String yAxisLabel, String xian1, String xian2,CopyOnWriteArrayList<Long> data1, CopyOnWriteArrayList<Long> data2)
   {
      super(applicationTitle);
      JFreeChart xylineChart = ChartFactory.createXYLineChart(
              chartTitle ,
              xAxisLabel ,
              yAxisLabel ,
              createDataset(xian1, xian2, data1, data2) ,
              PlotOrientation.VERTICAL ,
              true , true , false);

      //改的标题的字体
      xylineChart.getLegend().setItemFont(new Font("微软雅黑", Font.BOLD, 12));
      xylineChart.getTitle().setFont(new Font("微软雅黑", Font.BOLD, 18));

      ChartPanel chartPanel = new ChartPanel(xylineChart);
      chartPanel.setPreferredSize(new Dimension(846 ,567));

      final XYPlot plot = xylineChart.getXYPlot();
      // 设置网格背景颜色
      plot.setBackgroundPaint(Color.white);
      // 设置网格竖线颜色
      plot.setDomainGridlinePaint(Color.pink);
      // 设置网格横线颜色
      plot.setRangeGridlinePaint(Color.pink);


      //一张图上目前最多三条线，先设置好
      XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
      renderer.setSeriesPaint(0, Color.RED );
      renderer.setSeriesPaint(1, Color.BLACK );
      renderer.setSeriesPaint(2, Color.BLUE);

      renderer.setSeriesStroke(0, new BasicStroke(1));
      renderer.setSeriesStroke(1, new BasicStroke(1));
      renderer.setSeriesStroke(2, new BasicStroke(1));

//      renderer.setSeriesShape(0,star);//设置第一条曲线数据点的图形
      renderer.setBaseShapesVisible(true);//坐标点的形状是否可见
      renderer.setBaseShapesFilled(true);

      plot.setRenderer(renderer);
      setContentPane(chartPanel);

      //解决中文乱码问题
      Font font1 = new Font("微软雅黑",Font.BOLD,18);

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

      this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
   }

   private XYDataset createDataset(String title1, CopyOnWriteArrayList<Long> data)
   {
      final XYSeries xian = new XYSeries(title1);
      for(int i=0;i<data.size();i++){
         xian.add(i+1,data.get(i));
      }
      final XYSeriesCollection dataset = new XYSeriesCollection( );
      dataset.addSeries(xian);
      return dataset;
   }

   private XYDataset createDataset(String title1, CopyOnWriteArrayList<Long> data, boolean b)
   {
      final XYSeries xian = new XYSeries(title1);
      for(int i=0;i<data.size();i++){
         xian.add(i*50,100*data.get(i)/(26*16));
      }
      final XYSeriesCollection dataset = new XYSeriesCollection( );
      dataset.addSeries(xian);
      return dataset;
   }


   private XYDataset createDataset(String title1, String title2,  CopyOnWriteArrayList<Long> data1, CopyOnWriteArrayList<Long> data2)
   {
      final XYSeries xian = new XYSeries(title1);
      for(int i=0;i<data1.size();i++){
         xian.add(i*50,100*data1.get(i)/(26*16));
      }
      final XYSeries xian2 = new XYSeries(title2);
      for(int i=0;i<data2.size();i++){
         xian2.add(i*54,100*data2.get(i)/(26*16));
      }

      final XYSeriesCollection dataset = new XYSeriesCollection( );
      dataset.addSeries(xian);
      dataset.addSeries(xian2);
      return dataset;
   }

   private XYDataset createDataset(String title1, String title2, String title3, CopyOnWriteArrayList<Long> data1, CopyOnWriteArrayList<Long> data2, CopyOnWriteArrayList<Long> data3)
   {
      final XYSeries xian = new XYSeries(title1);
      for(int i=0;i<data1.size();i++){
         xian.add(i+1,data1.get(i));
      }
      final XYSeries xian2 = new XYSeries(title2);
      for(int i=0;i<data2.size();i++){
         xian2.add(i+1,data2.get(i));
      }
      final  XYSeries xian3 = new XYSeries(title3);
      for(int i=0;i<data3.size();i++){
         xian3.add(i+1,data3.get(i));
      }
      final XYSeriesCollection dataset = new XYSeriesCollection( );
      dataset.addSeries(xian);
      dataset.addSeries(xian2);
      dataset.addSeries(xian3);
      return dataset;
   }


}

