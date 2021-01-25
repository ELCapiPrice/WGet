package practica5;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

public class Server {

    private Selector selector;
    private ByteBuffer writeBuffer = ByteBuffer.allocate(1024);
    private ByteBuffer readBuffer = ByteBuffer.allocate(1024);
    Msg msg = new Msg();
    MsgSender msgSender = new MsgSender(msg);
    public static void main(String[] args) {
        Server server = new Server();
        new Thread(server.msgSender).start();
        server.start();

    }

    // Initialize the server
    public Server() {
        try {
            this.selector = Selector.open();
            ServerSocketChannel ssc = ServerSocketChannel.open();
            ssc.configureBlocking(false);
            ssc.bind(new InetSocketAddress(8899));
            ssc.register(this.selector, SelectionKey.OP_ACCEPT);
            System.out.println("The server is initialized...");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Start the server waiting for the selector event
    public void start() {
        while (true) {
            try {
                int num = this.selector.select();
                if (num > 0) {
                    Iterator<SelectionKey> it = this.selector.selectedKeys().iterator();
                    while (it.hasNext()) {
                        SelectionKey key = it.next();
                        it.remove();
                        if (key.isValid()) {
                            if (key.isAcceptable()) {
                                this.accept(key);
                            }
                            if (key.isReadable()) {
                                this.read(key);
                            }
                            if (key.isWritable()) {
                                this.write(key);
                            }
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //Send a message
    private void write(SelectionKey key) {
        SocketChannel sc = (SocketChannel) key.channel();
        try {
            sc.configureBlocking(false);
        } catch (IOException e) {
            e.printStackTrace();
        }
		// Determine if there is a message to send
        if(msg!=null && msg.getFlag()){
            System.out.println(msg);
            try {
                msg.setFlag(false);
                writeBuffer.clear();
                writeBuffer.put(msg.getContent().getBytes());
                writeBuffer.flip();
                sc.write(writeBuffer);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }

    // Read client messages
    private void read(SelectionKey key) {
        SocketChannel sc = (SocketChannel) key.channel();
        try {
            sc.configureBlocking(false);
            readBuffer.clear();
            int read = sc.read(readBuffer);
            if (read == -1) {
                sc.close();
                key.cancel();
            }
            readBuffer.flip();
            System.out.println(new String(readBuffer.array()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Accept client connection
    private void accept(SelectionKey key) {
        ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
        try {
            SocketChannel sc = ssc.accept();
            System.out.println(String.format("a new client join!!!host:%s;port:%d", sc.socket().getLocalAddress(), sc.socket().getPort()));
            sc.configureBlocking(false);
            sc.register(this.selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}