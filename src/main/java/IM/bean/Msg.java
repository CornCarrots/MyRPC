package IM.bean;

import java.io.Serializable;
import java.util.Objects;

/**
 * @author linhao
 * @date 2020/5/15 12:01
 * 发送的信息
 */
public class Msg implements Serializable {

    private static final long serialVersionUID = 8360721807148793783L;

    private long sendId;

    private long receiveId;

    private String text;

    private boolean check;

    @Override
    public String toString() {
        return "Msg{" +
                "sendId=" + sendId +
                ", receiveId=" + receiveId +
                ", text='" + text + '\'' +
                ", isCheck=" + check +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Msg msg = (Msg) o;
        return sendId == msg.sendId &&
                receiveId == msg.receiveId &&
                check == msg.check &&
                Objects.equals(text, msg.text);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sendId, receiveId, text, check);
    }

    public long getSendId() {
        return sendId;
    }

    public void setSendId(long sendId) {
        this.sendId = sendId;
    }

    public long getReceiveId() {
        return receiveId;
    }

    public void setReceiveId(long receiveId) {
        this.receiveId = receiveId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        check = check;
    }
}
