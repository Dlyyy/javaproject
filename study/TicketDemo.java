class TicketDemo {
	public static void main(String[] args) 	{
		TicketMachine m = new TicketMachine();
		for(int i = 0 ; i < 50 ; i ++){
			new Person(m,"tom" + i).start(); //ÿ������ֻ��һ��
		}
	}
}

//ȡƱ��
class TicketMachine{
	private int ticketNo = 1 ;
	//��ӡƱ��   ͬ������Ҫ�����ܶ�
	public synchronized int printTicketNo(){
		int currTicketNo = ticketNo ;
		 ticketNo ++ ;
		 return currTicketNo ;
	}
}
//��
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

