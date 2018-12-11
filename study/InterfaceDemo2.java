/**
 * 接口
 */
class InterfaceDemo2 {
	public static void main(String[] args) 	{
		TuHao laowang = new TuHao();
		WomanStar fbb = new WomanStar();
		laowang.marry(fbb);
	}
}
class TuHao{
	public void marry(WRB w){
		w.veryWhite();
		w.hasMoney();
		w.beau();
		System.out.println(" 不错~~~");
	}
}
interface White{
	public void veryWhite();
}
interface Rich{
	public void hasMoney();
}
interface Beautiful{
	public void beau();
}
//接口继承
interface WRB extends White,Rich,Beautiful{
}
//女明星
class WomanStar implements White,Rich,Beautiful{
	public void veryWhite(){
		System.out.println("很白~~");
	}
	public void hasMoney(){
		System.out.println("很有钱");
	}
	public void beau(){
		System.out.println("很漂亮");
	}
}

/**
 * 练习：
 定义类Anmial是抽象类，Dog继承Animal，Jing8继承Dog
 定义接口Pet,里面有meng()的方法。
 定义Person类，其中有buyPet(...)方法,可以购买宠物。
 提示:class C extends A implements B,D,...{
 }
 */
