/*     */ package org.jdesktop.swingx.plaf;
/*     */ 
/*     */ import java.net.URL;
/*     */ import javax.swing.ImageIcon;
/*     */ import javax.swing.UIManager;
/*     */ import javax.swing.plaf.IconUIResource;
/*     */ import javax.swing.plaf.InsetsUIResource;
/*     */ import org.jdesktop.swingx.JXSearchField.LayoutStyle;
/*     */ 
/*     */ 
/*     */ public class SearchFieldAddon
/*     */   extends AbstractComponentAddon
/*     */ {
/*     */   public static final String SEARCH_FIELD_SOURCE = "searchField";
/*     */   public static final String BUTTON_SOURCE = "button";
/*     */   
/*     */   public SearchFieldAddon()
/*     */   {
/*  19 */     super("JXSearchField");
/*     */   }
/*     */   
/*     */   protected void addBasicDefaults(LookAndFeelAddons addon, DefaultsList defaults)
/*     */   {
/*  24 */     super.addBasicDefaults(addon, defaults);
/*  25 */     defaults.add("SearchField.layoutStyle", JXSearchField.LayoutStyle.MAC);
/*  26 */     defaults.add("SearchField.icon", getIcon("basic/resources/search.gif"));
/*  27 */     defaults.add("SearchField.rolloverIcon", getIcon("basic/resources/search_rollover.gif"));
/*  28 */     defaults.add("SearchField.pressedIcon", getIcon("basic/resources/search.gif"));
/*  29 */     defaults.add("SearchField.popupIcon", getIcon("basic/resources/search_popup.gif"));
/*  30 */     defaults.add("SearchField.popupRolloverIcon", getIcon("basic/resources/search_popup_rollover.gif"));
/*  31 */     defaults.add("SearchField.clearIcon", getIcon("basic/resources/clear.gif"));
/*  32 */     defaults.add("SearchField.clearRolloverIcon", getIcon("basic/resources/clear_rollover.gif"));
/*  33 */     defaults.add("SearchField.clearPressedIcon", getIcon("basic/resources/clear_pressed.gif"));
/*  34 */     defaults.add("SearchField.buttonMargin", new InsetsUIResource(1, 1, 1, 1));
/*  35 */     defaults.add("SearchField.popupSource", "button");
/*     */     
/*     */ 
/*  38 */     UIManagerExt.addResourceBundle("org.jdesktop.swingx.plaf.basic.resources.SearchField");
/*     */   }
/*     */   
/*     */ 
/*     */   protected void addMetalDefaults(LookAndFeelAddons addon, DefaultsList defaults)
/*     */   {
/*  44 */     super.addMetalDefaults(addon, defaults);
/*     */     
/*  46 */     defaults.add("SearchField.buttonMargin", new InsetsUIResource(0, 0, 1, 1));
/*     */   }
/*     */   
/*     */   protected void addWindowsDefaults(LookAndFeelAddons addon, DefaultsList defaults)
/*     */   {
/*  51 */     super.addWindowsDefaults(addon, defaults);
/*     */     
/*  53 */     defaults.add("SearchField.promptFontStyle", Integer.valueOf(2));
/*  54 */     defaults.add("SearchField.layoutStyle", JXSearchField.LayoutStyle.VISTA);
/*  55 */     defaults.add("SearchField.icon", getIcon("windows/resources/search.gif"));
/*  56 */     defaults.add("SearchField.rolloverIcon", getIcon("windows/resources/search_rollover.gif"));
/*  57 */     defaults.add("SearchField.pressedIcon", getIcon("windows/resources/search_pressed.gif"));
/*  58 */     defaults.add("SearchField.popupIcon", getIcon("windows/resources/search_popup.gif"));
/*  59 */     defaults.add("SearchField.popupRolloverIcon", getIcon("windows/resources/search_popup_rollover.gif"));
/*  60 */     defaults.add("SearchField.popupPressedIcon", getIcon("windows/resources/search_popup_pressed.gif"));
/*  61 */     defaults.add("SearchField.clearIcon", getIcon("windows/resources/clear.gif"));
/*  62 */     defaults.add("SearchField.clearRolloverIcon", getIcon("windows/resources/clear_rollover.gif"));
/*  63 */     defaults.add("SearchField.clearPressedIcon", getIcon("windows/resources/clear_pressed.gif"));
/*  64 */     defaults.add("SearchField.useSeperatePopupButton", Boolean.TRUE);
/*  65 */     defaults.add("SearchField.popupOffset", Integer.valueOf(-1));
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*  70 */     if (UIManager.getLookAndFeel().getClass().getName().indexOf("Classic") == -1) {
/*  71 */       defaults.add("SearchField.buttonMargin", new InsetsUIResource(0, -1, 0, -1));
/*     */     } else {
/*  73 */       defaults.add("SearchField.buttonMargin", new InsetsUIResource(0, 0, 0, 0));
/*     */     }
/*     */   }
/*     */   
/*     */   protected void addMotifDefaults(LookAndFeelAddons addon, DefaultsList defaults)
/*     */   {
/*  79 */     super.addMotifDefaults(addon, defaults);
/*     */     
/*  81 */     defaults.add("SearchField.icon", getIcon("macosx/resources/search.png"));
/*  82 */     defaults.add("SearchField.rolloverIcon", getIcon("macosx/resources/search.png"));
/*  83 */     defaults.add("SearchField.pressedIcon", getIcon("macosx/resources/search.png"));
/*  84 */     defaults.add("SearchField.popupIcon", getIcon("macosx/resources/search_popup.png"));
/*  85 */     defaults.add("SearchField.popupRolloverIcon", getIcon("macosx/resources/search_popup.png"));
/*  86 */     defaults.add("SearchField.popupPressedIcon", getIcon("macosx/resources/search_popup.png"));
/*  87 */     defaults.add("SearchField.clearIcon", getIcon("macosx/resources/clear.png"));
/*  88 */     defaults.add("SearchField.clearRolloverIcon", getIcon("macosx/resources/clear_rollover.png"));
/*  89 */     defaults.add("SearchField.clearPressedIcon", getIcon("macosx/resources/clear_pressed.png"));
/*     */   }
/*     */   
/*     */   protected void addMacDefaults(LookAndFeelAddons addon, DefaultsList defaults)
/*     */   {
/*  94 */     super.addMacDefaults(addon, defaults);
/*     */     
/*  96 */     defaults.add("SearchField.icon", getIcon("macosx/resources/search.png"));
/*  97 */     defaults.add("SearchField.rolloverIcon", getIcon("macosx/resources/search.png"));
/*  98 */     defaults.add("SearchField.pressedIcon", getIcon("macosx/resources/search.png"));
/*  99 */     defaults.add("SearchField.popupIcon", getIcon("macosx/resources/search_popup.png"));
/* 100 */     defaults.add("SearchField.popupRolloverIcon", getIcon("macosx/resources/search_popup.png"));
/* 101 */     defaults.add("SearchField.popupPressedIcon", getIcon("macosx/resources/search_popup.png"));
/* 102 */     defaults.add("SearchField.clearIcon", getIcon("macosx/resources/clear.png"));
/* 103 */     defaults.add("SearchField.clearRolloverIcon", getIcon("macosx/resources/clear_rollover.png"));
/* 104 */     defaults.add("SearchField.clearPressedIcon", getIcon("macosx/resources/clear_pressed.png"));
/* 105 */     defaults.add("SearchField.buttonMargin", new InsetsUIResource(0, 0, 0, 0));
/* 106 */     defaults.add("SearchField.popupSource", "searchField");
/*     */   }
/*     */   
/*     */ 
/*     */   protected boolean isWindows(LookAndFeelAddons addon)
/*     */   {
/* 112 */     return (super.isWindows(addon)) || (UIManager.getLookAndFeel().getClass().getName().indexOf("Windows") != -1) || (UIManager.getLookAndFeel().getClass().getName().indexOf("PlasticXP") != -1);
/*     */   }
/*     */   
/*     */ 
/*     */   private IconUIResource getIcon(String resourceName)
/*     */   {
/* 118 */     URL url = getClass().getResource(resourceName);
/* 119 */     if (url == null) {
/* 120 */       return null;
/*     */     }
/* 122 */     return new IconUIResource(new ImageIcon(url));
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\plaf\SearchFieldAddon.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */