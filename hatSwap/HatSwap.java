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
    public Penguin(int id, Hat hat) {
        this.id = id;
        this.hat = hat;
    }
    
    public void run() {
        System.out.println(this);
    }
    
    public String toString() {
        return "Penguin " + this.id;
    }
}

class Hat {
    private int id;
    private boolean isTaken = false;
    public Hat(int id) {
        this.id = id;
    }
    
    public void take() {
        this.isTaken = true;
    }
    
    public void put() {
        this.isTaken = false;
    }
    
    public boolean isTaken() {
        return this.isTaken;
    }
    
    public String toString() {
        return "Hat " + this.id;
    }
}