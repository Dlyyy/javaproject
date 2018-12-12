class TicketDemo {
	public static void main(String[] args) 	{
		TicketMachine m = new TicketMachine();
		for(int i = 0 ; i < 50 ; i ++){
			new Person(m,"tom" + i).start(); //每个对象只用一次
		}
	}
}

//取票机
class TicketMachine{
	private int ticketNo = 1 ;
	//打印票号   同步方法要尽可能短
	public synchronized int printTicketNo(){
		int currTicketNo = ticketNo ;
		 ticketNo ++ ;
		 return currTicketNo ;
	}
}
//人
class Person extends Thread{
	private TicketMachine m ;
	private String name ;
	public Person(TicketMachine m,String name){
		this.m = m ;
		this.name = name ;
	}
	public void run(){
		int no = m.printTicketNo();
		System.out.println(name + " : " + no);
	}
}

