class PCDemo2 {
	public static void main(String[] args) 	{
		//��������
		java.util.List<Integer> list = new java.util.LinkedList<Integer>();
		Productor p = new Productor(list);
		Consumer c = new Consumer(list);
		p.start();
		c.start();
	}
}
//������   �̳��߳̾�Ҫ����run����
class Productor extends Thread{
	private java.util.List<Integer> list ;
	public Productor(java.util.List<Integer> list){  //���캯��
		this.list = list ;
	}
	public void run(){
		int i = 1 ;
		while(true){
			list.add(new Integer(i));
			System.out.println("Productor : added " + i);
			i ++ ;
			yield();
		}
	}
}
//������
class Consumer extends Thread{
	private java.util.List<Integer> list ;
	public Consumer(java.util.List<Integer> list){
		this.list = list ;
	}
	public void run(){
		while(true){
			if(!list.isEmpty()){
				int no = list.remove(0);
				System.out.println("Consumer : " + no);
				try{
					Thread.sleep(2000);
				}
				catch(Exception e){
				}
			}
		}
	}
}