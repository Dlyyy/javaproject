class ThreadDemo4 {
	public static void main(String[] args) 	{
		Saler s1 = new Saler("����");
		Saler s2 = new Saler("����");
		s1.start();
		s2.start();
	}
}

class Saler extends Thread{
	//��
	static Object lock = new Object(); //�κζ��󶼿�����Ϊ���� ����Object ��Stringɶ�Ķ���
	//Ʊ��
	static int tickets = 100 ; //����Ϊstatic  ��Ϊ��̬�������޹�
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
