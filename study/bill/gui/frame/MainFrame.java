/*    */ package gui.frame;
/*    */ 
/*    */ import gui.panel.MainPanel;
/*    */ import javax.swing.JFrame;
/*    */ 
/*    */ public class MainFrame
/*    */   extends JFrame
/*    */ {
/*  9 */   public static MainFrame instance = new MainFrame();
/*    */   
/*    */   private MainFrame() {
/* 12 */     setSize(500, 450);
/* 13 */     setTitle("一本糊涂账");
/* 14 */     setContentPane(MainPanel.instance);
/* 15 */     setLocationRelativeTo(null);
/* 16 */     setResizable(false);
/* 17 */     setDefaultCloseOperation(3);
/*    */   }
/*    */   
/*    */   public static void main(String[] args) {
/* 21 */     instance.setVisible(true);
/*    */   }
/*    */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\gui\frame\MainFrame.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */