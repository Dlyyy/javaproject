/*     */ package org.jdesktop.swingx.table;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Font;
/*     */ import java.beans.PropertyChangeEvent;
/*     */ import java.beans.PropertyChangeListener;
/*     */ import javax.swing.AbstractButton;
/*     */ import javax.swing.Icon;
/*     */ import javax.swing.JLabel;
/*     */ import javax.swing.table.TableCellRenderer;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Deprecated
/*     */ public class LabelProperties
/*     */   extends JLabel
/*     */ {
/*     */   private static final int BACKGROUND_SET = 1;
/*     */   private static final int FOREGROUND_SET = 2;
/*     */   private static final int FONT_SET = 4;
/*     */   private static final int HORIZONTAL_ALIGNMENT_SET = 8;
/*     */   private static final int HORIZONTAL_TEXT_POSITION_SET = 16;
/*     */   private static final int ICON_SET = 32;
/*     */   private static final int ICON_TEXT_GAP_SET = 64;
/*     */   private static final int TEXT_SET = 128;
/*     */   private static final int VERTICAL_ALIGNMENT_SET = 256;
/*     */   private static final int VERTICAL_TEXT_POSITION_SET = 512;
/*  58 */   private int setFlags = 0;
/*     */   
/*     */   public LabelProperties()
/*     */   {
/*  62 */     addPropertyChangeListener(new PropertyChangeListener() {
/*     */       public void propertyChange(PropertyChangeEvent e) {
/*  64 */         String propertyName = e.getPropertyName();
/*  65 */         Object value = e.getNewValue();
/*  66 */         if (propertyName.equals("background")) {
/*  67 */           if (value != null) {
/*  68 */             LabelProperties.access$076(LabelProperties.this, 1);
/*     */           } else {
/*  70 */             LabelProperties.access$072(LabelProperties.this, -2);
/*     */           }
/*     */         }
/*  73 */         else if (propertyName.equals("font")) {
/*  74 */           if (value != null) {
/*  75 */             LabelProperties.access$076(LabelProperties.this, 4);
/*     */           } else {
/*  77 */             LabelProperties.access$072(LabelProperties.this, -5);
/*     */           }
/*     */         }
/*  80 */         else if (propertyName.equals("foreground")) {
/*  81 */           if (value != null) {
/*  82 */             LabelProperties.access$076(LabelProperties.this, 2);
/*     */           } else {
/*  84 */             LabelProperties.access$072(LabelProperties.this, -3);
/*     */           }
/*     */         }
/*  87 */         else if (propertyName.equals("horizontalAlignment")) {
/*  88 */           if ((value != null) && (((Integer)value).intValue() != -1)) {
/*  89 */             LabelProperties.access$076(LabelProperties.this, 8);
/*     */           } else {
/*  91 */             LabelProperties.access$072(LabelProperties.this, -9);
/*     */           }
/*     */         }
/*  94 */         else if (propertyName.equals("horizontalTextPosition")) {
/*  95 */           if ((value != null) && (((Integer)value).intValue() != -1)) {
/*  96 */             LabelProperties.access$076(LabelProperties.this, 16);
/*     */           } else {
/*  98 */             LabelProperties.access$072(LabelProperties.this, -17);
/*     */           }
/*     */         }
/* 101 */         else if (propertyName.equals("icon")) {
/* 102 */           if (value != null) {
/* 103 */             LabelProperties.access$076(LabelProperties.this, 32);
/*     */           } else {
/* 105 */             LabelProperties.access$072(LabelProperties.this, -33);
/*     */           }
/*     */         }
/* 108 */         else if (propertyName.equals("iconTextGap")) {
/* 109 */           if ((value != null) && (((Integer)value).intValue() != -1)) {
/* 110 */             LabelProperties.access$076(LabelProperties.this, 64);
/*     */           } else {
/* 112 */             LabelProperties.access$072(LabelProperties.this, -65);
/*     */           }
/*     */         }
/* 115 */         else if (propertyName.equals("text")) {
/* 116 */           if (value != null) {
/* 117 */             LabelProperties.access$076(LabelProperties.this, 128);
/*     */           } else {
/* 119 */             LabelProperties.access$072(LabelProperties.this, 65407);
/*     */           }
/*     */         }
/* 122 */         else if (propertyName.equals("verticalAlignment")) {
/* 123 */           if ((value != null) && (((Integer)value).intValue() != -1)) {
/* 124 */             LabelProperties.access$076(LabelProperties.this, 256);
/*     */           } else {
/* 126 */             LabelProperties.access$072(LabelProperties.this, 65279);
/*     */           }
/*     */         }
/* 129 */         else if (propertyName.equals("verticalTextPosition")) {
/* 130 */           if ((value != null) && (((Integer)value).intValue() != -1)) {
/* 131 */             LabelProperties.access$076(LabelProperties.this, 512);
/*     */           } else {
/* 133 */             LabelProperties.access$072(LabelProperties.this, 65023);
/*     */           }
/*     */         }
/*     */       }
/*     */     });
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public LabelProperties(Color background, Color foreground, Font font, int horizontalAlignment, int horizontalTextPosition, int verticalAlignment, int verticalTextPosition, Icon icon, int iconTextGap, String text)
/*     */   {
/* 144 */     this();
/* 145 */     setBackground(background);
/* 146 */     setForeground(foreground);
/* 147 */     setFont(font);
/* 148 */     setHorizontalAlignment(horizontalAlignment);
/* 149 */     setHorizontalTextPosition(horizontalTextPosition);
/* 150 */     setVerticalAlignment(verticalAlignment);
/* 151 */     setVerticalTextPosition(verticalTextPosition);
/* 152 */     setIcon(icon);
/* 153 */     setIconTextGap(iconTextGap);
/* 154 */     setText(text);
/*     */   }
/*     */   
/*     */   public boolean isBackgroundSet()
/*     */   {
/* 159 */     return (this.setFlags & 0x1) > 0;
/*     */   }
/*     */   
/*     */   public boolean isForegroundSet()
/*     */   {
/* 164 */     return (this.setFlags & 0x2) > 0;
/*     */   }
/*     */   
/*     */   public boolean isFontSet()
/*     */   {
/* 169 */     return (this.setFlags & 0x4) > 0;
/*     */   }
/*     */   
/*     */   public boolean isHorizontalAlignmentSet() {
/* 173 */     return (this.setFlags & 0x8) > 0;
/*     */   }
/*     */   
/*     */   public boolean isHorizontalTextPositionSet() {
/* 177 */     return (this.setFlags & 0x10) > 0;
/*     */   }
/*     */   
/*     */   public boolean isIconSet() {
/* 181 */     return (this.setFlags & 0x20) > 0;
/*     */   }
/*     */   
/*     */   public boolean isIconTextGapSet() {
/* 185 */     return (this.setFlags & 0x40) > 0;
/*     */   }
/*     */   
/*     */   public boolean isTextSet() {
/* 189 */     return (this.setFlags & 0x80) > 0;
/*     */   }
/*     */   
/*     */   public boolean isVerticalAlignmentSet() {
/* 193 */     return (this.setFlags & 0x100) > 0;
/*     */   }
/*     */   
/*     */   public boolean isVerticalTextPositionSet() {
/* 197 */     return (this.setFlags & 0x200) > 0;
/*     */   }
/*     */   
/*     */   public boolean noPropertiesSet() {
/* 201 */     return this.setFlags == 0;
/*     */   }
/*     */   
/*     */   public void applyPropertiesTo(JLabel label) {
/* 205 */     if (noPropertiesSet()) {
/* 206 */       return;
/*     */     }
/* 208 */     if (isBackgroundSet()) {
/* 209 */       label.setBackground(getBackground());
/*     */     }
/* 211 */     if (isForegroundSet()) {
/* 212 */       label.setForeground(getForeground());
/*     */     }
/* 214 */     if (isFontSet()) {
/* 215 */       label.setFont(getFont());
/*     */     }
/* 217 */     if (isHorizontalAlignmentSet()) {
/* 218 */       label.setHorizontalAlignment(getHorizontalAlignment());
/*     */     }
/* 220 */     if (isHorizontalTextPositionSet()) {
/* 221 */       label.setHorizontalTextPosition(getHorizontalTextPosition());
/*     */     }
/* 223 */     if (isIconSet()) {
/* 224 */       label.setIcon(getIcon());
/*     */     }
/* 226 */     if (isIconTextGapSet()) {
/* 227 */       label.setIconTextGap(getIconTextGap());
/*     */     }
/* 229 */     if (isTextSet()) {
/* 230 */       label.setText(getText());
/*     */     }
/* 232 */     if (isVerticalAlignmentSet()) {
/* 233 */       label.setVerticalAlignment(getVerticalAlignment());
/*     */     }
/* 235 */     if (isVerticalTextPositionSet()) {
/* 236 */       label.setVerticalTextPosition(getVerticalTextPosition());
/*     */     }
/*     */   }
/*     */   
/*     */   public void applyPropertiesTo(AbstractButton button) {
/* 241 */     if (noPropertiesSet()) {
/* 242 */       return;
/*     */     }
/* 244 */     if (isBackgroundSet()) {
/* 245 */       button.setBackground(getBackground());
/*     */     }
/* 247 */     if (isForegroundSet()) {
/* 248 */       button.setForeground(getForeground());
/*     */     }
/* 250 */     if (isFontSet()) {
/* 251 */       button.setFont(getFont());
/*     */     }
/* 253 */     if (isHorizontalAlignmentSet()) {
/* 254 */       button.setHorizontalAlignment(getHorizontalAlignment());
/*     */     }
/* 256 */     if (isHorizontalTextPositionSet()) {
/* 257 */       button.setHorizontalTextPosition(getHorizontalTextPosition());
/*     */     }
/* 259 */     if (isIconSet()) {
/* 260 */       button.setIcon(getIcon());
/*     */     }
/* 262 */     if (isIconTextGapSet()) {
/* 263 */       button.setIconTextGap(getIconTextGap());
/*     */     }
/* 265 */     if (isTextSet()) {
/* 266 */       button.setText(getText());
/*     */     }
/* 268 */     if (isVerticalAlignmentSet()) {
/* 269 */       button.setVerticalAlignment(getVerticalAlignment());
/*     */     }
/* 271 */     if (isVerticalTextPositionSet()) {
/* 272 */       button.setVerticalTextPosition(getVerticalTextPosition());
/*     */     }
/*     */   }
/*     */   
/*     */   public void applyPropertiesTo(LabelProperties props) {
/* 277 */     if (noPropertiesSet()) {
/* 278 */       return;
/*     */     }
/* 280 */     if (isBackgroundSet()) {
/* 281 */       props.setBackground(getBackground());
/*     */     }
/* 283 */     if (isForegroundSet()) {
/* 284 */       props.setForeground(getForeground());
/*     */     }
/* 286 */     if (isFontSet()) {
/* 287 */       props.setFont(getFont());
/*     */     }
/* 289 */     if (isHorizontalAlignmentSet()) {
/* 290 */       props.setHorizontalAlignment(getHorizontalAlignment());
/*     */     }
/* 292 */     if (isHorizontalTextPositionSet()) {
/* 293 */       props.setHorizontalTextPosition(getHorizontalTextPosition());
/*     */     }
/* 295 */     if (isIconSet()) {
/* 296 */       props.setIcon(getIcon());
/*     */     }
/* 298 */     if (isIconTextGapSet()) {
/* 299 */       props.setIconTextGap(getIconTextGap());
/*     */     }
/* 301 */     if (isTextSet()) {
/* 302 */       props.setText(getText());
/*     */     }
/* 304 */     if (isVerticalAlignmentSet()) {
/* 305 */       props.setVerticalAlignment(getVerticalAlignment());
/*     */     }
/* 307 */     if (isVerticalTextPositionSet()) {
/* 308 */       props.setVerticalTextPosition(getVerticalTextPosition());
/*     */     }
/*     */   }
/*     */   
/*     */   public void applyPropertiesTo(TableCellRenderer renderer) {
/* 313 */     if ((renderer instanceof JLabel)) {
/* 314 */       applyPropertiesTo((JLabel)renderer);
/*     */     }
/* 316 */     else if ((renderer instanceof AbstractButton)) {
/* 317 */       applyPropertiesTo((AbstractButton)renderer);
/*     */     }
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\table\LabelProperties.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */