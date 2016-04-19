## peer.ether.camp

The EthereumJ peer with JSON RPC support and
Web3 console which can be connected via SSH 
#### To run the peer:
```
> ./gradlew run
```
#### To test console:
```
> ssh -p 2200 admin@localhost
  Password: admin
> repl js
> var Web3 = require('web3'); var web3 = new Web3(new Web3.providers.HttpProvider("http://localhost:8545"));
> web3.eth.getBalance("0x407d73d8a49eeb85d32cf465507dd71d507100c1").toNumber();
```