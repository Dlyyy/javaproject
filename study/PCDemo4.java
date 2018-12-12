class PCDemo4 {
	public static void main(String[] args) 	{
		//创建集合
		MyList myList = new MyList();
		Productor p1 = new Productor(myList);
		Productor p2 = new Productor(myList);
		Productor p3 = new Productor(myList);

		Consumer c1 = new Consumer(myList);
		//Consumer c2 = new Consumer(myList);

		p1.start();
		p2.start();
		p3.start();
		c1.start();
		//c2.start();
	}
}

//集合
class MyList{
	private int MAX = 1 ;
	private java.util.List<Integer> list = new java.util.LinkedList<Integer>();
	public synchronized void addLast(Integer i){
		while(list.size() >= MAX){
			try{
				wait(); //进入等待队列
			}
			catch(Exception e){
			}
		}
		list.add(i);
		System.out.println("add.size : " + list.size());
		notify();
	}
	//删除第一个元素
	public synchronized Integer removeFirst(){
		while(list.size() == 0){
			try{
				wait();
			}
			catch(Exception e){
			}
		}
		notify();  //通知
		int no = list.remove(0);
		System.out.println("remove.size : " + list.size());
		return no ;
	}
}

//生产者
class Productor extends Thread{
	static int i = 1 ;
	private MyList myList ;
	public Productor(MyList myList){
		this.myList = myList ;
	}
	public void run(){
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