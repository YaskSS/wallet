package com.example.yass.wallet.utilities;

import android.util.Log;

import com.example.yass.wallet.bip32.ByteUtils;
import com.example.yass.wallet.bip32.ExtendedKey;
import com.example.yass.wallet.bip32.Hash;
import com.example.yass.wallet.bip32.ValidationException;

import org.bitcoinj.core.DumpedPrivateKey;
import org.bitcoinj.core.ECKey;
import org.bitcoinj.crypto.DeterministicKey;
import org.bitcoinj.crypto.HDKeyDerivation;
import org.bitcoinj.crypto.MnemonicCode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by User on 03.10.2017.
 */

public class Bip32 {
    //0x80000000
    final static int ACCOUNT_BASE = 0x80000000;
    private static final String TAG = "bip32";
    private String words;
    private static ExtendedKey extendedKey;
    private static ExtendedKey accountM0;

    // Start with an extended PRIVATE key
    private static DeterministicKey key;
    // Create two accounts
    private static DeterministicKey account_0;
    // Create internal and external chain on Account 0
    private static DeterministicKey chain_EX;
    private static DeterministicKey chain_IN;

    public Bip32(String words) {
        this.words = words;
        try {
            extendedKey = ExtendedKey.create(getMnemonic(words));
            accountM0 = extendedKey.getChild(ACCOUNT_BASE);

            key = HDKeyDerivation.createMasterPrivateKey(getMnemonic(words));

            account_0 = HDKeyDerivation.deriveChildKey(key, 0x80000000);

            chain_EX = HDKeyDerivation.deriveChildKey(account_0, 0);
            chain_IN = HDKeyDerivation.deriveChildKey(account_0, 1);
        } catch (ValidationException e) {
            e.printStackTrace();
        }
    }

    public String getExternalAdresses(int start, int end) throws ValidationException {
        String result = "";
        ExtendedKey exteernalPath = accountM0.getChild(0);
        for (int i = start; i < end; i++) {
            if (i == end - 1) {
                result += exteernalPath.getChild(i).getAddress();
            } else {
                result += exteernalPath.getChild(i).getAddress() + ",";
            }

        }
        return result;
    }

    private String getAddressFromPubKey(byte[] pubKey) {
        byte[] addr = Hash.keyHash(pubKey);
        byte[] baddr = new byte[addr.length + 5];
        baddr[0] = 23;
        System.arraycopy(addr, 0, baddr, 1, addr.length);//
        byte[] ck = Hash.hash(baddr, 0, addr.length + 1);
        System.arraycopy(ck, 0, baddr, addr.length + 1, 4); //8
        return ByteUtils.toBase58(baddr);
    }

    private byte[] getMnemonic(String words) {
        return MnemonicCode.toSeed(Arrays.asList(words.split(" ")), "");
    }
}
