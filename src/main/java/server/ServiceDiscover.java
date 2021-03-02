package server;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.json.JSONUtil;
import cn.hutool.log.LogFactory;
import org.I0Itec.zkclient.ZkClient;
import serializer.NettySerializer;
import serializer.ZookeeperSerializer;
import util.Constants;
import util.PropertiesUtil;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: zerocoder
 * @Description: 服务发现者
 * @Date: 2021/3/2 19:15
 */

public class ServiceDiscover {

    private ZkClient zkClient;

    public ServiceDiscover() {
        String zkAddress = PropertiesUtil.getValue(Constants.ZOOKEEPER_ADDRESS);
        zkClient = new ZkClient(zkAddress);
        zkClient.setZkSerializer(new ZookeeperSerializer());
    }

    /**
     * 获取服务
     * @param name
     * @return
     */
    public List<Service> getServer(String name){
        String path = Constants.ZOOKEEPER_ADDRESS + "/" + name + "/service";
        List<String> children = zkClient.getChildren(path);
        List<Service> services = new ArrayList<>();
        for (String serviceName:children) {
            try {
                String decodeStr = URLDecoder.decode(serviceName, CharsetUtil.UTF_8);
                Service service = JSONUtil.toBean(decodeStr, Service.class);
                services.add(service);
            } catch (UnsupportedEncodingException e) {
                LogFactory.get().error("[getServer] name:{} error!", name, e);
            }
        }
        return services;
    }

    /**
     * 注册服务
     * @param service
     * @throws IllegalAccessException
     */
    public void register(Service service) throws IllegalAccessException {
        if (service == null){
            throw new IllegalAccessException("服务为空");
        }
        String name = service.getName();
        String path = Constants.ZOOKEEPER_ADDRESS + "/" + name + "/service";
        if (!zkClient.exists(path)){
            zkClient.createPersistent(path);
        }
        String uri = JSONUtil.toJsonStr(service);
        try {
            uri = URLEncoder.encode(uri, CharsetUtil.UTF_8);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String uriPath = path + "/" + uri;
        if (zkClient.exists(uriPath)){
            zkClient.delete(uriPath);
        }
        zkClient.createEphemeral(uriPath);
    }
}
