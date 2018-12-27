package digit;

public class StringDemo {
    public static void main(String[] args) {
        /*字符串即字符的组合，在Java中，字符串是一个类，所以我们见到的字符串都是对象
        常见创建字符串手段：
        1. 每当有一个字面值出现的时候，虚拟机就会创建一个字符串
        2. 调用String的构造方法创建一个字符串对象
        3. 通过+加号进行字符串拼接也会创建新的字符串对象*/
//        String 被修饰为final,所以是不能被继承的
        String garen ="盖伦"; //字面值,虚拟机碰到字面值就会创建一个字符串对象

        String teemo = new String("提莫"); //创建了两个字符串对象

        char[] cs = new char[]{'崔','斯','特'};

        String hero = new String(cs);//  通过字符数组创建一个字符串对象

        String hero3 = garen + teemo;//  通过+加号进行字符串拼接

        System.out.println(garen.length());

        String sentence = "   盖伦,在进行了连续8次击杀后,获得了 超神 的称号   ";
        //根据,进行分割，得到3个子字符串
        String subSentences[] = sentence.split(",");
        for (String sub : subSentences) {
            System.out.println(sub);
        }

        //去掉首尾空格
        System.out.println(sentence.trim());


        System.out.println(sentence.indexOf('8')); //字符第一次出现的位置

        System.out.println(sentence.indexOf("超神")); //字符串第一次出现的位置

        System.out.println(sentence.lastIndexOf("了")); //字符串最后出现的位置

        System.out.println(sentence.indexOf(',',5)); //从位置5开始，出现的第一次,的位置

        System.out.println(sentence.contains("击杀")); //是否包含字符串"击杀"

        String temp = sentence.replaceAll("击杀", "被击杀"); //替换所有的

        temp = temp.replaceAll("超神", "超鬼");

        System.out.println(temp);

        temp = sentence.replaceFirst(",","");//只替换第一个

        System.out.println(temp);
    }
}
