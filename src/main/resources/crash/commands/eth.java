package commands;

import org.crsh.command.*;
import org.crsh.cli.*;

import org.ethereum.facade.Ethereum;
import org.springframework.beans.factory.BeanFactory;

public class eth extends BaseCommand {

    @Usage("access ethereum blocks info")
    @Command
    public Object blocks() {

        BeanFactory factory = (BeanFactory)context.getAttributes().get("spring.beanfactory");
        Ethereum ethereumBean = factory.getBean(Ethereum.class);

        return ethereumBean.getBlockchain().getBestBlock().getNumber();
    }

    @Usage("exit safely")
    @Command
    public void exit(@Usage("exit after block number") @Option(names={"b","block"}) String block) {

//        if (block == null)
//            block = "-1";
//
//        BeanFactory factory = (BeanFactory)context.getAttributes().get("spring.beanfactory");
//        Ethereum ethereumBean = factory.getBean(Ethereum.class);
//
//        ethereumBean.exitOn(Long.parseLong(block));
    }

}