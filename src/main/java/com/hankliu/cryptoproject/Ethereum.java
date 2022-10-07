/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hankliu.cryptoproject;

import com.hankliu.cryptoproject.util.Http;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.hankliu.cryptoproject.model.Block;
import com.hankliu.cryptoproject.model.Gas;
import com.hankliu.cryptoproject.model.GasHistory;
import com.hankliu.cryptoproject.model.Pair;
import com.hankliu.cryptoproject.model.Result;
import java.math.BigDecimal;
import java.math.BigInteger;
import org.web3j.utils.Convert;
import org.web3j.utils.Numeric;

/**
 *
 * @author Hank Liu <kccc418@gmail.com>
 */
public class Ethereum {

    public enum ChainList {
        ETH("https://mainnet.infura.io/v3/", 1, "https://etherscan.io/tx/"),
        TT("https://mainnet-rpc.thundercore.com", 108, ""),
        SGB("https://songbird.towolabs.com/rpc", 19, "https://songbird-explorer.flare.network/tx/"),
        CRO("https://evm-cronos.crypto.org", 25, "https://cronos.crypto.org/explorer/tx/"),
        MATIC("https://polygon-rpc.com/", 137, "https://polygonscan.com/tx/"),
        AVAX("https://api.avax.network/ext/bc/C/rpc", 43114, "https://snowtrace.io/tx/"),
        BSC("https://bsc-dataseed1.binance.org", 56, "https://bscscan.com/tx/"),
        FTM("https://rpc.fantom.network", 250, "https://ftmscan.com/tx/"),
        ETHOP("https://mainnet.optimism.io", 10, "https://optimistic.etherscan.io/tx/"),
        ETHAR("https://arb1.arbitrum.io/rpc", 42161, "https://arbiscan.io/tx/"),
        ETHTEST("https://goerli.infura.io/v3/55c881399fe442bfb09d327b53dc5fdb",5,"");

        ChainList(String url, int chainId, String ethscanUrl) {
            this.url = url;
            this.chainId = new BigInteger(Integer.toString(chainId));
            this.ethscanUrl = ethscanUrl;
        }

        private final String url;
        private final BigInteger chainId;
        private final String ethscanUrl;

        public String getUrl() {
            return url;
        }

        public BigInteger getChainId() {
            return chainId;
        }

        public String getEthscanUrl() {
            return ethscanUrl;
        }

        @Override
        public String toString() {
            return this.name();
        }
    }

    private final String url;
    private final ChainList chain;

    Ethereum(ChainList chain, String apiKey) {
        this.chain = chain;
        this.url = chain.getUrl();
//        this.url = chain.getUrl() + apiKey;
    }

    public ChainList getChain() {
        return chain;
    }

    public void getBlockNumber() {
        System.out.println("Latest Block Number:");
        String ethMethod = "eth_blockNumber";
        // {"id":0,"jsonrpc":"2.0","method":"eth_getBlockByNumber","params":["blockCount(string)", newestBlock(string)]}
        JsonObject body = new JsonObject();
        body.addProperty("id", 0);
        body.addProperty("jsonrpc", "2.0");
        body.addProperty("method", ethMethod);
        body.add("params", new JsonArray());
        String response = Http.uploadToServer(this.url, body.toString());

        Gson gson = new Gson();
        Result result = gson.fromJson(response, Result.class);

        System.out.println(Long.decode(result.getResult()) + System.getProperty("line.separator"));
    }

    public void getMaxPriorityFeePerGas() {
        System.out.println("Default Tip fee:");
        String ethMethod = "eth_maxPriorityFeePerGas";
        // {"id":0,"jsonrpc":"2.0","method":"eth_maxPriorityFeePerGas","params":["blockCount(string)", newestBlock(string)]}
        JsonObject body = new JsonObject();
        body.addProperty("id", 0);
        body.addProperty("jsonrpc", "2.0");
        body.addProperty("method", ethMethod);
        body.add("params", new JsonArray());
        String response = Http.uploadToServer(this.url, body.toString());

        Gson gson = new Gson();
        Result result = gson.fromJson(response, Result.class);

        System.out.println(Convert.fromWei(new BigDecimal(Numeric.decodeQuantity(result.getResult())), Convert.Unit.GWEI).toPlainString() + " Gwei" + System.getProperty("line.separator"));
    }

