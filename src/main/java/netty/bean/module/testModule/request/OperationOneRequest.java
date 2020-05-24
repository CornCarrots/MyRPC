package netty.bean.module.testModule.request;

import annotation.MySerializable;
import annotation.MySerialize;

/**
 * @author linhao
 * @date 2020/5/24 10:58
 * @description:
 */
@MySerializable
public class OperationOneRequest {

    @MySerialize(order = 0)
    private int id;

    @MySerialize(order = 1)
    private String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
