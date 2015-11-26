package com.ethercamp.vmtrace.ethereum.listener;

import org.ethereum.core.TransactionExecutionSummary;
import org.ethereum.listener.EthereumListenerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


@Component
public class TransactionExecutionListener extends EthereumListenerAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger("vmtrace");

    @Override
    public void onTransactionExecuted(TransactionExecutionSummary summary) {
    }
}
