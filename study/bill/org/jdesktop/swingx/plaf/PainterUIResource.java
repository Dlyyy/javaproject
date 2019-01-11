/*    */ package org.jdesktop.swingx.plaf;
/*    */ 
/*    */ import java.awt.Graphics2D;
/*    */ import javax.swing.JComponent;
/*    */ import javax.swing.plaf.UIResource;
/*    */ import org.jdesktop.swingx.painter.Painter;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class PainterUIResource<T extends JComponent>
/*    */   implements Painter<T>, UIResource
/*    */ {
/*    */   private Painter<? super T> p;
/*    */   
/*    */   public PainterUIResource(Painter<? super T> p)
/*    */   {
/* 50 */     this.p = p;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public void paint(Graphics2D g, T component, int width, int height)
/*    */   {
/* 57 */     if (this.p != null) {
/* 58 */       this.p.paint(g, component, width, height);
/*    */     }
/*    */   }
/*    */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\plaf\PainterUIResource.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */