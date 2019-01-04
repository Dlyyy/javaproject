package multiplethread;

import java.util.concurrent.atomic.AtomicInteger;

public class AtomicIntegerDemo {
    public static void main(String[] args) throws InterruptedException {
        AtomicInteger atomicI =new AtomicInteger();
        int i = atomicI.decrementAndGet();
        int j = atomicI.incrementAndGet();
        int k = atomicI.addAndGet(3);

    }
}

/*
    原子性操作概念
        所谓的原子性操作即不可中断的操作，比如赋值操作

        int i = 5;


        原子性操作本身是线程安全的
        但是 i++ 这个行为，事实上是有3个原子性操作组成的。
        步骤 1. 取 i 的值
        步骤 2. i + 1
        步骤 3. 把新的值赋予i
        这三个步骤，每一步都是一个原子操作，但是合在一起，就不是原子操作。就不是线程安全的。
        换句话说，一个线程在步骤1 取i 的值结束后，还没有来得及进行步骤2，另一个线程也可以取 i的值了。
        这也是分析同步问题产生的原因 中的原理。
        i++ ，i--， i = i+1 这些都是非原子性操作。
        只有int i = 1,这个赋值操作是原子性的。

AtomicInteger
            JDK6 以后，新增加了一个包java.util.concurrent.atomic，里面有各种原子类，比如AtomicInteger。
            而AtomicInteger提供了各种自增，自减等方法，这些方法都是原子性的。
            换句话说，自增方法 incrementAndGet 是线程安全的，同一个时间，只有一个线程可以调用这个方法。*/
