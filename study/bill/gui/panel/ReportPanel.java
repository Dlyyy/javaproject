/*    */ package gui.panel;
/*    */ 
/*    */ import entity.Record;
/*    */ import java.awt.BorderLayout;
/*    */ import java.awt.Image;
/*    */ import java.util.List;
/*    */ import javax.swing.ImageIcon;
/*    */ import javax.swing.JLabel;
/*    */ import service.ReportService;
/*    */ import util.ChartUtil;
/*    */ import util.GUIUtil;
/*    */ 
/*    */ 
/*    */ public class ReportPanel
/*    */   extends WorkingPanel
/*    */ {
/* 17 */   public static ReportPanel instance = new ReportPanel();
/*    */   
/* 19 */   JLabel l = new JLabel();
/*    */   
/*    */   public ReportPanel() {
/* 22 */     setLayout(new BorderLayout());
/* 23 */     List<Record> rs = new ReportService().listThisMonthRecords();
/* 24 */     Image i = ChartUtil.getImage(rs, 400, 300);
/* 25 */     ImageIcon icon = new ImageIcon(i);
/* 26 */     this.l.setIcon(icon);
/* 27 */     add(this.l);
/* 28 */     addListener();
/*    */   }
/*    */   
/*    */   public static void main(String[] args) {
/* 32 */     GUIUtil.showPanel(instance);
/*    */   }
/*    */   
/*    */   public void updateData()
/*    */   {
/* 37 */     List<Record> rs = new ReportService().listThisMonthRecords();
/* 38 */     Image i = ChartUtil.getImage(rs, 350, 250);
/* 39 */     ImageIcon icon = new ImageIcon(i);
/* 40 */     this.l.setIcon(icon);
/*    */   }
/*    */   
/*    */   public void addListener() {}
/*    */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\gui\panel\ReportPanel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */