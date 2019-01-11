/*     */ package com.birosoft.liquid;
/*     */ 
/*     */ import java.awt.Component;
/*     */ import java.beans.PropertyChangeEvent;
/*     */ import java.beans.PropertyChangeListener;
/*     */ import javax.swing.DesktopManager;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JDesktopPane;
/*     */ import javax.swing.JInternalFrame;
/*     */ import javax.swing.LookAndFeel;
/*     */ import javax.swing.UIManager;
/*     */ import javax.swing.border.Border;
/*     */ import javax.swing.border.EmptyBorder;
/*     */ import javax.swing.plaf.ComponentUI;
/*     */ import javax.swing.plaf.UIResource;
/*     */ import javax.swing.plaf.basic.BasicInternalFrameUI;
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
/*     */ public class LiquidInternalFrameUI
/*     */   extends BasicInternalFrameUI
/*     */ {
/*  35 */   protected static String IS_PALETTE = "JInternalFrame.isPalette";
/*  36 */   private static String FRAME_TYPE = "JInternalFrame.frameType";
/*  37 */   private static String NORMAL_FRAME = "normal";
/*  38 */   private static String PALETTE_FRAME = "palette";
/*  39 */   private static String OPTION_DIALOG = "optionDialog";
/*  40 */   private static final PropertyChangeListener liquidPropertyChangeListener = new LiquidPropertyChangeHandler(null);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  49 */   static boolean allowRoundedWindows = false;
/*     */   
/*     */   private static DesktopManager sharedDesktopManager;
/*  52 */   Border frameBorder = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private LiquidInternalFrameTitlePane titlePane;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public LiquidInternalFrameUI(JInternalFrame frame)
/*     */   {
/*  68 */     super(frame);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static ComponentUI createUI(JComponent c)
/*     */   {
/*  78 */     return new LiquidInternalFrameUI((JInternalFrame)c);
/*     */   }
/*     */   
/*     */   JDesktopPane getDesktopPane(JComponent frame) {
/*  82 */     JDesktopPane pane = null;
/*  83 */     Component c = frame.getParent();
/*     */     
/*     */ 
/*  86 */     while (pane == null) {
/*  87 */       if ((c instanceof JDesktopPane)) {
/*  88 */         pane = (JDesktopPane)c;
/*  89 */       } else { if (c == null) {
/*     */           break;
/*     */         }
/*  92 */         c = c.getParent();
/*     */       }
/*     */     }
/*     */     
/*  96 */     return pane;
/*     */   }
/*     */   
/*     */   protected DesktopManager getDesktopManager() {
/* 100 */     if (!allowRoundedWindows) {
/* 101 */       return super.getDesktopManager();
/*     */     }
/*     */     
/* 104 */     if (sharedDesktopManager == null) {
/* 105 */       sharedDesktopManager = createDesktopManager();
/*     */     }
/*     */     
/* 108 */     return sharedDesktopManager;
/*     */   }
/*     */   
/*     */   protected DesktopManager createDesktopManager() {
/* 112 */     if (!allowRoundedWindows) {
/* 113 */       return super.createDesktopManager();
/*     */     }
/* 115 */     return new LiquidDesktopManager();
/*     */   }
/*     */   
/*     */   public void installUI(JComponent c)
/*     */   {
/* 120 */     super.installUI(c);
/*     */     
/* 122 */     if (allowRoundedWindows) {
/* 123 */       this.frame.setOpaque(false);
/*     */     }
/*     */     
/* 126 */     if (LiquidLookAndFeel.winDecoPanther) {
/* 127 */       this.frameBorder = UIManager.getBorder("RootPane.frameBorder");
/*     */     }
/*     */     else {
/* 130 */       this.frameBorder = new LiquidInternalFrameBorder();
/*     */     }
/*     */     
/* 133 */     this.frame.setBorder(this.frameBorder);
/* 134 */     this.frame.addPropertyChangeListener(liquidPropertyChangeListener);
/*     */     
/* 136 */     Object paletteProp = c.getClientProperty(IS_PALETTE);
/*     */     
/* 138 */     if (paletteProp != null) {
/* 139 */       setPalette(((Boolean)paletteProp).booleanValue());
/*     */     }
/*     */   }
/*     */   
/*     */   public void uninstallUI(JComponent c) {
/* 144 */     this.frame.removePropertyChangeListener(liquidPropertyChangeListener);
/* 145 */     super.uninstallUI(c);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected JComponent createNorthPane(JInternalFrame frame)
/*     */   {
/* 154 */     super.createNorthPane(frame);
/* 155 */     this.titlePane = new LiquidInternalFrameTitlePane(frame);
/*     */     
/* 157 */     return this.titlePane;
/*     */   }
/*     */   
/*     */   protected void activateFrame(JInternalFrame f) {
/* 161 */     super.activateFrame(f);
/* 162 */     if (!LiquidLookAndFeel.winDecoPanther) {
/* 163 */       ((LiquidInternalFrameBorder)this.frameBorder).setActive(true);
/*     */     }
/* 165 */     this.titlePane.activate();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected void deactivateFrame(JInternalFrame f)
/*     */   {
/* 172 */     super.deactivateFrame(f);
/* 173 */     if (!LiquidLookAndFeel.winDecoPanther) {
/* 174 */       ((LiquidInternalFrameBorder)this.frameBorder).setActive(false);
/*     */     }
/* 176 */     this.titlePane.deactivate();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setPalette(boolean isPalette)
/*     */   {
/* 186 */     if (isPalette) {
/* 187 */       LookAndFeel.installBorder(this.frame, "InternalFrame.paletteBorder");
/*     */     } else {
/* 189 */       LookAndFeel.installBorder(this.frame, "InternalFrame.border");
/*     */     }
/*     */     
/* 192 */     this.titlePane.setPalette(isPalette);
/* 193 */     if (!LiquidLookAndFeel.winDecoPanther) {
/* 194 */       ((LiquidInternalFrameBorder)this.frameBorder).isPalette = isPalette;
/*     */     }
/* 196 */     this.frame.setBorder(this.frameBorder);
/*     */   }
/*     */   
/*     */   private void stripContentBorder(Object c) {
/* 200 */     if ((c instanceof JComponent)) {
/* 201 */       JComponent contentComp = (JComponent)c;
/* 202 */       Border contentBorder = contentComp.getBorder();
/*     */       
/* 204 */       if ((contentBorder == null) || ((contentBorder instanceof UIResource))) {
/* 205 */         contentComp.setBorder(new EmptyBorder(0, 0, 0, 0));
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private void setFrameType(String frameType) {
/* 211 */     if (frameType.equals(OPTION_DIALOG)) {
/* 212 */       LookAndFeel.installBorder(this.frame, "InternalFrame.optionDialogBorder");
/* 213 */       this.titlePane.setPalette(false);
/* 214 */     } else if (frameType.equals(PALETTE_FRAME)) {
/* 215 */       LookAndFeel.installBorder(this.frame, "InternalFrame.paletteBorder");
/* 216 */       this.titlePane.setPalette(true);
/*     */     } else {
/* 218 */       LookAndFeel.installBorder(this.frame, "InternalFrame.border");
/* 219 */       this.titlePane.setPalette(false);
/*     */     }
/*     */   }
/*     */   
/* 223 */   private static class LiquidPropertyChangeHandler implements PropertyChangeListener { LiquidPropertyChangeHandler(LiquidInternalFrameUI.1 x0) { this(); }
/*     */     
/*     */     public void propertyChange(PropertyChangeEvent e) {
/* 226 */       String name = e.getPropertyName();
/* 227 */       JInternalFrame jif = (JInternalFrame)e.getSource();
/*     */       
/* 229 */       if (!(jif.getUI() instanceof LiquidInternalFrameUI)) {
/* 230 */         return;
/*     */       }
/*     */       
/* 233 */       LiquidInternalFrameUI ui = (LiquidInternalFrameUI)jif.getUI();
/*     */       
/* 235 */       if (name.equals(LiquidInternalFrameUI.FRAME_TYPE)) {
/* 236 */         if ((e.getNewValue() instanceof String)) {
/* 237 */           ui.setFrameType((String)e.getNewValue());
/*     */         }
/* 239 */       } else if (name.equals(LiquidInternalFrameUI.IS_PALETTE)) {
/* 240 */         if (e.getNewValue() != null) {
/* 241 */           ui.setPalette(((Boolean)e.getNewValue()).booleanValue());
/*     */         } else {
/* 243 */           ui.setPalette(false);
/*     */         }
/* 245 */       } else if (name.equals("contentPane")) {
/* 246 */         ui.stripContentBorder(e.getNewValue());
/*     */       }
/*     */     }
/*     */     
/*     */     private LiquidPropertyChangeHandler() {}
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\com\birosoft\liquid\LiquidInternalFrameUI.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       0.7.1
 */