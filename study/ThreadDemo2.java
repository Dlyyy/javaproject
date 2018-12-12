class ThreadDemo2 {
	public static void main(String[] args) 	{
		Player p1 = new Player("成龙",5000);
		Player p2 = new Player("狄龙",8000);
		Player p3 = new Player("史泰龙",2000);
		Player p4 = new Player("李小龙",3000);
		//
		try{
			p1.start();
			p2.start();
			p3.start();
			p4.start();

			p1.join();  //等p1执行完再继续往下运行  join方法定义了异常 要加try catch
			p2.join();
			p3.join();
			p4.join();
		}catch(Exception e){
		}
		System.out.println("人到齐了，开局！！");
	}
}
//玩家.
class Player extends Thread{
	private String name ;
	private int time ;
	public Player(String name,int time){
		this.name = name ;
		this.time = time ;
	}

	public void run(){
		System.out.println("玩家:" + name + " 出发了！");
		try{
			//让当前线程休眠time毫秒数
			Thread.sleep(time);
		}
		catch(Exception e){
		}
		System.out.println("玩家:" + name + ":" + time +  " 到了！");
	}
}