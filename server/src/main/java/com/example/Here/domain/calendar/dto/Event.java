package com.example.Here.domain.calendar.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Event {

        private String title;
        private Time time;
        private String description;
        private Location location;

    @Getter
    @Setter
    public static class Time {
        private String start_at;

        private String end_at;

        private String time_zone;

        @Override
        public String toString() {
            return "Time{" +
                    "start_at='" + start_at + '\'' +
                    ", end_at='" + end_at  + '\'' +
                    ", time_zone='" + time_zone + '}';
        }

    }

    @Getter
    @Setter
    public static class Location {

        private String name;

        @Override
        public String toString() {
            return "Location{" +
                    "name='" + name + '\'' +
                    '}';
        }

    }

    @Override
    public String toString() {
        return "Event{" +
                "title='" + title + '\'' +
                ", time=" + time +
                ", description='" + description + '\'' +
                ", location=" + location +
                '}';
    }

}
