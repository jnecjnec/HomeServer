package src;

public class Main {

    public static void main(String[] args) throws Exception {

        HomeServer homeServer = new HomeServer(Integer.parseInt(System.getProperty("clientPort", "8007")), Integer.parseInt(System.getProperty("devicePort", "8008")), (System.getProperty("ssl") != null));
        homeServer.runServer();
    }
}
