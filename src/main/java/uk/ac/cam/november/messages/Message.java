package uk.ac.cam.november.messages;

/**
 * A simple class that represents messages from the central system,
 * formatted in a way that can be used by {@link MessageHandler} facilities.
 * Its objects are immutable and very simple.
*/
public class Message {

    // ATTRIBUTES

    private String mText;
    private int mPriority;

    // METHODS

    /**
     * Constructs a {@link Message} object given its text and priority.
     *
     * @param   text        a {@code String} that represents the contents of
     *                      the message, will potentially be read out loud
     * @param   priority    a numeric value of the priority; by convention,
     *                      should be a positive integer not greater than 100
     */
    public Message(String text, int priority) {
        mText = text;
        mPriority = priority;
    }

    /**
     * Returns the contents of the {@link Message} object.
     * @return  contents of the {@link Message} object.
     */
    public String getText() { return mText; }

    /**
     * Returns the priority of the {@link Message} object.
     * @return  priority of the {@link Message} object.
     */
    public int getPriority() { return mPriority; }

}