    public void getBlockByNumber() {
        String ethMethod = "eth_getBlockByNumber";
        // {"id":0,"jsonrpc":"2.0","method":"eth_getBlockByNumber","params":["blockCount(string)", newestBlock(string)]}
        JsonObject body = new JsonObject();
        JsonArray params = new JsonArray();
        body.addProperty("id", 0);
        body.addProperty("jsonrpc", "2.0");
        body.addProperty("method", ethMethod);
        params.add("latest");
        params.add(false);
        body.add("params", params);
        String response = Http.uploadToServer(this.url, body.toString());

        Gson gson = new Gson();
        Block block = gson.fromJson(response, Block.class);

        BigDecimal baseFeePerGas = new BigDecimal(Numeric.decodeQuantity(block.getResult().getBaseFeePerGas()));
        System.out.println(Convert.fromWei(baseFeePerGas, Convert.Unit.ETHER).toPlainString());
        System.out.println(Convert.fromWei(baseFeePerGas, Convert.Unit.GWEI).toPlainString() + " Gwei" + System.getProperty("line.separator"));

        BigDecimal blockNumber = new BigDecimal(Numeric.decodeQuantity(block.getResult().getNumber()));
        System.out.println(blockNumber.toPlainString());

//        System.out.println(result.getResult());
    }

    public Pair<BigInteger, BigInteger> getFeeHistory() {
        System.out.println("Gas Tip & Cap:");
        String ethMethod = "eth_feeHistory";
        // {"id":0,"jsonrpc":"2.0","method":"eth_feeHistory","params":["blockCount(string)", newestBlock(string)]}

        int[] percentiles = {10, 25, 50, 75};
        int historicalBlocks = 4;

        JsonObject body = new JsonObject();
        JsonArray params = new JsonArray();
        body.addProperty("id", 0);
        body.addProperty("jsonrpc", "2.0");
        body.addProperty("method", ethMethod);
        params.add(Integer.toHexString(historicalBlocks));
        params.add("latest");
        JsonArray priorities = new JsonArray();
        for (int percent : percentiles) {
            priorities.add(percent);
        }
        params.add(priorities);
        body.add("params", params);
        String response = Http.uploadToServer(this.url, body.toString());

        Gson gson = new Gson();
        GasHistory result = gson.fromJson(response, GasHistory.class);

        String[] baseFeePerGas = result.getResult().getBaseFeePerGas();
        String nextBaseFee = baseFeePerGas[historicalBlocks];

        long[] sums = new long[percentiles.length];
        String[][] reward = result.getResult().getReward();
        System.out.println("oldestBlock: " + Long.decode(result.getResult().getOldestBlock()));
        System.out.println("reward:");
        for (String[] r : reward) {
            for (int i = 0; i < r.length; i++) {
                sums[i] += Long.decode(r[i]);
            }
        }
        System.out.println("maxPriorityFeePerGas:");
        long gasTipCap = 0;
        for (int i = 0; i < sums.length; i++) {
            if (i == 0) {
                gasTipCap = sums[i] / historicalBlocks;
            }
            System.out.println(percentiles[i] + "%:");
            System.out.println(Convert.fromWei(Long.toString(sums[i] / historicalBlocks), Convert.Unit.GWEI).toPlainString() + " Gwei");
        }
        System.out.println();

        return new Pair(new BigDecimal(gasTipCap).toBigInteger(), new BigDecimal(Long.decode(nextBaseFee)).toBigInteger());
    }

    public void sendTransaction(String transcation, String ethscanUrl) {
        String ethMethod = "eth_sendRawTransaction";
        // {"id":0,"jsonrpc":"2.0","method":"eth_sendRawTransaction","params":["TRANSACTION DATA(string)"]}
        JsonObject body = new JsonObject();
        JsonArray params = new JsonArray();
        body.addProperty("id", 0);
        body.addProperty("jsonrpc", "2.0");
        body.addProperty("method", ethMethod);
        params.add(transcation);
        body.add("params", params);
        String response = Http.uploadToServer(this.url, body.toString());

        Gson gson = new Gson();
        Result result = gson.fromJson(response, Result.class);
        System.out.println(ethscanUrl + result.getResult());
    }

