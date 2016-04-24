package com.abile2.stockcircuit.model;

public class StockVideo {

    public String id;
    public String name;
    private String fullid;
    private String nseid;
    private String description;
    private String video_url;
    private String thumbnail_url;
    private String is_active;
    private String display_seq;





    public StockVideo()
    {

    }


    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }


    public String getNseid() {
        return nseid;
    }
    public void setNseid(String nseid) {
        this.nseid = nseid;
    }


    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }


    public String getFullid() {
        return fullid;
    }


    public void setFullid(String fullid) {
        this.fullid = fullid;
    }


    public String getDescription() {
        return description;
    }


    public void setDescription(String description) {
        this.description = description;
    }


    public String getVideo_url() {
        return video_url;
    }


    public void setVideo_url(String video_url) {
        this.video_url = video_url;
    }


    public String getThumbnail_url() {
        return thumbnail_url;
    }


    public void setThumbnail_url(String thumbnail_url) {
        this.thumbnail_url = thumbnail_url;
    }


    public String getIs_active() {
        return is_active;
    }


    public void setIs_active(String is_active) {
        this.is_active = is_active;
    }


    public String getDisplay_seq() {
        return display_seq;
    }


    public void setDisplay_seq(String display_seq) {
        this.display_seq = display_seq;
    }



}
