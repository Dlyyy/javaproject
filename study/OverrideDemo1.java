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
			System.out.println("消费了 " + money + "w");
		}
		else{
			System.out.println("消费限额，不能超过500w");
		}
	}
}

class Rich2Man extends RichMan{
	public static void consume(int money){
		if(money < 1000){
			System.out.println("消费了 " + money + "w");
		}
		else{
			System.out.println("消费限额，不能超过1000w");
		}
	}
}
