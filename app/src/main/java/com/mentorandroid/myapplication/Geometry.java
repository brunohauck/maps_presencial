package com.mentorandroid.myapplication;

import java.io.Serializable;

/**
 * Created by BrunoHauck on 3/20/16.
 */

public class Geometry implements Serializable {


    private Long geometryId;

    private Double lat;

    private Double lng;

    public Long getGeometryId() {
        return geometryId;
    }

    public void setGeometryId(Long geometryId) {
        this.geometryId = geometryId;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

}
