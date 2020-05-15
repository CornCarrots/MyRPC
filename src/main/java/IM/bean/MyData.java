package IM.bean;

import IM.enums.MsgAction;

import java.io.Serializable;
import java.util.Objects;

/**
 * @author linhao
 * @date 2020/5/15 11:53
 * 信息载体
 */
public class MyData implements Serializable {

    private static final long serialVersionUID = -1300309485429981425L;
    /**
     * 动作
     */
    private MsgAction action;
    /**
     * 消息
     */
    private Msg msg;
    /**
     * 扩展字段
     */
    private String extand;

    public MsgAction getAction() {
        return action;
    }

    public void setAction(MsgAction action) {
        this.action = action;
    }

    public Msg getMsg() {
        return msg;
    }

    public void setMsg(Msg msg) {
        this.msg = msg;
    }

    public String getExtand() {
        return extand;
    }

    public void setExtand(String extand) {
        this.extand = extand;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MyData data = (MyData) o;
        return action == data.action &&
                Objects.equals(msg, data.msg) &&
                Objects.equals(extand, data.extand);
    }

    @Override
    public int hashCode() {
        return Objects.hash(action, msg, extand);
    }

    @Override
    public String toString() {
        return "MyData{" +
                "action=" + action +
                ", msg=" + msg +
                ", extand='" + extand + '\'' +
                '}';
    }
}
