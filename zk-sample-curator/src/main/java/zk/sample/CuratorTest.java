package zk.sample;

import org.apache.commons.lang.SerializationUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.CuratorEvent;
import org.apache.curator.framework.api.CuratorListener;
import org.apache.curator.framework.api.UnhandledErrorListener;
import org.apache.curator.framework.listen.Listenable;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.framework.state.ConnectionStateListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.zookeeper.data.Stat;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;

public class CuratorTest extends ZooKeeperClientBaseTest {

    private static final Logger logger = LogManager.getLogger(CuratorTest.class);
    private static final String PATH = "/user";
    private CuratorFramework client;
    private Listenable<ConnectionStateListener> connectionStateListenable;
    private Listenable<UnhandledErrorListener> unhandledErrorListenable;
    private Listenable<CuratorListener> curatorListenable;

    private ConnectionStateListener connectionStateListener;
    private UnhandledErrorListener unhandledErrorListener;
    private CuratorListener curatorListener;

    @Before
    public void init() {
        client = CuratorFrameworkFactory.builder().connectString(SERVERS).sessionTimeoutMs(30000)
                .connectionTimeoutMs(30000).canBeReadOnly(false).retryPolicy(new ExponentialBackoffRetry(1000, 20))
                .defaultData(null).build();

        connectionStateListenable = client.getConnectionStateListenable();
        connectionStateListener = new ZKConnectionStateListener();
        connectionStateListenable.addListener(connectionStateListener);

        unhandledErrorListenable = client.getUnhandledErrorListenable();
        unhandledErrorListener = new ZKUnhandledErrorListener();
        unhandledErrorListenable.addListener(unhandledErrorListener);

        curatorListenable = client.getCuratorListenable();
        curatorListener = new ZKCuratorListener();
        curatorListenable.addListener(curatorListener);

        client.start();
    }

    @Test
    public void create() throws Exception {
        Stat stat = client.checkExists().forPath(PATH);
        if (stat != null) {
            client.delete().forPath(PATH);
        }
        User user = new User(1, "aaa", 11, "shanghai", new Date());
        byte[] data = SerializationUtils.serialize(user);
        client.create().forPath(PATH, data);
    }

    @Test
    public void read() throws Exception {
        byte[] data = client.getData().forPath(PATH);
        User user = (User) SerializationUtils.deserialize(data);
        logger.info(user);
    }

    @Test
    public void update() throws Exception {
        byte[] data = client.getData().forPath(PATH);
        User user = (User) SerializationUtils.deserialize(data);
        user.setAddress("beijing");
        data = SerializationUtils.serialize(user);
        client.setData().forPath(PATH, data);
    }

    @Test
    public void delete() throws Exception {
        client.delete().forPath(PATH);
    }

    @After
    public void close() {
        connectionStateListenable.removeListener(connectionStateListener);
        unhandledErrorListenable.removeListener(unhandledErrorListener);
        curatorListenable.removeListener(curatorListener);
        client.close();
    }

    private static class ZKConnectionStateListener implements ConnectionStateListener {

        @Override
        public void stateChanged(CuratorFramework client, ConnectionState newState) {
            logger.info("ConnectionStateListener -> stateChanged");
            logger.info(newState);
        }
    }

    private static class ZKUnhandledErrorListener implements UnhandledErrorListener {

        @Override
        public void unhandledError(String message, Throwable e) {
            logger.info("UnhandledErrorListenable -> unhandledError");
            logger.info(message);
        }
    }

    private static class ZKCuratorListener implements CuratorListener {

        @Override
        public void eventReceived(CuratorFramework client, CuratorEvent event) throws Exception {
            logger.info("CuratorListener -> eventReceived");
            logger.info(event);
        }
    }
}