/*      */ package com.objectplanet.chart;
/*      */ 
/*      */ import java.io.BufferedOutputStream;
/*      */ import java.io.IOException;
/*      */ import java.io.PrintStream;
/*      */ import java.net.SocketException;
/*      */ import java.util.Vector;
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
/*      */ class Huffman
/*      */ {
/*      */   int bufferPutBits;
/*      */   int bufferPutBuffer;
/*      */   public int ImageHeight;
/*      */   public int ImageWidth;
/*      */   public int[][] DC_matrix0;
/*      */   public int[][] AC_matrix0;
/*      */   public int[][] DC_matrix1;
/*      */   public int[][] AC_matrix1;
/*      */   public Object[] DC_matrix;
/*      */   public Object[] AC_matrix;
/*      */   public int code;
/*      */   public int NumOfDCTables;
/*      */   public int NumOfACTables;
/*  784 */   public int[] bitsDCluminance = { 0, 0, 1, 5, 1, 1, 1, 1, 1, 1 };
/*  785 */   public int[] valDCluminance = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11 };
/*  786 */   public int[] bitsDCchrominance = { 1, 0, 3, 1, 1, 1, 1, 1, 1, 1, 1, 1 };
/*  787 */   public int[] valDCchrominance = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11 };
/*  788 */   public int[] bitsACluminance = { 16, 0, 2, 1, 3, 3, 2, 4, 3, 5, 5, 4, 4, 0, 0, 1, 125 };
/*      */   
/*  790 */   public int[] valACluminance = { 1, 2, 3, 0, 4, 17, 5, 18, 
/*  791 */     33, 49, 65, 6, 19, 81, 97, 7, 
/*  792 */     34, 113, 20, 50, 129, 145, 161, 8, 
/*  793 */     35, 66, 177, 193, 21, 82, 209, 240, 
/*  794 */     36, 51, 98, 114, 130, 9, 10, 22, 
/*  795 */     23, 24, 25, 26, 37, 38, 39, 40, 
/*  796 */     41, 42, 52, 53, 54, 55, 56, 57, 
/*  797 */     58, 67, 68, 69, 70, 71, 72, 73, 
/*  798 */     74, 83, 84, 85, 86, 87, 88, 89, 
/*  799 */     90, 99, 100, 101, 102, 103, 104, 105, 
/*  800 */     106, 115, 116, 117, 118, 119, 120, 121, 
/*  801 */     122, 131, 132, 133, 134, 135, 136, 137, 
/*  802 */     138, 146, 147, 148, 149, 150, 151, 152, 
/*  803 */     153, 154, 162, 163, 164, 165, 166, 167, 
/*  804 */     168, 169, 170, 178, 179, 180, 181, 182, 
/*  805 */     183, 184, 185, 186, 194, 195, 196, 197, 
/*  806 */     198, 199, 200, 201, 202, 210, 211, 212, 
/*  807 */     213, 214, 215, 216, 217, 218, 225, 226, 
/*  808 */     227, 228, 229, 230, 231, 232, 233, 234, 
/*  809 */     241, 242, 243, 244, 245, 246, 247, 248, 
/*  810 */     249, 250 };
/*  811 */   public int[] bitsACchrominance = { 17, 0, 2, 1, 2, 4, 4, 3, 4, 7, 5, 4, 4, 0, 1, 2, 119 };
/*      */   
/*  813 */   public int[] valACchrominance = { 0, 1, 2, 3, 17, 4, 5, 33, 
/*  814 */     49, 6, 18, 65, 81, 7, 97, 113, 
/*  815 */     19, 34, 50, 129, 8, 20, 66, 145, 
/*  816 */     161, 177, 193, 9, 35, 51, 82, 240, 
/*  817 */     21, 98, 114, 209, 10, 22, 36, 52, 
/*  818 */     225, 37, 241, 23, 24, 25, 26, 38, 
/*  819 */     39, 40, 41, 42, 53, 54, 55, 56, 
/*  820 */     57, 58, 67, 68, 69, 70, 71, 72, 
/*  821 */     73, 74, 83, 84, 85, 86, 87, 88, 
/*  822 */     89, 90, 99, 100, 101, 102, 103, 104, 
/*  823 */     105, 106, 115, 116, 117, 118, 119, 120, 
/*  824 */     121, 122, 130, 131, 132, 133, 134, 135, 
/*  825 */     136, 137, 138, 146, 147, 148, 149, 150, 
/*  826 */     151, 152, 153, 154, 162, 163, 164, 165, 
/*  827 */     166, 167, 168, 169, 170, 178, 179, 180, 
/*  828 */     181, 182, 183, 184, 185, 186, 194, 195, 
/*  829 */     196, 197, 198, 199, 200, 201, 202, 210, 
/*  830 */     211, 212, 213, 214, 215, 216, 217, 218, 
/*  831 */     226, 227, 228, 229, 230, 231, 232, 233, 
/*  832 */     234, 242, 243, 244, 245, 246, 247, 248, 
/*  833 */     249, 250 };
/*      */   
/*      */ 
/*      */   public Vector bits;
/*      */   
/*      */ 
/*      */   public Vector val;
/*      */   
/*  841 */   public static int[] jpegNaturalOrder = {
/*  842 */     0, 1, 8, 16, 9, 2, 3, 10, 
/*  843 */     17, 24, 32, 25, 18, 11, 4, 5, 
/*  844 */     12, 19, 26, 33, 40, 48, 41, 34, 
/*  845 */     27, 20, 13, 6, 7, 14, 21, 28, 
/*  846 */     35, 42, 49, 56, 57, 50, 43, 36, 
/*  847 */     29, 22, 15, 23, 30, 37, 44, 51, 
/*  848 */     58, 59, 52, 45, 38, 31, 39, 46, 
/*  849 */     53, 60, 61, 54, 47, 55, 62, 63 };
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Huffman(int Width, int Height)
/*      */   {
/*  857 */     this.bits = new Vector();
/*  858 */     this.bits.addElement(this.bitsDCluminance);
/*  859 */     this.bits.addElement(this.bitsACluminance);
/*  860 */     this.bits.addElement(this.bitsDCchrominance);
/*  861 */     this.bits.addElement(this.bitsACchrominance);
/*  862 */     this.val = new Vector();
/*  863 */     this.val.addElement(this.valDCluminance);
/*  864 */     this.val.addElement(this.valACluminance);
/*  865 */     this.val.addElement(this.valDCchrominance);
/*  866 */     this.val.addElement(this.valACchrominance);
/*  867 */     initHuf();
/*  868 */     this.code = this.code;
/*  869 */     this.ImageWidth = Width;
/*  870 */     this.ImageHeight = Height;
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
/*      */   public void HuffmanBlockEncoder(BufferedOutputStream outStream, int[] zigzag, int prec, int DCcode, int ACcode)
/*      */   {
/*  883 */     this.NumOfDCTables = 2;
/*  884 */     this.NumOfACTables = 2;
/*      */     
/*      */     int temp2;
/*      */     
/*  888 */     int temp = temp2 = zigzag[0] - prec;
/*  889 */     if (temp < 0) {
/*  890 */       temp = -temp;
/*  891 */       temp2--;
/*      */     }
/*  893 */     int nbits = 0;
/*  894 */     while (temp != 0) {
/*  895 */       nbits++;
/*  896 */       temp >>= 1;
/*      */     }
/*      */     
/*  899 */     bufferIt(outStream, ((int[][])this.DC_matrix[DCcode])[nbits][0], ((int[][])this.DC_matrix[DCcode])[nbits][1]);
/*      */     
/*  901 */     if (nbits != 0) {
/*  902 */       bufferIt(outStream, temp2, nbits);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  907 */     int r = 0;
/*      */     
/*  909 */     for (int k = 1; k < 64; k++) {
/*  910 */       if ((temp = zigzag[jpegNaturalOrder[k]]) == 0) {
/*  911 */         r++;
/*      */       }
/*      */       else {
/*  914 */         while (r > 15) {
/*  915 */           bufferIt(outStream, ((int[][])this.AC_matrix[ACcode])['ð'][0], ((int[][])this.AC_matrix[ACcode])['ð'][1]);
/*  916 */           r -= 16;
/*      */         }
/*  918 */         temp2 = temp;
/*  919 */         if (temp < 0) {
/*  920 */           temp = -temp;
/*  921 */           temp2--;
/*      */         }
/*  923 */         nbits = 1;
/*  924 */         while (temp >>= 1 != 0) {
/*  925 */           nbits++;
/*      */         }
/*  927 */         int i = (r << 4) + nbits;
/*  928 */         bufferIt(outStream, ((int[][])this.AC_matrix[ACcode])[i][0], ((int[][])this.AC_matrix[ACcode])[i][1]);
/*  929 */         bufferIt(outStream, temp2, nbits);
/*      */         
/*  931 */         r = 0;
/*      */       }
/*      */     }
/*      */     
/*  935 */     if (r > 0) {
/*  936 */       bufferIt(outStream, ((int[][])this.AC_matrix[ACcode])[0][0], ((int[][])this.AC_matrix[ACcode])[0][1]);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   void bufferIt(BufferedOutputStream outStream, int code, int size)
/*      */   {
/*  946 */     int PutBuffer = code;
/*  947 */     int PutBits = this.bufferPutBits;
/*      */     
/*  949 */     PutBuffer &= (1 << size) - 1;
/*  950 */     PutBits += size;
/*  951 */     PutBuffer <<= 24 - PutBits;
/*  952 */     PutBuffer |= this.bufferPutBuffer;
/*      */     
/*  954 */     while (PutBits >= 8) {
/*  955 */       int c = PutBuffer >> 16 & 0xFF;
/*      */       try
/*      */       {
/*  958 */         outStream.write(c);
/*      */       }
/*      */       catch (SocketException e) {
/*  961 */         throw new RuntimeException();
/*      */       }
/*      */       catch (IOException e) {
/*  964 */         System.out.println("IO Error: " + e.getMessage());
/*      */       }
/*  966 */       if (c == 255) {
/*      */         try
/*      */         {
/*  969 */           outStream.write(0);
/*      */         }
/*      */         catch (IOException e) {
/*  972 */           System.out.println("IO Error: " + e.getMessage());
/*      */         }
/*      */       }
/*  975 */       PutBuffer <<= 8;
/*  976 */       PutBits -= 8;
/*      */     }
/*  978 */     this.bufferPutBuffer = PutBuffer;
/*  979 */     this.bufferPutBits = PutBits;
/*      */   }
/*      */   
/*      */   void flushBuffer(BufferedOutputStream outStream)
/*      */   {
/*  984 */     int PutBuffer = this.bufferPutBuffer;
/*  985 */     int PutBits = this.bufferPutBits;
/*  986 */     while (PutBits >= 8) {
/*  987 */       int c = PutBuffer >> 16 & 0xFF;
/*      */       try
/*      */       {
/*  990 */         outStream.write(c);
/*      */       }
/*      */       catch (IOException e) {
/*  993 */         System.out.println("IO Error: " + e.getMessage());
/*      */       }
/*  995 */       if (c == 255) {
/*      */         try {
/*  997 */           outStream.write(0);
/*      */         }
/*      */         catch (IOException e) {
/* 1000 */           System.out.println("IO Error: " + e.getMessage());
/*      */         }
/*      */       }
/* 1003 */       PutBuffer <<= 8;
/* 1004 */       PutBits -= 8;
/*      */     }
/* 1006 */     if (PutBits > 0) {
/* 1007 */       int c = PutBuffer >> 16 & 0xFF;
/*      */       try
/*      */       {
/* 1010 */         outStream.write(c);
/*      */       }
/*      */       catch (IOException e) {
/* 1013 */         System.out.println("IO Error: " + e.getMessage());
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void initHuf()
/*      */   {
/* 1026 */     this.DC_matrix0 = new int[12][2];
/* 1027 */     this.DC_matrix1 = new int[12][2];
/* 1028 */     this.AC_matrix0 = new int['ÿ'][2];
/* 1029 */     this.AC_matrix1 = new int['ÿ'][2];
/* 1030 */     this.DC_matrix = new Object[2];
/* 1031 */     this.AC_matrix = new Object[2];
/*      */     
/* 1033 */     int[] huffsize = new int['ā'];
/* 1034 */     int[] huffcode = new int['ā'];
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1041 */     int p = 0;
/* 1042 */     for (int l = 1; l <= 16; l++)
/*      */     {
/* 1044 */       for (int i = 1; i <= this.bitsDCchrominance[l]; i++)
/*      */       {
/* 1046 */         huffsize[(p++)] = l;
/*      */       }
/*      */     }
/* 1049 */     huffsize[p] = 0;
/* 1050 */     int lastp = p;
/*      */     
/* 1052 */     int code = 0;
/* 1053 */     int si = huffsize[0];
/* 1054 */     p = 0;
/* 1055 */     while (huffsize[p] != 0)
/*      */     {
/* 1057 */       while (huffsize[p] == si)
/*      */       {
/* 1059 */         huffcode[(p++)] = code;
/* 1060 */         code++;
/*      */       }
/* 1062 */       code <<= 1;
/* 1063 */       si++;
/*      */     }
/*      */     
/* 1066 */     for (p = 0; p < lastp; p++)
/*      */     {
/* 1068 */       this.DC_matrix1[this.valDCchrominance[p]][0] = huffcode[p];
/* 1069 */       this.DC_matrix1[this.valDCchrominance[p]][1] = huffsize[p];
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1077 */     p = 0;
/* 1078 */     for (l = 1; l <= 16; l++)
/*      */     {
/* 1080 */       for (int i = 1; i <= this.bitsACchrominance[l]; i++)
/*      */       {
/* 1082 */         huffsize[(p++)] = l;
/*      */       }
/*      */     }
/* 1085 */     huffsize[p] = 0;
/* 1086 */     lastp = p;
/*      */     
/* 1088 */     code = 0;
/* 1089 */     si = huffsize[0];
/* 1090 */     p = 0;
/* 1091 */     while (huffsize[p] != 0)
/*      */     {
/* 1093 */       while (huffsize[p] == si)
/*      */       {
/* 1095 */         huffcode[(p++)] = code;
/* 1096 */         code++;
/*      */       }
/* 1098 */       code <<= 1;
/* 1099 */       si++;
/*      */     }
/*      */     
/* 1102 */     for (p = 0; p < lastp; p++)
/*      */     {
/* 1104 */       this.AC_matrix1[this.valACchrominance[p]][0] = huffcode[p];
/* 1105 */       this.AC_matrix1[this.valACchrominance[p]][1] = huffsize[p];
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1112 */     p = 0;
/* 1113 */     for (l = 1; l <= 16; l++)
/*      */     {
/* 1115 */       for (int i = 1; i <= this.bitsDCluminance[l]; i++)
/*      */       {
/* 1117 */         huffsize[(p++)] = l;
/*      */       }
/*      */     }
/* 1120 */     huffsize[p] = 0;
/* 1121 */     lastp = p;
/*      */     
/* 1123 */     code = 0;
/* 1124 */     si = huffsize[0];
/* 1125 */     p = 0;
/* 1126 */     while (huffsize[p] != 0)
/*      */     {
/* 1128 */       while (huffsize[p] == si)
/*      */       {
/* 1130 */         huffcode[(p++)] = code;
/* 1131 */         code++;
/*      */       }
/* 1133 */       code <<= 1;
/* 1134 */       si++;
/*      */     }
/*      */     
/* 1137 */     for (p = 0; p < lastp; p++)
/*      */     {
/* 1139 */       this.DC_matrix0[this.valDCluminance[p]][0] = huffcode[p];
/* 1140 */       this.DC_matrix0[this.valDCluminance[p]][1] = huffsize[p];
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1148 */     p = 0;
/* 1149 */     for (l = 1; l <= 16; l++)
/*      */     {
/* 1151 */       for (int i = 1; i <= this.bitsACluminance[l]; i++)
/*      */       {
/* 1153 */         huffsize[(p++)] = l;
/*      */       }
/*      */     }
/* 1156 */     huffsize[p] = 0;
/* 1157 */     lastp = p;
/*      */     
/* 1159 */     code = 0;
/* 1160 */     si = huffsize[0];
/* 1161 */     p = 0;
/* 1162 */     while (huffsize[p] != 0)
/*      */     {
/* 1164 */       while (huffsize[p] == si)
/*      */       {
/* 1166 */         huffcode[(p++)] = code;
/* 1167 */         code++;
/*      */       }
/* 1169 */       code <<= 1;
/* 1170 */       si++;
/*      */     }
/* 1172 */     for (int q = 0; q < lastp; q++)
/*      */     {
/* 1174 */       this.AC_matrix0[this.valACluminance[q]][0] = huffcode[q];
/* 1175 */       this.AC_matrix0[this.valACluminance[q]][1] = huffsize[q];
/*      */     }
/*      */     
/* 1178 */     this.DC_matrix[0] = this.DC_matrix0;
/* 1179 */     this.DC_matrix[1] = this.DC_matrix1;
/* 1180 */     this.AC_matrix[0] = this.AC_matrix0;
/* 1181 */     this.AC_matrix[1] = this.AC_matrix1;
/*      */   }
/*      */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\com\objectplanet\chart\Huffman.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */