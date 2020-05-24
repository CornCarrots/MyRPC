package netty.multiple_client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author linhao
 * @date 2020/5/16 23:32
 */
public class ClientStart {
    public static void main(String[] args) throws IOException {
        MyMultipleClient client = new MyMultipleClient();
        client.init(4);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        while (true){
            System.out.println("请输入");
            String msg = bufferedReader.readLine();
            // 用多个子线程来执行任务
            client.nextChannel().writeAndFlush(msg);
        }
    }
}
