class ThreadDemo3 {
	public static void main(String[] args) 	{
		Room r1 = new Room("no1",5000);
		Waiter w = new Waiter();
		//�����߳�Ϊ�ػ��߳�
		//w.setDaemon(true);
		r1.start();
		w.start();
	}
}
class Room extends Thread{
	private String no ;
	private int time ;
	public Room(String no,int time){
		this.no = no ;
		this.time = time ;
	}
	public void run(){
		System.out.println(no + "�ŷ������ڳ���!");
		try{
			Thread.sleep(time);
		}
		catch(Exception e){
		}
		System.out.println(no + "�ŷ������ˣ�������!");
	}
}
//����Ա
class Waiter extends Thread{
	public Waiter(){
		this.setDaemon(true);
	}
	public void run(){
		while(true){
			System.out.println(new java.util.Date());  //ÿ��һ���ӱ�һ��ʱ��
			try{
				Thread.sleep(1000);
			}
			catch(Exception e){
			}
		}
	}
}
