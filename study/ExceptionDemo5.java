class ExceptionDemo5 {
	public static void main(String[] args) 	{
	}
}

class Person{
	public int age ;
	public void setAge(int age) throws AgeInvalidException{
		if(age < 0 || age > 200){
			throw new AgeTooBigException();
		}
		this.age = age ;
	}
}

//всюЮ
class Chinese extends Person{
	public void setAge(int age) throws AgeTooSmallException{
		if(age < 0 ){
			throw new AgeTooSmallException();
		}
		this.age = age ;
	}
}

class AgeInvalidException extends Exception{
}
class AgeTooBigException extends AgeInvalidException{
}
class AgeTooSmallException extends AgeInvalidException{
}

