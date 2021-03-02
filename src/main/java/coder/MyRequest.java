package coder;

/**
 * @author linhao
 * @date 2020/5/22 17:36
 * @description: 自定义的请求协议
 */
public class MyRequest {

    /**
     * 请求模块
     */
    private short module;

    /**
     * 请求具体操作
     */
    private short operation;

    /**
     * 请求数据
     */
    private byte[] data;

    public short getModule() {
        return module;
    }

    public void setModule(short module) {
        this.module = module;
    }

    public short getOperation() {
        return operation;
    }

    public void setOperation(short operation) {
        this.operation = operation;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public int getDataLength(){
        if (data == null) {
            return 0;
        }
        return data.length;
    }
}
