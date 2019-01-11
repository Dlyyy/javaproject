/*    */ package org.jdesktop.swingx.multisplitpane;
/*    */ 
/*    */ import org.jdesktop.swingx.MultiSplitLayout.Divider;
/*    */ import org.jdesktop.swingx.MultiSplitLayout.Leaf;
/*    */ import org.jdesktop.swingx.MultiSplitLayout.Node;
/*    */ import org.jdesktop.swingx.MultiSplitLayout.Split;
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
/*    */ public class DefaultSplitPaneModel
/*    */   extends MultiSplitLayout.Split
/*    */ {
/*    */   public static final String LEFT = "left";
/*    */   public static final String TOP = "top";
/*    */   public static final String BOTTOM = "bottom";
/*    */   
/*    */   public DefaultSplitPaneModel()
/*    */   {
/* 45 */     MultiSplitLayout.Split row = new MultiSplitLayout.Split();
/* 46 */     MultiSplitLayout.Split col = new MultiSplitLayout.Split();
/* 47 */     col.setRowLayout(false);
/* 48 */     setChildren(new MultiSplitLayout.Node[] { new MultiSplitLayout.Leaf("left"), new MultiSplitLayout.Divider(), col });
/* 49 */     col.setChildren(new MultiSplitLayout.Node[] { new MultiSplitLayout.Leaf("top"), new MultiSplitLayout.Divider(), new MultiSplitLayout.Leaf("bottom") });
/*    */   }
/*    */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\multisplitpane\DefaultSplitPaneModel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */