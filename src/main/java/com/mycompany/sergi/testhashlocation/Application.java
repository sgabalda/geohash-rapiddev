/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.sergi.testhashlocation;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveAction;
import java.util.stream.Collector;

/**
 *
 * @author gabalca
 */
public class Application {
    
    public static void main(String args[]) throws IOException, URISyntaxException{
        URL res = Application.class.getClassLoader().getResource("test_points.txt");
        Path file=Paths.get(res.toURI());
        
        List<PrefixHashedPoint> orderedPoints=
                Files.lines(file)    //read each line
                    .parallel()                 //comment this line to do sequential
                    .map(str->{
                        try{
                            String strarr[]=str.split(","); //split into lat and long
                            return new Point(
                                    Double.parseDouble(strarr[0]),
                                    Double.parseDouble(strarr[1])
                            );
                        }catch(Exception e){
                            System.out.println("error parsing line: "+str);
                        }
                        return new Point(-1,-1);
                    })
                    .filter(p->p.getLat()>=0)
                    .map(HashedPoint::new)
                    .collect(Collector.of(
                            ()->new ArrayList<>(), //collect in a ordered arraylist
                            (acc,hp) -> {
                                    int index=Collections.binarySearch(acc, hp,Comparator.comparing(HashedPoint::getHash));
                                    if(index>=0){
                                        System.out.println("Duplicat: "+hp+" \n ----> vs "+acc.get(index));
                                    }
                                    else{
                                        acc.add(-index-1,new PrefixHashedPoint(hp));
                                    }
                                },
                            (acc1,acc2) -> {
                                acc1.stream().forEach(hp ->{
                                    int index=Collections.binarySearch(acc2, hp,Comparator.comparing(HashedPoint::getHash));
                                    if(index>=0){
                                        System.out.println("Duplicat: "+hp+" \n ----> vs "+acc2.get(index));
                                    }
                                    else{
                                        acc2.add(-index-1,new PrefixHashedPoint(hp));
                                    }
                                });
                                return acc2;
                            }
                    ));
        
        
        
        
        ForkJoinTask<?> task=new CalculatePrefixTask(0, orderedPoints.size(), orderedPoints);
        ForkJoinPool pool=new ForkJoinPool();
        
        pool.invoke(task);
        
        orderedPoints.forEach(System.out::println);      
        
    }
    
}

class CalculatePrefixTask extends RecursiveAction{
    
    public static final int SEGMENT_SIZE=100;
    
    private int start;
    private int end;
    private List<PrefixHashedPoint> elements;

    public CalculatePrefixTask(int start, int end, List<PrefixHashedPoint> elements) {
        this.start = start;
        this.end = end;
        this.elements = elements;
    }

    @Override
    protected void compute() {
        if(end-start <= SEGMENT_SIZE){
            //compute from start to end-1
            
            //imperative shame :-(
            
            for(int i=start; i<end; i++){
                
                if(i!=0){
                    elements.get(i).updatePrefixToNotCollide(elements.get(i-1));
                }
                if(i!=elements.size()-1){
                    elements.get(i).updatePrefixToNotCollide(elements.get(i+1));
                }
            }
            
        }else{
            int middle = start+((end-start)/2);
            invokeAll(new CalculatePrefixTask(start, middle, elements),
                    new CalculatePrefixTask(middle, end, elements));
        }
    }
    
}

