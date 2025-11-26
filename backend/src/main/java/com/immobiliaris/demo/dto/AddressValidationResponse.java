package com.immobiliaris.demo.dto;

import java.util.List;

public class AddressValidationResponse {
    private boolean valid;
    private List<AddressSuggestion> suggestions;

    public AddressValidationResponse() {
    }

    public AddressValidationResponse(boolean valid, List<AddressSuggestion> suggestions) {
        this.valid = valid;
        this.suggestions = suggestions;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public List<AddressSuggestion> getSuggestions() {
        return suggestions;
    }

    public void setSuggestions(List<AddressSuggestion> suggestions) {
        this.suggestions = suggestions;
    }

    public static class AddressSuggestion {
        private String displayName;
        private String via;
        private String citta;
        private String cap;
        private Double lat;
        private Double lon;
        private String mapUrl;

        public AddressSuggestion() {}

        public AddressSuggestion(String displayName, String via, String citta, String cap, Double lat, Double lon) {
            this.displayName = displayName;
            this.via = via;
            this.citta = citta;
            this.cap = cap;
            this.lat = lat;
            this.lon = lon;
        }

        public String getDisplayName() {
            return displayName;
        }
        public void setDisplayName(String displayName) {
            this.displayName = displayName;
        }
        public String getVia() {
            return via;
        }
        public void setVia(String via) {
            this.via = via;
        }
        public String getCitta() {
            return citta;
        }
        public void setCitta(String citta) {
            this.citta = citta;
        }
        public String getCap() {
            return cap;
        }
        public void setCap(String cap) {
            this.cap = cap;
        }
        public Double getLat() {
            return lat;
        }
        public void setLat(Double lat) {
            this.lat = lat;
        }
        public Double getLon() {
            return lon;
        }
        public void setLon(Double lon) {
            this.lon = lon;
        }
        public String getMapUrl() {
            return mapUrl;
        }
        public void setMapUrl(String mapUrl) {
            this.mapUrl = mapUrl;
        }
    }
}
