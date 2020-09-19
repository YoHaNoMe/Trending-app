package com.yohanome.moviediscussion.movieutils;

import com.yohanome.moviediscussion.models.Actor;

import java.util.ArrayList;
import java.util.List;

public interface MovieUtils {

    static List<Actor> populateActorData(){
        List<Actor> actors = new ArrayList<>();
        String[] profileImages = {
                "https://cdn.thegentlemansjournal.com/wp-content/uploads/2014/11/stylish-actor-TGJ.00-900x600-c-center.jpg",
                "https://passionbuz.com/wp-content/uploads/2019/11/richest-hollywood-celebrities-Tom-Cruise.jpg",
                "https://i.pinimg.com/originals/16/0d/15/160d15439074dd7db0dd000c38e577d1.jpg",
                "https://qph.fs.quoracdn.net/main-qimg-fa166cb42693e4643faeb6340a056934",
                "https://www.10greatest.com/wp-content/uploads/2017/04/Robert-Downey-Jr.jpg"
        };
        for(int i=1;i<=5;++i){
            actors.add(new Actor(i, "Yussef Hatem", profileImages[i-1]));
        }
        return actors;
    }

    static double rescaleRange(double x, double max, double min, double newMax, double newMin){
        return ((newMax - newMin) * (x-min)) / (max-min);
    }

    static double getRating(double x){
        return rescaleRange(x, 10.0, 1.0, 5.0, 0.5);
    }

    class PostFavourite{
        private int favourite;

        public PostFavourite(int favourite){
            this.favourite = favourite;
        }

        public int getFavourite() {
            return favourite;
        }
    }
}
