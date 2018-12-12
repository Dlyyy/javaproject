class ThreadDemo4 {
	public static void main(String[] args) 	{
		Saler s1 = new Saler("老张");
		Saler s2 = new Saler("老王");
		s1.start();
		s2.start();
	}
}

class Saler extends Thread{
	//锁
	static Object lock = new Object(); //任何对象都可以作为锁。 不用Object 用String啥的都行
	//票数
	static int tickets = 100 ; //定义为static  因为静态跟对象无关
	private String name ;
	public Saler(String name){
		this.name = name ;
	}

	public void run(){
		synchronized(lock){
			while(tickets > 0){
				int currTicket = tickets ;
				tickets = tickets - 1 ;
				System.out.println(name  + " : " + currTicket);
			}
		}
	}
}
