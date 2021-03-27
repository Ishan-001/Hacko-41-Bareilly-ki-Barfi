package com.ethereum.securex.web3;

import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.gas.DefaultGasProvider;

public class app {
    String CONTRACT_ADDRESS = "0xd9145CCE52D386f254917e481eB44e9943F39138"

    Web3j web3j = Web3j.build(new HttpService("NODE_URL"));
    String hash;
    HashContract hashContract = HashContract.load(CONTRACT_ADDRESS, web3j, Credentials.create("your_private_key"), new DefaultGasProvider());
     if (hashContract.isValid()) {
            hash = hashContract.greeting().send();
        }
     web3j.shutdown();
}
