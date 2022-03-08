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
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class XYLineChart_AWT extends ApplicationFrame
{
   public XYLineChart_AWT( String applicationTitle, String chartTitle )
   {
      super(applicationTitle);
      JFreeChart xylineChart = ChartFactory.createXYLineChart(
         chartTitle ,
         "Gc最大连通集" ,
         "生成时间t" ,
         createDataset() ,
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


      XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
      renderer.setSeriesPaint(0 , Color.RED );
      renderer.setSeriesPaint(1 , Color.GREEN );

      renderer.setSeriesStroke(0 , new BasicStroke(2));
      renderer.setSeriesStroke(1 , new BasicStroke(2));

      renderer.setBaseShapesVisible(false);//坐标点的形状是否可见
      renderer.setBaseShapesFilled(false);

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


   }

   private XYDataset createDataset( )
   {
      final XYSeries xian = new XYSeries( "正常G拓扑巡航检测任务" );
      xian.add( 1 , 4 );
      xian.add( 2 , 3 );
      xian.add( 3 , 2 );
      xian.add( 4 , 7 );
      xian.add( 5 , 10 );
      xian.add( 6 , 15 );
      xian.add( 7 , 47 );
      xian.add( 8 , 37 );
      xian.add( 9 , 96 );
      xian.add( 10 , 105 );
      xian.add( 11 , 56 );
      xian.add( 12 , 38 );
      final XYSeries xian2 = new XYSeries( "受损G'拓扑巡航检测任务" );
      xian2.add( 1 , 47 );
      xian2.add( 2 , 7 );
      xian2.add( 3 , 6 );
      xian2.add( 4 , 4 );
      xian2.add( 5 , 3 );
      xian2.add( 6 , 6 );
      xian2.add( 7 , 10 );
      xian2.add( 8 , 20 );
      xian2.add( 9 , 34 );
      xian2.add( 10 , 46 );
      xian2.add( 11 , 96 );
      xian2.add( 12 , 134 );

      final XYSeriesCollection dataset = new XYSeriesCollection( );
      dataset.addSeries( xian );
      dataset.addSeries( xian2 );

      return dataset;
   }

   public static void main( String[ ] args )
   {
      XYLineChart_AWT chart = new XYLineChart_AWT("网络生成图", "网络生成图");
      chart.pack( );
      RefineryUtilities.centerFrameOnScreen( chart );
      chart.setVisible( true );
   }


   //获取List中Double数据的最大最小值
   public double getMin(List<Double> data){
      double res = data.get(0);
      for(int i=0; i<data.size()-1; i++){
         if(res>data.get(i+1)){
            res = data.get(i+1);
         }
      }
      return res;
   }


   //获取List中Double数据的最大最小值
   public double getMax(List<Double> data){
      double res = data.get(0);
      for(int i=0; i<data.size()-1; i++){
         if(res<data.get(i+1)){
            res = data.get(i+1);
         }
      }
      return res;
   }

   //计算一元多次方程前面的系数，其排列为xm.xm-1.…….x0(多项式次数从高到低排列)
   public List<Double> MatrixCalcu(double[][] d){
      int i = d.length -1;
      int j = d[0].length -1;
      List<Double> list = new ArrayList<Double>();
      double res = d[i][j]/d[i][j-1];
      list.add(res);
      for(int k=i-1; k>=0; k--){
         double num = d[k][j];
         for(int q=j-1; q>k; q--){
            num = num - d[k][q]*list.get(j-1-q);
         }
         res = num/d[k][k];
         list.add(res);
      }
      return list;
   }

   //以下代码为最小二乘法计算多项式系数
   //最小二乘法多项式拟合
   public List<Double> getCurveEquation(List<Double> x, List<Double> y, int m){
      if(x.size() != y.size() || x.size() <= m+1){
         return new ArrayList<Double>();
      }
      List<Double> result = new ArrayList<Double>();
      List<Double> S = new ArrayList<Double>();
      List<Double> T = new ArrayList<Double>();
      //计算S0 S1 …… S2m
      for(int i=0; i<=2*m; i++){
         double si = 0.0;
         for(double xx:x){
            si = si + Math.pow(xx, i);
         }
         S.add(si);
      }
      //计算T0 T1 …… Tm
      for(int j=0; j<=m; j++){
         double ti = 0.0;
         for(int k=0; k<y.size(); k++){
            ti = ti + y.get(k)*Math.pow(x.get(k), j);
         }
         T.add(ti);
      }
      //把S和T放入二维数组，作为矩阵
      double[][] matrix = new double[m+1][m+2];
      for(int k=0; k<m+1; k++){
         double[] matrixi = matrix[k];
         for(int q=0; q<m+1; q++){
            matrixi[q] = S.get(k+q);
         }
         matrixi[m+1] = T.get(k);
      }
      for(int p=0; p<matrix.length; p++){
         for(int pp=0; pp<matrix[p].length; pp++){
            System.out.print("  matrix["+p+"]["+pp+"]="+matrix[p][pp]);
         }
         System.out.println();
      }
      //把矩阵转化为三角矩阵
      matrix = this.matrixConvert(matrix);
      //计算多项式系数，多项式从高到低排列
      result = this.MatrixCalcu(matrix);
      return result;
   }

   public double[][] matrixConvert(double[][] d){
      for(int i=0; i<d.length-1; i++){
         double[] dd1 = d[i];
         double num1 = dd1[i];

         for(int j=i; j<d.length-1;j++ ){
            double[] dd2 = d[j+1];
            double num2 = dd2[i];

            for(int k=0; k<dd2.length; k++){
               dd2[k] = (dd2[k]*num1 - dd1[k]*num2);
            }
         }
      }
      for(int ii=0; ii<d.length; ii++){
         for(int kk=0; kk<d[ii].length; kk++)
            System.out.print(d[ii][kk]+"  ");
         System.out.println();
      }
      return d;
   }
}

