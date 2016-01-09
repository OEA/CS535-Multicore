import java.util.concurrent.atomic.AtomicMarkableReference;
/**
 * Created by omer on 09/01/16.
 */

public class ReaderAndWriterSpinningLock {
    int readers;
    boolean writer;
    ReaderLock readerLock;
    WriterLock writerLock;
    AtomicMarkableReference<Integer> reference;
    
    public ReaderAndWriterSpinningLock () {
        readers = 0;
        writer = false;
        reference = new AtomicMarkableReference<Integer>(readers, writer);
        readerLock = new ReaderLock(reference);
        writerLock = new WriterLock(reference);
    }
    
    public void readLock() {
        readerLock.readLock();
    }
    
    public readUnlock() {
        readerLock.readUnlock();
    }
    
    public void writeLock() {
        writerLock.writeLock();
    }
    public void writeUnlock() {
        writerLock.writeUnlock();
    }
    
}

public class ReaderLock {
    
    AtomicMarkableReference<Integer> reference;
    public ReaderLock(AtomicMarkableReference<Integer> reference) {
        this.reference = reference;
    }
    
    public void readLock() {
        while(!reference.compareAndSet(reference.getReference(), reference.getReference()+1, false, false)) {}
    }
    public void readUnlock() {
        while(!reference.compareAndSet(reference.getReference(), reference.getReference()-1, false, false)) {}
    }
}

public class WriterLock {
    AtomicMarkableReference<Integer> reference;
    public WriterLock(AtomicMarkableReference<Integer> reference) {
        this.reference = reference;
    }
    public void writeLock() {
        while(!reference.compareAndSet(0, 0, false, true)) {}
    }
    public void writeUnlock() {
        while(!reference.compareAndSet(0, 0, true, false)) {}
    }
}


