/*     */ package org.jdesktop.swingx.prompt;
/*     */ 
/*     */ import java.awt.Component;
/*     */ import java.awt.Insets;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JTextField;
/*     */ import org.jdesktop.swingx.plaf.TextUIWrapper;
/*     */ 
/*     */ public class BuddySupport
/*     */ {
/*     */   public static final String OUTER_MARGIN = "outerMargin";
/*     */   
/*     */   public static enum Position
/*     */   {
/*  18 */     LEFT,  RIGHT;
/*     */     
/*     */     private Position() {}
/*     */   }
/*     */   
/*     */   public static void addLeft(Component c, JTextField textField) {
/*  24 */     add(c, Position.LEFT, textField);
/*     */   }
/*     */   
/*     */   public static void addRight(Component c, JTextField textField) {
/*  28 */     add(c, Position.RIGHT, textField);
/*     */   }
/*     */   
/*     */   public static void add(Component c, Position pos, JTextField textField) {
/*  32 */     TextUIWrapper.getDefaultWrapper().install(textField, true);
/*     */     
/*  34 */     List<Component> leftBuddies = buddies(Position.LEFT, textField);
/*  35 */     List<Component> rightBuddies = buddies(Position.RIGHT, textField);
/*     */     
/*     */ 
/*  38 */     setLeft(textField, leftBuddies);
/*  39 */     setRight(textField, rightBuddies);
/*     */     
/*     */ 
/*  42 */     if (isBuddy(c, textField)) {
/*  43 */       throw new IllegalStateException("Component already added.");
/*     */     }
/*     */     
/*  46 */     if (Position.LEFT == pos) {
/*  47 */       leftBuddies.add(c);
/*     */     } else {
/*  49 */       rightBuddies.add(0, c);
/*     */     }
/*     */     
/*  52 */     addToComponentHierarchy(c, pos, textField);
/*     */   }
/*     */   
/*     */   public static void addGap(int width, Position pos, JTextField textField) {
/*  56 */     add(createGap(width), pos, textField);
/*     */   }
/*     */   
/*     */   public static void setRight(JTextField textField, List<Component> rightBuddies) {
/*  60 */     set(rightBuddies, Position.RIGHT, textField);
/*     */   }
/*     */   
/*     */   public static void setLeft(JTextField textField, List<Component> leftBuddies) {
/*  64 */     set(leftBuddies, Position.LEFT, textField);
/*     */   }
/*     */   
/*     */   public static void set(List<Component> buddies, Position pos, JTextField textField) {
/*  68 */     textField.putClientProperty(pos, buddies);
/*     */   }
/*     */   
/*     */   private static void addToComponentHierarchy(Component c, Position pos, JTextField textField) {
/*  72 */     textField.add(c, pos.toString());
/*     */   }
/*     */   
/*     */   public static List<Component> getLeft(JTextField textField) {
/*  76 */     return getBuddies(Position.LEFT, textField);
/*     */   }
/*     */   
/*     */   public static List<Component> getRight(JTextField textField) {
/*  80 */     return getBuddies(Position.RIGHT, textField);
/*     */   }
/*     */   
/*     */   public static List<Component> getBuddies(Position pos, JTextField textField) {
/*  84 */     return Collections.unmodifiableList(buddies(pos, textField));
/*     */   }
/*     */   
/*     */   private static List<Component> buddies(Position pos, JTextField textField)
/*     */   {
/*  89 */     List<Component> buddies = (List)textField.getClientProperty(pos);
/*     */     
/*  91 */     if (buddies != null) {
/*  92 */       return buddies;
/*     */     }
/*  94 */     return new ArrayList();
/*     */   }
/*     */   
/*     */   public static boolean isBuddy(Component c, JTextField textField) {
/*  98 */     return (buddies(Position.LEFT, textField).contains(c)) || (buddies(Position.RIGHT, textField).contains(c));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void remove(JComponent c, JTextField textField)
/*     */   {
/* 109 */     buddies(Position.LEFT, textField).remove(c);
/* 110 */     buddies(Position.RIGHT, textField).remove(c);
/*     */     
/* 112 */     textField.remove(c);
/*     */   }
/*     */   
/*     */   public static void removeAll(JTextField textField) {
/* 116 */     List<Component> left = buddies(Position.LEFT, textField);
/* 117 */     for (Component c : left) {
/* 118 */       textField.remove(c);
/*     */     }
/* 120 */     left.clear();
/* 121 */     List<Component> right = buddies(Position.RIGHT, textField);
/* 122 */     for (Component c : right) {
/* 123 */       textField.remove(c);
/*     */     }
/* 125 */     right.clear();
/*     */   }
/*     */   
/*     */   public static void setOuterMargin(JTextField buddyField, Insets margin)
/*     */   {
/* 130 */     buddyField.putClientProperty("outerMargin", margin);
/*     */   }
/*     */   
/*     */   public static Insets getOuterMargin(JTextField buddyField) {
/* 134 */     return (Insets)buddyField.getClientProperty("outerMargin");
/*     */   }
/*     */   
/*     */   public static void ensureBuddiesAreInComponentHierarchy(JTextField textField) {
/* 138 */     for (Component c : getLeft(textField)) {
/* 139 */       addToComponentHierarchy(c, Position.LEFT, textField);
/*     */     }
/* 141 */     for (Component c : getRight(textField)) {
/* 142 */       addToComponentHierarchy(c, Position.RIGHT, textField);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Component createGap(int width)
/*     */   {
/* 153 */     return javax.swing.Box.createHorizontalStrut(width);
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\prompt\BuddySupport.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */