class MultiStateDemo2{
	public static void main(String[] args) 	{
		WomanStar fbb = new WomanStar();
		White w = fbb ;
		Rich r = fbb ;
		
		Beautiful b = (Beautiful)w ;
		b.beau();//得到“很漂亮”

		//Dog d = (Dog)b ;
		WRB wrb = (WRB)b ;

		White www = wrb ;


	}
}

interface White{
	public void veryWhite();
}
interface Rich{
	public void hasMoney();
 interface Beautiful{
	public void beau();
}
//接口继承
interface WRB extends White,Rich,Beautiful{
}
//女明星
class WomanStar implements WRB{
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

class Dog{
}