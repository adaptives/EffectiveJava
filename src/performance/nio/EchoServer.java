package performance.nio;

/**
 * A server which echos back the text that is sent to it.
 * 
 * The server considers a single line of text, terminating with '\n' as a request and echos back this line.
 */
public interface EchoServer {
    void start();
    void stop();
}
