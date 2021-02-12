package control;

public interface HaikuListener {

    void notifyMessage(String message);
    void notifyShutdown();

}
