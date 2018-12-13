class PCDemo3 {
	public static void main(String[] args) 	{
		//创建集合
		MyList myList = new MyList();
		Productor p = new Productor(myList);
		Consumer c = new Consumer(myList);
		p.start();
		c.start();
	}
}

//集合
class MyList{
	private int MAX = 100 ;
	private java.util.List<Integer> list = new java.util.LinkedList<Integer>();
	public void addLast(Integer i){
		while(true){
			synchronized(list){ //加上同步方法  用仓库作锁
				if(list.size() < MAX){
					list.add(i);
					System.out.println("p.Size : " + list.size());
					return ;
				}
			}
		}
	}
	//删除第一个元素
	public Integer removeFirst(){
		while(true){
			synchronized(list){
				if(!list.isEmpty()){
					System.out.println("c.Size : " + list.size());
					return list.remove(0) ;
				}
			}
			try{
				Thread.sleep(2000);
			}
			catch(Exception e){
			}
		}
	}
}

//生产者
class Productor extends Thread{
	private MyList myList ;
	public Productor(MyList myList){
		this.myList = myList ;
	}
	public void run(){
		int i = 1 ;
		while(true){
			myList.addLast(i);
			System.out.println("P : " + i);
			i ++ ;
		}
	}
}
//消费者
class Consumer extends Thread{
	private MyList myList ;
	public Consumer(MyList myList){
		this.myList = myList ;
	}
	public void run(){
		while(true){
			int no = myList.removeFirst();
			System.out.println("C : " + no);
		}
	}
}