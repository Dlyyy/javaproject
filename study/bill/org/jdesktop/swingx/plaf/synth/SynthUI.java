package org.jdesktop.swingx.plaf.synth;

import java.awt.Graphics;
import javax.swing.JComponent;
import javax.swing.plaf.synth.SynthContext;

public abstract interface SynthUI
{
  public abstract SynthContext getContext(JComponent paramJComponent);
  
  public abstract void paintBorder(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4);
}


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\plaf\synth\SynthUI.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */