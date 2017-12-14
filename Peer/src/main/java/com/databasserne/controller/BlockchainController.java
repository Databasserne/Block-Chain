package com.databasserne.controller;

import com.databasserne.model.Node;

import java.util.ArrayList;
import java.util.List;

public class BlockchainController {

    private static List<Node> blocks = new ArrayList<Node>();

    public BlockchainController() throws Exception {
        Node genesisBlock = Node.getGenesisBlock();

        // Second
        Node secondBlock = new Node();
        secondBlock.setId(2);
        secondBlock.setData("my genesis block!!");
        secondBlock.setPrevious(genesisBlock.getHash());
        secondBlock.setHash(Node.calculateHash(secondBlock));

        // Third
        Node third = new Node();
        third.setId(3);
        third.setData("my genesis block!!");
        third.setPrevious(secondBlock.getHash());
        third.setHash(Node.calculateHash(third));

        // Fourth
        Node fourth = new Node();
        fourth.setId(4);
        fourth.setData("my genesis block!!");
        fourth.setPrevious(third.getHash());
        fourth.setHash(Node.calculateHash(fourth));
        blocks.add(genesisBlock);
        //blocks.add(secondBlock);
        //blocks.add(third);
        //blocks.add(fourth);
    }

    public static List<Node> getBlocks() {
        return blocks;
    }

    public static Node getLatestBlock() {
        return blocks.get(blocks.size()-1);
    }

    public static void addToBlockchain(Node n) {
        blocks.add(n);
    }

    public static int getBlockchainSize() {
        return blocks.size();
    }

    public static boolean replaceBlockchain(List<Node> newBlocks) throws Exception {
        if(isValidChain(newBlocks) && newBlocks.size() > getBlockchainSize()) {
            blocks = newBlocks;
            return true;
        }
        return false;
    }

    private static boolean isValidChain(List<Node> newChain) throws Exception {
        if(!newChain.get(0).equals(Node.getGenesisBlock())) return false;
        Node tmpBlock = newChain.get(0);
        for (int i = 1; i < newChain.size(); i++) {
            if(isValidNewBlock(newChain.get(i), tmpBlock)) {
                tmpBlock = newChain.get(i);
            } else {
                return false;
            }
        }
        return true;
    }

    private static boolean isValidNewBlock(Node newBlock, Node previousBlock) throws Exception {
        if(previousBlock.getId()+1 != newBlock.getId()) return false;
        if(!previousBlock.getHash().equals(newBlock.getPrevious())) return false;
        if(!Node.calculateHash(newBlock).equals(newBlock.getHash())) return false;
        return true;
    }
}
