package fr.ensibs.player;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.jms.TextMessage;

public class TextListener implements MessageListener {
    private Player player;
    public TextListener(Player player){
        this.player=player;
    }

    @Override
    public void onMessage(Message m) {
        try{
            if(m instanceof TextMessage){
                TextMessage tMessage = (TextMessage) m;
                System.out.println(tMessage.getText());
            }else{
                System.out.println("Message is not an ObjectMessage ");
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
