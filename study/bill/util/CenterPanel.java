/*    */ package util;
/*    */ 
/*    */ import gui.panel.WorkingPanel;
/*    */ import java.awt.Component;
/*    */ import java.awt.Dimension;
/*    */ import javax.swing.JButton;
/*    */ import javax.swing.JComponent;
/*    */ import javax.swing.JFrame;
/*    */ import javax.swing.JPanel;
/*    */ 
/*    */ public class CenterPanel
/*    */   extends JPanel
/*    */ {
/*    */   private double rate;
/*    */   private JComponent c;
/*    */   private boolean strech;
/*    */   
/*    */   public CenterPanel(double rate, boolean strech)
/*    */   {
/* 20 */     setLayout(null);
/* 21 */     this.rate = rate;
/* 22 */     this.strech = strech;
/*    */   }
/*    */   
/*    */   public CenterPanel(double rate) {
/* 26 */     this(rate, true);
/*    */   }
/*    */   
/*    */   public void repaint() {
/* 30 */     if (this.c != null) {
/* 31 */       Dimension containerSize = getSize();
/* 32 */       Dimension componentSize = this.c.getPreferredSize();
/*    */       
/* 34 */       if (this.strech) {
/* 35 */         this.c.setSize((int)(containerSize.width * this.rate), (int)(containerSize.height * this.rate));
/*    */       } else {
/* 37 */         this.c.setSize(componentSize);
/*    */       }
/* 39 */       this.c.setLocation(containerSize.width / 2 - this.c.getSize().width / 2, 
/* 40 */         containerSize.height / 2 - this.c.getSize().height / 2);
/*    */     }
/* 42 */     super.repaint();
/*    */   }
/*    */   
/*    */   public void show(JComponent p) {
/* 46 */     this.c = p;
/* 47 */     Component[] cs = getComponents();
/* 48 */     Component[] arrayOfComponent1; int j = (arrayOfComponent1 = cs).length; for (int i = 0; i < j; i++) { Component c = arrayOfComponent1[i];
/* 49 */       remove(c);
/*    */     }
/* 51 */     add(p);
/*    */     
/* 53 */     if ((p instanceof WorkingPanel)) {
/* 54 */       ((WorkingPanel)p).updateData();
/*    */     }
/* 56 */     updateUI();
/*    */   }
/*    */   
/*    */   public static void main(String[] args) {
/* 60 */     JFrame f = new JFrame();
/* 61 */     f.setSize(200, 200);
/* 62 */     f.setLocationRelativeTo(null);
/* 63 */     CenterPanel cp = new CenterPanel(0.85D, true);
/* 64 */     f.setContentPane(cp);
/* 65 */     f.setDefaultCloseOperation(3);
/* 66 */     f.setVisible(true);
/* 67 */     JButton b = new JButton("abc");
/* 68 */     cp.show(b);
/*    */   }
/*    */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\util\CenterPanel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */