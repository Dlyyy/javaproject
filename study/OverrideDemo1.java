class OverrideDemo1 {
	public static void main(String[] args) {
		Rich2Man s2 = new Rich2Man();
		//s2.consume(800);
	}
}
//
class RichMan{
	static void consume(int money){
		if(money < 500){
			System.out.println("������ " + money + "w");
		}
		else{
			System.out.println("�����޶���ܳ���500w");
		}
	}
}

class Rich2Man extends RichMan{
	public static void consume(int money){
		if(money < 1000){
			System.out.println("������ " + money + "w");
		}
		else{
			System.out.println("�����޶���ܳ���1000w");
		}
	}
}
