/*     */ package com.objectplanet.chart;
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
/*     */ class DCT
/*     */ {
/* 334 */   public int N = 8;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 339 */   public int QUALITY = 80;
/*     */   
/* 341 */   public Object[] quantum = new Object[2];
/* 342 */   public Object[] Divisors = new Object[2];
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 347 */   public int[] quantum_luminance = new int[this.N * this.N];
/* 348 */   public double[] DivisorsLuminance = new double[this.N * this.N];
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 353 */   public int[] quantum_chrominance = new int[this.N * this.N];
/* 354 */   public double[] DivisorsChrominance = new double[this.N * this.N];
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
/*     */   public DCT(int QUALITY)
/*     */   {
/* 368 */     initMatrix(QUALITY);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void initMatrix(int quality)
/*     */   {
/* 378 */     double[] AANscaleFactor = { 1.0D, 1.387039845D, 1.306562965D, 1.175875602D, 
/* 379 */       1.0D, 0.785694958D, 0.5411961D, 0.275899379D };
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 389 */     int Quality = quality;
/* 390 */     if (Quality <= 0)
/* 391 */       Quality = 1;
/* 392 */     if (Quality > 100)
/* 393 */       Quality = 100;
/* 394 */     if (Quality < 50) {
/* 395 */       Quality = 5000 / Quality;
/*     */     } else {
/* 397 */       Quality = 200 - Quality * 2;
/*     */     }
/*     */     
/*     */ 
/* 401 */     this.quantum_luminance[0] = 16;
/* 402 */     this.quantum_luminance[1] = 11;
/* 403 */     this.quantum_luminance[2] = 10;
/* 404 */     this.quantum_luminance[3] = 16;
/* 405 */     this.quantum_luminance[4] = 24;
/* 406 */     this.quantum_luminance[5] = 40;
/* 407 */     this.quantum_luminance[6] = 51;
/* 408 */     this.quantum_luminance[7] = 61;
/* 409 */     this.quantum_luminance[8] = 12;
/* 410 */     this.quantum_luminance[9] = 12;
/* 411 */     this.quantum_luminance[10] = 14;
/* 412 */     this.quantum_luminance[11] = 19;
/* 413 */     this.quantum_luminance[12] = 26;
/* 414 */     this.quantum_luminance[13] = 58;
/* 415 */     this.quantum_luminance[14] = 60;
/* 416 */     this.quantum_luminance[15] = 55;
/* 417 */     this.quantum_luminance[16] = 14;
/* 418 */     this.quantum_luminance[17] = 13;
/* 419 */     this.quantum_luminance[18] = 16;
/* 420 */     this.quantum_luminance[19] = 24;
/* 421 */     this.quantum_luminance[20] = 40;
/* 422 */     this.quantum_luminance[21] = 57;
/* 423 */     this.quantum_luminance[22] = 69;
/* 424 */     this.quantum_luminance[23] = 56;
/* 425 */     this.quantum_luminance[24] = 14;
/* 426 */     this.quantum_luminance[25] = 17;
/* 427 */     this.quantum_luminance[26] = 22;
/* 428 */     this.quantum_luminance[27] = 29;
/* 429 */     this.quantum_luminance[28] = 51;
/* 430 */     this.quantum_luminance[29] = 87;
/* 431 */     this.quantum_luminance[30] = 80;
/* 432 */     this.quantum_luminance[31] = 62;
/* 433 */     this.quantum_luminance[32] = 18;
/* 434 */     this.quantum_luminance[33] = 22;
/* 435 */     this.quantum_luminance[34] = 37;
/* 436 */     this.quantum_luminance[35] = 56;
/* 437 */     this.quantum_luminance[36] = 68;
/* 438 */     this.quantum_luminance[37] = 109;
/* 439 */     this.quantum_luminance[38] = 103;
/* 440 */     this.quantum_luminance[39] = 77;
/* 441 */     this.quantum_luminance[40] = 24;
/* 442 */     this.quantum_luminance[41] = 35;
/* 443 */     this.quantum_luminance[42] = 55;
/* 444 */     this.quantum_luminance[43] = 64;
/* 445 */     this.quantum_luminance[44] = 81;
/* 446 */     this.quantum_luminance[45] = 104;
/* 447 */     this.quantum_luminance[46] = 113;
/* 448 */     this.quantum_luminance[47] = 92;
/* 449 */     this.quantum_luminance[48] = 49;
/* 450 */     this.quantum_luminance[49] = 64;
/* 451 */     this.quantum_luminance[50] = 78;
/* 452 */     this.quantum_luminance[51] = 87;
/* 453 */     this.quantum_luminance[52] = 103;
/* 454 */     this.quantum_luminance[53] = 121;
/* 455 */     this.quantum_luminance[54] = 120;
/* 456 */     this.quantum_luminance[55] = 101;
/* 457 */     this.quantum_luminance[56] = 72;
/* 458 */     this.quantum_luminance[57] = 92;
/* 459 */     this.quantum_luminance[58] = 95;
/* 460 */     this.quantum_luminance[59] = 98;
/* 461 */     this.quantum_luminance[60] = 112;
/* 462 */     this.quantum_luminance[61] = 100;
/* 463 */     this.quantum_luminance[62] = 103;
/* 464 */     this.quantum_luminance[63] = 99;
/*     */     
/* 466 */     for (int j = 0; j < 64; j++)
/*     */     {
/* 468 */       int temp = (this.quantum_luminance[j] * Quality + 50) / 100;
/* 469 */       if (temp <= 0) temp = 1;
/* 470 */       if (temp > 255) temp = 255;
/* 471 */       this.quantum_luminance[j] = temp;
/*     */     }
/* 473 */     int index = 0;
/* 474 */     for (int i = 0; i < 8; i++) {
/* 475 */       for (j = 0; j < 8; j++)
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 481 */         this.DivisorsLuminance[index] = (1.0D / (this.quantum_luminance[index] * AANscaleFactor[i] * AANscaleFactor[j] * 8.0D));
/* 482 */         index++;
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 489 */     this.quantum_chrominance[0] = 17;
/* 490 */     this.quantum_chrominance[1] = 18;
/* 491 */     this.quantum_chrominance[2] = 24;
/* 492 */     this.quantum_chrominance[3] = 47;
/* 493 */     this.quantum_chrominance[4] = 99;
/* 494 */     this.quantum_chrominance[5] = 99;
/* 495 */     this.quantum_chrominance[6] = 99;
/* 496 */     this.quantum_chrominance[7] = 99;
/* 497 */     this.quantum_chrominance[8] = 18;
/* 498 */     this.quantum_chrominance[9] = 21;
/* 499 */     this.quantum_chrominance[10] = 26;
/* 500 */     this.quantum_chrominance[11] = 66;
/* 501 */     this.quantum_chrominance[12] = 99;
/* 502 */     this.quantum_chrominance[13] = 99;
/* 503 */     this.quantum_chrominance[14] = 99;
/* 504 */     this.quantum_chrominance[15] = 99;
/* 505 */     this.quantum_chrominance[16] = 24;
/* 506 */     this.quantum_chrominance[17] = 26;
/* 507 */     this.quantum_chrominance[18] = 56;
/* 508 */     this.quantum_chrominance[19] = 99;
/* 509 */     this.quantum_chrominance[20] = 99;
/* 510 */     this.quantum_chrominance[21] = 99;
/* 511 */     this.quantum_chrominance[22] = 99;
/* 512 */     this.quantum_chrominance[23] = 99;
/* 513 */     this.quantum_chrominance[24] = 47;
/* 514 */     this.quantum_chrominance[25] = 66;
/* 515 */     this.quantum_chrominance[26] = 99;
/* 516 */     this.quantum_chrominance[27] = 99;
/* 517 */     this.quantum_chrominance[28] = 99;
/* 518 */     this.quantum_chrominance[29] = 99;
/* 519 */     this.quantum_chrominance[30] = 99;
/* 520 */     this.quantum_chrominance[31] = 99;
/* 521 */     this.quantum_chrominance[32] = 99;
/* 522 */     this.quantum_chrominance[33] = 99;
/* 523 */     this.quantum_chrominance[34] = 99;
/* 524 */     this.quantum_chrominance[35] = 99;
/* 525 */     this.quantum_chrominance[36] = 99;
/* 526 */     this.quantum_chrominance[37] = 99;
/* 527 */     this.quantum_chrominance[38] = 99;
/* 528 */     this.quantum_chrominance[39] = 99;
/* 529 */     this.quantum_chrominance[40] = 99;
/* 530 */     this.quantum_chrominance[41] = 99;
/* 531 */     this.quantum_chrominance[42] = 99;
/* 532 */     this.quantum_chrominance[43] = 99;
/* 533 */     this.quantum_chrominance[44] = 99;
/* 534 */     this.quantum_chrominance[45] = 99;
/* 535 */     this.quantum_chrominance[46] = 99;
/* 536 */     this.quantum_chrominance[47] = 99;
/* 537 */     this.quantum_chrominance[48] = 99;
/* 538 */     this.quantum_chrominance[49] = 99;
/* 539 */     this.quantum_chrominance[50] = 99;
/* 540 */     this.quantum_chrominance[51] = 99;
/* 541 */     this.quantum_chrominance[52] = 99;
/* 542 */     this.quantum_chrominance[53] = 99;
/* 543 */     this.quantum_chrominance[54] = 99;
/* 544 */     this.quantum_chrominance[55] = 99;
/* 545 */     this.quantum_chrominance[56] = 99;
/* 546 */     this.quantum_chrominance[57] = 99;
/* 547 */     this.quantum_chrominance[58] = 99;
/* 548 */     this.quantum_chrominance[59] = 99;
/* 549 */     this.quantum_chrominance[60] = 99;
/* 550 */     this.quantum_chrominance[61] = 99;
/* 551 */     this.quantum_chrominance[62] = 99;
/* 552 */     this.quantum_chrominance[63] = 99;
/*     */     
/* 554 */     for (j = 0; j < 64; j++)
/*     */     {
/* 556 */       int temp = (this.quantum_chrominance[j] * Quality + 50) / 100;
/* 557 */       if (temp <= 0) temp = 1;
/* 558 */       if (temp >= 255) temp = 255;
/* 559 */       this.quantum_chrominance[j] = temp;
/*     */     }
/* 561 */     index = 0;
/* 562 */     for (i = 0; i < 8; i++) {
/* 563 */       for (j = 0; j < 8; j++)
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 569 */         this.DivisorsChrominance[index] = (1.0D / (this.quantum_chrominance[index] * AANscaleFactor[i] * AANscaleFactor[j] * 8.0D));
/* 570 */         index++;
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 576 */     this.quantum[0] = this.quantum_luminance;
/* 577 */     this.Divisors[0] = this.DivisorsLuminance;
/* 578 */     this.quantum[1] = this.quantum_chrominance;
/* 579 */     this.Divisors[1] = this.DivisorsChrominance;
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
/*     */   public double[][] forwardDCTExtreme(float[][] input)
/*     */   {
/* 598 */     double[][] output = new double[this.N][this.N];
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 605 */     for (int v = 0; v < 8; v++) {
/* 606 */       for (int u = 0; u < 8; u++) {
/* 607 */         for (int x = 0; x < 8; x++) {
/* 608 */           for (int y = 0; y < 8; y++) {
/* 609 */             output[v][u] += input[x][y] * Math.cos((2 * x + 1) * u * 3.141592653589793D / 16.0D) * Math.cos((2 * y + 1) * v * 3.141592653589793D / 16.0D);
/*     */           }
/*     */         }
/* 612 */         output[v][u] *= 0.25D * (u == 0 ? 1.0D / Math.sqrt(2.0D) : 1.0D) * (v == 0 ? 1.0D / Math.sqrt(2.0D) : 1.0D);
/*     */       }
/*     */     }
/* 615 */     return output;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public double[][] forwardDCT(float[][] input)
/*     */   {
/* 625 */     double[][] output = new double[this.N][this.N];
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 633 */     for (int i = 0; i < 8; i++) {
/* 634 */       for (int j = 0; j < 8; j++) {
/* 635 */         output[i][j] = (input[i][j] - 128.0D);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 641 */     for (i = 0; i < 8; i++) {
/* 642 */       double tmp0 = output[i][0] + output[i][7];
/* 643 */       double tmp7 = output[i][0] - output[i][7];
/* 644 */       double tmp1 = output[i][1] + output[i][6];
/* 645 */       double tmp6 = output[i][1] - output[i][6];
/* 646 */       double tmp2 = output[i][2] + output[i][5];
/* 647 */       double tmp5 = output[i][2] - output[i][5];
/* 648 */       double tmp3 = output[i][3] + output[i][4];
/* 649 */       double tmp4 = output[i][3] - output[i][4];
/*     */       
/* 651 */       double tmp10 = tmp0 + tmp3;
/* 652 */       double tmp13 = tmp0 - tmp3;
/* 653 */       double tmp11 = tmp1 + tmp2;
/* 654 */       double tmp12 = tmp1 - tmp2;
/*     */       
/* 656 */       output[i][0] = (tmp10 + tmp11);
/* 657 */       output[i][4] = (tmp10 - tmp11);
/*     */       
/* 659 */       double z1 = (tmp12 + tmp13) * 0.707106781D;
/* 660 */       output[i][2] = (tmp13 + z1);
/* 661 */       output[i][6] = (tmp13 - z1);
/*     */       
/* 663 */       tmp10 = tmp4 + tmp5;
/* 664 */       tmp11 = tmp5 + tmp6;
/* 665 */       tmp12 = tmp6 + tmp7;
/*     */       
/* 667 */       double z5 = (tmp10 - tmp12) * 0.382683433D;
/* 668 */       double z2 = 0.5411961D * tmp10 + z5;
/* 669 */       double z4 = 1.306562965D * tmp12 + z5;
/* 670 */       double z3 = tmp11 * 0.707106781D;
/*     */       
/* 672 */       double z11 = tmp7 + z3;
/* 673 */       double z13 = tmp7 - z3;
/*     */       
/* 675 */       output[i][5] = (z13 + z2);
/* 676 */       output[i][3] = (z13 - z2);
/* 677 */       output[i][1] = (z11 + z4);
/* 678 */       output[i][7] = (z11 - z4);
/*     */     }
/*     */     
/* 681 */     for (i = 0; i < 8; i++) {
/* 682 */       double tmp0 = output[0][i] + output[7][i];
/* 683 */       double tmp7 = output[0][i] - output[7][i];
/* 684 */       double tmp1 = output[1][i] + output[6][i];
/* 685 */       double tmp6 = output[1][i] - output[6][i];
/* 686 */       double tmp2 = output[2][i] + output[5][i];
/* 687 */       double tmp5 = output[2][i] - output[5][i];
/* 688 */       double tmp3 = output[3][i] + output[4][i];
/* 689 */       double tmp4 = output[3][i] - output[4][i];
/*     */       
/* 691 */       double tmp10 = tmp0 + tmp3;
/* 692 */       double tmp13 = tmp0 - tmp3;
/* 693 */       double tmp11 = tmp1 + tmp2;
/* 694 */       double tmp12 = tmp1 - tmp2;
/*     */       
/* 696 */       output[0][i] = (tmp10 + tmp11);
/* 697 */       output[4][i] = (tmp10 - tmp11);
/*     */       
/* 699 */       double z1 = (tmp12 + tmp13) * 0.707106781D;
/* 700 */       output[2][i] = (tmp13 + z1);
/* 701 */       output[6][i] = (tmp13 - z1);
/*     */       
/* 703 */       tmp10 = tmp4 + tmp5;
/* 704 */       tmp11 = tmp5 + tmp6;
/* 705 */       tmp12 = tmp6 + tmp7;
/*     */       
/* 707 */       double z5 = (tmp10 - tmp12) * 0.382683433D;
/* 708 */       double z2 = 0.5411961D * tmp10 + z5;
/* 709 */       double z4 = 1.306562965D * tmp12 + z5;
/* 710 */       double z3 = tmp11 * 0.707106781D;
/*     */       
/* 712 */       double z11 = tmp7 + z3;
/* 713 */       double z13 = tmp7 - z3;
/*     */       
/* 715 */       output[5][i] = (z13 + z2);
/* 716 */       output[3][i] = (z13 - z2);
/* 717 */       output[1][i] = (z11 + z4);
/* 718 */       output[7][i] = (z11 - z4);
/*     */     }
/*     */     
/* 721 */     return output;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int[] quantizeBlock(double[][] inputData, int code)
/*     */   {
/* 729 */     int[] outputData = new int[this.N * this.N];
/*     */     
/*     */ 
/* 732 */     int index = 0;
/* 733 */     for (int i = 0; i < 8; i++) {
/* 734 */       for (int j = 0; j < 8; j++)
/*     */       {
/* 736 */         outputData[index] = ((int)Math.round(inputData[i][j] * ((double[])this.Divisors[code])[index]));
/*     */         
/* 738 */         index++;
/*     */       }
/*     */     }
/*     */     
/* 742 */     return outputData;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int[] quantizeBlockExtreme(double[][] inputData, int code)
/*     */   {
/* 751 */     int[] outputData = new int[this.N * this.N];
/*     */     
/*     */ 
/* 754 */     int index = 0;
/* 755 */     for (int i = 0; i < 8; i++) {
/* 756 */       for (int j = 0; j < 8; j++) {
/* 757 */         outputData[index] = ((int)Math.round(inputData[i][j] / ((int[])this.quantum[code])[index]));
/* 758 */         index++;
/*     */       }
/*     */     }
/*     */     
/* 762 */     return outputData;
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\com\objectplanet\chart\DCT.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */