package IM.enums;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: linhao
 * @date: 2020/05/15/11:55
 * @description:
 */
public enum MsgAction {

    REGISTER(1, "注册"),
    SEND(2, "发送信息"),
    CHECK(3, "签收信息"),
    KEEPALIVE(4, "发送心跳");

    int action;
    String text;

    MsgAction(int action, String text) {
        this.action = action;
        this.text = text;
    }
}
