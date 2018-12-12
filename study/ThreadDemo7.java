class ThreadDemo7 {
	public static void main(String[] args) {
		TicketPool pool = new TicketPool();
		Saler s1 = new Saler("Marry" , pool);
		Saler s2 = new Saler("======>john" , pool);
		s1.start();
		s2.start();
	}
}

//票池
class TicketPool{
	private int tickets = 100 ;
	//从票池取票   静态同步方法
	public synchronized int getTicket(){
		int ticket = tickets ;
		tickets -- ;
		return ticket ;
	}
}
//售票员
class Saler extends Thread{
	private TicketPool pool ; //不要接着new一个pool
	private String name ;
	public Saler(String name , TicketPool pool){
		this.name = name ;
		this.pool = pool ;
	}

	public void run(){
		while(true){
			int no = pool.getTicket();
			if(no > 0){
				System.out.println(name + " : " + no);
			}
			else{
				return ;
			}
		}
	}
}

