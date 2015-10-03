
public class HatSwap {
    public static final int NUM_PENGUIN = 5;
    public static void main(String[] args) throws InterruptedException{
        Hat[] hats = new Hat[NUM_PENGUIN];
        for (int i = 0; i < hats.length; i++) {
            hats[i] = new Hat(i);
        }
        
        Penguin[] penguins = new Penguin[NUM_PENGUIN];
        for (int i = 0; i < penguins.length; i++) {
            penguins[i] = new Penguin(i, hats[i]);
        }
        
        for (int i = 0; i < penguins.length; i++) {
            penguins[i].setFrontPenguin((i != penguins.length -1) ? penguins[i+1] : penguins[0]);
        }
        
        for (Penguin peng : penguins) {
            peng.start();
        }
        
        for (Penguin peng : penguins) {
            peng.join();
        }
    }
    
}

class Penguin extends Thread{
    private Hat hat;
    private int id;
    private Penguin frontPenguin;
    public Penguin(int id, Hat hat) {
        this.id = id;
        this.hat = hat;
    }
    
    
    public void setFrontPenguin(Penguin frontPenguin) {
        this.frontPenguin = frontPenguin;
    }
    
    public void run() {
        while(true) {
            try{
                hat.take();
                frontPenguin.hat.take();
                
                hat.put();
                frontPenguin.hat.put();
                
                swap();
                think();
            } catch(InterruptedException e) {
                return;
            }
        }
    }
    
    public String toString() {
        return "Penguin " + this.id;
    }
    
    private void think() throws InterruptedException {
        sleep(500);
        System.out.println(this + " is thinking...");
    }
    
    private void swap() throws InterruptedException {
        System.out.println(this + " is swapping with " + frontPenguin);
        Hat temp = frontPenguin.hat;
        frontPenguin.hat = this.hat;
        this.hat = temp;
    }
    
}

class Hat {
    private int id;
    private boolean isTaken;
    public Hat(int id) {
        this.id = id;
        this.isTaken = false;
    }
    
    public synchronized void take() {
        while (isTaken()) {
            try {
                wait();
            } catch (InterruptedException e) {
            }
        }
        isTaken = true;
    }
    
    public synchronized void put() {
        isTaken = false;
        notifyAll();
    }
    
    public boolean isTaken() {
        return isTaken;
    }
    
    public String toString() {
        return "Hat " + id;
    }
}