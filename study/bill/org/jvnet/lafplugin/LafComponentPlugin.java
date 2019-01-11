package org.jvnet.lafplugin;

public abstract interface LafComponentPlugin
  extends LafPlugin
{
  public static final String COMPONENT_TAG_PLUGIN_CLASS = "component-plugin-class";
  
  public abstract void initialize();
  
  public abstract void uninitialize();
  
  public abstract Object[] getDefaults(Object paramObject);
}


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jvnet\lafplugin\LafComponentPlugin.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       0.7.1
 */