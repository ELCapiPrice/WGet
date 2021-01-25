package practica5;

import java.util.Scanner;

class MsgSender implements Runnable{
    private Msg msg;

    public MsgSender(Msg msg) {
        this.msg = msg;
    }

    @Override
    public void run() {
        while (true) {
            System.out.println("input:\n");
            Scanner scanner = new Scanner(System.in);
            this.msg.setContent(scanner.next());
            this.msg.setFlag(true);
        }
    }
}