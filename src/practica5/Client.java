package practica5;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Scanner;


    public class Client {
        private SocketChannel sc;
        ByteBuffer writeBuffer = ByteBuffer.allocate(1024);
        ByteBuffer readBuffer = ByteBuffer.allocate(1024);

        public static void main(String[] args) {
            new Client();
        }
        public Client() {
            try {
                sc = SocketChannel.open();
            // Connect to the server
                sc.connect(new InetSocketAddress(8899));
                //Send a message
                this.write(sc);
            // Read the message
                this.read(sc);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private void read(SocketChannel sc) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (true) {
                        try {
                            readBuffer.clear();
                            int read = sc.read(readBuffer);
                            readBuffer.flip();
                            System.out.println(new String(readBuffer.array()));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();
        }

        private void write(SocketChannel sc) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (true) {
                        Scanner scanner = new Scanner(System.in);
                        String next = scanner.next();
                        writeBuffer.clear();
                        writeBuffer.put(next.getBytes());
                        writeBuffer.flip();
                        try {
                            sc.write(writeBuffer);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();
        }
    }

