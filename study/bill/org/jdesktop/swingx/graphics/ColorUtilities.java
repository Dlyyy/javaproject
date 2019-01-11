/*     */ package org.jdesktop.swingx.graphics;
/*     */ 
/*     */ import java.awt.Color;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ColorUtilities
/*     */ {
/*     */   public static float[] RGBtoHSL(Color color)
/*     */   {
/*  57 */     return RGBtoHSL(color.getRed(), color.getGreen(), color.getBlue(), null);
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
/*     */   public static float[] RGBtoHSL(Color color, float[] hsl)
/*     */   {
/*  71 */     return RGBtoHSL(color.getRed(), color.getGreen(), color.getBlue(), hsl);
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
/*     */   public static float[] RGBtoHSL(int r, int g, int b)
/*     */   {
/*  84 */     return RGBtoHSL(r, g, b, null);
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
/*     */   public static float[] RGBtoHSL(int r, int g, int b, float[] hsl)
/*     */   {
/* 100 */     if (hsl == null) {
/* 101 */       hsl = new float[3];
/* 102 */     } else if (hsl.length < 3) {
/* 103 */       throw new IllegalArgumentException("hsl array must have a length of at least 3");
/*     */     }
/*     */     
/*     */ 
/* 107 */     if (r < 0) { r = 0;
/* 108 */     } else if (r > 255) r = 255;
/* 109 */     if (g < 0) { g = 0;
/* 110 */     } else if (g > 255) g = 255;
/* 111 */     if (b < 0) { b = 0;
/* 112 */     } else if (b > 255) { b = 255;
/*     */     }
/* 114 */     float var_R = r / 255.0F;
/* 115 */     float var_G = g / 255.0F;
/* 116 */     float var_B = b / 255.0F;
/*     */     
/*     */     float var_Max;
/*     */     
/*     */     float var_Min;
/*     */     float var_Max;
/* 122 */     if (var_R > var_G) {
/* 123 */       float var_Min = var_G;
/* 124 */       var_Max = var_R;
/*     */     } else {
/* 126 */       var_Min = var_R;
/* 127 */       var_Max = var_G;
/*     */     }
/* 129 */     if (var_B > var_Max) {
/* 130 */       var_Max = var_B;
/*     */     }
/* 132 */     if (var_B < var_Min) {
/* 133 */       var_Min = var_B;
/*     */     }
/*     */     
/* 136 */     float del_Max = var_Max - var_Min;
/*     */     
/*     */ 
/* 139 */     float L = (var_Max + var_Min) / 2.0F;
/*     */     float S;
/* 141 */     float S; float H; if (del_Max - 0.01F <= 0.0F) {
/* 142 */       float H = 0.0F;
/* 143 */       S = 0.0F;
/*     */     } else { float S;
/* 145 */       if (L < 0.5F) {
/* 146 */         S = del_Max / (var_Max + var_Min);
/*     */       } else {
/* 148 */         S = del_Max / (2.0F - var_Max - var_Min);
/*     */       }
/*     */       
/* 151 */       float del_R = ((var_Max - var_R) / 6.0F + del_Max / 2.0F) / del_Max;
/* 152 */       float del_G = ((var_Max - var_G) / 6.0F + del_Max / 2.0F) / del_Max;
/* 153 */       float del_B = ((var_Max - var_B) / 6.0F + del_Max / 2.0F) / del_Max;
/*     */       float H;
/* 155 */       if (var_R == var_Max) {
/* 156 */         H = del_B - del_G; } else { float H;
/* 157 */         if (var_G == var_Max) {
/* 158 */           H = 0.33333334F + del_R - del_B;
/*     */         } else
/* 160 */           H = 0.6666667F + del_G - del_R;
/*     */       }
/* 162 */       if (H < 0.0F) {
/* 163 */         H += 1.0F;
/*     */       }
/* 165 */       if (H > 1.0F) {
/* 166 */         H -= 1.0F;
/*     */       }
/*     */     }
/*     */     
/* 170 */     hsl[0] = H;
/* 171 */     hsl[1] = S;
/* 172 */     hsl[2] = L;
/*     */     
/* 174 */     return hsl;
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
/*     */   public static Color HSLtoRGB(float h, float s, float l)
/*     */   {
/* 187 */     int[] rgb = HSLtoRGB(h, s, l, null);
/* 188 */     return new Color(rgb[0], rgb[1], rgb[2]);
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
/*     */   public static int[] HSLtoRGB(float h, float s, float l, int[] rgb)
/*     */   {
/* 204 */     if (rgb == null) {
/* 205 */       rgb = new int[3];
/* 206 */     } else if (rgb.length < 3) {
/* 207 */       throw new IllegalArgumentException("rgb array must have a length of at least 3");
/*     */     }
/*     */     
/*     */ 
/* 211 */     if (h < 0.0F) { h = 0.0F;
/* 212 */     } else if (h > 1.0F) h = 1.0F;
/* 213 */     if (s < 0.0F) { s = 0.0F;
/* 214 */     } else if (s > 1.0F) s = 1.0F;
/* 215 */     if (l < 0.0F) { l = 0.0F;
/* 216 */     } else if (l > 1.0F) l = 1.0F;
/*     */     int B;
/*     */     int R;
/*     */     int G;
/* 220 */     int B; if (s - 0.01F <= 0.0F) {
/* 221 */       int R = (int)(l * 255.0F);
/* 222 */       int G = (int)(l * 255.0F);
/* 223 */       B = (int)(l * 255.0F);
/*     */     } else { float var_2;
/*     */       float var_2;
/* 226 */       if (l < 0.5F) {
/* 227 */         var_2 = l * (1.0F + s);
/*     */       } else {
/* 229 */         var_2 = l + s - s * l;
/*     */       }
/* 231 */       float var_1 = 2.0F * l - var_2;
/*     */       
/* 233 */       R = (int)(255.0F * hue2RGB(var_1, var_2, h + 0.33333334F));
/* 234 */       G = (int)(255.0F * hue2RGB(var_1, var_2, h));
/* 235 */       B = (int)(255.0F * hue2RGB(var_1, var_2, h - 0.33333334F));
/*     */     }
/*     */     
/* 238 */     rgb[0] = R;
/* 239 */     rgb[1] = G;
/* 240 */     rgb[2] = B;
/*     */     
/* 242 */     return rgb;
/*     */   }
/*     */   
/*     */   private static float hue2RGB(float v1, float v2, float vH) {
/* 246 */     if (vH < 0.0F) {
/* 247 */       vH += 1.0F;
/*     */     }
/* 249 */     if (vH > 1.0F) {
/* 250 */       vH -= 1.0F;
/*     */     }
/* 252 */     if (6.0F * vH < 1.0F) {
/* 253 */       return v1 + (v2 - v1) * 6.0F * vH;
/*     */     }
/* 255 */     if (2.0F * vH < 1.0F) {
/* 256 */       return v2;
/*     */     }
/* 258 */     if (3.0F * vH < 2.0F) {
/* 259 */       return v1 + (v2 - v1) * (0.6666667F - vH) * 6.0F;
/*     */     }
/* 261 */     return v1;
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\graphics\ColorUtilities.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */