class ExceptionDemo2{
	public static void main(String[] args) 	{
		Person p = new Person();
		try{
			p.setAge(500);
		}
		catch(AgeTooBigException e){
			e.printlnError();
		}
		catch(Throwable e){
			e.printStackTrace();
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
}
//
class Person{
	private int age ;
	public int getAge(){
		return age ;
	}
	//����,�����׳��쳣�ķ���
	public void setAge(int age) throws AgeTooBigException{
		if(age > 200){
			//�׳��쳣
			throw new AgeTooBigException();
		}
		this.age = age ;
	}
}
//�����쳣
class AgeTooBigException extends Exception{
	private String info ;
	public AgeTooBigException(String info){
		this.info = info ;
	}
	public AgeTooBigException() {
		this("����̫�󣡣�");
	}

	public void printlnError(){
		System.out.println(info);
	}
}

/**
	1.�����쳣
	  class xxx extends Exception{...}

	2.ʹ���쳣
	  throw new MyException();

	3.�����쳣
	  a.try-catch-finally
	  b.�����׳�
 */