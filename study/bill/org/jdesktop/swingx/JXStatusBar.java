/*     */ package org.jdesktop.swingx;
/*     */ 
/*     */ import java.awt.Insets;
/*     */ import javax.swing.JComponent;
/*     */ import org.jdesktop.swingx.plaf.LookAndFeelAddons;
/*     */ import org.jdesktop.swingx.plaf.StatusBarAddon;
/*     */ import org.jdesktop.swingx.plaf.StatusBarUI;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class JXStatusBar
/*     */   extends JComponent
/*     */ {
/*     */   public static final String uiClassID = "StatusBarUI";
/*     */   private boolean resizeHandleEnabled;
/*     */   
/*     */   static
/*     */   {
/* 118 */     LookAndFeelAddons.contribute(new StatusBarAddon());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public JXStatusBar()
/*     */   {
/* 126 */     updateUI();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void setResizeHandleEnabled(boolean resizeHandleEnabled)
/*     */   {
/* 133 */     boolean oldValue = isResizeHandleEnabled();
/* 134 */     this.resizeHandleEnabled = resizeHandleEnabled;
/* 135 */     firePropertyChange("resizeHandleEnabled", oldValue, isResizeHandleEnabled());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean isResizeHandleEnabled()
/*     */   {
/* 142 */     return this.resizeHandleEnabled;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public StatusBarUI getUI()
/*     */   {
/* 151 */     return (StatusBarUI)this.ui;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setUI(StatusBarUI ui)
/*     */   {
/* 167 */     super.setUI(ui);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getUIClassID()
/*     */   {
/* 182 */     return "StatusBarUI";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void updateUI()
/*     */   {
/* 194 */     setUI((StatusBarUI)LookAndFeelAddons.getUI(this, StatusBarUI.class));
/*     */   }
/*     */   
/*     */   public static class Constraint
/*     */   {
/*     */     private Insets insets;
/*     */     private ResizeBehavior resizeBehavior;
/*     */     
/*     */     public static enum ResizeBehavior
/*     */     {
/* 204 */       FILL,  FIXED;
/*     */       
/*     */       private ResizeBehavior() {} }
/*     */     
/* 208 */     private int fixedWidth = 0;
/*     */     
/*     */ 
/*     */ 
/*     */     public Constraint()
/*     */     {
/* 214 */       this(ResizeBehavior.FIXED, null);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public Constraint(Insets insets)
/*     */     {
/* 223 */       this(ResizeBehavior.FIXED, insets);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public Constraint(int fixedWidth)
/*     */     {
/* 233 */       this(fixedWidth, null);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public Constraint(int fixedWidth, Insets insets)
/*     */     {
/* 244 */       if (fixedWidth < 0) {
/* 245 */         throw new IllegalArgumentException("fixedWidth must be >= 0");
/*     */       }
/* 247 */       this.fixedWidth = fixedWidth;
/* 248 */       this.insets = (insets == null ? new Insets(0, 0, 0, 0) : (Insets)insets.clone());
/* 249 */       this.resizeBehavior = ResizeBehavior.FIXED;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public Constraint(ResizeBehavior resizeBehavior)
/*     */     {
/* 259 */       this(resizeBehavior, null);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public Constraint(ResizeBehavior resizeBehavior, Insets insets)
/*     */     {
/* 270 */       this.resizeBehavior = resizeBehavior;
/* 271 */       this.insets = (insets == null ? new Insets(0, 0, 0, 0) : (Insets)insets.clone());
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public void setFixedWidth(int width)
/*     */     {
/* 284 */       if (width < 0) {
/* 285 */         throw new IllegalArgumentException("width must be >= 0");
/*     */       }
/* 287 */       this.fixedWidth = (this.resizeBehavior == ResizeBehavior.FIXED ? width : 0);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public ResizeBehavior getResizeBehavior()
/*     */     {
/* 296 */       return this.resizeBehavior;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public Insets getInsets()
/*     */     {
/* 305 */       return (Insets)this.insets.clone();
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public int getFixedWidth()
/*     */     {
/* 313 */       return this.fixedWidth;
/*     */     }
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\JXStatusBar.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */