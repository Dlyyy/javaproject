/*     */ package util;
/*     */ 
/*     */ import com.objectplanet.chart.BarChart;
/*     */ import entity.Record;
/*     */ import java.awt.Color;
/*     */ import java.awt.Font;
/*     */ import java.awt.Image;
/*     */ import java.util.List;
/*     */ import javax.swing.Icon;
/*     */ import javax.swing.ImageIcon;
/*     */ import javax.swing.JLabel;
/*     */ import javax.swing.JPanel;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ChartUtil
/*     */ {
/*     */   private static String[] sampleLabels(List<Record> rs)
/*     */   {
/*  20 */     String[] sampleLabels = new String[rs.size()];
/*  21 */     for (int i = 0; i < sampleLabels.length; i++) {
/*  22 */       if (i % 5 == 0) {
/*  23 */         sampleLabels[i] = String.valueOf(i + 1 + "日");
/*     */       }
/*     */     }
/*  26 */     return sampleLabels;
/*     */   }
/*     */   
/*     */   public static double[] sampleValues(List<Record> rs)
/*     */   {
/*  31 */     double[] sampleValues = new double[rs.size()];
/*  32 */     for (int i = 0; i < sampleValues.length; i++) {
/*  33 */       sampleValues[i] = ((Record)rs.get(i)).spend;
/*     */     }
/*     */     
/*  36 */     return sampleValues;
/*     */   }
/*     */   
/*     */   public static Image getImage(List<Record> rs, int width, int height)
/*     */   {
/*  41 */     double[] sampleValues = sampleValues(rs);
/*     */     
/*  43 */     String[] sampleLabels = sampleLabels(rs);
/*     */     
/*  45 */     int max = max(sampleValues);
/*     */     
/*     */ 
/*  48 */     Color[] sampleColors = { ColorUtil.blueColor };
/*     */     
/*     */ 
/*  51 */     BarChart chart = new BarChart();
/*     */     
/*     */ 
/*  54 */     chart.setSampleCount(sampleValues.length);
/*     */     
/*  56 */     chart.setSampleValues(0, sampleValues);
/*     */     
/*  58 */     chart.setSampleLabels(sampleLabels);
/*     */     
/*  60 */     chart.setSampleColors(sampleColors);
/*     */     
/*  62 */     chart.setRange(0, max * 1.2D);
/*     */     
/*  64 */     chart.setValueLinesOn(true);
/*     */     
/*  66 */     chart.setSampleLabelsOn(true);
/*     */     
/*  68 */     chart.setSampleLabelStyle(2);
/*     */     
/*     */ 
/*  71 */     chart.setFont("rangeLabelFont", new Font("Arial", 1, 12));
/*     */     
/*  73 */     chart.setLegendOn(true);
/*     */     
/*  75 */     chart.setLegendPosition(0);
/*     */     
/*  77 */     chart.setLegendLabels(new String[] { "月消费报表" });
/*     */     
/*  79 */     chart.setFont("legendFont", new Font("Dialog", 1, 13));
/*     */     
/*  81 */     chart.setFont("sampleLabelFont", new Font("Dialog", 1, 13));
/*     */     
/*  83 */     chart.setChartBackground(Color.white);
/*     */     
/*  85 */     chart.setBackground(ColorUtil.backgroundColor);
/*     */     
/*  87 */     Image im = chart.getImage(width, height);
/*  88 */     return im;
/*     */   }
/*     */   
/*     */   public static int max(double[] sampleValues) {
/*  92 */     int max = 0;
/*  93 */     double[] arrayOfDouble = sampleValues;int j = sampleValues.length; for (int i = 0; i < j; i++) { double v = arrayOfDouble[i];
/*  94 */       if (v > max)
/*  95 */         max = (int)v;
/*     */     }
/*  97 */     return max;
/*     */   }
/*     */   
/*     */   private static String[] sampleLabels()
/*     */   {
/* 102 */     String[] sampleLabels = new String[30];
/*     */     
/* 104 */     for (int i = 0; i < sampleLabels.length; i++) {
/* 105 */       if (i % 5 == 0)
/* 106 */         sampleLabels[i] = String.valueOf(i + 1 + "日");
/*     */     }
/* 108 */     return sampleLabels;
/*     */   }
/*     */   
/*     */   public static Image getImage(int width, int height)
/*     */   {
/* 113 */     double[] sampleValues = sampleValues();
/*     */     
/* 115 */     String[] sampleLabels = sampleLabels();
/*     */     
/* 117 */     int max = max(sampleValues);
/*     */     
/*     */ 
/* 120 */     Color[] sampleColors = { ColorUtil.blueColor };
/*     */     
/*     */ 
/* 123 */     BarChart chart = new BarChart();
/*     */     
/*     */ 
/* 126 */     chart.setSampleCount(sampleValues.length);
/*     */     
/* 128 */     chart.setSampleValues(0, sampleValues);
/*     */     
/* 130 */     chart.setSampleLabels(sampleLabels);
/*     */     
/* 132 */     chart.setSampleColors(sampleColors);
/*     */     
/* 134 */     chart.setRange(0, max * 1.2D);
/*     */     
/* 136 */     chart.setValueLinesOn(true);
/*     */     
/* 138 */     chart.setSampleLabelsOn(true);
/*     */     
/* 140 */     chart.setSampleLabelStyle(2);
/*     */     
/*     */ 
/* 143 */     chart.setFont("rangeLabelFont", new Font("Arial", 1, 12));
/*     */     
/* 145 */     chart.setLegendOn(true);
/*     */     
/* 147 */     chart.setLegendPosition(0);
/*     */     
/* 149 */     chart.setLegendLabels(new String[] { "月消费报表" });
/*     */     
/* 151 */     chart.setFont("legendFont", new Font("Dialog", 1, 13));
/*     */     
/* 153 */     chart.setFont("sampleLabelFont", new Font("Dialog", 1, 13));
/*     */     
/* 155 */     chart.setChartBackground(Color.white);
/*     */     
/* 157 */     chart.setBackground(ColorUtil.backgroundColor);
/*     */     
/* 159 */     Image im = chart.getImage(width, height);
/* 160 */     return im;
/*     */   }
/*     */   
/*     */   private static double[] sampleValues()
/*     */   {
/* 165 */     double[] result = new double[30];
/* 166 */     for (int i = 0; i < result.length; i++) {
/* 167 */       result[i] = ((int)(Math.random() * 300.0D));
/*     */     }
/* 169 */     return result;
/*     */   }
/*     */   
/*     */   public static void main(String[] args)
/*     */   {
/* 174 */     JPanel p = new JPanel();
/* 175 */     JLabel l = new JLabel();
/* 176 */     Image img = getImage(400, 300);
/* 177 */     Icon icon = new ImageIcon(img);
/* 178 */     l.setIcon(icon);
/* 179 */     p.add(l);
/* 180 */     GUIUtil.showPanel(p);
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\util\ChartUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */