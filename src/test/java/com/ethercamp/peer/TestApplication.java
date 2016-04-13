package com.ethercamp.peer;

import com.ethercamp.peer.rest.JsonRpcServiceImpl;
import org.bouncycastle.util.encoders.Hex;
import org.ethereum.config.DefaultConfig;
import org.ethereum.config.SystemProperties;
import org.ethereum.config.blockchain.FrontierConfig;
import org.ethereum.config.net.AbstractNetConfig;
import org.ethereum.core.Block;
import org.ethereum.core.BlockchainImpl;
import org.ethereum.core.ImportResult;
import org.ethereum.core.Transaction;
import org.ethereum.crypto.ECKey;
import org.ethereum.facade.EthereumImpl;
import org.ethereum.jsontestsuite.BlockTestCase;
import org.ethereum.jsontestsuite.BlockTestSuite;
import org.ethereum.jsontestsuite.JSONReader;
import org.ethereum.jsontestsuite.builder.BlockBuilder;
import org.ethereum.jsontestsuite.model.BlockTck;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.io.IOException;

@SpringBootApplication
@EnableScheduling
public class TestApplication {

    public static class OldFrontierBCConfig extends AbstractNetConfig {
        public OldFrontierBCConfig() {
            add(0, new FrontierConfig() {
                @Override
                public boolean acceptTransactionSignature(Transaction tx) {
                    return true;
                }
            });
        }
    }

    public static void main(String[] args) throws IOException {
        SystemProperties.CONFIG.setBlockchainConfig(new OldFrontierBCConfig());
        SystemProperties.CONFIG.overrideParams(
                "genesis", "rpc.json",
                "database.dir", "no-dir");
        ConfigurableApplicationContext context =
                SpringApplication.run(new Object[]{DefaultConfig.class, Application.class}, args);
        EthereumImpl ethereum = context.getBean(EthereumImpl.class);
        String json = JSONReader.loadJSONFromCommit("BlockchainTests/bcRPC_API_Test.json", "c58d3dce3f64f7ee1f711054fc464202bb0b7d64");
        BlockTestSuite testSuite = new BlockTestSuite(json);
        BlockTestCase aCase = testSuite.getTestCases().get("RPC_API_Test");
        ((BlockchainImpl) ethereum.getBlockchain()).byTest = true;

        for (BlockTck blockTck : aCase.getBlocks()) {
            Block block = BlockBuilder.build(blockTck.getBlockHeader(),
                    blockTck.getTransactions(),
                    blockTck.getUncleHeaders());
            ImportResult result = ((BlockchainImpl) ethereum.getBlockchain()).tryToConnect(block);
            if (result != ImportResult.IMPORTED_BEST && result != ImportResult.IMPORTED_NOT_BEST) {
                throw new RuntimeException("Can't import test block: " + result + "\n" + block);
            }
        }

        JsonRpcServiceImpl rpcService = context.getBean(JsonRpcServiceImpl.class);
        rpcService.addAccount(ECKey.fromPrivate(Hex.decode("45a915e4d060149eb4365960e6a7a45f334393093061116b197e3240065ff2d8")));
    }
}
