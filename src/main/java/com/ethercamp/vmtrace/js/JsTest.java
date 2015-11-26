package com.ethercamp.vmtrace.js;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.io.FileReader;

/**
 * Created by Anton Nashatyrev on 25.11.2015.
 */
public class JsTest {
    public static void main(String[] args) throws Exception {
        ScriptEngineManager factory = new ScriptEngineManager();
        // create a Nashorn script engine
        ScriptEngine engine = factory.getEngineByName("javascript");
        Object o = engine.eval("234");
        System.out.println(o);
        o = engine.eval(new FileReader("src/main/js/bignumber.min.js"));
        o = engine.eval(new FileReader("src/main/js/env.nashorn.1.2-debug.js"));
//        System.out.println(o);
//        o = engine.eval(new FileReader("../web3.js/lib/web3.js"));
        o = engine.eval(new FileReader("src/main/js/web3-light.js"));
        o = engine.eval("var Web3 = require('web3');\n" +
                "var web3 = new Web3(new Web3.providers.HttpProvider(\"http://localhost:8545\"));\n" +
                "web3.eth.getBalance(\"0x407d73d8a49eeb85d32cf465507dd71d507100c1\").toNumber();");
//                "web3.eth.getStorageAt(\"0x407d73d8a49eeb85d32cf465507dd71d507100c1\", 1);");
//                "var request = new XMLHttpRequest();");
        System.out.println(o);

    }
}
