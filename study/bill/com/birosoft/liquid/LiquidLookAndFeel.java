/*      */ package com.birosoft.liquid;
/*      */ 
/*      */ import com.birosoft.liquid.borders.LiquidFocusCellHighlightBorder;
/*      */ import com.birosoft.liquid.borders.LiquidListBorder;
/*      */ import com.birosoft.liquid.borders.LiquidPopupMenuBorder;
/*      */ import com.birosoft.liquid.borders.LiquidTextFieldBorder;
/*      */ import com.birosoft.liquid.skin.SkinImageCache;
/*      */ import java.awt.Color;
/*      */ import java.awt.Dimension;
/*      */ import java.awt.Font;
/*      */ import java.awt.Image;
/*      */ import java.awt.Insets;
/*      */ import java.awt.Toolkit;
/*      */ import java.io.PrintStream;
/*      */ import java.util.HashMap;
/*      */ import java.util.Properties;
/*      */ import javax.swing.ImageIcon;
/*      */ import javax.swing.JDialog;
/*      */ import javax.swing.JFrame;
/*      */ import javax.swing.UIDefaults;
/*      */ import javax.swing.UIDefaults.LazyInputMap;
/*      */ import javax.swing.UIManager;
/*      */ import javax.swing.UIManager.LookAndFeelInfo;
/*      */ import javax.swing.border.Border;
/*      */ import javax.swing.border.CompoundBorder;
/*      */ import javax.swing.border.EmptyBorder;
/*      */ import javax.swing.border.LineBorder;
/*      */ import javax.swing.plaf.ColorUIResource;
/*      */ import javax.swing.plaf.InsetsUIResource;
/*      */ import javax.swing.plaf.basic.BasicBorders.MarginBorder;
/*      */ import javax.swing.plaf.basic.BasicLookAndFeel;
/*      */ import org.jvnet.lafplugin.ComponentPluginManager;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class LiquidLookAndFeel
/*      */   extends BasicLookAndFeel
/*      */ {
/*      */   public static final String PLUGIN_XML = "META-INF/liquid-plugin.xml";
/*  102 */   protected static ComponentPluginManager pluginManager = new ComponentPluginManager("META-INF/liquid-plugin.xml");
/*      */   
/*      */   protected static UIDefaults uiDefaults;
/*      */   
/*  106 */   protected static boolean defaultRowBackgroundMode = true;
/*  107 */   protected static boolean showTableGrids = false;
/*  108 */   protected static boolean panelTransparency = true;
/*  109 */   protected static boolean winDecoPanther = false;
/*  110 */   protected static boolean toolbarButtonsFocusable = false;
/*  111 */   protected static boolean toolbarFlattedButtons = true;
/*  112 */   protected static boolean toolbarFlattedButtonsRollover = true;
/*  113 */   private static boolean bgStipples = true;
/*  114 */   private static boolean isInstalled = false;
/*  115 */   protected static String fontName = "Helvetica";
/*      */   
/*      */ 
/*      */ 
/*      */   static boolean isWindows()
/*      */   {
/*  121 */     String osName = System.getProperty("os.name");
/*  122 */     if ((osName != null) && (osName.indexOf("Windows") != -1)) {
/*  123 */       return true;
/*      */     }
/*  125 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   static boolean isPreTiger()
/*      */   {
/*  132 */     String jre = System.getProperty("java.version", "");
/*  133 */     String versionString = jre.substring(2, 3);
/*  134 */     Integer version = versionString != null ? Integer.valueOf(versionString) : null;
/*      */     
/*  136 */     if ((version != null) && (version.intValue() < 5)) {
/*  137 */       return true;
/*      */     }
/*  139 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*  145 */   protected Toolkit awtToolkit = Toolkit.getDefaultToolkit();
/*  146 */   private HashMap colorMap = new HashMap();
/*  147 */   Border focusCellHighlightBorder = new LiquidFocusCellHighlightBorder(new Color(86, 46, 0));
/*      */   
/*  149 */   Border listBorder = new LiquidListBorder();
/*  150 */   Border zeroEmptyBorder = new EmptyBorder(0, 0, 0, 0);
/*  151 */   Object fieldInputMap = new UIDefaults.LazyInputMap(new Object[] { "ctrl C", "copy-to-clipboard", "ctrl V", "paste-from-clipboard", "ctrl X", "cut-to-clipboard", "COPY", "copy-to-clipboard", "PASTE", "paste-from-clipboard", "CUT", "cut-to-clipboard", "shift LEFT", "selection-backward", "shift KP_LEFT", "selection-backward", "shift RIGHT", "selection-forward", "shift KP_RIGHT", "selection-forward", "ctrl LEFT", "caret-previous-word", "ctrl KP_LEFT", "caret-previous-word", "ctrl RIGHT", "caret-next-word", "ctrl KP_RIGHT", "caret-next-word", "ctrl shift LEFT", "selection-previous-word", "ctrl shift KP_LEFT", "selection-previous-word", "ctrl shift RIGHT", "selection-next-word", "ctrl shift KP_RIGHT", "selection-next-word", "ctrl A", "select-all", "HOME", "caret-begin-line", "END", "caret-end-line", "shift HOME", "selection-begin-line", "shift END", "selection-end-line", "typed \b", "delete-previous", "DELETE", "delete-next", "RIGHT", "caret-forward", "LEFT", "caret-backward", "KP_RIGHT", "caret-forward", "KP_LEFT", "caret-backward", "ENTER", "notify-field-accept", "ctrl BACK_SLASH", "unselect", "control shift O", "toggle-componentOrientation" });
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  186 */   Object multilineInputMap = new UIDefaults.LazyInputMap(new Object[] { "ctrl C", "copy-to-clipboard", "ctrl V", "paste-from-clipboard", "ctrl X", "cut-to-clipboard", "COPY", "copy-to-clipboard", "PASTE", "paste-from-clipboard", "CUT", "cut-to-clipboard", "shift LEFT", "selection-backward", "shift KP_LEFT", "selection-backward", "shift RIGHT", "selection-forward", "shift KP_RIGHT", "selection-forward", "ctrl LEFT", "caret-previous-word", "ctrl KP_LEFT", "caret-previous-word", "ctrl RIGHT", "caret-next-word", "ctrl KP_RIGHT", "caret-next-word", "ctrl shift LEFT", "selection-previous-word", "ctrl shift KP_LEFT", "selection-previous-word", "ctrl shift RIGHT", "selection-next-word", "ctrl shift KP_RIGHT", "selection-next-word", "ctrl A", "select-all", "HOME", "caret-begin-line", "END", "caret-end-line", "shift HOME", "selection-begin-line", "shift END", "selection-end-line", "UP", "caret-up", "KP_UP", "caret-up", "DOWN", "caret-down", "KP_DOWN", "caret-down", "PAGE_UP", "page-up", "PAGE_DOWN", "page-down", "shift PAGE_UP", "selection-page-up", "shift PAGE_DOWN", "selection-page-down", "ctrl shift PAGE_UP", "selection-page-left", "ctrl shift PAGE_DOWN", "selection-page-right", "shift UP", "selection-up", "shift KP_UP", "selection-up", "shift DOWN", "selection-down", "shift KP_DOWN", "selection-down", "ENTER", "insert-break", "typed \b", "delete-previous", "DELETE", "delete-next", "RIGHT", "caret-forward", "LEFT", "caret-backward", "KP_RIGHT", "caret-forward", "KP_LEFT", "caret-backward", "TAB", "insert-tab", "ctrl BACK_SLASH", "unselect", "ctrl HOME", "caret-begin", "ctrl END", "caret-end", "ctrl shift HOME", "selection-begin", "ctrl shift END", "selection-end", "ctrl T", "next-link-action", "ctrl shift T", "previous-link-action", "ctrl SPACE", "activate-link-action", "control shift O", "toggle-componentOrientation" });
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  240 */   Object formattedInputMap = new UIDefaults.LazyInputMap(new Object[] { "ctrl C", "copy-to-clipboard", "ctrl V", "paste-from-clipboard", "ctrl X", "cut-to-clipboard", "COPY", "copy-to-clipboard", "PASTE", "paste-from-clipboard", "CUT", "cut-to-clipboard", "shift LEFT", "selection-backward", "shift KP_LEFT", "selection-backward", "shift RIGHT", "selection-forward", "shift KP_RIGHT", "selection-forward", "ctrl LEFT", "caret-previous-word", "ctrl KP_LEFT", "caret-previous-word", "ctrl RIGHT", "caret-next-word", "ctrl KP_RIGHT", "caret-next-word", "ctrl shift LEFT", "selection-previous-word", "ctrl shift KP_LEFT", "selection-previous-word", "ctrl shift RIGHT", "selection-next-word", "ctrl shift KP_RIGHT", "selection-next-word", "ctrl A", "select-all", "HOME", "caret-begin-line", "END", "caret-end-line", "shift HOME", "selection-begin-line", "shift END", "selection-end-line", "typed \b", "delete-previous", "DELETE", "delete-next", "RIGHT", "caret-forward", "LEFT", "caret-backward", "KP_RIGHT", "caret-forward", "KP_LEFT", "caret-backward", "ENTER", "notify-field-accept", "ctrl BACK_SLASH", "unselect", "control shift O", "toggle-componentOrientation", "ESCAPE", "reset-field-edit", "UP", "increment", "KP_UP", "increment", "DOWN", "decrement", "KP_DOWN", "decrement" });
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  275 */   private boolean windowslfforfilechooser = false;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public LiquidLookAndFeel()
/*      */   {
/*  283 */     this.windowslfforfilechooser = false;
/*      */     
/*  285 */     if (!isInstalled) {
/*  286 */       isInstalled = true;
/*      */       
/*      */ 
/*  289 */       UIManager.installLookAndFeel(new UIManager.LookAndFeelInfo("LiquidLookAndFeel", "com.birosoft.liquid.LiquidLookAndFeel"));
/*      */       
/*      */ 
/*      */ 
/*  293 */       loadConfProperties();
/*      */       
/*      */ 
/*  296 */       loadSystemProperties();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getID()
/*      */   {
/*  312 */     return "Liquid";
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getName()
/*      */   {
/*  327 */     return "Liquid";
/*      */   }
/*      */   
/*      */   public static ColorUIResource getControl() {
/*  331 */     return (ColorUIResource)uiDefaults.get("control");
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getDescription()
/*      */   {
/*  342 */     return "The Liquid Look and Feel version 2.9.1. Published under the GNU Lesser General Public License.";
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isNativeLookAndFeel()
/*      */   {
/*  352 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public final boolean isSupportedLookAndFeel()
/*      */   {
/*  362 */     return true;
/*      */   }
/*      */   
/*      */   public boolean getSupportsWindowDecorations() {
/*  366 */     return true;
/*      */   }
/*      */   
/*      */   private void loadConfProperties() {
/*  370 */     Properties liquidProps = new Properties();
/*      */     try
/*      */     {
/*  373 */       ClassLoader classLoader = getClass().getClassLoader();
/*  374 */       liquidProps.load(classLoader.getResourceAsStream("liquidlnf.conf"));
/*      */       
/*  376 */       boolean boolValue = false;
/*  377 */       String stringValue = null;
/*      */       
/*      */ 
/*  380 */       stringValue = liquidProps.getProperty("panelTransparency", null);
/*  381 */       if (stringValue != null) {
/*  382 */         boolValue = Boolean.valueOf(stringValue).booleanValue();
/*  383 */         setPanelTransparency(boolValue);
/*      */       }
/*      */       
/*      */ 
/*  387 */       stringValue = liquidProps.getProperty("showTableGrids", null);
/*  388 */       if (stringValue != null) {
/*  389 */         boolValue = Boolean.valueOf(stringValue).booleanValue();
/*  390 */         setShowTableGrids(boolValue);
/*      */       }
/*      */       
/*      */ 
/*  394 */       stringValue = liquidProps.getProperty("stipples", null);
/*  395 */       if (stringValue != null) {
/*  396 */         boolValue = Boolean.valueOf(stringValue).booleanValue();
/*  397 */         setStipples(boolValue);
/*      */       }
/*      */       
/*      */ 
/*  401 */       stringValue = liquidProps.getProperty("alternateBackground", null);
/*  402 */       if (stringValue != null) {
/*  403 */         boolValue = Boolean.valueOf(stringValue).booleanValue();
/*  404 */         setDefaultRowBackgroundMode(boolValue);
/*      */       }
/*      */       
/*      */ 
/*  408 */       stringValue = liquidProps.getProperty("windowDecoration", null);
/*  409 */       if (stringValue != null) {
/*  410 */         setLiquidDecorations((stringValue != null) && (stringValue.length() > 0), stringValue);
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*  415 */       stringValue = liquidProps.getProperty("toolbarFlattedButtons", null);
/*  416 */       if (stringValue != null) {
/*  417 */         boolValue = Boolean.valueOf(stringValue).booleanValue();
/*  418 */         setToolbarFlattedButtons(boolValue);
/*      */       }
/*      */       
/*      */ 
/*  422 */       stringValue = liquidProps.getProperty("toolbarFlattedButtonsRollover", null);
/*  423 */       if (stringValue != null) {
/*  424 */         boolValue = Boolean.valueOf(stringValue).booleanValue();
/*  425 */         setToolbarFlattedButtonsRollover(boolValue);
/*      */       }
/*      */       
/*      */ 
/*  429 */       stringValue = liquidProps.getProperty("toolbarButtonsFocusable", null);
/*  430 */       if (stringValue != null) {
/*  431 */         boolValue = Boolean.valueOf(stringValue).booleanValue();
/*  432 */         setToolbarButtonsFocusable(boolValue);
/*      */       }
/*      */     }
/*      */     catch (Exception e) {}
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void loadSystemProperties()
/*      */   {
/*      */     try
/*      */     {
/*  449 */       boolean boolValue = false;
/*  450 */       String stringValue = null;
/*      */       
/*      */ 
/*  453 */       stringValue = System.getProperty("liquidlnf.panelTransparency", null);
/*  454 */       if (stringValue != null) {
/*  455 */         boolValue = Boolean.valueOf(stringValue).booleanValue();
/*  456 */         setPanelTransparency(boolValue);
/*      */       }
/*      */       
/*      */ 
/*  460 */       stringValue = System.getProperty("liquidlnf.showTableGrids", null);
/*  461 */       if (stringValue != null) {
/*  462 */         boolValue = Boolean.valueOf(stringValue).booleanValue();
/*  463 */         setShowTableGrids(boolValue);
/*      */       }
/*      */       
/*      */ 
/*  467 */       stringValue = System.getProperty("liquidlnf.stipples", null);
/*  468 */       if (stringValue != null) {
/*  469 */         boolValue = Boolean.valueOf(stringValue).booleanValue();
/*  470 */         setStipples(boolValue);
/*      */       }
/*      */       
/*      */ 
/*  474 */       stringValue = System.getProperty("liquidlnf.alternateBackground", null);
/*  475 */       if (stringValue != null) {
/*  476 */         boolValue = Boolean.valueOf(stringValue).booleanValue();
/*  477 */         setDefaultRowBackgroundMode(boolValue);
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*  482 */       stringValue = System.getProperty("liquidlnf.windowDecoration", null);
/*  483 */       if (stringValue != null) {
/*  484 */         setLiquidDecorations((stringValue != null) && (stringValue.length() > 0), stringValue);
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*  489 */       stringValue = System.getProperty("liquidlnf.toolbarFlattedButtons", null);
/*  490 */       if (stringValue != null) {
/*  491 */         boolValue = Boolean.valueOf(stringValue).booleanValue();
/*  492 */         setToolbarFlattedButtons(boolValue);
/*      */       }
/*      */       
/*      */ 
/*  496 */       stringValue = System.getProperty("liquidlnf.toolbarFlattedButtonsRollover", null);
/*  497 */       if (stringValue != null) {
/*  498 */         boolValue = Boolean.valueOf(stringValue).booleanValue();
/*  499 */         setToolbarFlattedButtonsRollover(boolValue);
/*      */       }
/*      */       
/*      */ 
/*  503 */       stringValue = System.getProperty("liquidlnf.toolbarButtonsFocusable", null);
/*  504 */       if (stringValue != null) {
/*  505 */         boolValue = Boolean.valueOf(stringValue).booleanValue();
/*  506 */         setToolbarButtonsFocusable(boolValue);
/*      */       }
/*      */     }
/*      */     catch (Exception e) {}
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void initClassDefaults(UIDefaults table)
/*      */   {
/*  529 */     uiDefaults = table;
/*  530 */     super.initClassDefaults(table);
/*      */     
/*  532 */     table.putDefaults(new Object[] { "ButtonUI", "com.birosoft.liquid.LiquidButtonUI", "CheckBoxUI", "com.birosoft.liquid.LiquidCheckBoxUI", "TextFieldUI", "com.birosoft.liquid.LiquidTextFieldUI", "FormattedTextFieldUI", "com.birosoft.liquid.LiquidTextFieldUI", "PasswordTextFieldUI", "com.birosoft.liquid.LiquidPasswordFieldUI", "PasswordFieldUI", "com.birosoft.liquid.LiquidPasswordFieldUI", "SliderUI", "com.birosoft.liquid.LiquidSliderUI", "SpinnerUI", "com.birosoft.liquid.LiquidSpinnerUI", "ToolBarUI", "com.birosoft.liquid.LiquidToolBarUI", "MenuBarUI", "com.birosoft.liquid.LiquidMenuBarUI", "MenuUI", "com.birosoft.liquid.LiquidMenuUI", "PanelUI", "com.birosoft.liquid.LiquidPanelUI", "MenuItemUI", "com.birosoft.liquid.LiquidMenuItemUI", "CheckBoxMenuItemUI", "com.birosoft.liquid.LiquidCheckBoxMenuItemUI", "RadioButtonMenuItemUI", "com.birosoft.liquid.LiquidRadioButtonMenuItemUI", "TableUI", "com.birosoft.liquid.LiquidTableUI", "TableHeaderUI", "com.birosoft.liquid.LiquidTableHeaderUI", "ScrollBarUI", "com.birosoft.liquid.LiquidScrollBarUI", "TabbedPaneUI", "com.birosoft.liquid.LiquidTabbedPaneUI", "ToggleButtonUI", "com.birosoft.liquid.LiquidToggleButtonUI", "ScrollPaneUI", "com.birosoft.liquid.LiquidScrollPaneUI", "ProgressBarUI", "com.birosoft.liquid.LiquidProgressBarUI", "InternalFrameUI", "com.birosoft.liquid.LiquidInternalFrameUI", "DesktopIconUI", "com.birosoft.liquid.LiquidDesktopIconUI", "RadioButtonUI", "com.birosoft.liquid.LiquidRadioButtonUI", "ComboBoxUI", "com.birosoft.liquid.LiquidComboBoxUI", "ListUI", "com.birosoft.liquid.LiquidListUI", "SeparatorUI", "com.birosoft.liquid.LiquidSeparatorUI", "PopupMenuSeparatorUI", "com.birosoft.liquid.LiquidPopupMenuSeparatorUI", "SplitPaneUI", "com.birosoft.liquid.LiquidSplitPaneUI", "FileChooserUI", "com.birosoft.liquid.FileChooserBasicUI", "RootPaneUI", "com.birosoft.liquid.LiquidRootPaneUI", "OptionPaneUI", "com.birosoft.liquid.LiquidOptionPaneUI" });
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  570 */     pluginManager.processAllDefaultsEntries(table, null);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void initSystemColorDefaults(UIDefaults table)
/*      */   {
/*  584 */     this.colorMap.put("activeBackground", "#3E91EB");
/*  585 */     this.colorMap.put("activeBlend", "#3E91EB");
/*  586 */     this.colorMap.put("activeForeground", "#FFFFFF");
/*  587 */     this.colorMap.put("activeTitleBtnBg", "#AFD6FF");
/*  588 */     this.colorMap.put("alternateBackground", "#EEF6FF");
/*  589 */     this.colorMap.put("background", "#F6F5F4");
/*  590 */     this.colorMap.put("buttonBackground", "#D7E7F9");
/*  591 */     this.colorMap.put("buttonForeground", "#000000");
/*  592 */     this.colorMap.put("foreground", "#000000");
/*  593 */     this.colorMap.put("inactiveBackground", "#AFD6FF");
/*  594 */     this.colorMap.put("inactiveBlend", "#AFD6FF");
/*  595 */     this.colorMap.put("inactiveForeground", "#232323");
/*  596 */     this.colorMap.put("inactiveTitleBtnBg", "#DAEEFF");
/*  597 */     this.colorMap.put("linkColor", "#0000C0");
/*  598 */     this.colorMap.put("selectBackground", "#A9D1FF");
/*  599 */     this.colorMap.put("selectForeground", "#030303");
/*  600 */     this.colorMap.put("visitedLinkColor", "#800080");
/*  601 */     this.colorMap.put("windowBackground", "#FFFFFF");
/*  602 */     this.colorMap.put("windowForeground", "#000000");
/*      */     
/*  604 */     String[] defaultSystemColors = { "desktop", (String)this.colorMap.get("alternateBackground"), "activeCaption", (String)this.colorMap.get("activeBackground"), "activeCaptionText", (String)this.colorMap.get("activeForeground"), "activeCaptionBorder", (String)this.colorMap.get("activeBackground"), "inactiveCaption", (String)this.colorMap.get("inactiveBackground"), "inactiveCaptionText", (String)this.colorMap.get("inactiveForeground"), "inactiveCaptionBorder", (String)this.colorMap.get("inactiveBackground"), "window", (String)this.colorMap.get("background"), "windowBorder", (String)this.colorMap.get("windowBackground"), "windowText", (String)this.colorMap.get("windowForeground"), "menu", (String)this.colorMap.get("background"), "menuText", (String)this.colorMap.get("foreground"), "text", (String)this.colorMap.get("windowBackground"), "textText", (String)this.colorMap.get("windowForeground"), "textHighlight", (String)this.colorMap.get("selectBackground"), "textHighlightText", (String)this.colorMap.get("selectForeground"), "textInactiveText", "#A7A5A3", "control", (String)this.colorMap.get("background"), "controlText", (String)this.colorMap.get("buttonForeground"), "controlHighlight", (String)this.colorMap.get("buttonBackground"), "controlLtHighlight", (String)this.colorMap.get("selectBackground"), "controlShadow", "#BBBBBB", "controlLightShadow", "#000000", "controlDkShadow", "#000000", "scrollbar", "#000000", "info", (String)this.colorMap.get("foreground"), "infoText", (String)this.colorMap.get("foreground") };
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  635 */     loadSystemColors(table, defaultSystemColors, false);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void initComponentDefaults(UIDefaults table)
/*      */   {
/*  649 */     super.initComponentDefaults(table);
/*      */     
/*      */ 
/*  652 */     Border border = new EmptyBorder(0, 0, 0, 0);
/*      */     
/*      */ 
/*      */ 
/*  656 */     table.put("Button.margin", getButtonMargin());
/*  657 */     table.put("Button.border", new BasicBorders.MarginBorder());
/*  658 */     table.put("ToggleButton.margin", getButtonMargin());
/*  659 */     table.put("ToggleButton.border", new BasicBorders.MarginBorder());
/*  660 */     table.put("ToggleButton.background", table.get("window"));
/*  661 */     table.put("TextField.border", new LiquidTextFieldBorder());
/*  662 */     table.put("PasswordField.border", new LiquidTextFieldBorder());
/*  663 */     table.put("Spinner.border", new LiquidTextFieldBorder(new Insets(2, 2, 2, 2)));
/*      */     
/*      */ 
/*      */ 
/*  667 */     table.put("ToolBar.background", table.get("window"));
/*  668 */     table.put("MenuBar.background", table.get("window"));
/*  669 */     border = new EmptyBorder(2, 2, 2, 2);
/*  670 */     table.put("InternalFrame.border", border);
/*  671 */     table.put("InternalFrame.paletteBorder", border);
/*  672 */     table.put("InternalFrame.optionDialogBorder", new LiquidInternalFrameBorder());
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  680 */     border = new EmptyBorder(3, 1, 3, 1);
/*  681 */     table.put("Menu.border", border);
/*  682 */     table.put("MenuItem.border", border);
/*  683 */     table.put("CheckBoxMenuItem.border", border);
/*  684 */     table.put("RadioButtonMenuItem.border", border);
/*  685 */     table.put("CheckBoxMenuItem.checkIcon", loadIcon("menucheck.png", this));
/*  686 */     table.put("CheckBoxMenuItem.checkedIcon", loadIcon("menuchecked.png", this));
/*      */     
/*  688 */     table.put("RadioButtonMenuItem.checkIcon", loadIcon("menuradio.png", this));
/*      */     
/*  690 */     table.put("RadioButtonMenuItem.checkedIcon", loadIcon("menuradio_down.png", this));
/*      */     
/*  692 */     table.put("MenuItem.arrowIcon", loadIcon("menuarrow.png", this));
/*  693 */     table.put("MenuItem.selArrowIcon", loadIcon("menuarrowsel.png", this));
/*  694 */     table.put("Menu.submenuPopupOffsetX", new Integer(-3));
/*  695 */     table.put("Menu.submenuPopupOffsetY", new Integer(4));
/*  696 */     border = new LiquidPopupMenuBorder();
/*  697 */     table.put("PopupMenu.border", border);
/*      */     
/*      */ 
/*  700 */     table.put("SplitPane.dividerSize", new Integer(7));
/*  701 */     table.put("InternalFrame.paletteTitleHeight", new Integer(18));
/*  702 */     table.put("InternalFrame.frameTitleHeight", new Integer(24));
/*  703 */     table.put("InternalFrame.paletteTitleFont", getFont(0, 12));
/*  704 */     table.put("InternalFrame.titleFont", getFont(1, 13));
/*  705 */     table.put("InternalFrame.pantherTitleFont", getFont(1, 13));
/*  706 */     table.put("Panel.background", table.get("window"));
/*      */     
/*  708 */     table.put("TabbedPane.selectedTabPadInsets", new Insets(1, 1, 1, 1));
/*  709 */     table.put("TabbedPane.tabAreaInsets", new Insets(4, 2, 0, 0));
/*  710 */     table.put("TabbedPane.contentBorderInsets", new Insets(5, 0, 0, 0));
/*  711 */     table.put("TabbedPane.unselected", table.get("shadow"));
/*  712 */     table.put("TabbedPane.tabsOverlapBorder", Boolean.TRUE);
/*      */     
/*  714 */     table.put("Checkbox.select", table.get("shadow"));
/*  715 */     table.put("PopupMenu.background", table.get("window"));
/*  716 */     table.put("PopupMenu.foreground", Color.black);
/*      */     
/*  718 */     table.put("TextField.selectionForeground", table.get("textHighlightText"));
/*      */     
/*  720 */     table.put("TextField.selectionBackground", table.get("textHighlight"));
/*  721 */     table.put("TextField.background", table.get("text"));
/*  722 */     table.put("TextField.disabledBackground", table.get("window"));
/*      */     
/*  724 */     table.put("TextField.focusInputMap", this.fieldInputMap);
/*  725 */     table.put("PasswordField.focusInputMap", this.fieldInputMap);
/*  726 */     table.put("TextArea.focusInputMap", this.multilineInputMap);
/*  727 */     table.put("TextPane.focusInputMap", this.multilineInputMap);
/*  728 */     table.put("TextPane.background", table.get("text"));
/*  729 */     table.put("EditorPane.focusInputMap", this.multilineInputMap);
/*  730 */     table.put("FormattedTextField.focusInputMap", this.formattedInputMap);
/*      */     
/*  732 */     table.put("List.background", table.get("text"));
/*  733 */     table.put("List.border", this.zeroEmptyBorder);
/*  734 */     table.put("List.selectionForeground", table.get("textHighlightText"));
/*  735 */     table.put("List.selectionBackground", table.get("textHighlight"));
/*  736 */     table.put("List.focusCellHighlightBorder", this.focusCellHighlightBorder);
/*      */     
/*  738 */     table.put("ScrollPane.border", this.listBorder);
/*      */     
/*  740 */     table.put("ComboBox.border", new EmptyBorder(1, 1, 1, 1));
/*  741 */     table.put("ComboBox.foreground", table.get("textHighlightText"));
/*  742 */     table.put("ComboBox.background", table.get("text"));
/*  743 */     table.put("ComboBox.selectionForeground", table.get("textHighlightText"));
/*  744 */     table.put("ComboBox.selectionBackground", table.get("textHighlight"));
/*      */     
/*      */ 
/*  747 */     table.put("ComboBox.ancestorInputMap", new UIDefaults.LazyInputMap(new Object[] { "ESCAPE", "hidePopup", "PAGE_UP", "pageUpPassThrough", "PAGE_DOWN", "pageDownPassThrough", "HOME", "homePassThrough", "END", "endPassThrough", "DOWN", "selectNext", "KP_DOWN", "selectNext", "alt DOWN", "togglePopup", "alt KP_DOWN", "togglePopup", "alt UP", "togglePopup", "alt KP_UP", "togglePopup", "SPACE", "spacePopup", "ENTER", "enterPressed", "UP", "selectPrevious", "KP_UP", "selectPrevious" }));
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  766 */     table.put("InternalFrame.paletteCloseIcon", loadIcon("closebutton.png", this));
/*      */     
/*  768 */     table.put("InternalFrame.closeIcon", loadWinButtonIcon("close.png", this));
/*      */     
/*  770 */     table.put("InternalFrame.maximizeIcon", loadWinButtonIcon("maximize.png", this));
/*      */     
/*  772 */     table.put("InternalFrame.iconifyIcon", loadWinButtonIcon("minimize.png", this));
/*      */     
/*  774 */     table.put("InternalFrame.minimizeIcon", loadWinButtonIcon("restore.png", this));
/*      */     
/*  776 */     table.put("InternalFrame.icon", loadIcon("internalframeicon.png", this));
/*  777 */     table.put("InternalFrame.pantherFrameIcon", null);
/*      */     
/*  779 */     table.put("InternalFrame.pantherIcon", null);
/*      */     
/*  781 */     table.put("InternalFrame.pantherIconInactive", null);
/*      */     
/*      */ 
/*      */ 
/*  785 */     table.put("Table.background", table.get("text"));
/*  786 */     table.put("Table.foreground", table.get("controlText"));
/*  787 */     table.put("Table.selectionBackground", table.get("textHighlight"));
/*  788 */     table.put("Table.selectionForeground", table.get("textHighlightText"));
/*  789 */     table.put("Table.focusCellBackground", table.get("textHighlight"));
/*  790 */     table.put("Table.focusCellForeground", table.get("textHighlightText"));
/*  791 */     table.put("Table.focusCellHighlightBorder", this.focusCellHighlightBorder);
/*  792 */     table.put("Table.scrollPaneBorder", this.listBorder);
/*      */     
/*  794 */     table.put("TableHeader.font", getFont(0, 12));
/*  795 */     table.put("TableHeader.foreground", table.get("textText"));
/*  796 */     table.put("TableHeader.background", table.get("window"));
/*  797 */     table.put("TableHeader.cellBorder", this.zeroEmptyBorder);
/*      */     
/*  799 */     table.put("ToolTip.background", new Color(255, 255, 225));
/*  800 */     table.put("ToolTip.foreground", new Color(0, 0, 0));
/*  801 */     table.put("ToolTip.font", getFont(0, 12));
/*  802 */     table.put("ToolTip.border", new CompoundBorder(new LineBorder(Color.black, 1), new EmptyBorder(2, 2, 2, 2)));
/*      */     
/*      */ 
/*      */ 
/*  806 */     table.put("Tree.font", getFont(0, 12));
/*  807 */     table.put("Tree.selectionForeground", table.get("textHighlightText"));
/*  808 */     table.put("Tree.selectionBackground", table.get("textHighlight"));
/*  809 */     table.put("Tree.foreground", table.get("textText"));
/*  810 */     table.put("Tree.background", table.get("text"));
/*  811 */     table.put("Tree.hash", table.get("text"));
/*  812 */     table.put("Tree.expandedIcon", loadIcon("treeminus.png", this));
/*  813 */     table.put("Tree.collapsedIcon", loadIcon("treeplus.png", this));
/*  814 */     table.put("Tree.openIcon", loadIcon("treefolderopened.png", this));
/*  815 */     table.put("Tree.closedIcon", loadIcon("treefolderclosed.png", this));
/*  816 */     table.put("Tree.leafIcon", loadIcon("treeleaf.png", this));
/*  817 */     table.put("Tree.rowHeight", new Integer(18));
/*  818 */     table.put("Tree.selectionBorderColor", new Color(86, 46, 0));
/*      */     
/*  820 */     table.put("SplitPane.background", table.get("text"));
/*  821 */     table.put("SplitPane.border", this.listBorder);
/*  822 */     table.put("SplitPaneDivider.border", this.zeroEmptyBorder);
/*      */     
/*  824 */     table.put("FileView.directoryIcon", loadIcon("treefolderclosed.png", this));
/*      */     
/*  826 */     table.put("FileView.computerIcon", loadIcon("computericon.png", this));
/*  827 */     table.put("FileView.fileIcon", loadIcon("document.png", this));
/*  828 */     table.put("FileView.floppyDriveIcon", loadIcon("floppy.png", this));
/*  829 */     table.put("FileView.hardDriveIcon", loadIcon("harddisk.png", this));
/*      */     
/*  831 */     table.put("FileChooser.detailsViewIcon", loadIcon("filedetails.png", this));
/*      */     
/*  833 */     table.put("FileChooser.homeFolderIcon", loadIcon("desktopicon.png", this));
/*      */     
/*  835 */     table.put("FileChooser.listViewIcon", loadIcon("filelist.png", this));
/*  836 */     table.put("FileChooser.newFolderIcon", loadIcon("newfolder.png", this));
/*  837 */     table.put("FileChooser.upFolderIcon", loadIcon("parentdirectory.png", this));
/*      */     
/*  839 */     table.put("FileChooser.upFolderIcon", loadIcon("parentdirectory.png", this));
/*      */     
/*      */ 
/*  842 */     table.put("OptionPane.errorIcon", loadIcon("error.png", this));
/*  843 */     table.put("OptionPane.informationIcon", loadIcon("information.png", this));
/*      */     
/*  845 */     table.put("OptionPane.warningIcon", loadIcon("warning.png", this));
/*  846 */     table.put("OptionPane.questionIcon", loadIcon("question.png", this));
/*      */     
/*  848 */     table.put("RootPane.colorChooserDialogBorder", LiquidFrameBorder.getInstance());
/*      */     
/*  850 */     table.put("RootPane.errorDialogBorder", LiquidFrameBorder.getInstance());
/*  851 */     table.put("RootPane.fileChooserDialogBorder", LiquidFrameBorder.getInstance());
/*      */     
/*  853 */     table.put("RootPane.frameBorder", LiquidFrameBorder.getInstance());
/*  854 */     table.put("RootPane.informationDialogBorder", LiquidFrameBorder.getInstance());
/*      */     
/*  856 */     table.put("RootPane.plainDialogBorder", LiquidFrameBorder.getInstance());
/*  857 */     table.put("RootPane.questionDialogBorder", LiquidFrameBorder.getInstance());
/*      */     
/*  859 */     table.put("RootPane.warningDialogBorder", LiquidFrameBorder.getInstance());
/*      */     
/*      */ 
/*      */ 
/*  863 */     table.put("ProgressBar.horizontalSize", new Dimension(146, 14));
/*  864 */     table.put("ProgressBar.verticalSize", new Dimension(14, 146));
/*  865 */     table.put("ProgressBar.font", getFont(0, 12));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static ImageIcon loadIcon(String file, Object invoker)
/*      */   {
/*  881 */     return loadIconImmediately(file, invoker);
/*      */   }
/*      */   
/*      */ 
/*      */   public static ImageIcon loadWinButtonIcon(String file, Object invoker)
/*      */   {
/*  887 */     if (winDecoPanther) {
/*  888 */       file = "panther-" + file;
/*      */     }
/*      */     
/*  891 */     return loadIconImmediately(file, invoker);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static ImageIcon loadIconImmediately(String file, Object invoker)
/*      */   {
/*      */     try
/*      */     {
/*  903 */       Image img = SkinImageCache.getInstance().getImage(file);
/*  904 */       ImageIcon icon = new ImageIcon(img);
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*  909 */       if (icon.getIconWidth() <= 0) {
/*  910 */         System.out.println("******************** File " + file + " not found. Exiting");
/*      */         
/*  912 */         System.exit(1);
/*      */       }
/*      */       
/*  915 */       return icon;
/*      */     } catch (Exception exception) {
/*  917 */       exception.printStackTrace();
/*  918 */       System.out.println("Error getting resource " + file);
/*      */     }
/*  920 */     return null;
/*      */   }
/*      */   
/*      */   protected Font getFont(int type, int size)
/*      */   {
/*  925 */     return new Font(fontName, type, size);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private InsetsUIResource getButtonMargin()
/*      */   {
/*  933 */     return new InsetsUIResource(4, 16, 4, 16);
/*      */   }
/*      */   
/*      */   public static Color getLightControl() {
/*  937 */     return (Color)uiDefaults.get("control");
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public static Color getDarkControl()
/*      */   {
/*  944 */     return new Color(127, 127, 127);
/*      */   }
/*      */   
/*      */   public static Color getBackgroundColor() {
/*  948 */     return (Color)uiDefaults.get("window");
/*      */   }
/*      */   
/*      */   public static Color getDesktopColor() {
/*  952 */     return (Color)uiDefaults.get("desktop");
/*      */   }
/*      */   
/*      */   protected static Color getWindowTitleInactiveForeground() {
/*  956 */     return (Color)uiDefaults.get("inactiveCaptionText");
/*      */   }
/*      */   
/*      */   public static Color getWindowBackground() {
/*  960 */     return (Color)uiDefaults.get("window");
/*      */   }
/*      */   
/*      */   public static Color getButtonBackground() {
/*  964 */     return (Color)uiDefaults.get("controlHighlight");
/*      */   }
/*      */   
/*      */ 
/*      */   /**
/*      */    * @deprecated
/*      */    */
/*      */   public static void setDefaultTableBackgroundMode(boolean b)
/*      */   {
/*  973 */     setDefaultRowBackgroundMode(b);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static void setDefaultRowBackgroundMode(boolean b)
/*      */   {
/*  982 */     defaultRowBackgroundMode = b;
/*      */     
/*  984 */     if (!b) {
/*  985 */       showTableGrids = true;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static void setLiquidDecorations(boolean useWinDeco)
/*      */   {
/*  997 */     setLiquidDecorations(useWinDeco, "liquid");
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static void setLiquidDecorations(boolean useWinDeco, String internalFrameType)
/*      */   {
/* 1011 */     JFrame.setDefaultLookAndFeelDecorated(useWinDeco);
/* 1012 */     JDialog.setDefaultLookAndFeelDecorated(useWinDeco);
/*      */     
/*      */ 
/* 1015 */     if ((internalFrameType.equalsIgnoreCase("panther")) || (internalFrameType.equalsIgnoreCase("mac"))) {
/* 1016 */       winDecoPanther = true;
/*      */     }
/* 1018 */     else if (internalFrameType.equalsIgnoreCase("liquid")) {
/* 1019 */       winDecoPanther = false;
/*      */     }
/*      */     
/*      */ 
/* 1023 */     UIManager.put("InternalFrame.closeIcon", loadWinButtonIcon("close.png", null));
/*      */     
/* 1025 */     UIManager.put("InternalFrame.maximizeIcon", loadWinButtonIcon("maximize.png", null));
/*      */     
/* 1027 */     UIManager.put("InternalFrame.iconifyIcon", loadWinButtonIcon("minimize.png", null));
/*      */     
/* 1029 */     UIManager.put("InternalFrame.minimizeIcon", loadWinButtonIcon("restore.png", null));
/*      */   }
/*      */   
/*      */   protected static boolean areStipplesUsed()
/*      */   {
/* 1034 */     return bgStipples;
/*      */   }
/*      */   
/*      */   public static void setStipples(boolean b) {
/* 1038 */     bgStipples = b;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public static void setShowTableGrids(boolean showTableGrids)
/*      */   {
/* 1045 */     showTableGrids = showTableGrids;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public static void setPanelTransparency(boolean autoTransparency)
/*      */   {
/* 1052 */     panelTransparency = autoTransparency;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public static void setToolbarButtonsFocusable(boolean focusableButtons)
/*      */   {
/* 1059 */     toolbarButtonsFocusable = focusableButtons;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public static void setToolbarFlattedButtons(boolean flatedButtons)
/*      */   {
/* 1066 */     toolbarFlattedButtons = flatedButtons;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public static void setToolbarFlattedButtons(boolean flatedButtons, boolean paintRollover)
/*      */   {
/* 1074 */     setToolbarFlattedButtons(flatedButtons);
/* 1075 */     setToolbarFlattedButtonsRollover(paintRollover);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public static void setToolbarFlattedButtonsRollover(boolean paintRollover)
/*      */   {
/* 1082 */     toolbarFlattedButtonsRollover = paintRollover;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void initialize()
/*      */   {
/* 1092 */     super.initialize();
/*      */     
/*      */ 
/* 1095 */     pluginManager.initializeAll();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void uninitialize()
/*      */   {
/* 1103 */     super.uninitialize();
/*      */     
/*      */ 
/* 1106 */     pluginManager.uninitializeAll();
/*      */   }
/*      */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\com\birosoft\liquid\LiquidLookAndFeel.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       0.7.1
 */