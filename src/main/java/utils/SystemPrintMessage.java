package utils;

public class SystemPrintMessage extends MessagePrintBase{

    public SystemPrintMessage(){}

    public void run(){
        while (true){
            String message = this.readMessage();
            if (message != null) {
                System.out.println(message);
            }
        }
    }

}
