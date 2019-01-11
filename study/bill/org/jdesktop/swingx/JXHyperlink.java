/*     */ package org.jdesktop.swingx;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.beans.PropertyChangeEvent;
/*     */ import java.beans.PropertyChangeListener;
/*     */ import java.net.URI;
/*     */ import javax.swing.Action;
/*     */ import javax.swing.JButton;
/*     */ import javax.swing.plaf.ButtonUI;
/*     */ import org.jdesktop.swingx.hyperlink.HyperlinkAction;
/*     */ import org.jdesktop.swingx.plaf.HyperlinkAddon;
/*     */ import org.jdesktop.swingx.plaf.LookAndFeelAddons;
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
/*     */ public class JXHyperlink
/*     */   extends JButton
/*     */ {
/*     */   public static final String uiClassID = "HyperlinkUI";
/*     */   
/*     */   static
/*     */   {
/*  92 */     LookAndFeelAddons.contribute(new HyperlinkAddon());
/*     */   }
/*     */   
/*  95 */   private boolean hasBeenVisited = false;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private Color unclickedColor;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private Color clickedColor;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private boolean overrulesActionOnClick;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public JXHyperlink()
/*     */   {
/* 117 */     this(null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public JXHyperlink(Action action)
/*     */   {
/* 128 */     setAction(action);
/* 129 */     init();
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
/*     */   public void setURI(URI uri)
/*     */   {
/* 145 */     setAction(HyperlinkAction.createHyperlinkAction(uri));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Color getUnclickedColor()
/*     */   {
/* 154 */     return this.unclickedColor;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setClickedColor(Color color)
/*     */   {
/* 164 */     Color old = getClickedColor();
/* 165 */     this.clickedColor = color;
/* 166 */     if (isClicked()) {
/* 167 */       setForeground(getClickedColor());
/*     */     }
/* 169 */     firePropertyChange("clickedColor", old, getClickedColor());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Color getClickedColor()
/*     */   {
/* 178 */     return this.clickedColor;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setUnclickedColor(Color color)
/*     */   {
/* 188 */     Color old = getUnclickedColor();
/* 189 */     this.unclickedColor = color;
/* 190 */     if (!isClicked()) {
/* 191 */       setForeground(getUnclickedColor());
/*     */     }
/* 193 */     firePropertyChange("unclickedColor", old, getUnclickedColor());
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
/*     */   public void setClicked(boolean clicked)
/*     */   {
/* 209 */     boolean old = isClicked();
/* 210 */     this.hasBeenVisited = clicked;
/* 211 */     setForeground(isClicked() ? getClickedColor() : getUnclickedColor());
/* 212 */     firePropertyChange("clicked", old, isClicked());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isClicked()
/*     */   {
/* 222 */     return this.hasBeenVisited;
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
/*     */   public void setOverrulesActionOnClick(boolean overrule)
/*     */   {
/* 238 */     boolean old = getOverrulesActionOnClick();
/* 239 */     this.overrulesActionOnClick = overrule;
/* 240 */     firePropertyChange("overrulesActionOnClick", old, getOverrulesActionOnClick());
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
/*     */   public boolean getOverrulesActionOnClick()
/*     */   {
/* 255 */     return this.overrulesActionOnClick;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void fireActionPerformed(ActionEvent event)
/*     */   {
/* 264 */     super.fireActionPerformed(event);
/* 265 */     if (isAutoSetClicked()) {
/* 266 */       setClicked(true);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected boolean isAutoSetClicked()
/*     */   {
/* 277 */     return (getAction() == null) || (getOverrulesActionOnClick());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected PropertyChangeListener createActionPropertyChangeListener(final Action a)
/*     */   {
/* 288 */     final PropertyChangeListener superListener = super.createActionPropertyChangeListener(a);
/*     */     
/*     */ 
/*     */ 
/* 292 */     new PropertyChangeListener()
/*     */     {
/*     */       public void propertyChange(PropertyChangeEvent evt) {
/* 295 */         if ("visited".equals(evt.getPropertyName())) {
/* 296 */           JXHyperlink.this.configureClickedPropertyFromAction(a);
/*     */         } else {
/* 298 */           superListener.propertyChange(evt);
/*     */         }
/*     */       }
/*     */     };
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void configurePropertiesFromAction(Action a)
/*     */   {
/* 312 */     super.configurePropertiesFromAction(a);
/* 313 */     configureClickedPropertyFromAction(a);
/*     */   }
/*     */   
/*     */   private void configureClickedPropertyFromAction(Action a) {
/* 317 */     boolean clicked = false;
/* 318 */     if (a != null) {
/* 319 */       clicked = Boolean.TRUE.equals(a.getValue("visited"));
/*     */     }
/*     */     
/* 322 */     setClicked(clicked);
/*     */   }
/*     */   
/*     */   private void init() {
/* 326 */     setForeground(isClicked() ? getClickedColor() : getUnclickedColor());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getUIClassID()
/*     */   {
/* 335 */     return "HyperlinkUI";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void updateUI()
/*     */   {
/* 346 */     setUI((ButtonUI)LookAndFeelAddons.getUI(this, ButtonUI.class));
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\JXHyperlink.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */