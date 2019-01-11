/*     */ package org.jdesktop.swingx.util;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.FontMetrics;
/*     */ import java.awt.GradientPaint;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Graphics2D;
/*     */ import java.awt.Paint;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.geom.Point2D;
/*     */ import java.awt.geom.Point2D.Double;
/*     */ import java.awt.geom.Point2D.Float;
/*     */ import java.awt.geom.Rectangle2D;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JLabel;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PaintUtils
/*     */ {
/*  47 */   public static final GradientPaint BLUE_EXPERIENCE = new GradientPaint(new Point2D.Double(0.0D, 0.0D), new Color(168, 204, 241), new Point2D.Double(0.0D, 1.0D), new Color(44, 61, 146));
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  52 */   public static final GradientPaint MAC_OSX_SELECTED = new GradientPaint(new Point2D.Double(0.0D, 0.0D), new Color(81, 141, 236), new Point2D.Double(0.0D, 1.0D), new Color(36, 96, 192));
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  57 */   public static final GradientPaint MAC_OSX = new GradientPaint(new Point2D.Double(0.0D, 0.0D), new Color(167, 210, 250), new Point2D.Double(0.0D, 1.0D), new Color(99, 147, 206));
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  62 */   public static final GradientPaint AERITH = new GradientPaint(new Point2D.Double(0.0D, 0.0D), Color.WHITE, new Point2D.Double(0.0D, 1.0D), new Color(64, 110, 161));
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  67 */   public static final GradientPaint GRAY = new GradientPaint(new Point2D.Double(0.0D, 0.0D), new Color(226, 226, 226), new Point2D.Double(0.0D, 1.0D), new Color(250, 248, 248));
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  72 */   public static final GradientPaint RED_XP = new GradientPaint(new Point2D.Double(0.0D, 0.0D), new Color(236, 81, 81), new Point2D.Double(0.0D, 1.0D), new Color(192, 36, 36));
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  77 */   public static final GradientPaint NIGHT_GRAY = new GradientPaint(new Point2D.Double(0.0D, 0.0D), new Color(102, 111, 127), new Point2D.Double(0.0D, 1.0D), new Color(38, 45, 61));
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  82 */   public static final GradientPaint NIGHT_GRAY_LIGHT = new GradientPaint(new Point2D.Double(0.0D, 0.0D), new Color(129, 138, 155), new Point2D.Double(0.0D, 1.0D), new Color(58, 66, 82));
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Rectangle getTextBounds(Graphics g, JLabel label)
/*     */   {
/*  97 */     FontMetrics fm = g.getFontMetrics();
/*  98 */     Rectangle2D r2d = fm.getStringBounds(label.getText(), g);
/*  99 */     Rectangle rect = r2d.getBounds();
/* 100 */     int xOffset = 0;
/* 101 */     switch (label.getHorizontalAlignment()) {
/*     */     case 4: 
/*     */     case 11: 
/* 104 */       xOffset = label.getBounds().width - rect.width;
/* 105 */       break;
/*     */     case 0: 
/* 107 */       xOffset = (label.getBounds().width - rect.width) / 2;
/* 108 */       break;
/*     */     case 1: case 2: case 3: case 5: 
/*     */     case 6: case 7: case 8: 
/*     */     case 9: case 10: default: 
/* 112 */       xOffset = 0;
/*     */     }
/*     */     
/* 115 */     int yOffset = 0;
/* 116 */     switch (label.getVerticalAlignment()) {
/*     */     case 1: 
/* 118 */       yOffset = 0;
/* 119 */       break;
/*     */     case 0: 
/* 121 */       yOffset = (label.getBounds().height - rect.height) / 2;
/* 122 */       break;
/*     */     case 3: 
/* 124 */       yOffset = label.getBounds().height - rect.height;
/*     */     }
/*     */     
/* 127 */     return new Rectangle(xOffset, yOffset, rect.width, rect.height);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void paintGradient(Graphics g, JComponent comp, Color color1, Color color2)
/*     */   {
/* 136 */     GradientPaint paint = new GradientPaint(0.0F, 0.0F, color1, 0.0F, comp.getHeight(), color2, true);
/*     */     
/*     */ 
/* 139 */     Graphics2D g2 = (Graphics2D)g;
/* 140 */     Paint oldPaint = g2.getPaint();
/* 141 */     g2.setPaint(paint);
/* 142 */     g2.fillRect(0, 0, comp.getWidth(), comp.getHeight());
/* 143 */     g2.setPaint(oldPaint);
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
/*     */   public static Paint resizeGradient(Paint p, int width, int height)
/*     */   {
/* 166 */     if (p == null) { return p;
/*     */     }
/* 168 */     if ((p instanceof GradientPaint)) {
/* 169 */       GradientPaint gp = (GradientPaint)p;
/* 170 */       Point2D[] pts = new Point2D[2];
/* 171 */       pts[0] = gp.getPoint1();
/* 172 */       pts[1] = gp.getPoint2();
/* 173 */       pts = adjustPoints(pts, width, height);
/* 174 */       return new GradientPaint(pts[0], gp.getColor1(), pts[1], gp.getColor2(), gp.isCyclic());
/*     */     }
/*     */     
/* 177 */     if (("java.awt.LinearGradientPaint".equals(p.getClass().getName())) || ("org.apache.batik.ext.awt.LinearGradientPaint".equals(p.getClass().getName())))
/*     */     {
/* 179 */       return resizeLinearGradient(p, width, height);
/*     */     }
/* 181 */     return p;
/*     */   }
/*     */   
/*     */   private static Paint resizeLinearGradient(Paint p, int width, int height)
/*     */   {
/*     */     try {
/* 187 */       Point2D[] pts = new Point2D[2];
/* 188 */       pts[0] = ((Point2D)invokeMethod(p, "getStartPoint"));
/* 189 */       pts[1] = ((Point2D)invokeMethod(p, "getEndPoint"));
/* 190 */       pts = adjustPoints(pts, width, height);
/* 191 */       float[] fractions = (float[])invokeMethod(p, "getFractions");
/* 192 */       Color[] colors = (Color[])invokeMethod(p, "getColors");
/*     */       
/* 194 */       Constructor<?> con = p.getClass().getDeclaredConstructor(new Class[] { Point2D.class, Point2D.class, new float[0].getClass(), new Color[0].getClass() });
/*     */       
/*     */ 
/*     */ 
/* 198 */       return (Paint)con.newInstance(new Object[] { pts[0], pts[1], fractions, colors });
/*     */     } catch (Exception ex) {
/* 200 */       ex.printStackTrace();
/*     */     }
/* 202 */     return p;
/*     */   }
/*     */   
/*     */   private static Object invokeMethod(Object p, String methodName) throws NoSuchMethodException, InvocationTargetException, IllegalArgumentException, SecurityException, IllegalAccessException
/*     */   {
/* 207 */     Method meth = p.getClass().getMethod(methodName, new Class[0]);
/* 208 */     return meth.invoke(p, new Object[0]);
/*     */   }
/*     */   
/*     */   private static Point2D[] adjustPoints(Point2D[] pts, int width, int height)
/*     */   {
/* 213 */     Point2D start = pts[0];
/* 214 */     Point2D end = pts[1];
/*     */     
/* 216 */     double angle = calcAngle(start, end);
/* 217 */     double a2 = Math.toDegrees(angle);
/* 218 */     double e = 1.0D;
/*     */     
/*     */ 
/* 221 */     if ((Math.abs(angle) < Math.toRadians(e)) || (Math.abs(angle) > Math.toRadians(360.0D - e)))
/*     */     {
/* 223 */       start = new Point2D.Float(0.0F, 0.0F);
/* 224 */       end = new Point2D.Float(normalize(end.getX(), width), 0.0F);
/*     */     }
/*     */     
/*     */ 
/* 228 */     if (isNear(a2, 45.0D, e)) {
/* 229 */       start = new Point2D.Float(0.0F, 0.0F);
/* 230 */       end = new Point2D.Float(normalize(end.getX(), width), normalize(end.getY(), height));
/*     */     }
/*     */     
/*     */ 
/* 234 */     if (isNear(a2, 90.0D, e)) {
/* 235 */       start = new Point2D.Float(0.0F, 0.0F);
/* 236 */       end = new Point2D.Float(0.0F, normalize(end.getY(), height));
/*     */     }
/*     */     
/*     */ 
/* 240 */     if (isNear(a2, 135.0D, e)) {
/* 241 */       start = new Point2D.Float(normalize(start.getX(), width), 0.0F);
/* 242 */       end = new Point2D.Float(0.0F, normalize(end.getY(), height));
/*     */     }
/*     */     
/*     */ 
/* 246 */     if (isNear(a2, 180.0D, e)) {
/* 247 */       start = new Point2D.Float(normalize(start.getX(), width), 0.0F);
/* 248 */       end = new Point2D.Float(0.0F, 0.0F);
/*     */     }
/*     */     
/*     */ 
/* 252 */     if (isNear(a2, 225.0D, e)) {
/* 253 */       start = new Point2D.Float(normalize(start.getX(), width), normalize(start.getY(), height));
/* 254 */       end = new Point2D.Float(0.0F, 0.0F);
/*     */     }
/*     */     
/*     */ 
/* 258 */     if (isNear(a2, 270.0D, e)) {
/* 259 */       start = new Point2D.Float(0.0F, normalize(start.getY(), height));
/* 260 */       end = new Point2D.Float(0.0F, 0.0F);
/*     */     }
/*     */     
/*     */ 
/* 264 */     if (isNear(a2, 315.0D, e)) {
/* 265 */       start = new Point2D.Float(0.0F, normalize(start.getY(), height));
/* 266 */       end = new Point2D.Float(normalize(end.getX(), width), 0.0F);
/*     */     }
/*     */     
/* 269 */     return new Point2D[] { start, end };
/*     */   }
/*     */   
/*     */   private static boolean isNear(double angle, double target, double error) {
/* 273 */     return Math.abs(target - Math.abs(angle)) < error;
/*     */   }
/*     */   
/*     */   private static float normalize(double original, float target) {
/* 277 */     if (original < 1.0D) {
/* 278 */       return target * (float)original;
/*     */     }
/*     */     
/* 281 */     return target;
/*     */   }
/*     */   
/*     */   private static double calcAngle(Point2D p1, Point2D p2) {
/* 285 */     double x_off = p2.getX() - p1.getX();
/* 286 */     double y_off = p2.getY() - p1.getY();
/* 287 */     double angle = Math.atan(y_off / x_off);
/* 288 */     if (x_off < 0.0D) {
/* 289 */       angle += 3.141592653589793D;
/*     */     }
/*     */     
/* 292 */     if (angle < 0.0D) angle += 6.283185307179586D;
/* 293 */     if (angle > 6.283185307179586D) angle -= 6.283185307179586D;
/* 294 */     return angle;
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\util\PaintUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */