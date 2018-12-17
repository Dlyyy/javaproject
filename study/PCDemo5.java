class PCDemo5{  //死锁
	public static void main(String[] args){
		//使用java中集合类，List是列表。
		Pool pool = new Pool();
		Productor p1 = new Productor("生产者1",pool);
		p1.setName("p1");
		Consumer c1 = new Consumer("消费者",pool);
		c1.setName("c1");
		Consumer c2 = new Consumer("消费者",pool);
		c2.setName("c2");
		p1.start();
		c1.start();
		c2.start();
	}
}
//生产者
class Productor extends Thread{
	static int i = 0 ;  //静态的
	private String name ;
	private Pool pool ;
	public Productor(String name ,Pool pool){
		this.name = name ;
		this.pool = pool ;
	}
	public void run(){   
		while(true){    //死循环  一直加
			pool.add(i ++);
		}
	}
}
//消费者
class Consumer extends Thread{
	private String name ;
	private Pool pool ;
	public Consumer(String name ,Pool pool){
		this.name = name ;
		this.pool = pool;
	}
	public void run(){
		while(true){
			pool.remove();
			//System.out.println("-: " + i);
		}
	}
}

class Pool{
	private java.util.List<Integer> list = new java.util.ArrayList<Integer>();
	//容器最大值
	private int MAX = 1 ;
	//添加元素
	public void add(int n){
		synchronized(this){  //以当前对象list作为锁   也可把上下的this都换为list
			try{
				String name = Thread.currentThread().getName();
				while(list.size() == MAX){
					System.out.println(name + ".wait()");
					this.wait();  //调用的是锁的等待方法
				}
				list.add(n);
				System.out.println(name + " + : " + n);
				System.out.println(name + ".notify()");
				this.notifyAll();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	//删除元素
	public int remove(){
		synchronized(this){
			try{
				String name = Thread.currentThread().getName();
				while(list.size() == 0){
					System.out.println(name + ".wait()");
					this.wait();
				}
				int i = list.remove(0);
				System.out.println(name + " - : "  + i);
				System.out.println(name + ".notify()");
				this.notifyAll();
				return i ;
			}
			catch(Exception e){
				e.printStackTrace();
			}
			return -1 ;
		}
	}
}
