class ExceptionDemo3 {
	public static void main(String[] args) throws Exception	{
		Person p = new Person();
		p.setAge(20000);
		//�����쳣
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
	//ʹ���쳣
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
			throw new AgeInvalidException("����Ƿ�");
		}
		this.age = age ;
	}
}
//����Ƿ��쳣
class AgeInvalidException extends Exception{
	private String info ;
	public AgeInvalidException(String info){    //���캯��
		this.info = info ;
	}
	public void printError(){
		System.out.println(info);
	}
}
//����̫���쳣
class AgeTooBigException extends AgeInvalidException{
	public AgeTooBigException(String info){
		super(info);
	}
	public AgeTooBigException(){
		this("����̫��");
	}
}
//����̫С�쳣
class AgeTooSmallException extends AgeInvalidException{
	public AgeTooSmallException(String info){
		super(info);
	}
	public AgeTooSmallException(){
		this("����̫С");
	}
}

