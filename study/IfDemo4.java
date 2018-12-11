class IfDemo4{
	public static void main(String[] args){
		int a = 3 , b = 2 ;
		int max = a ;
		//1.if() {}
		if(a < b){
			max = b ;
			System.out.println("Hello World") ;
		}
		System.out.println(max);

		//2.if(...){...}else{...}
		
		//3.if () {} else if(..){...}else{...}

		int age = 80 ;
		String year = "";
		if(age > 0 && age < 18){
			year = "少年" ;
		}
		else if(age >= 18 && age < 50) {
			year = "中年" ;
		}
		else if(age >= 50 && age < 130){
			year = "老年" ;
		}
		else{
			year = "年龄错误" ;
		}
		System.out.println(year);
	}
	

}