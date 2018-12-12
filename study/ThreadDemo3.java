class ThreadDemo3 {
	public static void main(String[] args) 	{
		Room r1 = new Room("no1",5000);
		Waiter w = new Waiter();
		//设置线程为守护线程
		//w.setDaemon(true);
		r1.start();
		w.start();
	}
}
class Room extends Thread{
	private String no ;
	private int time ;
	public Room(String no,int time){
		this.no = no ;
		this.time = time ;
	}
	public void run(){
		System.out.println(no + "号房间正在唱歌!");
		try{
			Thread.sleep(time);
		}
		catch(Exception e){
		}
		System.out.println(no + "号房间买单了，走人了!");
	}
}
//服务员
class Waiter extends Thread{
	public Waiter(){
		this.setDaemon(true);
	}
	public void run(){
		while(true){
			System.out.println(new java.util.Date());  //每隔一秒钟报一次时间
			try{
				Thread.sleep(1000);
			}
			catch(Exception e){
			}
		}
	}
}
