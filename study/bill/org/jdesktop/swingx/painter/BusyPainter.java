/*     */ package org.jdesktop.swingx.painter;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Graphics2D;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.Shape;
/*     */ import java.awt.geom.Ellipse2D.Float;
/*     */ import java.awt.geom.PathIterator;
/*     */ import java.awt.geom.Point2D.Float;
/*     */ import java.awt.geom.RoundRectangle2D.Float;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.NoSuchElementException;
/*     */ import org.jdesktop.swingx.JXBusyLabel.Direction;
/*     */ import org.jdesktop.swingx.color.ColorUtil;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class BusyPainter
/*     */   extends AbstractPainter<Object>
/*     */ {
/*  46 */   private int frame = -1;
/*     */   
/*  48 */   private int points = 8;
/*     */   
/*  50 */   private Color baseColor = new Color(200, 200, 200);
/*     */   
/*  52 */   private Color highlightColor = Color.BLACK;
/*     */   
/*  54 */   private int trailLength = 4;
/*     */   
/*     */   private Shape pointShape;
/*     */   
/*     */   private Shape trajectory;
/*     */   
/*  60 */   private JXBusyLabel.Direction direction = JXBusyLabel.Direction.RIGHT;
/*     */   
/*     */ 
/*     */   private boolean paintCentered;
/*     */   
/*     */ 
/*     */   public BusyPainter()
/*     */   {
/*  68 */     this(26);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public BusyPainter(int height)
/*     */   {
/*  76 */     this(getScaledDefaultPoint(height), getScaledDefaultTrajectory(height));
/*     */   }
/*     */   
/*     */   protected static Shape getScaledDefaultTrajectory(int height)
/*     */   {
/*  81 */     return new Ellipse2D.Float(height * 8 / 26 / 2, height * 8 / 26 / 2, height - height * 8 / 26, height - height * 8 / 26);
/*     */   }
/*     */   
/*     */   protected static Shape getScaledDefaultPoint(int height)
/*     */   {
/*  86 */     return new RoundRectangle2D.Float(0.0F, 0.0F, height * 8 / 26, 4.0F, 4.0F, 4.0F);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public BusyPainter(Shape point, Shape trajectory)
/*     */   {
/*  96 */     init(point, trajectory, Color.LIGHT_GRAY, Color.BLACK);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void init(Shape point, Shape trajectory, Color baseColor, Color highlightColor)
/*     */   {
/* 105 */     this.baseColor = baseColor;
/* 106 */     this.highlightColor = highlightColor;
/* 107 */     this.pointShape = point;
/* 108 */     this.trajectory = trajectory;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void doPaint(Graphics2D g, Object t, int width, int height)
/*     */   {
/* 116 */     Rectangle r = getTrajectory().getBounds();
/* 117 */     int tw = width - r.width - 2 * r.x;
/* 118 */     int th = height - r.height - 2 * r.y;
/* 119 */     if (isPaintCentered()) {
/* 120 */       g.translate(tw / 2, th / 2);
/*     */     }
/*     */     
/* 123 */     PathIterator pi = this.trajectory.getPathIterator(null);
/* 124 */     float[] coords = new float[6];
/* 125 */     Point2D.Float cp = new Point2D.Float();
/* 126 */     Point2D.Float sp = new Point2D.Float();
/*     */     
/* 128 */     float totalDist = 0.0F;
/* 129 */     List<float[]> segStack = new ArrayList();
/*     */     do {
/*     */       int ret;
/* 132 */       try { ret = pi.currentSegment(coords);
/*     */       }
/*     */       catch (NoSuchElementException e) {
/* 135 */         return;
/*     */       }
/* 137 */       if ((ret == 1) || ((ret == 4) && ((sp.x != cp.x) || (sp.y != cp.y))))
/*     */       {
/* 139 */         float c = calcLine(coords, cp);
/* 140 */         totalDist += c;
/*     */         
/* 142 */         segStack.add(new float[] { c, 0.0F, 0.0F, 0.0F, 0.0F, coords[0], coords[1], ret });
/* 143 */         cp.x = coords[0];
/* 144 */         cp.y = coords[1];
/*     */       }
/* 146 */       if (ret == 0) {
/* 147 */         sp.x = (cp.x = coords[0]);
/* 148 */         sp.y = (cp.y = coords[1]);
/*     */       }
/*     */       
/* 151 */       if (ret == 3) {
/* 152 */         float c = calcCube(coords, cp);
/* 153 */         totalDist += c;
/* 154 */         segStack.add(new float[] { c, coords[0], coords[1], coords[2], coords[3], coords[4], coords[5], ret });
/*     */         
/* 156 */         cp.x = coords[4];
/* 157 */         cp.y = coords[5];
/*     */       }
/* 159 */       if (ret == 2) {
/* 160 */         float c = calcLengthOfQuad(coords, cp);
/* 161 */         totalDist += c;
/* 162 */         segStack.add(new float[] { c, coords[0], coords[1], 0.0F, 0.0F, coords[2], coords[3], ret });
/*     */         
/* 164 */         cp.x = coords[2];
/* 165 */         cp.y = coords[3];
/*     */       }
/*     */       
/* 168 */       pi.next();
/* 169 */     } while (!pi.isDone());
/* 170 */     float nxtP = totalDist / getPoints();
/* 171 */     List<Point2D.Float> pList = new ArrayList();
/* 172 */     pList.add(new Point2D.Float(sp.x, sp.y));
/* 173 */     int sgIdx = 0;
/* 174 */     float[] sgmt = (float[])segStack.get(sgIdx);
/* 175 */     float len = sgmt[0];
/* 176 */     float travDist = nxtP;
/* 177 */     Point2D.Float center = new Point2D.Float(sp.x, sp.y);
/* 178 */     for (int i = 1; i < getPoints(); i++) {
/* 179 */       while (len < nxtP) {
/* 180 */         sgIdx++;
/*     */         
/* 182 */         sp.x = sgmt[5];
/* 183 */         sp.y = sgmt[6];
/* 184 */         sgmt = (float[])segStack.get(sgIdx);
/* 185 */         travDist = nxtP - len;
/* 186 */         len += sgmt[0];
/*     */       }
/* 188 */       len -= nxtP;
/* 189 */       Point2D.Float p = calcPoint(travDist, sp, sgmt, width, height);
/* 190 */       pList.add(p);
/* 191 */       center.x += p.x;
/* 192 */       center.y += p.y;
/* 193 */       travDist += nxtP;
/*     */     }
/*     */     
/* 196 */     center.x = (width / 2.0F);
/* 197 */     center.y = (height / 2.0F);
/*     */     
/*     */ 
/* 200 */     int i = 0;
/* 201 */     g.translate(center.x, center.y);
/* 202 */     for (Point2D.Float p : pList) {
/* 203 */       drawAt(g, i++, p, center);
/*     */     }
/* 205 */     g.translate(-center.x, -center.y);
/*     */     
/* 207 */     if (isPaintCentered()) {
/* 208 */       g.translate(-tw / 2, -th / 2);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isPaintCentered()
/*     */   {
/* 217 */     return this.paintCentered;
/*     */   }
/*     */   
/*     */   private void drawAt(Graphics2D g, int i, Point2D.Float p, Point2D.Float c) {
/* 221 */     g.setColor(calcFrameColor(i));
/* 222 */     paintRotatedCenteredShapeAtPoint(p, c, g);
/*     */   }
/*     */   
/*     */   private void paintRotatedCenteredShapeAtPoint(Point2D.Float p, Point2D.Float c, Graphics2D g) {
/* 226 */     Shape s = getPointShape();
/* 227 */     double hh = s.getBounds().getHeight() / 2.0D;
/* 228 */     double wh = s.getBounds().getWidth() / 2.0D;
/*     */     
/* 230 */     double a = c.y - p.y;
/* 231 */     double b = p.x - c.x;
/* 232 */     double sa = Math.signum(a);
/* 233 */     double sb = Math.signum(b);
/* 234 */     sa = sa == 0.0D ? 1.0D : sa;
/* 235 */     sb = sb == 0.0D ? 1.0D : sb;
/* 236 */     a = Math.abs(a);
/* 237 */     b = Math.abs(b);
/* 238 */     double t = Math.atan(a / b);
/* 239 */     t = sb > 0.0D ? t : sa > 0.0D ? -3.141592653589793D + t : sb > 0.0D ? -t : 3.141592653589793D - t;
/* 240 */     double x = Math.sqrt(a * a + b * b) - wh;
/* 241 */     double y = -hh;
/* 242 */     g.rotate(t);
/* 243 */     g.translate(x, y);
/* 244 */     g.fill(s);
/* 245 */     g.translate(-x, -y);
/* 246 */     g.rotate(-t);
/*     */   }
/*     */   
/*     */ 
/*     */   private Point2D.Float calcPoint(float dist2go, Point2D.Float startPoint, float[] sgmt, int w, int h)
/*     */   {
/* 252 */     Point2D.Float f = new Point2D.Float();
/* 253 */     if (sgmt[7] == 1.0F)
/*     */     {
/* 255 */       float a = sgmt[5] - startPoint.x;
/* 256 */       float b = sgmt[6] - startPoint.y;
/* 257 */       float pathLen = sgmt[0];
/* 258 */       startPoint.x += a * dist2go / pathLen;
/* 259 */       startPoint.y += b * dist2go / pathLen;
/* 260 */     } else if (sgmt[7] == 2.0F)
/*     */     {
/* 262 */       Point2D.Float ctrl = new Point2D.Float(sgmt[1] / w, sgmt[2] / h);
/* 263 */       Point2D.Float end = new Point2D.Float(sgmt[5] / w, sgmt[6] / h);
/* 264 */       Point2D.Float start = new Point2D.Float(startPoint.x / w, startPoint.y / h);
/*     */       
/*     */ 
/* 267 */       f = getXY(dist2go / sgmt[0], start, ctrl, end);
/* 268 */       f.x *= w;
/* 269 */       f.y *= h;
/*     */     }
/* 271 */     else if (sgmt[7] == 3.0F)
/*     */     {
/* 273 */       float x = Math.abs(startPoint.x - sgmt[5]);
/* 274 */       float y = Math.abs(startPoint.y - sgmt[6]);
/*     */       
/*     */ 
/* 277 */       float c1rx = Math.abs(startPoint.x - sgmt[1]) / x;
/* 278 */       float c1ry = Math.abs(startPoint.y - sgmt[2]) / y;
/* 279 */       float c2rx = Math.abs(startPoint.x - sgmt[3]) / x;
/* 280 */       float c2ry = Math.abs(startPoint.y - sgmt[4]) / y;
/* 281 */       f = getXY(dist2go / sgmt[0], c1rx, c1ry, c2rx, c2ry);
/*     */       
/* 283 */       float a = startPoint.x - sgmt[5];
/* 284 */       float b = startPoint.y - sgmt[6];
/*     */       
/* 286 */       startPoint.x -= f.x * a;
/* 287 */       startPoint.y -= f.y * b;
/*     */     }
/* 289 */     return f;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private float calcLine(float[] coords, Point2D.Float cp)
/*     */   {
/* 300 */     float a = cp.x - coords[0];
/* 301 */     float b = cp.y - coords[1];
/* 302 */     float c = (float)Math.sqrt(a * a + b * b);
/* 303 */     return c;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private float calcCube(float[] coords, Point2D.Float cp)
/*     */   {
/* 313 */     float x = Math.abs(cp.x - coords[4]);
/* 314 */     float y = Math.abs(cp.y - coords[5]);
/*     */     
/*     */ 
/* 317 */     float c1rx = Math.abs(cp.x - coords[0]) / x;
/* 318 */     float c1ry = Math.abs(cp.y - coords[1]) / y;
/* 319 */     float c2rx = Math.abs(cp.x - coords[2]) / x;
/* 320 */     float c2ry = Math.abs(cp.y - coords[3]) / y;
/* 321 */     float prevLength = 0.0F;float prevX = 0.0F;float prevY = 0.0F;
/* 322 */     for (float t = 0.01F; t <= 1.0F; t += 0.01F) {
/* 323 */       Point2D.Float xy = getXY(t, c1rx, c1ry, c2rx, c2ry);
/* 324 */       prevLength += (float)Math.sqrt((xy.x - prevX) * (xy.x - prevX) + (xy.y - prevY) * (xy.y - prevY));
/*     */       
/* 326 */       prevX = xy.x;
/* 327 */       prevY = xy.y;
/*     */     }
/*     */     
/* 330 */     float z = (Math.abs(x) + Math.abs(y)) / 2.0F * prevLength;
/* 331 */     return z;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private float calcLengthOfQuad(float[] coords, Point2D.Float cp)
/*     */   {
/* 341 */     Point2D.Float ctrl = new Point2D.Float(coords[0], coords[1]);
/* 342 */     Point2D.Float end = new Point2D.Float(coords[2], coords[3]);
/*     */     
/*     */ 
/* 345 */     float c1ax = Math.abs(cp.x - ctrl.x);
/* 346 */     float c1ay = Math.abs(cp.y - ctrl.y);
/*     */     
/* 348 */     float e1ax = Math.abs(cp.x - end.x);
/* 349 */     float e1ay = Math.abs(cp.y - end.y);
/*     */     
/* 351 */     float maxX = Math.max(c1ax, e1ax);
/* 352 */     float maxY = Math.max(c1ay, e1ay);
/*     */     
/*     */ 
/*     */ 
/* 356 */     ctrl.x = (c1ax / maxX);
/* 357 */     ctrl.y = (c1ay / maxY);
/*     */     
/* 359 */     end.x = (e1ax / maxX);
/* 360 */     end.y = (e1ay / maxY);
/*     */     
/*     */ 
/* 363 */     float prevLength = 0.0F;float prevX = 0.0F;float prevY = 0.0F;
/* 364 */     for (float t = 0.01F; t <= 1.0F; t += 0.01F) {
/* 365 */       Point2D.Float xy = getXY(t, new Point2D.Float(0.0F, 0.0F), ctrl, end);
/* 366 */       prevLength += (float)Math.sqrt((xy.x - prevX) * (xy.x - prevX) + (xy.y - prevY) * (xy.y - prevY));
/*     */       
/* 368 */       prevX = xy.x;
/* 369 */       prevY = xy.y;
/*     */     }
/*     */     
/* 372 */     float a = Math.abs(coords[2] - cp.x);
/* 373 */     float b = Math.abs(coords[3] - cp.y);
/* 374 */     float dist = (float)Math.sqrt(a * a + b * b);
/* 375 */     return prevLength * dist;
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
/*     */   private Point2D.Float getXY(float t, float x1, float y1, float x2, float y2)
/*     */   {
/* 393 */     float invT = 1.0F - t;
/* 394 */     float b1 = 3.0F * t * (invT * invT);
/* 395 */     float b2 = 3.0F * (t * t) * invT;
/* 396 */     float b3 = t * t * t;
/* 397 */     Point2D.Float xy = new Point2D.Float(b1 * x1 + b2 * x2 + b3, b1 * y1 + b2 * y2 + b3);
/*     */     
/* 399 */     return xy;
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
/*     */   public static Point2D.Float getXY(float t, Point2D.Float begin, Point2D.Float ctrl, Point2D.Float end)
/*     */   {
/* 423 */     float invT = 1.0F - t;
/* 424 */     float b0 = invT * invT;
/* 425 */     float b1 = 2.0F * t * invT;
/* 426 */     float b2 = t * t;
/* 427 */     Point2D.Float xy = new Point2D.Float(b0 * begin.x + b1 * ctrl.x + b2 * end.x, b0 * begin.y + b1 * ctrl.y + b2 * end.y);
/*     */     
/* 429 */     return xy;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private Color calcFrameColor(int i)
/*     */   {
/* 438 */     if (this.frame == -1) {
/* 439 */       return getBaseColor();
/*     */     }
/*     */     
/* 442 */     for (int t = 0; t < getTrailLength(); t++) {
/* 443 */       if ((this.direction == JXBusyLabel.Direction.RIGHT) && (i == (this.frame - t + getPoints()) % getPoints()))
/*     */       {
/* 445 */         float terp = 1.0F - (getTrailLength() - t) / getTrailLength();
/*     */         
/* 447 */         return ColorUtil.interpolate(getBaseColor(), getHighlightColor(), terp);
/*     */       }
/* 449 */       if ((this.direction == JXBusyLabel.Direction.LEFT) && (i == (this.frame + t) % getPoints()))
/*     */       {
/* 451 */         float terp = t / getTrailLength();
/* 452 */         return ColorUtil.interpolate(getBaseColor(), getHighlightColor(), terp);
/*     */       }
/*     */     }
/*     */     
/* 456 */     return getBaseColor();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getFrame()
/*     */   {
/* 464 */     return this.frame;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void setFrame(int frame)
/*     */   {
/* 471 */     int old = getFrame();
/* 472 */     this.frame = frame;
/* 473 */     firePropertyChange("frame", Integer.valueOf(old), Integer.valueOf(getFrame()));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Color getBaseColor()
/*     */   {
/* 481 */     return this.baseColor;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setBaseColor(Color baseColor)
/*     */   {
/* 489 */     Color old = getBaseColor();
/* 490 */     this.baseColor = baseColor;
/* 491 */     firePropertyChange("baseColor", old, getBaseColor());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Color getHighlightColor()
/*     */   {
/* 499 */     return this.highlightColor;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setHighlightColor(Color highlightColor)
/*     */   {
/* 507 */     Color old = getHighlightColor();
/* 508 */     this.highlightColor = highlightColor;
/* 509 */     firePropertyChange("highlightColor", old, getHighlightColor());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getPoints()
/*     */   {
/* 517 */     return this.points;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setPoints(int points)
/*     */   {
/* 525 */     int old = getPoints();
/* 526 */     this.points = points;
/* 527 */     firePropertyChange("points", Integer.valueOf(old), Integer.valueOf(getPoints()));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getTrailLength()
/*     */   {
/* 535 */     return this.trailLength;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setTrailLength(int trailLength)
/*     */   {
/* 543 */     int old = getTrailLength();
/* 544 */     this.trailLength = trailLength;
/* 545 */     firePropertyChange("trailLength", Integer.valueOf(old), Integer.valueOf(getTrailLength()));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public final Shape getPointShape()
/*     */   {
/* 553 */     return this.pointShape;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public final void setPointShape(Shape pointShape)
/*     */   {
/* 561 */     Shape old = getPointShape();
/* 562 */     this.pointShape = pointShape;
/* 563 */     if ((getPointShape() != old) && (getPointShape() != null) && (!getPointShape().equals(old)))
/*     */     {
/* 565 */       firePropertyChange("pointShape", old, getPointShape());
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public final Shape getTrajectory()
/*     */   {
/* 574 */     return this.trajectory;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public final void setTrajectory(Shape trajectory)
/*     */   {
/* 582 */     Shape old = getTrajectory();
/* 583 */     this.trajectory = trajectory;
/* 584 */     if ((getTrajectory() != old) && (getTrajectory() != null) && (!getTrajectory().equals(old)))
/*     */     {
/* 586 */       firePropertyChange("trajectory", old, getTrajectory());
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setDirection(JXBusyLabel.Direction dir)
/*     */   {
/* 595 */     JXBusyLabel.Direction old = getDirection();
/* 596 */     this.direction = dir;
/* 597 */     if ((getDirection() != old) && (getDirection() != null) && (!getDirection().equals(old)))
/*     */     {
/* 599 */       firePropertyChange("direction", old, getDirection());
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public JXBusyLabel.Direction getDirection()
/*     */   {
/* 608 */     return this.direction;
/*     */   }
/*     */   
/*     */   protected Shape provideShape(Graphics2D g, Object comp, int width, int height) {
/* 612 */     return new Rectangle(0, 0, width, height);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setPaintCentered(boolean paintCentered)
/*     */   {
/* 620 */     boolean old = isPaintCentered();
/* 621 */     this.paintCentered = paintCentered;
/* 622 */     firePropertyChange("paintCentered", Boolean.valueOf(old), Boolean.valueOf(isPaintCentered()));
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\painter\BusyPainter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */