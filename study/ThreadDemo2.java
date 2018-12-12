class ThreadDemo2 {
	public static void main(String[] args) 	{
		Player p1 = new Player("����",5000);
		Player p2 = new Player("����",8000);
		Player p3 = new Player("ʷ̩��",2000);
		Player p4 = new Player("��С��",3000);
		//
		try{
			p1.start();
			p2.start();
			p3.start();
			p4.start();

			p1.join();  //��p1ִ�����ټ�����������  join�����������쳣 Ҫ��try catch
			p2.join();
			p3.join();
			p4.join();
		}catch(Exception e){
		}
		System.out.println("�˵����ˣ����֣���");
	}
}
//���.
class Player extends Thread{
	private String name ;
	private int time ;
	public Player(String name,int time){
		this.name = name ;
		this.time = time ;
	}

	public void run(){
		System.out.println("���:" + name + " �����ˣ�");
		try{
			//�õ�ǰ�߳�����time������
			Thread.sleep(time);
		}
		catch(Exception e){
		}
		System.out.println("���:" + name + ":" + time +  " ���ˣ�");
	}
}