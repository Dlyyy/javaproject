/*    */ import com.birosoft.liquid.LiquidLookAndFeel;
/*    */ import java.beans.PropertyChangeEvent;
/*    */ import java.beans.PropertyChangeListener;
/*    */ import javax.swing.UIManager;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class Launcher$1
/*    */   implements PropertyChangeListener
/*    */ {
/*    */   public void propertyChange(PropertyChangeEvent event)
/*    */   {
/* 42 */     Object newLF = event.getNewValue();
/*    */     
/* 44 */     if (!(newLF instanceof LiquidLookAndFeel)) {
/*    */       try {
/* 46 */         UIManager.setLookAndFeel(new LiquidLookAndFeel());
/*    */       } catch (Exception e) {
/* 48 */         e.printStackTrace();
/*    */       }
/*    */     }
/*    */   }
/*    */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\Launcher$1.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       0.7.1
 */