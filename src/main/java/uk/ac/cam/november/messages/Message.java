/*
    This class represents a message being sent to the message handler.
    It is immutable and very simple.
*/

package uk.ac.cam.november.messages;

public class Message {

    // Attributes
    private String mText;
    private int mPriority;

    // Methods
    public Message(String text, int priority) {
        mText = text;
        mPriority = priority;
        System.out.println("created message");
    }
    public String getText() { return mText; }
    public int getPriority() { return mPriority; }

}
