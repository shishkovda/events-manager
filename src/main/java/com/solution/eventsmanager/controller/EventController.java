package com.solution.eventsmanager.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.solution.eventsmanager.entity.Event;
import com.solution.eventsmanager.representation.EventRepresentation;
import com.solution.eventsmanager.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.xml.ws.Response;
import java.util.*;

@RestController
@RequestMapping("/api/events")
public class EventController {

    @Autowired
    EventService eventService;

    @Autowired
    ObjectMapper objectMapper;


    @PostMapping("")
    public void createEvent(@RequestBody EventRepresentation eventRepresentation){
        Event event = new Event();
        String title = eventRepresentation.getTitle();
        if (title!=null){
            event.setTitle(title);
        }
        String date = eventRepresentation.getDate();
        if (date!=null){
            event.setDate(date);
        }
        String assignee = eventRepresentation.getAssignee();
        if (assignee!=null){
            event.setAssignee(Long.valueOf(assignee));
        }
        eventService.createEvent(event);
    }

    @GetMapping("")
    public ResponseEntity<List<Event>> createEvent(){
        return new ResponseEntity<>(eventService.getAllEvents(), HttpStatus.OK);
    }

//ToDo: check update method
    @PostMapping("/{id}")
    public void updateEvent(@PathVariable("id") Long id,
                            @RequestBody EventRepresentation eventRepresentation){

        Event event = eventService.getEventById(id);

        String title = eventRepresentation.getTitle();
        if (title!=null){
            event.setTitle(title);
        }
        String date = eventRepresentation.getDate();
        if (date!=null){
            event.setDate(date);
        }
        String assignee = eventRepresentation.getAssignee();
        if (assignee!=null){
            event.setAssignee(Long.valueOf(assignee));
        }

        eventService.createEvent(event);
    }



}
