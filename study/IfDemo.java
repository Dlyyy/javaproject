class IfDemo{
	public static void main(String[] args){
		int age = 100 ;
		if(age <= 50){
			System.out.println("С");
		}
		else{
			System.out.println("��");
		}
		//
		System.out.println(age <= 50 ? "С" : "��");
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