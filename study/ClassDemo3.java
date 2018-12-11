class ClassDemo3 {
	public static void main(String[] args) 	{
		Man man = new Man();
		man.addMoney(-200);
		man.huabei(3);
		System.out.println(man.getMoney());

	}
}

class Man{
	private int money = 1000;

	public int getMoney(){
		return money ;
	}

	public void addMoney(int amount){
		int temp = 100 ;
		if(amount > 0){
			money = money + amount ;
		}
	}
	//
	private void huabei(int amount){
		if(amount < 5 && amount <= money){
			money = money - amount ;
		}
	}
}
