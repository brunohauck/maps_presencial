package com.mentorandroid.myapplication;

import java.io.Serializable;

/**
 * Created by BrunoHauck on 3/20/16.
 */

public class MapItem implements Serializable {

    public final static String FORMATTED_ADDRESS = "formatted_address";
    private Long mapItemId;
    private String formatted_address;
    private Geometry geometry;
    private boolean flag_display_all;

    public Long getMapItemId() {
        return mapItemId;
    }

    public void setMapItemId(Long mapItemId) {
        this.mapItemId = mapItemId;
    }

    public boolean isFlag_display_all() {
        return flag_display_all;
    }

    public void setFlag_display_all(boolean flag_display_all) {
        this.flag_display_all = flag_display_all;
    }

    public String getFormatted_address() {
        return formatted_address;
    }

    public void setFormatted_address(String formatted_address) {
        this.formatted_address = formatted_address;
    }

    public Geometry getGeometry() {
        return geometry;
    }

    public void setGeometry(Geometry geometry) {
        this.geometry = geometry;
    }
}
