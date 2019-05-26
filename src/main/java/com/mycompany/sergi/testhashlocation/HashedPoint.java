/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.sergi.testhashlocation;

import ch.hsr.geohash.GeoHash;

/**
 *
 * @author gabalca
 */
public class HashedPoint extends Point{
    
    private final String hash;

    public HashedPoint(String hash, double lat, double lon) {
        super(lat, lon);
        this.hash = hash;
    }
    
    public HashedPoint(Point p){
        super(p.getLat(),p.getLon());
        hash=
        GeoHash.geoHashStringWithCharacterPrecision(p.getLat(), p.getLon(), 12);
    }

    public String getHash() {
        return hash;
    }

    @Override
    public String toString() {
        return "HashedPoint{lat="+getLat()+", long="+getLon()+",hash=" + hash + '}';
    }

}
