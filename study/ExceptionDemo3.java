class ExceptionDemo3 {
	public static void main(String[] args) throws Exception	{
		Person p = new Person();
		p.setAge(20000);
		//处理异常
		/**
		try{
			p.setAge(45);
		}
		catch(AgeTooBigException e){
			e.printError();
		}
		catch(AgeTooSmallException e){
			e.printError();
		}
		catch(AgeInvalidException e){
			e.printError();
		}
		*/
	}
}

class Person{
	private int age ;
	public int getAge(){
		return age ;
	}
	//使用异常
	public void setAge(int age) throws AgeTooBigException,
									AgeTooSmallException,
									AgeInvalidException {
		if(age > 200){
			AgeTooBigException e = new AgeTooBigException() ;
			throw e;
		}
		else if( age == 0){
			throw new AgeTooSmallException();
		}
		else if(age < 0){
			throw new AgeInvalidException("年龄非法");
		}
		this.age = age ;
	}
}
//年龄非法异常
class AgeInvalidException extends Exception{
	private String info ;
	public AgeInvalidException(String info){    //构造函数
		this.info = info ;
	}
	public void printError(){
		System.out.println(info);
	}
}
//年龄太大异常
class AgeTooBigException extends AgeInvalidException{
	public AgeTooBigException(String info){
		super(info);
	}
	public AgeTooBigException(){
		this("年龄太大");
	}
}
//年龄太小异常
class AgeTooSmallException extends AgeInvalidException{
	public AgeTooSmallException(String info){
		super(info);
	}
	public AgeTooSmallException(){
		this("年龄太小");
	}
}

