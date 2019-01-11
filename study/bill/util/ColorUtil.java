/*    */ package util;
/*    */ 
/*    */ import java.awt.Color;
/*    */ 
/*    */ public class ColorUtil {
/*  6 */   public static Color blueColor = Color.decode("#3399FF");
/*  7 */   public static Color grayColor = Color.decode("#999999");
/*  8 */   public static Color backgroundColor = Color.decode("#eeeeee");
/*  9 */   public static Color warningColor = Color.decode("#FF3333");
/*    */   
/*    */   public static Color getByPercentage(int per) {
/* 12 */     if (per > 100)
/* 13 */       per = 100;
/* 14 */     int r = 51;
/* 15 */     int g = 255;
/* 16 */     int b = 51;
/* 17 */     float rate = per / 100.0F;
/* 18 */     r = (int)(204.0F * rate + 51.0F);
/* 19 */     g = 255 - r + 51;
/* 20 */     Color color = new Color(r, g, b);
/* 21 */     return color;
/*    */   }
/*    */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\util\ColorUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */