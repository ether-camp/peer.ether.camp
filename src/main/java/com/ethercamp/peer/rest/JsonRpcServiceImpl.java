package com.ethercamp.peer.rest;

import com.googlecode.jsonrpc4j.JsonRpcError;
import com.googlecode.jsonrpc4j.JsonRpcErrors;
import com.googlecode.jsonrpc4j.JsonRpcService;
import org.ethereum.core.Account;
import org.ethereum.crypto.ECKey;
import org.ethereum.jsonrpc.JsonRpcImpl;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * Created by Anton Nashatyrev on 11.04.2016.
 */
@JsonRpcService("/jr")
public class JsonRpcServiceImpl extends JsonRpcImpl {

    public Account addAccount(ECKey key) {
        return super.addAccount(key);
    }

        @Override
    public String eth_protocolVersion() {
        System.out.println("JsonRpcServiceImpl.eth_protocolVersion");
        return super.eth_protocolVersion();
    }
}
