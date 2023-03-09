package utils;

public class MessagePrintBase implements Runnable {

    public String readMessage() {
        return Util.messageQueue.poll();
    }

    @Override
    public void run() {

    }
}
