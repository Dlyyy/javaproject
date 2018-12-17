/**
 * 降低耦合
 */
class RunnableDemo2 {
	public static void main(String[] args)	{
		//匿名内部类对象
		/*
		new Thread(new Runnable(){    //runnable是个接口 需要大括号加方法实现
			public void run(){
				System.out.println("hello world");
			}
		}).start();
		*/

		new Thread(){
			public void run(){
				print();
			}

			private void print(){
				System.out.println("hello world");
			}
		}.start();

		//MyThread d = new MyThread();
		//d.start();
	}
}

class MyThread extends Thread{
	public void run(){
		System.out.println("hello world");
	}
}