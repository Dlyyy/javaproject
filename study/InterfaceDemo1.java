class InterfaceDemo1 {
	public static void main(String[] args) 	{
		PC pc = new PC();
		Mouse m = new Mouse();
		pc.insertUSB(m);
	}
}
class PC{
	public void insertUSB(USB usb){           //usb是形参，不知道具体对象
		System.out.println("插入了usb设备");
		usb.play();
	}
}
//定义接口，接口中定义了抽象方法，要在具体类中实现
interface USB{
	void play() ;
}
//类实现了接口
class Mouse implements USB{
	public void play(){
		System.out.println("鼠标滑动");
	}
}


Computer
-------------
	