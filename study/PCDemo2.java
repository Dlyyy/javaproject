class PCDemo2 {
	public static void main(String[] args) 	{
		//创建集合
		java.util.List<Integer> list = new java.util.LinkedList<Integer>();
		Productor p = new Productor(list);
		Consumer c = new Consumer(list);
		p.start();
		c.start();
	}
}
//生产者   继承线程就要加上run函数
class Productor extends Thread{
	private java.util.List<Integer> list ;
	public Productor(java.util.List<Integer> list){  //构造函数
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
//消费者
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