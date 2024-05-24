package zad1;

public class Main {
    public static void main(String[] args) {
        String topicName = "chat_topic";
        String factoryName = "ConnectionFactory";
        ChatApp usr1 = new ChatApp(topicName, factoryName,"usr1", "usr1");
        ChatApp usr2 = new ChatApp(topicName, factoryName,"usr2", "usr2.");
        ChatApp usr3 = new ChatApp(topicName, factoryName,"usr3", "usr3");
    }
}
