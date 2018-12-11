class StaticDemo1{
	public static void main(String[] args) {
		//Benz b1 = new Benz();
		System.out.println(Benz.getBrand());
	}
}

class Benz{
	//static,
	private static String brand = "BENZ" ;
	private String color ;

	public static String getBrand(){
		//System.out.println(this.color);
		return brand ;
	}

	public void setColor(String color){
		this.color = color ;
	}
}
