package com.nrs1209.travelapp.external.google.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class GooglePlacesDTO {

    private List<PlaceItem> places;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class PlaceItem {
        private String id;
        private DisplayName displayName;
        private String formattedAddress;
        private Location location;
        private Double rating;
        private Integer userRatingCount;
        private List<Photo> photos;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class DisplayName {
        private String text;
        private String languageCode;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Location {
        private Double latitude;
        private Double longitude;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Photo {
        private String name;
        private Integer widthPx;
        private Integer heightPx;
    }
}
