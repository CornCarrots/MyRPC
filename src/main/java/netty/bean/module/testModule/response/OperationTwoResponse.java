package netty.bean.module.testModule.response;

import annotation.MySerializable;
import annotation.MySerialize;

/**
 * @author linhao
 * @date 2020/5/24 11:11
 * @description:
 */
@MySerializable
public class OperationTwoResponse {

    @MySerialize(order = 0)
    private int id;

    @MySerialize(order = 1)
    private double money;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }
}
