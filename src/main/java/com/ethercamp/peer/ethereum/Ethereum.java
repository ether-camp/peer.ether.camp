package com.ethercamp.vmtrace.ethereum;

import org.ethereum.facade.EthereumFactory;
import org.ethereum.listener.EthereumListener;
import org.ethereum.net.rlpx.Node;
import org.spongycastle.util.encoders.Hex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.apache.commons.lang3.StringUtils.isEmpty;
import static org.ethereum.config.SystemProperties.CONFIG;

@Component
public class Ethereum {

    @Autowired
    private List<EthereumListener> listeners;

    private org.ethereum.facade.Ethereum ethereum;
    private ExecutorService executor = Executors.newSingleThreadExecutor();

    private static org.ethereum.facade.Ethereum createEthereum(List<EthereumListener> listeners) {
        org.ethereum.facade.Ethereum ethereum = EthereumFactory.createEthereum();
        listeners.stream().forEach(ethereum::addListener);

        return ethereum;
    }

    @PostConstruct
    public void init() {
        ethereum = createEthereum(listeners);
        executor.submit(() -> {
            if (isEmpty(CONFIG.blocksLoader())) {
                Node node = CONFIG.peerActive().get(0);
                ethereum.connect(
                        node.getHost(),
                        node.getPort(),
                        Hex.toHexString(node.getId()));
            } else {
                ethereum.getBlockLoader().loadBlocks();
            }
        });
    }
    @PreDestroy
    public void destroy() {
        executor.shutdown();
    }

    public long getMaxBlockNumber(){
        return ethereum.getBlockchain().getBestBlock().getNumber();
    }

    public void exitOn(long number){
        ethereum.exitOn(number);
    }
}
