/*    */ package org.jdesktop.swingx.plaf.synth;
/*    */ 
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ import javax.swing.JComponent;
/*    */ import javax.swing.plaf.synth.Region;
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
/*    */ public class XRegion
/*    */   extends Region
/*    */ {
/* 37 */   static Map<String, XRegion> uiToXRegionMap = new HashMap();
/* 38 */   public static final Region XLIST = new XRegion("XList", null, false, "XListUI", LIST);
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   private Region parent;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public XRegion(String name, String dummyUI, boolean subregion, String realUI, Region parent)
/*    */   {
/* 53 */     super(name, dummyUI, subregion);
/* 54 */     this.parent = parent;
/* 55 */     if (realUI != null) {
/* 56 */       uiToXRegionMap.put(realUI, this);
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public static Region getXRegion(JComponent component, boolean useParent)
/*    */   {
/* 69 */     XRegion region = (XRegion)uiToXRegionMap.get(component.getUIClassID());
/* 70 */     if (region != null)
/* 71 */       return (useParent) && (region.parent != null) ? region.parent : region;
/* 72 */     return region;
/*    */   }
/*    */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\plaf\synth\XRegion.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */