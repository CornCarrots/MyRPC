package netty.coder;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: linhao
 * @date: 2020/05/23/18:52
 * @description:
 */
public enum StateEnum {
    SUCCESS(1),
    FAIL(0);

    int code;
    StateEnum(int code){
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public static StateEnum getByCode(int code){
        for (StateEnum state: StateEnum.values()) {
            if (state.getCode() == code){
                return state;
            }
        }
        return null;
    }
}
