/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.sergi.testhashlocation;

import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 *
 * @author gabalca
 */
public class PrefixHashedPoint extends HashedPoint{
    private int prefixDigits;

    public PrefixHashedPoint(String hash, double lat, double lon) {
        this(hash, lat, lon,1);
    }
    private PrefixHashedPoint(String hash, double lat, double lon, int prefixDigits) {
        super(hash, lat, lon);
        this.prefixDigits=prefixDigits;
    }
    public PrefixHashedPoint(HashedPoint hp){
        this(hp.getHash(),hp.getLat(),hp.getLon());
    }
    
    public String getPrefix(){
        return getHash().substring(0, prefixDigits);
    }


    public int getPrefixDigits() {
        return prefixDigits;
    }

    @Override
    public String toString() {
        return "PrefixHashedPoint{lat="+getLat()+", long="+getLon()+", hash="+getHash()+", " + "prefix=" + getPrefix() + '}';
    }
    
    public void updatePrefixToNotCollide(HashedPoint p){
        int index=0;
        String otherHash=p.getHash();
        String thisHash=getHash();
        while(otherHash.charAt(index)==thisHash.charAt(index)){
            index ++;
            if(index==thisHash.length() || index==otherHash.length()){
                System.out.println("hash Collision! "+this+" VS "+p);
                break;
            }
        }
        if(this.prefixDigits<index+1)
            this.prefixDigits=index+1;
    }
    
}
