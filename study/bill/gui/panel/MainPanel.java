/*    */ package gui.panel;
/*    */ 
/*    */ import gui.listener.ToolBarListener;
/*    */ import java.awt.BorderLayout;
/*    */ import javax.swing.JButton;
/*    */ import javax.swing.JPanel;
/*    */ import javax.swing.JToolBar;
/*    */ import util.CenterPanel;
/*    */ import util.GUIUtil;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class MainPanel
/*    */   extends JPanel
/*    */ {
/* 18 */   public static MainPanel instance = new MainPanel();
/* 19 */   public JToolBar tb = new JToolBar();
/* 20 */   public JButton bSpend = new JButton();
/* 21 */   public JButton bRecord = new JButton();
/* 22 */   public JButton bCategory = new JButton();
/* 23 */   public JButton bReport = new JButton();
/* 24 */   public JButton bConfig = new JButton();
/* 25 */   public JButton bBackup = new JButton();
/* 26 */   public JButton bRecover = new JButton();
/*    */   public CenterPanel workingPanel;
/*    */   
/*    */   static {}
/*    */   
/*    */   private MainPanel() {
/* 32 */     GUIUtil.setImageIcon(this.bSpend, "home.png", "消费一览");
/* 33 */     GUIUtil.setImageIcon(this.bRecord, "record.png", "记一笔");
/* 34 */     GUIUtil.setImageIcon(this.bCategory, "category2.png", "消费分类");
/* 35 */     GUIUtil.setImageIcon(this.bReport, "report.png", "月消费报表");
/* 36 */     GUIUtil.setImageIcon(this.bConfig, "config.png", "设置");
/* 37 */     GUIUtil.setImageIcon(this.bBackup, "backup.png", "备份");
/* 38 */     GUIUtil.setImageIcon(this.bRecover, "restore.png", "恢复");
/*    */     
/* 40 */     this.tb.add(this.bSpend);
/* 41 */     this.tb.add(this.bRecord);
/* 42 */     this.tb.add(this.bCategory);
/* 43 */     this.tb.add(this.bReport);
/* 44 */     this.tb.add(this.bConfig);
/* 45 */     this.tb.add(this.bBackup);
/* 46 */     this.tb.add(this.bRecover);
/* 47 */     this.tb.setFloatable(false);
/*    */     
/* 49 */     this.workingPanel = new CenterPanel(0.8D);
/*    */     
/* 51 */     setLayout(new BorderLayout());
/* 52 */     add(this.tb, "North");
/* 53 */     add(this.workingPanel, "Center");
/*    */     
/* 55 */     addListener();
/*    */   }
/*    */   
/*    */   private void addListener() {
/* 59 */     ToolBarListener listener = new ToolBarListener();
/*    */     
/* 61 */     this.bSpend.addActionListener(listener);
/* 62 */     this.bRecord.addActionListener(listener);
/* 63 */     this.bCategory.addActionListener(listener);
/* 64 */     this.bReport.addActionListener(listener);
/* 65 */     this.bConfig.addActionListener(listener);
/* 66 */     this.bBackup.addActionListener(listener);
/* 67 */     this.bRecover.addActionListener(listener);
/*    */   }
/*    */   
/*    */   public static void main(String[] args)
/*    */   {
/* 72 */     GUIUtil.showPanel(instance, 1.0D);
/*    */   }
/*    */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\gui\panel\MainPanel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */