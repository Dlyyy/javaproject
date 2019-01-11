/*     */ package org.jdesktop.swingx;
/*     */ 
/*     */ import java.awt.Component;
/*     */ import java.awt.HeadlessException;
/*     */ import java.util.prefs.Preferences;
/*     */ import javax.swing.JDialog;
/*     */ import org.jdesktop.swingx.plaf.LookAndFeelAddons;
/*     */ import org.jdesktop.swingx.plaf.TipOfTheDayAddon;
/*     */ import org.jdesktop.swingx.plaf.TipOfTheDayUI;
/*     */ import org.jdesktop.swingx.tips.DefaultTipOfTheDayModel;
/*     */ import org.jdesktop.swingx.tips.TipOfTheDayModel;
/*     */ import org.jdesktop.swingx.tips.TipOfTheDayModel.Tip;
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
/*     */ public class JXTipOfTheDay
/*     */   extends JXPanel
/*     */ {
/*     */   public static final String uiClassID = "swingx/TipOfTheDayUI";
/*     */   public static final String PREFERENCE_KEY = "ShowTipOnStartup";
/*     */   public static final String CURRENT_TIP_CHANGED_KEY = "currentTip";
/*     */   private TipOfTheDayModel model;
/*     */   
/*     */   static
/*     */   {
/* 125 */     LookAndFeelAddons.contribute(new TipOfTheDayAddon());
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
/* 139 */   private int currentTip = 0;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public JXTipOfTheDay()
/*     */   {
/* 146 */     this(new DefaultTipOfTheDayModel(new TipOfTheDayModel.Tip[0]));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public JXTipOfTheDay(TipOfTheDayModel model)
/*     */   {
/* 156 */     this.model = model;
/* 157 */     updateUI();
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
/* 169 */     setUI((TipOfTheDayUI)LookAndFeelAddons.getUI(this, TipOfTheDayUI.class));
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
/*     */   public void setUI(TipOfTheDayUI ui)
/*     */   {
/* 183 */     super.setUI(ui);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public TipOfTheDayUI getUI()
/*     */   {
/* 193 */     return (TipOfTheDayUI)this.ui;
/*     */   }
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
/* 205 */     return "swingx/TipOfTheDayUI";
/*     */   }
/*     */   
/*     */   public TipOfTheDayModel getModel() {
/* 209 */     return this.model;
/*     */   }
/*     */   
/*     */   public void setModel(TipOfTheDayModel model) {
/* 213 */     if (model == null) {
/* 214 */       throw new IllegalArgumentException("model can not be null");
/*     */     }
/* 216 */     TipOfTheDayModel old = this.model;
/* 217 */     this.model = model;
/* 218 */     firePropertyChange("model", old, model);
/*     */   }
/*     */   
/*     */   public int getCurrentTip() {
/* 222 */     return this.currentTip;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setCurrentTip(int currentTip)
/*     */   {
/* 233 */     if ((currentTip < 0) || (currentTip >= getModel().getTipCount())) {
/* 234 */       throw new IllegalArgumentException("Current tip must be within the bounds [0, " + getModel().getTipCount() + "]");
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 239 */     int oldTip = this.currentTip;
/* 240 */     this.currentTip = currentTip;
/* 241 */     firePropertyChange("currentTip", oldTip, currentTip);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void nextTip()
/*     */   {
/* 248 */     int count = getModel().getTipCount();
/* 249 */     if (count == 0) { return;
/*     */     }
/* 251 */     int nextTip = this.currentTip + 1;
/* 252 */     if (nextTip >= count) {
/* 253 */       nextTip = 0;
/*     */     }
/* 255 */     setCurrentTip(nextTip);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void previousTip()
/*     */   {
/* 262 */     int count = getModel().getTipCount();
/* 263 */     if (count == 0) { return;
/*     */     }
/* 265 */     int previousTip = this.currentTip - 1;
/* 266 */     if (previousTip < 0) {
/* 267 */       previousTip = count - 1;
/*     */     }
/* 269 */     setCurrentTip(previousTip);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void showDialog(Component parentComponent)
/*     */     throws HeadlessException
/*     */   {
/* 281 */     showDialog(parentComponent, (ShowOnStartupChoice)null);
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
/*     */ 
/*     */ 
/*     */   public boolean showDialog(Component parentComponent, Preferences showOnStartupPref)
/*     */     throws HeadlessException
/*     */   {
/* 300 */     return showDialog(parentComponent, showOnStartupPref, false);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean showDialog(Component parentComponent, final Preferences showOnStartupPref, boolean force)
/*     */     throws HeadlessException
/*     */   {
/* 323 */     if (showOnStartupPref == null) { throw new IllegalArgumentException("Preferences can not be null");
/*     */     }
/*     */     
/* 326 */     ShowOnStartupChoice store = new ShowOnStartupChoice()
/*     */     {
/* 328 */       public boolean isShowingOnStartup() { return showOnStartupPref.getBoolean("ShowTipOnStartup", true); }
/*     */       
/*     */       public void setShowingOnStartup(boolean showOnStartup) {
/* 331 */         if ((showOnStartup) && (!showOnStartupPref.getBoolean("ShowTipOnStartup", true)))
/*     */         {
/*     */ 
/* 334 */           showOnStartupPref.remove("ShowTipOnStartup");
/* 335 */         } else if (!showOnStartup)
/*     */         {
/* 337 */           showOnStartupPref.putBoolean("ShowTipOnStartup", showOnStartup);
/*     */         }
/*     */       }
/* 340 */     };
/* 341 */     return showDialog(parentComponent, store, force);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean showDialog(Component parentComponent, ShowOnStartupChoice choice)
/*     */   {
/* 363 */     return showDialog(parentComponent, choice, false);
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
/*     */   public boolean showDialog(Component parentComponent, ShowOnStartupChoice choice, boolean force)
/*     */   {
/* 389 */     if (choice == null) {
/* 390 */       JDialog dialog = createDialog(parentComponent, choice);
/* 391 */       dialog.setVisible(true);
/* 392 */       dialog.dispose();
/* 393 */       return true; }
/* 394 */     if ((force) || (choice.isShowingOnStartup())) {
/* 395 */       JDialog dialog = createDialog(parentComponent, choice);
/* 396 */       dialog.setVisible(true);
/* 397 */       dialog.dispose();
/* 398 */       return choice.isShowingOnStartup();
/*     */     }
/* 400 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static boolean isShowingOnStartup(Preferences showOnStartupPref)
/*     */   {
/* 409 */     return showOnStartupPref.getBoolean("ShowTipOnStartup", true);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void forceShowOnStartup(Preferences showOnStartupPref)
/*     */   {
/* 420 */     showOnStartupPref.remove("ShowTipOnStartup");
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
/*     */   protected JDialog createDialog(Component parentComponent, ShowOnStartupChoice choice)
/*     */   {
/* 436 */     return getUI().createDialog(parentComponent, choice);
/*     */   }
/*     */   
/*     */   public static abstract interface ShowOnStartupChoice
/*     */   {
/*     */     public abstract void setShowingOnStartup(boolean paramBoolean);
/*     */     
/*     */     public abstract boolean isShowingOnStartup();
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\JXTipOfTheDay.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */