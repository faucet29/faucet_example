package com.faucet.map;

/**
 * Created by faucet on 2017/11/20.
 */

public class MapWebApiUtils {

    private final String webApiKey = "e62830d390ffbc0873746a078ab55913";

    private String markerIcon = "http://api.map.baidu.com/images/marker_red.png";
    private double lat = 0;
    private double lon = 0;
    private int width;
    private int height;
    private int zoom = 18;
    private String labContent;

    public MapWebApiUtils(double lat, double lon, int width, int height) {
        this.lat = lat;
        this.lon = lon;
        this.width = width;
        this.height = height;
    }

    public String getMarkerIcon() {
        return markerIcon;
    }

    public void setMarkerIcon(String markerIcon) {
        this.markerIcon = markerIcon;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getZoom() {
        return zoom;
    }

    public void setZoom(int zoom) {
        this.zoom = zoom;
    }

    public String getLabContent() {
        return labContent;
    }

    public void setLabContent(String labContent) {
        this.labContent = labContent;
    }

    public String getStaticMapUrl() {
        String labContentParams = "";
        if (getLabContent() != null && !getLabContent().isEmpty()) {
            labContentParams = "&labels=" + getLabContent() + ",2,0,18,0xffffff,0x00A0FF:" + getLat() + "," + getLon();
        }
        String url = "http://restapi.amap.com/v3/staticmap?location=" + getLat() + "," + getLon() + "&zoom=" + getZoom() + "&size=" + getWidth() + "*" + getHeight() + "&markers=-1," + getMarkerIcon() + ",0:" + getLat() + "," + getLon() + labContentParams + "&key=" + webApiKey;
        return url;
    }
}