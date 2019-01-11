/*     */ package gui.panel;
/*     */ 
/*     */ import gui.page.SpendPage;
/*     */ import java.awt.BorderLayout;
/*     */ import java.awt.Component;
/*     */ import java.awt.Font;
/*     */ import java.awt.GridLayout;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JLabel;
/*     */ import javax.swing.JPanel;
/*     */ import service.SpendService;
/*     */ import util.CircleProgressBar;
/*     */ import util.ColorUtil;
/*     */ import util.GUIUtil;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SpendPanel
/*     */   extends WorkingPanel
/*     */ {
/*  21 */   public static SpendPanel instance = new SpendPanel();
/*     */   
/*  23 */   JLabel lMonthSpend = new JLabel("本月消费");
/*  24 */   JLabel lTodaySpend = new JLabel("今日消费");
/*  25 */   JLabel lAvgSpendPerDay = new JLabel("日均消费");
/*  26 */   JLabel lMonthLeft = new JLabel("本月剩余");
/*  27 */   JLabel lDayAvgAvailable = new JLabel("日均可用");
/*  28 */   JLabel lMonthLeftDay = new JLabel("距离月末");
/*     */   
/*  30 */   JLabel vMonthSpend = new JLabel("￥2300");
/*  31 */   JLabel vTodaySpend = new JLabel("￥25");
/*  32 */   JLabel vAvgSpendPerDay = new JLabel("￥120");
/*  33 */   JLabel vMonthAvailable = new JLabel("￥2084");
/*  34 */   JLabel vDayAvgAvailable = new JLabel("￥389");
/*  35 */   JLabel vMonthLeftDay = new JLabel("15天");
/*     */   CircleProgressBar bar;
/*     */   
/*     */   public SpendPanel()
/*     */   {
/*  40 */     setLayout(new BorderLayout());
/*  41 */     this.bar = new CircleProgressBar();
/*  42 */     this.bar.setBackgroundColor(ColorUtil.blueColor);
/*     */     
/*  44 */     GUIUtil.setColor(ColorUtil.grayColor, new JComponent[] { this.lMonthSpend, this.lTodaySpend, this.lAvgSpendPerDay, this.lMonthLeft, this.lDayAvgAvailable, 
/*  45 */       this.lMonthLeftDay, this.vAvgSpendPerDay, this.vMonthAvailable, this.vDayAvgAvailable, this.vMonthLeftDay });
/*  46 */     GUIUtil.setColor(ColorUtil.blueColor, new JComponent[] { this.vMonthSpend, this.vTodaySpend });
/*     */     
/*  48 */     this.vMonthSpend.setFont(new Font("微软雅黑", 1, 23));
/*  49 */     this.vTodaySpend.setFont(new Font("微软雅黑", 1, 23));
/*     */     
/*  51 */     add(center(), "Center");
/*  52 */     add(south(), "South");
/*     */   }
/*     */   
/*     */   private JPanel center()
/*     */   {
/*  57 */     JPanel p = new JPanel();
/*  58 */     p.setLayout(new BorderLayout());
/*  59 */     p.add(west(), "West");
/*  60 */     p.add(east());
/*     */     
/*  62 */     return p;
/*     */   }
/*     */   
/*     */   private Component east()
/*     */   {
/*  67 */     return this.bar;
/*     */   }
/*     */   
/*     */   private Component west() {
/*  71 */     JPanel p = new JPanel();
/*  72 */     p.setLayout(new GridLayout(4, 1));
/*  73 */     p.add(this.lMonthSpend);
/*  74 */     p.add(this.vMonthSpend);
/*  75 */     p.add(this.lTodaySpend);
/*  76 */     p.add(this.vTodaySpend);
/*  77 */     return p;
/*     */   }
/*     */   
/*     */   private JPanel south() {
/*  81 */     JPanel p = new JPanel();
/*  82 */     p.setLayout(new GridLayout(2, 4));
/*     */     
/*  84 */     p.add(this.lAvgSpendPerDay);
/*  85 */     p.add(this.lMonthLeft);
/*  86 */     p.add(this.lDayAvgAvailable);
/*  87 */     p.add(this.lMonthLeftDay);
/*  88 */     p.add(this.vAvgSpendPerDay);
/*  89 */     p.add(this.vMonthAvailable);
/*  90 */     p.add(this.vDayAvgAvailable);
/*  91 */     p.add(this.vMonthLeftDay);
/*     */     
/*  93 */     return p;
/*     */   }
/*     */   
/*     */   public static void main(String[] args) {
/*  97 */     GUIUtil.showPanel(instance);
/*     */   }
/*     */   
/*     */   public void updateData()
/*     */   {
/* 102 */     SpendPage spend = new SpendService().getSpendPage();
/* 103 */     this.vMonthSpend.setText(spend.monthSpend);
/* 104 */     this.vTodaySpend.setText(spend.todaySpend);
/* 105 */     this.vAvgSpendPerDay.setText(spend.avgSpendPerDay);
/* 106 */     this.vMonthAvailable.setText(spend.monthAvailable);
/* 107 */     this.vDayAvgAvailable.setText(spend.dayAvgAvailable);
/* 108 */     this.vMonthLeftDay.setText(spend.monthLeftDay);
/*     */     
/* 110 */     this.bar.setProgress(spend.usagePercentage);
/* 111 */     if (spend.isOverSpend) {
/* 112 */       this.vMonthAvailable.setForeground(ColorUtil.warningColor);
/* 113 */       this.vMonthSpend.setForeground(ColorUtil.warningColor);
/* 114 */       this.vTodaySpend.setForeground(ColorUtil.warningColor);
/*     */     }
/*     */     else {
/* 117 */       this.vMonthAvailable.setForeground(ColorUtil.grayColor);
/* 118 */       this.vMonthSpend.setForeground(ColorUtil.blueColor);
/* 119 */       this.vTodaySpend.setForeground(ColorUtil.blueColor);
/*     */     }
/* 121 */     this.bar.setForegroundColor(ColorUtil.getByPercentage(spend.usagePercentage));
/* 122 */     addListener();
/*     */   }
/*     */   
/*     */   public void addListener() {}
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\gui\panel\SpendPanel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */