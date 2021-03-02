package coder;

/**
 * @author linhao
 * @date 2020/5/23 18:10
 * @description: 自定义响应协议
 */
public class MyResponse {
    /**
     * 响应模块
     */
    private short module;

    /**
     * 响应具体操作
     */
    private short operation;

    /**
     * 响应状态
     */
    private StateEnum state;

    /**
     * 响应数据
     */
    private byte[] data;

    public MyResponse() {
    }

    public MyResponse(MyRequest request) {
        module = request.getModule();
        operation = request.getOperation();
    }

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

    public StateEnum getState() {
        return state;
    }

    public void setState(StateEnum state) {
        this.state = state;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public int getDataLength(){
        if (data == null){
            return 0;
        }
        return data.length;
    }
}
