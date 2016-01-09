import java.util.concurrent.*;

/**
 * Created by omer on 09/01/16.
 */

class ArraySum {
    ExecutorService executorService = Executors.newCachedThreadPool();

    public int sum(int[] a) throws Exception {
        Future<Integer> future = executorService.submit(new Task(a));
        return future.get();
    }

    class Task implements Callable<Integer> {
        int[] array;
        int[] leftArray;
        int[] rightArray;
        int halfIndex;
        int size;

        public Task(int[] array) {
            this.array = array;
            size = array.length;
            halfIndex = size / 2;
            leftArray = new int[halfIndex];
            rightArray = new int[size - halfIndex];
            addElementsToArray();
        }

        public void addElementsToArray() {
            for (int i = 0; i < size; i++) {
                if (i < halfIndex)
                    leftArray[i] = array[i];
                else
                    rightArray[i-halfIndex] = array[i];
            }
        }

        public Integer call() throws Exception {
            if (array.length == 1) {
                return array[0];
            } else {
                Future<Integer> leftPartSum = executorService.submit(new Task(leftArray));
                Future<Integer> rightPartSum = executorService.submit(new Task(rightArray));
                return leftPartSum.get() + rightPartSum.get();
            }
        }
    }
}
