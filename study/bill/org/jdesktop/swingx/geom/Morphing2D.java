/*     */ package org.jdesktop.swingx.geom;
/*     */ 
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.Shape;
/*     */ import java.awt.geom.AffineTransform;
/*     */ import java.awt.geom.FlatteningPathIterator;
/*     */ import java.awt.geom.IllegalPathStateException;
/*     */ import java.awt.geom.PathIterator;
/*     */ import java.awt.geom.Point2D;
/*     */ import java.awt.geom.Rectangle2D;
/*     */ import java.awt.geom.Rectangle2D.Double;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Morphing2D
/*     */   implements Shape
/*     */ {
/*     */   private double morph;
/*     */   private Geometry startGeometry;
/*     */   private Geometry endGeometry;
/*     */   
/*     */   public Morphing2D(Shape startShape, Shape endShape)
/*     */   {
/*  61 */     this.startGeometry = new Geometry(startShape);
/*  62 */     this.endGeometry = new Geometry(endShape);
/*  63 */     if (this.startGeometry.getWindingRule() != this.endGeometry.getWindingRule()) {
/*  64 */       throw new IllegalPathStateException("shapes must use same winding rule");
/*     */     }
/*     */     
/*  67 */     double[] tvals0 = this.startGeometry.getTvals();
/*  68 */     double[] tvals1 = this.endGeometry.getTvals();
/*  69 */     double[] masterTvals = mergeTvals(tvals0, tvals1);
/*  70 */     this.startGeometry.setTvals(masterTvals);
/*  71 */     this.endGeometry.setTvals(masterTvals);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public double getMorphing()
/*     */   {
/*  82 */     return this.morph;
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
/*     */   public void setMorphing(double morph)
/*     */   {
/*  98 */     if (morph > 1.0D) {
/*  99 */       morph = 1.0D;
/* 100 */     } else if (morph < 0.0D)
/*     */     {
/*     */ 
/*     */ 
/* 104 */       morph = 0.0D;
/*     */     }
/* 106 */     this.morph = morph;
/*     */   }
/*     */   
/*     */   private static double interp(double v0, double v1, double t) {
/* 110 */     return v0 + (v1 - v0) * t;
/*     */   }
/*     */   
/*     */   private static double[] mergeTvals(double[] tvals0, double[] tvals1) {
/* 114 */     int i0 = 0;
/* 115 */     int i1 = 0;
/* 116 */     int numtvals = 0;
/* 117 */     while ((i0 < tvals0.length) && (i1 < tvals1.length)) {
/* 118 */       double t0 = tvals0[i0];
/* 119 */       double t1 = tvals1[i1];
/* 120 */       if (t0 <= t1) {
/* 121 */         i0++;
/*     */       }
/* 123 */       if (t1 <= t0) {
/* 124 */         i1++;
/*     */       }
/* 126 */       numtvals++;
/*     */     }
/* 128 */     double[] newtvals = new double[numtvals];
/* 129 */     i0 = 0;
/* 130 */     i1 = 0;
/* 131 */     numtvals = 0;
/* 132 */     while ((i0 < tvals0.length) && (i1 < tvals1.length)) {
/* 133 */       double t0 = tvals0[i0];
/* 134 */       double t1 = tvals1[i1];
/* 135 */       if (t0 <= t1) {
/* 136 */         newtvals[numtvals] = t0;
/* 137 */         i0++;
/*     */       }
/* 139 */       if (t1 <= t0) {
/* 140 */         newtvals[numtvals] = t1;
/* 141 */         i1++;
/*     */       }
/* 143 */       numtvals++;
/*     */     }
/* 145 */     return newtvals;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Rectangle getBounds()
/*     */   {
/* 152 */     return getBounds2D().getBounds();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Rectangle2D getBounds2D()
/*     */   {
/* 159 */     int n = this.startGeometry.getNumCoords();
/*     */     double xmax;
/* 161 */     double xmin = xmax = interp(this.startGeometry.getCoord(0), this.endGeometry.getCoord(0), this.morph);
/*     */     double ymax;
/* 163 */     double ymin = ymax = interp(this.startGeometry.getCoord(1), this.endGeometry.getCoord(1), this.morph);
/*     */     
/* 165 */     for (int i = 2; i < n; i += 2) {
/* 166 */       double x = interp(this.startGeometry.getCoord(i), this.endGeometry.getCoord(i), this.morph);
/*     */       
/* 168 */       double y = interp(this.startGeometry.getCoord(i + 1), this.endGeometry.getCoord(i + 1), this.morph);
/*     */       
/* 170 */       if (xmin > x) {
/* 171 */         xmin = x;
/*     */       }
/* 173 */       if (ymin > y) {
/* 174 */         ymin = y;
/*     */       }
/* 176 */       if (xmax < x) {
/* 177 */         xmax = x;
/*     */       }
/* 179 */       if (ymax < y) {
/* 180 */         ymax = y;
/*     */       }
/*     */     }
/* 183 */     return new Rectangle2D.Double(xmin, ymin, xmax - xmin, ymax - ymin);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean contains(double x, double y)
/*     */   {
/* 190 */     throw new InternalError("unimplemented");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean contains(Point2D p)
/*     */   {
/* 197 */     return contains(p.getX(), p.getY());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean intersects(double x, double y, double w, double h)
/*     */   {
/* 204 */     throw new InternalError("unimplemented");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean intersects(Rectangle2D r)
/*     */   {
/* 211 */     return intersects(r.getX(), r.getY(), r.getWidth(), r.getHeight());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean contains(double x, double y, double w, double h)
/*     */   {
/* 218 */     throw new InternalError("unimplemented");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean contains(Rectangle2D r)
/*     */   {
/* 225 */     return contains(r.getX(), r.getY(), r.getWidth(), r.getHeight());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public PathIterator getPathIterator(AffineTransform at)
/*     */   {
/* 232 */     return new Iterator(at, this.startGeometry, this.endGeometry, this.morph);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public PathIterator getPathIterator(AffineTransform at, double flatness)
/*     */   {
/* 239 */     return new FlatteningPathIterator(getPathIterator(at), flatness);
/*     */   }
/*     */   
/*     */   private static class Geometry
/*     */   {
/*     */     static final double THIRD = 0.3333333333333333D;
/*     */     static final double MIN_LEN = 0.001D;
/*     */     double[] bezierCoords;
/*     */     int numCoords;
/*     */     int windingrule;
/*     */     double[] myTvals;
/*     */     
/*     */     public Geometry(Shape s) {
/* 252 */       this.bezierCoords = new double[20];
/* 253 */       PathIterator pi = s.getPathIterator(null);
/* 254 */       this.windingrule = pi.getWindingRule();
/* 255 */       if (pi.isDone())
/*     */       {
/*     */ 
/* 258 */         this.numCoords = 8;
/*     */       }
/* 260 */       double[] coords = new double[6];
/* 261 */       int type = pi.currentSegment(coords);
/* 262 */       pi.next();
/* 263 */       if (type != 0) {
/* 264 */         throw new IllegalPathStateException("missing initial moveto");
/*     */       }
/* 266 */       double curx = this.bezierCoords[0] = coords[0];
/* 267 */       double cury = this.bezierCoords[1] = coords[1];
/*     */       
/* 269 */       this.numCoords = 2;
/* 270 */       double newx; double newy; while (!pi.isDone()) {
/* 271 */         if (this.numCoords + 6 > this.bezierCoords.length)
/*     */         {
/* 273 */           int newsize = (this.numCoords - 2) * 2 + 2;
/* 274 */           double[] newCoords = new double[newsize];
/* 275 */           System.arraycopy(this.bezierCoords, 0, newCoords, 0, this.numCoords);
/* 276 */           this.bezierCoords = newCoords;
/*     */         }
/* 278 */         switch (pi.currentSegment(coords)) {
/*     */         case 0: 
/* 280 */           throw new InternalError("Cannot handle multiple subpaths");
/*     */         
/*     */         case 4: 
/* 283 */           if ((curx != this.bezierCoords[0]) || (cury != this.bezierCoords[1]))
/*     */           {
/*     */ 
/*     */ 
/* 287 */             coords[0] = this.bezierCoords[0];
/* 288 */             coords[1] = this.bezierCoords[1];
/*     */           }
/*     */           break;
/* 291 */         case 1:  newx = coords[0];
/* 292 */           newy = coords[1];
/*     */           
/* 294 */           this.bezierCoords[(this.numCoords++)] = Morphing2D.interp(curx, newx, 0.3333333333333333D);
/* 295 */           this.bezierCoords[(this.numCoords++)] = Morphing2D.interp(cury, newy, 0.3333333333333333D);
/*     */           
/* 297 */           this.bezierCoords[(this.numCoords++)] = Morphing2D.interp(newx, curx, 0.3333333333333333D);
/* 298 */           this.bezierCoords[(this.numCoords++)] = Morphing2D.interp(newy, cury, 0.3333333333333333D);
/* 299 */           this.bezierCoords[(this.numCoords++)] = (curx = newx);
/* 300 */           this.bezierCoords[(this.numCoords++)] = (cury = newy);
/* 301 */           break;
/*     */         case 2: 
/* 303 */           double ctrlx = coords[0];
/* 304 */           double ctrly = coords[1];
/* 305 */           newx = coords[2];
/* 306 */           newy = coords[3];
/*     */           
/* 308 */           this.bezierCoords[(this.numCoords++)] = Morphing2D.interp(ctrlx, curx, 0.3333333333333333D);
/* 309 */           this.bezierCoords[(this.numCoords++)] = Morphing2D.interp(ctrly, cury, 0.3333333333333333D);
/*     */           
/* 311 */           this.bezierCoords[(this.numCoords++)] = Morphing2D.interp(ctrlx, newx, 0.3333333333333333D);
/* 312 */           this.bezierCoords[(this.numCoords++)] = Morphing2D.interp(ctrly, newy, 0.3333333333333333D);
/* 313 */           this.bezierCoords[(this.numCoords++)] = (curx = newx);
/* 314 */           this.bezierCoords[(this.numCoords++)] = (cury = newy);
/* 315 */           break;
/*     */         case 3: 
/* 317 */           this.bezierCoords[(this.numCoords++)] = coords[0];
/* 318 */           this.bezierCoords[(this.numCoords++)] = coords[1];
/* 319 */           this.bezierCoords[(this.numCoords++)] = coords[2];
/* 320 */           this.bezierCoords[(this.numCoords++)] = coords[3];
/* 321 */           this.bezierCoords[(this.numCoords++)] = (curx = coords[4]);
/* 322 */           this.bezierCoords[(this.numCoords++)] = (cury = coords[5]);
/*     */         }
/*     */         
/* 325 */         pi.next();
/*     */       }
/*     */       
/*     */ 
/*     */ 
/* 330 */       if ((this.numCoords < 8) || (curx != this.bezierCoords[0]) || (cury != this.bezierCoords[1]))
/*     */       {
/*     */ 
/* 333 */         newx = this.bezierCoords[0];
/* 334 */         newy = this.bezierCoords[1];
/*     */         
/* 336 */         this.bezierCoords[(this.numCoords++)] = Morphing2D.interp(curx, newx, 0.3333333333333333D);
/* 337 */         this.bezierCoords[(this.numCoords++)] = Morphing2D.interp(cury, newy, 0.3333333333333333D);
/*     */         
/* 339 */         this.bezierCoords[(this.numCoords++)] = Morphing2D.interp(newx, curx, 0.3333333333333333D);
/* 340 */         this.bezierCoords[(this.numCoords++)] = Morphing2D.interp(newy, cury, 0.3333333333333333D);
/* 341 */         this.bezierCoords[(this.numCoords++)] = newx;
/* 342 */         this.bezierCoords[(this.numCoords++)] = newy;
/*     */       }
/*     */       
/* 345 */       int minPt = 0;
/* 346 */       double minX = this.bezierCoords[0];
/* 347 */       double minY = this.bezierCoords[1];
/* 348 */       for (int ci = 6; ci < this.numCoords; ci += 6) {
/* 349 */         double x = this.bezierCoords[ci];
/* 350 */         double y = this.bezierCoords[(ci + 1)];
/* 351 */         if ((y < minY) || ((y == minY) && (x < minX))) {
/* 352 */           minPt = ci;
/* 353 */           minX = x;
/* 354 */           minY = y;
/*     */         }
/*     */       }
/*     */       
/*     */ 
/* 359 */       if (minPt > 0)
/*     */       {
/* 361 */         double[] newCoords = new double[this.numCoords];
/*     */         
/*     */ 
/* 364 */         System.arraycopy(this.bezierCoords, minPt, newCoords, 0, this.numCoords - minPt);
/*     */         
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 374 */         System.arraycopy(this.bezierCoords, 2, newCoords, this.numCoords - minPt, minPt);
/*     */         
/*     */ 
/* 377 */         this.bezierCoords = newCoords;
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 407 */       double area = 0.0D;
/*     */       
/*     */ 
/* 410 */       curx = this.bezierCoords[0];
/* 411 */       cury = this.bezierCoords[1];
/* 412 */       for (int i = 2; i < this.numCoords; i += 2) {
/* 413 */         double newx = this.bezierCoords[i];
/* 414 */         double newy = this.bezierCoords[(i + 1)];
/* 415 */         area += curx * newy - newx * cury;
/* 416 */         curx = newx;
/* 417 */         cury = newy;
/*     */       }
/* 419 */       if (area < 0.0D)
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 434 */         int i = 2;
/* 435 */         int j = this.numCoords - 4;
/* 436 */         while (i < j) {
/* 437 */           curx = this.bezierCoords[i];
/* 438 */           cury = this.bezierCoords[(i + 1)];
/* 439 */           this.bezierCoords[i] = this.bezierCoords[j];
/* 440 */           this.bezierCoords[(i + 1)] = this.bezierCoords[(j + 1)];
/* 441 */           this.bezierCoords[j] = curx;
/* 442 */           this.bezierCoords[(j + 1)] = cury;
/* 443 */           i += 2;
/* 444 */           j -= 2;
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */     public int getWindingRule() {
/* 450 */       return this.windingrule;
/*     */     }
/*     */     
/*     */     public int getNumCoords() {
/* 454 */       return this.numCoords;
/*     */     }
/*     */     
/*     */     public double getCoord(int i) {
/* 458 */       return this.bezierCoords[i];
/*     */     }
/*     */     
/*     */     public double[] getTvals() {
/* 462 */       if (this.myTvals != null) {
/* 463 */         return this.myTvals;
/*     */       }
/*     */       
/*     */ 
/*     */ 
/* 468 */       double[] tvals = new double[(this.numCoords - 2) / 6 + 1];
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 474 */       double segx = this.bezierCoords[0];
/* 475 */       double segy = this.bezierCoords[1];
/* 476 */       double tlen = 0.0D;
/* 477 */       int ci = 2;
/* 478 */       int ti = 0;
/* 479 */       while (ci < this.numCoords)
/*     */       {
/* 481 */         double prevx = segx;
/* 482 */         double prevy = segy;
/* 483 */         double newx = this.bezierCoords[(ci++)];
/* 484 */         double newy = this.bezierCoords[(ci++)];
/* 485 */         prevx -= newx;
/* 486 */         prevy -= newy;
/* 487 */         double len = Math.sqrt(prevx * prevx + prevy * prevy);
/* 488 */         prevx = newx;
/* 489 */         prevy = newy;
/* 490 */         newx = this.bezierCoords[(ci++)];
/* 491 */         newy = this.bezierCoords[(ci++)];
/* 492 */         prevx -= newx;
/* 493 */         prevy -= newy;
/* 494 */         len += Math.sqrt(prevx * prevx + prevy * prevy);
/* 495 */         prevx = newx;
/* 496 */         prevy = newy;
/* 497 */         newx = this.bezierCoords[(ci++)];
/* 498 */         newy = this.bezierCoords[(ci++)];
/* 499 */         prevx -= newx;
/* 500 */         prevy -= newy;
/* 501 */         len += Math.sqrt(prevx * prevx + prevy * prevy);
/*     */         
/* 503 */         segx -= newx;
/* 504 */         segy -= newy;
/* 505 */         len += Math.sqrt(segx * segx + segy * segy);
/*     */         
/* 507 */         len /= 2.0D;
/*     */         
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 520 */         if (len < 0.001D) {
/* 521 */           len = 0.001D;
/*     */         }
/* 523 */         tlen += len;
/* 524 */         tvals[(ti++)] = tlen;
/* 525 */         segx = newx;
/* 526 */         segy = newy;
/*     */       }
/*     */       
/*     */ 
/*     */ 
/* 531 */       double prevt = tvals[0];
/* 532 */       tvals[0] = 0.0D;
/* 533 */       for (ti = 1; ti < tvals.length - 1; ti++) {
/* 534 */         double nextt = tvals[ti];
/* 535 */         tvals[ti] = (prevt / tlen);
/* 536 */         prevt = nextt;
/*     */       }
/* 538 */       tvals[ti] = 1.0D;
/* 539 */       return this.myTvals = tvals;
/*     */     }
/*     */     
/*     */     public void setTvals(double[] newTvals) {
/* 543 */       double[] oldCoords = this.bezierCoords;
/* 544 */       double[] newCoords = new double[2 + (newTvals.length - 1) * 6];
/* 545 */       double[] oldTvals = getTvals();
/* 546 */       int oldci = 0;
/*     */       double x1;
/*     */       double xc1;
/* 549 */       double xc0; double x0 = xc0 = xc1 = x1 = oldCoords[(oldci++)];
/* 550 */       double y1; double yc1; double yc0; double y0 = yc0 = yc1 = y1 = oldCoords[(oldci++)];
/* 551 */       int newci = 0;
/* 552 */       newCoords[(newci++)] = x0;
/* 553 */       newCoords[(newci++)] = y0;
/* 554 */       double t0 = 0.0D;
/* 555 */       double t1 = 0.0D;
/* 556 */       int oldti = 1;
/* 557 */       int newti = 1;
/* 558 */       while (newti < newTvals.length) {
/* 559 */         if (t0 >= t1) {
/* 560 */           x0 = x1;
/* 561 */           y0 = y1;
/* 562 */           xc0 = oldCoords[(oldci++)];
/* 563 */           yc0 = oldCoords[(oldci++)];
/* 564 */           xc1 = oldCoords[(oldci++)];
/* 565 */           yc1 = oldCoords[(oldci++)];
/* 566 */           x1 = oldCoords[(oldci++)];
/* 567 */           y1 = oldCoords[(oldci++)];
/* 568 */           t1 = oldTvals[(oldti++)];
/*     */         }
/* 570 */         double nt = newTvals[(newti++)];
/*     */         
/* 572 */         if (nt < t1)
/*     */         {
/* 574 */           double relt = (nt - t0) / (t1 - t0);
/* 575 */           newCoords[(newci++)] = (x0 = Morphing2D.interp(x0, xc0, relt));
/* 576 */           newCoords[(newci++)] = (y0 = Morphing2D.interp(y0, yc0, relt));
/* 577 */           xc0 = Morphing2D.interp(xc0, xc1, relt);
/* 578 */           yc0 = Morphing2D.interp(yc0, yc1, relt);
/* 579 */           xc1 = Morphing2D.interp(xc1, x1, relt);
/* 580 */           yc1 = Morphing2D.interp(yc1, y1, relt);
/* 581 */           newCoords[(newci++)] = (x0 = Morphing2D.interp(x0, xc0, relt));
/* 582 */           newCoords[(newci++)] = (y0 = Morphing2D.interp(y0, yc0, relt));
/* 583 */           xc0 = Morphing2D.interp(xc0, xc1, relt);
/* 584 */           yc0 = Morphing2D.interp(yc0, yc1, relt);
/* 585 */           newCoords[(newci++)] = (x0 = Morphing2D.interp(x0, xc0, relt));
/* 586 */           newCoords[(newci++)] = (y0 = Morphing2D.interp(y0, yc0, relt));
/*     */         } else {
/* 588 */           newCoords[(newci++)] = xc0;
/* 589 */           newCoords[(newci++)] = yc0;
/* 590 */           newCoords[(newci++)] = xc1;
/* 591 */           newCoords[(newci++)] = yc1;
/* 592 */           newCoords[(newci++)] = x1;
/* 593 */           newCoords[(newci++)] = y1;
/*     */         }
/* 595 */         t0 = nt;
/*     */       }
/* 597 */       this.bezierCoords = newCoords;
/* 598 */       this.numCoords = newCoords.length;
/* 599 */       this.myTvals = newTvals;
/*     */     }
/*     */   }
/*     */   
/*     */   private static class Iterator implements PathIterator
/*     */   {
/*     */     AffineTransform at;
/*     */     Morphing2D.Geometry g0;
/*     */     Morphing2D.Geometry g1;
/*     */     double t;
/*     */     int cindex;
/*     */     double[] dcoords;
/*     */     
/*     */     public Iterator(AffineTransform at, Morphing2D.Geometry g0, Morphing2D.Geometry g1, double t) {
/* 613 */       this.at = at;
/* 614 */       this.g0 = g0;
/* 615 */       this.g1 = g1;
/* 616 */       this.t = t;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     public int getWindingRule()
/*     */     {
/* 623 */       return this.g0.getWindingRule();
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     public boolean isDone()
/*     */     {
/* 630 */       return this.cindex > this.g0.getNumCoords();
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     public void next()
/*     */     {
/* 637 */       if (this.cindex == 0) {
/* 638 */         this.cindex = 2;
/*     */       } else {
/* 640 */         this.cindex += 6;
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public int currentSegment(float[] coords)
/*     */     {
/* 650 */       if (this.dcoords == null) {
/* 651 */         this.dcoords = new double[6];
/*     */       }
/* 653 */       int type = currentSegment(this.dcoords);
/* 654 */       if (type != 4) {
/* 655 */         coords[0] = ((float)this.dcoords[0]);
/* 656 */         coords[1] = ((float)this.dcoords[1]);
/* 657 */         if (type != 0) {
/* 658 */           coords[2] = ((float)this.dcoords[2]);
/* 659 */           coords[3] = ((float)this.dcoords[3]);
/* 660 */           coords[4] = ((float)this.dcoords[4]);
/* 661 */           coords[5] = ((float)this.dcoords[5]);
/*     */         }
/*     */       }
/* 664 */       return type;
/*     */     }
/*     */     
/*     */ 
/*     */     public int currentSegment(double[] coords)
/*     */     {
/*     */       int n;
/*     */       int type;
/*     */       int n;
/* 673 */       if (this.cindex == 0) {
/* 674 */         int type = 0;
/* 675 */         n = 2; } else { int n;
/* 676 */         if (this.cindex >= this.g0.getNumCoords()) {
/* 677 */           int type = 4;
/* 678 */           n = 0;
/*     */         } else {
/* 680 */           type = 3;
/* 681 */           n = 6;
/*     */         } }
/* 683 */       if (n > 0) {
/* 684 */         for (int i = 0; i < n; i++) {
/* 685 */           coords[i] = Morphing2D.interp(this.g0.getCoord(this.cindex + i), this.g1.getCoord(this.cindex + i), this.t);
/*     */         }
/*     */         
/*     */ 
/* 689 */         if (this.at != null) {
/* 690 */           this.at.transform(coords, 0, coords, 0, n / 2);
/*     */         }
/*     */       }
/* 693 */       return type;
/*     */     }
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\geom\Morphing2D.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */