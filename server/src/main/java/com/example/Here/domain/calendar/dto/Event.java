package com.example.Here.domain.calendar.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Event {

        private String title;
        private Time time;
        private String description;
        private Location location;

    @Getter
    @Setter
    @ToString
    public static class Time {

        private String start_at;

        private String end_at;

        private String time_zone;

    }

    @Getter
    @Setter
    @ToString
    public static class Location {

        private String name;

    }

}