    public BigInteger getGasLimit(String from, String to, BigInteger gasPrice, BigInteger value, String data) {
        System.out.println("Gas Limit:");
        String ethMethod = "eth_estimateGas";
        JsonObject body = new JsonObject();
        JsonArray params = new JsonArray();
        JsonObject param = new JsonObject();
        body.addProperty("id", 0);
        body.addProperty("jsonrpc", "2.0");
        body.addProperty("method", ethMethod);

        param.addProperty("from", from);
        param.addProperty("to", to);
        param.addProperty("gasPrice", Numeric.encodeQuantity(gasPrice));
        param.addProperty("value", Numeric.encodeQuantity(value));
        param.addProperty("data", data);
        params.add(param);
        body.add("params", params);

        String response = Http.uploadToServer(this.url, body.toString());
        System.out.println(response);
        Gson gson = new Gson();
        Result result = gson.fromJson(response, Result.class);
        String res;
        if (null == result.getResult()) {
            res = "0x5208"; // the gas parameter to eth_estimateGas and eth_call are capped at 10x (1000%) the current block gas limit.
        } else {
            res = result.getResult();
        }
        BigInteger gasLimit = Numeric.decodeQuantity(res);
        System.out.println(gasLimit.toString() + System.getProperty("line.separator"));
        return gasLimit;

    }

    public BigInteger getNonce(String address) {
        System.out.println("Nonce:");
        String ethMethod = "eth_getTransactionCount";
        // {"id":0,"jsonrpc":"2.0","method":"eth_getTransactionCount","params":["ADDRESS(string)", "BLOCK PARAMETER(string)"]}
        JsonObject body = new JsonObject();
        JsonArray params = new JsonArray();
        body.addProperty("id", 0);
        body.addProperty("jsonrpc", "2.0");
        body.addProperty("method", ethMethod);
        params.add(address);
        params.add("latest");
        body.add("params", params);
        String response = Http.uploadToServer(this.url, body.toString());

        Gson gson = new Gson();
        Result result = gson.fromJson(response, Result.class);

        BigDecimal nonce = new BigDecimal(Numeric.decodeQuantity(result.getResult()));
        System.out.println(nonce.toPlainString() + System.getProperty("line.separator"));

        return nonce.toBigInteger();
    }

    public BigInteger getBalance(String url) {
        System.out.println("Balance:");
        String ethMethod = "eth_getBalance";
        // {"id":0,"jsonrpc":"2.0","method":"eth_getBalance","params":["string"]}
        JsonObject body = new JsonObject();
        JsonArray params = new JsonArray();
        body.addProperty("id", 0);
        body.addProperty("jsonrpc", "2.0");
        body.addProperty("method", ethMethod);
        params.add(url);
        params.add("latest");
        body.add("params", params);

        String response = Http.uploadToServer(this.url, body.toString());
        Gson gson = new Gson();
        Result result = gson.fromJson(response, Result.class);

        BigDecimal balance = Convert.fromWei(new BigDecimal(Numeric.decodeQuantity(result.getResult())), Convert.Unit.ETHER);
        System.out.println(balance.toPlainString() + System.getProperty("line.separator"));

        return balance.toBigInteger();
    }

    public BigInteger getGasPrice() {
        System.out.println("Gas Price:");
        String ethMethod = "eth_gasPrice";
        // {"id":0,"jsonrpc":"2.0","method":"eth_gasPrice","params":[]}
        JsonObject body = new JsonObject();
        body.addProperty("id", 0);
        body.addProperty("jsonrpc", "2.0");
        body.addProperty("method", ethMethod);
        body.add("params", new JsonArray());

        String response = Http.uploadToServer(this.url, body.toString());

        Gson gson = new Gson();
        Result result = gson.fromJson(response, Result.class);
        System.out.println(result.getResult());
        BigDecimal gasPrice = new BigDecimal(Numeric.decodeQuantity(result.getResult()));
        System.out.println(Convert.fromWei(gasPrice, Convert.Unit.ETHER).toPlainString());
        System.out.println(Convert.fromWei(gasPrice, Convert.Unit.GWEI).toPlainString() + " Gwei" + System.getProperty("line.separator"));

        return gasPrice.toBigInteger();
    }

    private static int getGas() {
        String url = "https://ethgasstation.info/json/ethgasAPI.json";
        String response = Http.sendRequest(url, "POST");

        Gson gson = new Gson();
        Gas gas = gson.fromJson(response, Gas.class);
        return gas.getFast();
    }
}
