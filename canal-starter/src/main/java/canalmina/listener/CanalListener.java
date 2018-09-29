package canalmina.listener;

import canalmina.config.properties.CanalProperty;
import canalmina.util.ThreadPoolUtil;
import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.client.CanalConnectors;
import com.alibaba.otter.canal.protocol.CanalEntry;
import com.alibaba.otter.canal.protocol.Message;
import com.google.protobuf.InvalidProtocolBufferException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.List;

/**
 * 启动canal监听
 *
 * @author yihang.lv 2018/9/29、9:20
 */
@Slf4j
@Component
public class CanalListener implements ApplicationListener<ApplicationReadyEvent> {

    @Autowired
    private CanalProperty canalProperty;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
        initCanal();
    }

    public void initCanal() {
        ThreadPoolUtil.getInstance().execute(() -> {
        log.info("canal init ...");
        //创建连接 SocketAddress address, String destination, String username, String password
        SocketAddress address = new InetSocketAddress(canalProperty.getHost(), canalProperty.getPort());
        CanalConnector connector = CanalConnectors.newSingleConnector(address, canalProperty.getDestination(),
                canalProperty.getUsername(), canalProperty.getPassword());
        int batchSize = 1000;
        try {
            connector.connect();
            connector.subscribe(".*\\..*");
            connector.rollback();
            while (true) {
                //获取指定增量数据
                Message message = connector.getWithoutAck(batchSize);
                long batchId = message.getId();
                int size = message.getEntries().size();
                //如果没获取到数据，等待数据
                if (batchId == -1 || size == 0) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    printEntry(message.getEntries());
                }
                //确认提交
                connector.ack(batchId);
                //处理失败，回滚数据
                //connector.rollback();
            }
        } finally {
            connector.disconnect();
        }
        });
    }

    /**
     * 解析处理监听到的数据
     *
     * @param entries
     */
    private void printEntry(List<CanalEntry.Entry> entries) {
        for (CanalEntry.Entry entry : entries) {
            CanalEntry.EntryType entryType = entry.getEntryType();
            if (entryType == CanalEntry.EntryType.TRANSACTIONBEGIN ||
                    entryType == CanalEntry.EntryType.TRANSACTIONEND) {
                continue;
            }
            CanalEntry.RowChange rowChange;
            try {
                rowChange = CanalEntry.RowChange.parseFrom(entry.getStoreValue());
            } catch (InvalidProtocolBufferException e) {
                throw new RuntimeException("ERROR parser data: " + entry);
            }

            CanalEntry.EventType eventType = rowChange.getEventType();
            CanalEntry.Header header = entry.getHeader();
//            System.out.println(String.format("==========> binlog[%s,%s] , name[%s,%s] eventType : %s", header.getLogfileName(),
//                    header.getLogfileOffset(), header.getSchemaName(), header.getTableName(), eventType
//            ));

            //获取每一行的变化数据
            for (CanalEntry.RowData rowData : rowChange.getRowDatasList()) {
                //删除数据时的事件
                switch (eventType) {
                    case DELETE:
                        printColumn(rowData.getBeforeColumnsList());
                        break;
                    case INSERT:
                        printColumn(rowData.getAfterColumnsList());
                        break;
                    case UPDATE:
                        printColumn(rowData.getBeforeColumnsList());
                        printColumn(rowData.getAfterColumnsList());
                        break;
                    case ALTER:
                        break;
                }

            }
        }
    }

    private void printColumn(List<CanalEntry.Column> columns) {
        for (CanalEntry.Column column : columns) {
            System.out.println(column.getName() + " : " + column.getValue() +
            "     update = "+ column.getUpdated());
        }
    }
}
