package com.example.NovaCare.model;

import lombok.Getter;

@Getter
public enum DoctorAvailability {




        ONLINE("Online"),
        AVAILABLE("Available"),
        BUSY("Busy"),
        OFFLINE("Offline");

        private final String displayName;

        DoctorAvailability(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }