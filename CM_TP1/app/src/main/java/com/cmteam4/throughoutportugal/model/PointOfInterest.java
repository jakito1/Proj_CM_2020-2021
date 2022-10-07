package com.cmteam4.throughoutportugal.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class PointOfInterest {
    private final String uniqueID;

    public List<String> getLikeList() {
        return likeList;
    }

    public List<String> getDislikeList() {
        return dislikeList;
    }

    public List<String> getFavouriteList() {
        return favouriteList;
    }

    private final List<String> likeList;
    private final List<String> dislikeList;
    private final List<String> favouriteList;
    private final String regionOfPoint;
    private final double latitude;
    private final double longitude;
    private final String imgPoint;
    private String txtTitle;

    public PointOfInterest(String txtTitle, String imgPoint, double latitude, double longitude, String regionOfPoint) {
        this.uniqueID = UUID.randomUUID().toString();
        this.txtTitle = txtTitle;
        this.imgPoint = imgPoint;
        this.likeList = new ArrayList<>();
        this.dislikeList = new ArrayList<>();
        this.favouriteList = new ArrayList<>();
        this.latitude = latitude;
        this.longitude = longitude;
        this.regionOfPoint = regionOfPoint;
    }

    public PointOfInterest(String txtTitle, String imgPoint, double latitude, double longitude,
                           String regionOfPoint, String uniqueID, ArrayList<String> likeList,
                           ArrayList<String> dislikeList, ArrayList<String> favouriteList) {
        this.uniqueID = uniqueID;
        this.txtTitle = txtTitle;
        this.imgPoint = imgPoint;
        this.likeList = likeList;
        this.dislikeList = dislikeList;
        this.favouriteList = favouriteList;
        this.latitude = latitude;
        this.longitude = longitude;
        this.regionOfPoint = regionOfPoint;
    }

    public String getUniqueID() {
        return uniqueID;
    }

    public String getTxtTitle() {
        return txtTitle;
    }

    public void setTxtTitle(String txtTitle) {
        this.txtTitle = txtTitle;
    }

    public String getImgPoint() {
        return imgPoint;
    }

    public int getAmountLikes() {
        return likeList.size();
    }

    public void clearLikeList() {
        likeList.clear();
    }

    public boolean checkLiked(String userUID) {
        return likeList.contains(userUID);
    }

    public void addLike(String userUID) {
        this.likeList.add(userUID);
    }

    public void removeLike(String userUID) {
        this.likeList.remove(userUID);
    }

    public int getAmountDislikes() {
        return dislikeList.size();
    }

    public void clearDislikeList() {
        dislikeList.clear();
    }

    public boolean checkDislike(String userUID) {
        return dislikeList.contains(userUID);
    }

    public void addDislike(String userUID) {
        this.dislikeList.add(userUID);
    }

    public void removeDislike(String userUID) {
        this.dislikeList.remove(userUID);
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getRegionOfPoint() {
        return regionOfPoint;
    }

    public void addToFavourites(String userUID) {
        favouriteList.add(userUID);
    }

    public void removeFromFavourites(String userUID) {
        favouriteList.remove(userUID);
    }

    public boolean isFavourite(String userUID) {
        return favouriteList.contains(userUID);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PointOfInterest that = (PointOfInterest) o;
        return getUniqueID().equals(that.getUniqueID());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUniqueID());
    }
}
