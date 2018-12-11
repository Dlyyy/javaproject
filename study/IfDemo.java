class IfDemo{
	public static void main(String[] args){
		int age = 100 ;
		if(age <= 50){
			System.out.println("小");
		}
		else{
			System.out.println("大");
		}
		//
		System.out.println(age <= 50 ? "小" : "大");
		int a = 1 , b = 2 , c = 3 ;
		int max = 0;
		if(a > b){
			if(a > c){
				max = a ;
			}
			else{
				max = c ;
			}
		}
		else{
			if(b > c){
				max = b ;
			}
			else{
				max = c ;
			}
		}
		System.out.println("max is " + max);
	}
}