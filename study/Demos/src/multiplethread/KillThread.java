package multiplethread;

import character.Hero2;

public class KillThread extends Thread{
    private Hero2 h1;
    private Hero2 h2;

    public KillThread(Hero2 h1, Hero2 h2){
        this.h1 = h1;
        this.h2 = h2;
    }

    public void run(){
        while(!h2.isDead()){
            h1.attackHero(h2);
        }
    }
}