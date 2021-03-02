package server;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import lombok.Data;

/**
 * @Author: zerocoder
 * @Description: 服务
 * @Date: 2021/3/2 18:54
 */
@Data
public class Service {

    private String name;

    private String address;

    private String ip;

    private Integer port;

    public String getIp() {
        if (StrUtil.isEmpty(ip)) {
            return StrUtil.subBefore(address, ":", false);
        }
        return ip;
    }

    public Integer getPort() {
        if (ObjectUtil.isNull(port)){
            return Convert.toInt(StrUtil.subAfter(address, ":", false));
        }
        return port;
    }
}
