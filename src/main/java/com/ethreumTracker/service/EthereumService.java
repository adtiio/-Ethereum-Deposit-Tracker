package com.ethreumTracker.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthBlock.Block;
import org.web3j.protocol.core.methods.response.EthGetTransactionReceipt;
import org.web3j.protocol.core.methods.response.Transaction;
import org.web3j.protocol.http.HttpService;

import com.ethreumTracker.model.Deposit;
import com.ethreumTracker.repository.DepositRepository;

import jakarta.transaction.Transactional;
import java.time.LocalDateTime;


@Service
public class EthereumService {

    private final Web3j web3j;
    private final DepositRepository depositRepository;
    private static final String BEACON_CONTRACT_ADDRESS = "0x00000000219ab540356cBB839Cbe05303d7705Fa";

    @Autowired
    public EthereumService(DepositRepository depositRepository) {
        String alchemyUrl = "https://eth-mainnet.g.alchemy.com/v2/$SECRET_KEY";
        this.web3j = Web3j.build(new HttpService(alchemyUrl));
        this.depositRepository = depositRepository;
        System.out.println("Block: : "+web3j);
    }

    public Block getLatestBlock() throws Exception {
        return web3j.ethGetBlockByNumber(DefaultBlockParameterName.LATEST, true).send().getBlock();
    }

    public EthGetTransactionReceipt getTransactionReceipt(String txHash) throws Exception {
        return web3j.ethGetTransactionReceipt(txHash).send();
    }

    private String extractPubkey(String inputData) {
        return inputData != null && inputData.length() >= 128 ? inputData.substring(0, 128) : "";
    }

    @Transactional
    public void saveDeposit(String txHash) throws Exception {
        EthGetTransactionReceipt receipt = getTransactionReceipt(txHash);
        if (receipt.getTransactionReceipt().isPresent()) {
            Transaction transaction = web3j.ethGetTransactionByHash(txHash).send().getTransaction().orElseThrow();
            

                    Deposit deposit=new Deposit();
                    deposit.setBlockNumber(receipt.getTransactionReceipt().get().getBlockNumber());
                    deposit.setBlockTimestamp(LocalDateTime.now());
                    deposit.setFee(receipt.getTransactionReceipt().get().getCumulativeGasUsed());
                    deposit.setHash(txHash);
                    
                    String pubkey = extractPubkey(transaction.getInput());
                    deposit.setPubkey(pubkey);

                    depositRepository.save(deposit);
                                
            
        } else {
            throw new Exception("Transaction receipt not found.");
        }
    }

    public void startBlockListener() {
        web3j.blockFlowable(false).subscribe(block -> {
            System.out.println("New block received: " + block.getBlock().getNumber());

            block.getBlock().getTransactions().forEach(txObject -> {
                Transaction tx = (Transaction) txObject.get();
                
                if (BEACON_CONTRACT_ADDRESS.equalsIgnoreCase(tx.getTo())) {
                    System.out.println("Deposit transaction found in block " + block.getBlock().getNumber());
                    try {
                        saveDeposit(tx.getHash()); 
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }, throwable -> {
            System.err.println("Error in block subscription: " + throwable.getMessage());
        });
    }
     
    public void seperate(String inputData) throws Exception{
        String[] depositHexes = inputData.split(",");

        for (String txHash : depositHexes) {
            saveDeposit(txHash);
        }
    }

}
