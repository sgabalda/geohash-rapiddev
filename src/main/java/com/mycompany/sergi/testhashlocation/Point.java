/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.sergi.testhashlocation;

/**
 *
 * @author gabalca
 */
public class Point {
    
    private double lat;
    private double lon;

    public double getLat() {
        return lat;
    }


    public double getLon() {
        return lon;
    }

    public Point(double lat, double lon) {
        this.lat = lat;
        this.lon = lon;
    }

}
