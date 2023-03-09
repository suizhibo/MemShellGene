package utils;


import javafx.scene.control.TextArea;

import java.io.UnsupportedEncodingException;

public class TextAreaPrintMessage extends MessagePrintBase{

    private TextArea jTextArea;

    public TextAreaPrintMessage(TextArea jTextArea){
        this.jTextArea = jTextArea;
    }

    public void run(){
        while (true){
            String message = this.readMessage();
            if (message != null) {
                try {
                    this.jTextArea.appendText(new String(message.getBytes("UTF-8"), "utf-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
