package networking;

public class NetworkHandlerSingleton {

    private Subscriber subscriber;

    private NetworkHandlerSingleton() {
        subscriber = new Subscriber();
    }

    private static NetworkHandlerSingleton instance;

    public static NetworkHandlerSingleton getInstance() {
        if (instance == null) {
            instance = new NetworkHandlerSingleton();
        }
        return instance;
    }

    public void initialize() {
        subscriber.initialize();
    }

}
