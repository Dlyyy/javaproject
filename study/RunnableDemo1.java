/**
 * �������
 */
class RunnableDemo1 {
	public static void main(String[] args)	{
		MyRunnable r = new MyRunnable();
		new Thread(r).start();
	}
}

//runnableʵ����
class MyRunnable implements Runnable{
	public void run(){
		while(true){
			System.out.println("hello world");
		}
	}
}
