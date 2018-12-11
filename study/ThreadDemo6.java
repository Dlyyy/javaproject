class ThreadDemo6 {
	public static void main(String[] args) 	{
		Saler s1 = new Saler("==>老张");
		Saler s2 = new Saler("=====>老王");
		s1.start();
		s2.start();
	}
}
class Saler extends Thread{
	//票数
	static int tickets = 100 ;
	private String name ;
	public Saler(String name){
		this.name = name ;
	}
	public void run(){
		while(true){
			int tick = getTicket();
			if(tick > 0){
				System.out.println(name + " : " + tick);
			}
			else{
				return ;
			}
		}
	}

	//同步方法
	public static synchronized int getTicket(){
		//同步代码块
		int currTicket = tickets ;
		tickets -- ;
		return currTicket ;
	}
}
