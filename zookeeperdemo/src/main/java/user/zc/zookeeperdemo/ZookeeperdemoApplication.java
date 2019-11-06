package user.zc.zookeeperdemo;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.CuratorWatcher;
import org.apache.curator.retry.RetryNTimes;
import org.apache.zookeeper.*;

import java.util.List;

public class ZookeeperdemoApplication {

    public static void test1()throws Exception{
        CuratorFramework client = CuratorFrameworkFactory.newClient("192.168.220.134:2181", new RetryNTimes(10, 5000));
        client.start();// 连接
        // 获取子节点，顺便监控子节点
        List<String> children = client.getChildren().usingWatcher(new CuratorWatcher() {
            public void process(WatchedEvent event) throws Exception
            {
                System.out.println("监控： " + event);
            }
        }).forPath("/");
        System.out.println(children);
        // 创建节点
        String result = client.create().withMode(CreateMode.PERSISTENT).withACL(ZooDefs.Ids.OPEN_ACL_UNSAFE).forPath("/test", "Data".getBytes());
        System.out.println(result);
        // 设置节点数据
        client.setData().forPath("/test", "111".getBytes());
        client.setData().forPath("/test", "222".getBytes());
        // 删除节点
        //System.out.println(client.checkExists().forPath("/test"));
        /*client.delete().withVersion(-1).forPath("/test");
        System.out.println(client.checkExists().forPath("/test"));*/
        client.close();
        System.out.println("OK！");
    }

    public static void test2() throws Exception{
        ZooKeeper zk = new ZooKeeper("192.168.220.134:2181", 3000, new Watcher() {
            public void process(WatchedEvent watchedEvent) {
                System.out.println(watchedEvent.toString());
            }
        });
        System.out.println("OK!");
        // 创建一个目录节点
        /**
         * CreateMode:
         *       PERSISTENT (持续的，相对于EPHEMERAL，不会随着client的断开而消失)
         *       PERSISTENT_SEQUENTIAL（持久的且带顺序的）
         *       EPHEMERAL (短暂的，生命周期依赖于client session)
         *       EPHEMERAL_SEQUENTIAL  (短暂的，带顺序的)
         */
        zk.create("/country", "China".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        // 创建一个子目录节点
        zk.create("/country/city", "China/Hangzhou".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        System.out.println(new String(zk.getData("/country", false, null)));
        // 取出子目录节点列表
        System.out.println(zk.getChildren("/country", true));
        // 创建另外一个子目录节点
        zk.create("/country/view", "China/WestLake".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        System.out.println(zk.getChildren("/country", true));
        // 修改子目录节点数据
        zk.setData("/country/city", "China/Shanghai".getBytes(), -1);
        byte[] datas = zk.getData("/country/city", true, null);
        String str = new String(datas, "utf-8");
        System.out.println(str);
        // 删除整个子目录 -1代表version版本号，-1是删除所有版本
//        zk.delete("/path01/path01", -1);
//        zk.delete("/path01/path02", -1);
//        zk.delete("/path01", -1);
//        System.out.println(str);
        Thread.sleep(15000);


        System.out.println(zk.getChildren("/country", true));
        zk.close();
        System.out.println("OK");
    }

    public static void main(String[] args) throws Exception{
        test2();
//        SpringApplication.run(ZookeeperdemoApplication.class, args);







    }

}
