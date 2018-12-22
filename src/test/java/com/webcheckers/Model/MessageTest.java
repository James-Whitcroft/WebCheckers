package com.webcheckers.Model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MessageTest {

    private Message messageInfo;
    private Message messageError;
    @BeforeEach
    public void setUp(){
        this.messageInfo = mock(Message.class);
        when(messageInfo.getText()).thenReturn("info");
        when(messageInfo.getType()).thenReturn(Message.TYPE.info);
        this.messageError = mock(Message.class);
        when(messageError.getText()).thenReturn("error");
        when(messageError.getType()).thenReturn(Message.TYPE.error);
    }

    @Test
    public void constructorTest(){
        Message error = new Message(Message.TYPE.error);
        assertTrue(error.getText().equals("ERROR: The player you have chosen is currently in a game, please select another."));
        assertTrue(error.getType().equals(Message.TYPE.error));

        Message normal = new Message(Message.TYPE.info);
        assertTrue(normal.getText().equals("This player is available for a game, enjoy your game!"));
        assertTrue(normal.getType().equals(Message.TYPE.info));
    }

    @Test
    public void equalsTest(){
        Message error = new Message(Message.TYPE.error);
        assertTrue(error.equals(new Message(Message.TYPE.error)));

        Message info = new Message(Message.TYPE.info);
        assertTrue(info.equals(new Message(Message.TYPE.info)));

        assertFalse(error.equals(info));
        assertFalse(error.equals("Not a message object"));
    }

    @Test
    public void getTextInfoTest(){
        assertEquals("info", messageInfo.getText());
    }

    @Test
    public void getTextErrorTest(){
        assertEquals("error", messageError.getText());
    }


    @Test
    public void getTypeInfoTest(){
        assertEquals(Message.TYPE.info, messageInfo.getType());
    }

    @Test
    public void getTypeErrorTest(){
        assertEquals(Message.TYPE.error, messageError.getType());
    }

    @Test
    public void testEqualsTrue(){
        Message message = new Message(Message.TYPE.info, "info");
        Message mess = new Message(Message.TYPE.info, "info");
        assertTrue(mess.equals(message), "These players are the same.");
    }

    @Test
    public void testEqualsFalse(){
        Message message = new Message(Message.TYPE.info, "chicken");
        Message mess = new Message(Message.TYPE.info, "turkey");
        assertFalse(mess.equals(message), "These players are the same.");
    }

    @Test
    public void testEqualsNotMessage(){
        assertFalse(messageInfo.equals(new Player("john")));
    }
    @Test
    public void testHashCode(){
        Message mess = new Message(Message.TYPE.info,"info");
        assertEquals(mess.hashCode(), Objects.hash(Message.TYPE.info,"info"));
    }
}
