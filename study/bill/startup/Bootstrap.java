/*    */ package startup;
/*    */ 
/*    */ import gui.frame.MainFrame;
/*    */ import gui.panel.MainPanel;
/*    */ import gui.panel.SpendPanel;
/*    */ import javax.swing.SwingUtilities;
/*    */ import util.CenterPanel;
/*    */ import util.GUIUtil;
/*    */ 
/*    */ public class Bootstrap
/*    */ {
/*    */   public static void main(String[] args) throws Exception
/*    */   {
/* 14 */     GUIUtil.useLNF();
/* 15 */     SwingUtilities.invokeAndWait(new Runnable()
/*    */     {
/*    */       public void run() {
/* 18 */         MainFrame.instance.setVisible(true);
/* 19 */         MainPanel.instance.workingPanel.show(SpendPanel.instance);
/*    */       }
/*    */     });
/*    */   }
/*    */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\startup\Bootstrap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */