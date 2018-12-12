/**
 * 线程
 */
class ThreadDemo1{
	public static void main(String[] args) 	{		
		MyThread t1 = new MyThread();
		YourThread t2 = new YourThread();
		//启动线程
		t1.run();			//若start改为run  只有一个1个线程. 
		                    //在主线程中运行 不会调t2 必须都为start t1 t2才会都执行
		t2.start();
	}
}

class MyThread extends Thread{
	//执行线程的时候进行调用,jvm负责调用
	public void run(){
		while(true){
			System.out.println("myThread");
			yield();		//放弃cpu的抢占权，若为单核一个程序执行一次
		}
	}
}
//
class YourThread extends Thread{
	public void run(){
		while(true){
			System.out.println("yourThread");
			yield();
		}
	}
}
