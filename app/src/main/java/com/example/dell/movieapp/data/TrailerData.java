package com.example.dell.movieapp.data;

/**
 * Created by dell on 6/20/2016.
 */
public class TrailerData {

    /*
    "id":"571cb2569251415fd0000156",
            "iso_639_1":"en",
            "iso_3166_1":"US",
            "key":"jWM0ct-OLsM",
            "name":"Official US Trailer #2",
            "site":"YouTube",
            "size":1080,
            "type":"Trailer"
    */

    private String id;
    private String key;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
