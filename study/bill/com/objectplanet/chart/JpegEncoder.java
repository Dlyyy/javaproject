/*     */ package com.objectplanet.chart;
/*     */ 
/*     */ import java.awt.Image;
/*     */ import java.io.BufferedOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.io.PrintStream;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class JpegEncoder
/*     */ {
/*     */   Thread runner;
/*     */   BufferedOutputStream outStream;
/*     */   Image image;
/*     */   JpegInfo JpegObj;
/*     */   Huffman Huf;
/*     */   DCT dct;
/*     */   int imageHeight;
/*     */   int imageWidth;
/*     */   int Quality;
/*     */   int code;
/*  39 */   public static int[] jpegNaturalOrder = {
/*  40 */     0, 1, 8, 16, 9, 2, 3, 10, 
/*  41 */     17, 24, 32, 25, 18, 11, 4, 5, 
/*  42 */     12, 19, 26, 33, 40, 48, 41, 34, 
/*  43 */     27, 20, 13, 6, 7, 14, 21, 28, 
/*  44 */     35, 42, 49, 56, 57, 50, 43, 36, 
/*  45 */     29, 22, 15, 23, 30, 37, 44, 51, 
/*  46 */     58, 59, 52, 45, 38, 31, 39, 46, 
/*  47 */     53, 60, 61, 54, 47, 55, 62, 63 };
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public JpegEncoder(Image image, int quality, OutputStream out)
/*     */   {
/*  57 */     this.Quality = quality;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  63 */     this.JpegObj = new JpegInfo(image);
/*     */     
/*  65 */     this.imageHeight = this.JpegObj.imageHeight;
/*  66 */     this.imageWidth = this.JpegObj.imageWidth;
/*  67 */     this.outStream = new BufferedOutputStream(out);
/*  68 */     this.dct = new DCT(this.Quality);
/*  69 */     this.Huf = new Huffman(this.imageWidth, this.imageHeight);
/*     */   }
/*     */   
/*     */   public void setQuality(int quality) {
/*  73 */     this.dct = new DCT(quality);
/*     */   }
/*     */   
/*     */   public int getQuality() {
/*  77 */     return this.Quality;
/*     */   }
/*     */   
/*     */   public void Compress() {
/*  81 */     WriteHeaders(this.outStream);
/*  82 */     WriteCompressedData(this.outStream);
/*  83 */     WriteEOI(this.outStream);
/*     */     try {
/*  85 */       this.outStream.flush();
/*     */     } catch (IOException e) {
/*  87 */       System.out.println("IO Error: " + e.getMessage());
/*     */     }
/*     */   }
/*     */   
/*     */   public void WriteCompressedData(BufferedOutputStream outStream) {
/*  92 */     int temp = 0;
/*     */     
/*     */ 
/*  95 */     float[][] dctArray1 = new float[8][8];
/*  96 */     double[][] dctArray2 = new double[8][8];
/*  97 */     int[] dctArray3 = new int[64];
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 105 */     int[] lastDCvalue = new int[this.JpegObj.NumberOfComponents];
/* 106 */     int[] zeroArray = new int[64];
/* 107 */     int Width = 0;int Height = 0;
/* 108 */     int nothing = 0;
/*     */     
/*     */ 
/*     */ 
/* 112 */     int MinBlockWidth = this.imageWidth % 8 != 0 ? (int)(Math.floor(this.imageWidth / 8.0D) + 1.0D) * 8 : this.imageWidth;
/* 113 */     int MinBlockHeight = this.imageHeight % 8 != 0 ? (int)(Math.floor(this.imageHeight / 8.0D) + 1.0D) * 8 : this.imageHeight;
/* 114 */     for (int comp = 0; comp < this.JpegObj.NumberOfComponents; comp++) {
/* 115 */       MinBlockWidth = Math.min(MinBlockWidth, this.JpegObj.BlockWidth[comp]);
/* 116 */       MinBlockHeight = Math.min(MinBlockHeight, this.JpegObj.BlockHeight[comp]);
/*     */     }
/* 118 */     int xpos = 0;
/* 119 */     for (int r = 0; r < MinBlockHeight; r++) {
/* 120 */       for (int c = 0; c < MinBlockWidth; c++) {
/* 121 */         xpos = c * 8;
/* 122 */         int ypos = r * 8;
/* 123 */         for (comp = 0; comp < this.JpegObj.NumberOfComponents; comp++) {
/* 124 */           Width = this.JpegObj.BlockWidth[comp];
/* 125 */           Height = this.JpegObj.BlockHeight[comp];
/* 126 */           float[][] inputArray = (float[][])this.JpegObj.Components[comp];
/*     */           
/* 128 */           for (int i = 0; i < this.JpegObj.VsampFactor[comp]; i++) {
/* 129 */             for (int j = 0; j < this.JpegObj.HsampFactor[comp]; j++) {
/* 130 */               int xblockoffset = j * 8;
/* 131 */               int yblockoffset = i * 8;
/* 132 */               for (int a = 0; a < 8; a++) {
/* 133 */                 for (int b = 0; b < 8; b++)
/*     */                 {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 140 */                   dctArray1[a][b] = inputArray[(ypos + yblockoffset + a)][(xpos + xblockoffset + b)];
/*     */                 }
/*     */               }
/*     */               
/*     */ 
/*     */ 
/* 146 */               dctArray2 = this.dct.forwardDCT(dctArray1);
/* 147 */               dctArray3 = this.dct.quantizeBlock(dctArray2, this.JpegObj.QtableNumber[comp]);
/*     */               
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 154 */               this.Huf.HuffmanBlockEncoder(outStream, dctArray3, lastDCvalue[comp], this.JpegObj.DCtableNumber[comp], this.JpegObj.ACtableNumber[comp]);
/* 155 */               lastDCvalue[comp] = dctArray3[0];
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 161 */     this.Huf.flushBuffer(outStream);
/*     */   }
/*     */   
/*     */   public void WriteEOI(BufferedOutputStream out) {
/* 165 */     byte[] EOI = { -1, -39 };
/* 166 */     WriteMarker(EOI, out);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void WriteHeaders(BufferedOutputStream out)
/*     */   {
/* 174 */     byte[] SOI = { -1, -40 };
/* 175 */     WriteMarker(SOI, out);
/*     */     
/*     */ 
/*     */ 
/* 179 */     byte[] JFIF = new byte[18];
/* 180 */     JFIF[0] = -1;
/* 181 */     JFIF[1] = -32;
/* 182 */     JFIF[2] = 0;
/* 183 */     JFIF[3] = 16;
/* 184 */     JFIF[4] = 74;
/* 185 */     JFIF[5] = 70;
/* 186 */     JFIF[6] = 73;
/* 187 */     JFIF[7] = 70;
/* 188 */     JFIF[8] = 0;
/* 189 */     JFIF[9] = 1;
/* 190 */     JFIF[10] = 0;
/* 191 */     JFIF[11] = 0;
/* 192 */     JFIF[12] = 0;
/* 193 */     JFIF[13] = 1;
/* 194 */     JFIF[14] = 0;
/* 195 */     JFIF[15] = 1;
/* 196 */     JFIF[16] = 0;
/* 197 */     JFIF[17] = 0;
/* 198 */     WriteArray(JFIF, out);
/*     */     
/*     */ 
/* 201 */     String comment = new String();
/* 202 */     comment = this.JpegObj.getComment();
/* 203 */     int length = comment.length();
/* 204 */     byte[] COM = new byte[length + 4];
/* 205 */     COM[0] = -1;
/* 206 */     COM[1] = -2;
/* 207 */     COM[2] = ((byte)(length >> 8 & 0xFF));
/* 208 */     COM[3] = ((byte)(length & 0xFF));
/* 209 */     System.arraycopy(this.JpegObj.Comment.getBytes(), 0, COM, 4, this.JpegObj.Comment.length());
/* 210 */     WriteArray(COM, out);
/*     */     
/*     */ 
/*     */ 
/* 214 */     byte[] DQT = new byte[''];
/* 215 */     DQT[0] = -1;
/* 216 */     DQT[1] = -37;
/* 217 */     DQT[2] = 0;
/* 218 */     DQT[3] = -124;
/* 219 */     int offset = 4;
/* 220 */     for (int i = 0; i < 2; i++) {
/* 221 */       DQT[(offset++)] = ((byte)(0 + i));
/* 222 */       int[] tempArray = (int[])this.dct.quantum[i];
/* 223 */       for (int j = 0; j < 64; j++) {
/* 224 */         DQT[(offset++)] = ((byte)tempArray[jpegNaturalOrder[j]]);
/*     */       }
/*     */     }
/* 227 */     WriteArray(DQT, out);
/*     */     
/*     */ 
/* 230 */     byte[] SOF = new byte[19];
/* 231 */     SOF[0] = -1;
/* 232 */     SOF[1] = -64;
/* 233 */     SOF[2] = 0;
/* 234 */     SOF[3] = 17;
/* 235 */     SOF[4] = ((byte)this.JpegObj.Precision);
/* 236 */     SOF[5] = ((byte)(this.JpegObj.imageHeight >> 8 & 0xFF));
/* 237 */     SOF[6] = ((byte)(this.JpegObj.imageHeight & 0xFF));
/* 238 */     SOF[7] = ((byte)(this.JpegObj.imageWidth >> 8 & 0xFF));
/* 239 */     SOF[8] = ((byte)(this.JpegObj.imageWidth & 0xFF));
/* 240 */     SOF[9] = ((byte)this.JpegObj.NumberOfComponents);
/* 241 */     int index = 10;
/* 242 */     for (i = 0; i < SOF[9]; i++) {
/* 243 */       SOF[(index++)] = ((byte)this.JpegObj.CompID[i]);
/* 244 */       SOF[(index++)] = ((byte)((this.JpegObj.HsampFactor[i] << 4) + this.JpegObj.VsampFactor[i]));
/* 245 */       SOF[(index++)] = ((byte)this.JpegObj.QtableNumber[i]);
/*     */     }
/* 247 */     WriteArray(SOF, out);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 252 */     length = 2;
/* 253 */     index = 4;
/* 254 */     int oldindex = 4;
/* 255 */     byte[] DHT1 = new byte[17];
/* 256 */     byte[] DHT4 = new byte[4];
/* 257 */     DHT4[0] = -1;
/* 258 */     DHT4[1] = -60;
/* 259 */     for (i = 0; i < 4; i++) {
/* 260 */       int bytes = 0;
/* 261 */       DHT1[(index++ - oldindex)] = ((byte)((int[])this.Huf.bits.elementAt(i))[0]);
/* 262 */       for (int j = 1; j < 17; j++) {
/* 263 */         int temp = ((int[])this.Huf.bits.elementAt(i))[j];
/* 264 */         DHT1[(index++ - oldindex)] = ((byte)temp);
/* 265 */         bytes += temp;
/*     */       }
/* 267 */       int intermediateindex = index;
/* 268 */       byte[] DHT2 = new byte[bytes];
/* 269 */       for (j = 0; j < bytes; j++) {
/* 270 */         DHT2[(index++ - intermediateindex)] = ((byte)((int[])this.Huf.val.elementAt(i))[j]);
/*     */       }
/* 272 */       byte[] DHT3 = new byte[index];
/* 273 */       System.arraycopy(DHT4, 0, DHT3, 0, oldindex);
/* 274 */       System.arraycopy(DHT1, 0, DHT3, oldindex, 17);
/* 275 */       System.arraycopy(DHT2, 0, DHT3, oldindex + 17, bytes);
/* 276 */       DHT4 = DHT3;
/* 277 */       oldindex = index;
/*     */     }
/* 279 */     DHT4[2] = ((byte)(index - 2 >> 8 & 0xFF));
/* 280 */     DHT4[3] = ((byte)(index - 2 & 0xFF));
/* 281 */     WriteArray(DHT4, out);
/*     */     
/*     */ 
/*     */ 
/* 285 */     byte[] SOS = new byte[14];
/* 286 */     SOS[0] = -1;
/* 287 */     SOS[1] = -38;
/* 288 */     SOS[2] = 0;
/* 289 */     SOS[3] = 12;
/* 290 */     SOS[4] = ((byte)this.JpegObj.NumberOfComponents);
/* 291 */     index = 5;
/* 292 */     for (i = 0; i < SOS[4]; i++) {
/* 293 */       SOS[(index++)] = ((byte)this.JpegObj.CompID[i]);
/* 294 */       SOS[(index++)] = ((byte)((this.JpegObj.DCtableNumber[i] << 4) + this.JpegObj.ACtableNumber[i]));
/*     */     }
/* 296 */     SOS[(index++)] = ((byte)this.JpegObj.Ss);
/* 297 */     SOS[(index++)] = ((byte)this.JpegObj.Se);
/* 298 */     SOS[(index++)] = ((byte)((this.JpegObj.Ah << 4) + this.JpegObj.Al));
/* 299 */     WriteArray(SOS, out);
/*     */   }
/*     */   
/*     */   void WriteMarker(byte[] data, BufferedOutputStream out)
/*     */   {
/*     */     try {
/* 305 */       out.write(data, 0, 2);
/*     */     } catch (IOException e) {
/* 307 */       System.out.println("IO Error: " + e.getMessage());
/*     */     }
/*     */   }
/*     */   
/*     */   void WriteArray(byte[] data, BufferedOutputStream out)
/*     */   {
/*     */     try {
/* 314 */       int length = ((data[2] & 0xFF) << 8) + (data[3] & 0xFF) + 2;
/* 315 */       out.write(data, 0, length);
/*     */     } catch (IOException e) {
/* 317 */       System.out.println("IO Error: " + e.getMessage());
/*     */     }
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\com\objectplanet\chart\JpegEncoder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */