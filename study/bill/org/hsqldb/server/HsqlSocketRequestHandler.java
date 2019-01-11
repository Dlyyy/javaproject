package org.hsqldb.server;

import java.net.Socket;

public abstract interface HsqlSocketRequestHandler
{
  public abstract void handleConnection(Socket paramSocket);
  
  public abstract void signalCloseAllServerConnections();
}


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\hsqldb\server\HsqlSocketRequestHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */