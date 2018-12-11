class ThreadDemo6 {
	public static void main(String[] args) 	{
		Saler s1 = new Saler("==>����");
		Saler s2 = new Saler("=====>����");
		s1.start();
		s2.start();
	}
}
class Saler extends Thread{
	//Ʊ��
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

	//ͬ������
	public static synchronized int getTicket(){
		//ͬ�������
		int currTicket = tickets ;
		tickets -- ;
		return currTicket ;
	}
}
