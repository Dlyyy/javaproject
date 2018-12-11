class ThreadDemo5 {
	public static void main(String[] args) 	{
		Saler s1 = new Saler("==>����");
		Saler s2 = new Saler("=====>����");
		Saler s3 = new Saler("===========>����");
		s1.start();
		s2.start();
		s3.start();
	}
}
class Saler extends Thread{
	//��
	static Object lock = new Object();
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
	//ȡƱ
	public int getTicket(){
		//ͬ�������
		synchronized(lock){
			int currTicket = tickets ;
			tickets -- ;
			return currTicket ;
		}
	}
}
