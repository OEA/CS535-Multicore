import java.util.concurrent.locks.ReentrantLock;

// Göktuğ Berk Ulu - Ömer Emre Aslan

class Sign {
    private char letter;
    public Sign next;
    private ReentrantLock rLock = new ReentrantLock();
    
    public Sign(char c) {
        // creating a letter sign is not cheap
        try {
            Thread.sleep(10);
        } catch(InterruptedException e) {}
        letter = c;
    }
    
    public char getLetter() {
        // reading the letter sign is not cheap
        try {
            Thread.sleep(2);
        } catch(InterruptedException e) {}
        return letter;
    }
    
    public void lock(){
        rLock.lock();
    }
    
    public void unlock() {
        rLock.unlock();
    }
}

class Billboard {
    private Sign head = new Sign('0'); // sentinel node
    
    public void write(String message) {
        Sign previous = head;
        Sign current = head.next;
        previous.lock();
        if(current != null)
            current.lock();
        for(int i=0; i < message.length(); i++) {
            Sign newSign = new Sign(message.charAt(i));
            if (current != null)
                newSign.next = current.next;
            previous.next = newSign;
            previous.unlock();
            previous = newSign;
            previous.lock();
            if(current != null)
                current.unlock();
            current = newSign.next;
            if(current != null)
                current.lock();
        }
        previous.unlock();
    }
    
    public String read() {
        Sign current = head.next;
        
        String message = "";
        while(current != null) {
            current.lock();
            message += current.getLetter();
            current.unlock();
            current = current.next;
        }
        return message;
    }
}

class Writer extends Thread {
    private Billboard bb;
    
    public Writer(Billboard bb) {
        this.bb = bb;
    }
    
    public void run() {
        while(true) {
            bb.write("WASH THE DOG");
            try {
                sleep(50);
            } catch(InterruptedException e) {}
            bb.write("SELL THE CAR");
            try {
                sleep(50);
            } catch(InterruptedException e) {}
        }
    }
}

class Reader extends Thread {
    private Billboard bb;
    
    public Reader(Billboard bb) {
        this.bb = bb;
    }
    
    public void run() {
        while(true) {
            String message = bb.read();
            System.out.println("Message: " + message);
            try {
                sleep(50);
            } catch(InterruptedException e) {}
        }
    }
}

public class BillboardTest {
    public static void main(String[] args) throws InterruptedException {
        Billboard bb = new Billboard();
        
        // Sequential test
        bb.write("WASH THE DOG");
        System.out.println("Message: " + bb.read());
        bb.write("SELL THE CAR");
        System.out.println("Message: " + bb.read());
        System.out.println();
        
        // Parallel test
        Writer writer1 = new Writer(bb);
        Reader reader1 = new Reader(bb);
        Reader reader2 = new Reader(bb);
        writer1.start();
        reader1.start();
        reader2.start();
        
        writer1.join();
        reader1.join();
        reader2.join();
    }
}
