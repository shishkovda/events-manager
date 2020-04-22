package com.solution.eventsmanager.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.solution.eventsmanager.entity.Event;
import com.solution.eventsmanager.entity.UsersEventsTemplate;
import com.solution.eventsmanager.representation.EventRepresentation;
import com.solution.eventsmanager.service.EventService;
import com.solution.eventsmanager.service.UserEventsTemplateService;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class MainController {

    @Autowired
    EventService eventService;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    UserEventsTemplateService userEventsTemplateService;


    @PostMapping("")
    public void processMessage(@RequestBody EventRepresentation eventRepresentation){
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

    @GetMapping("/payers")
    public ResponseEntity<List<String>> getPayers(@RequestParam("eventId") String eventId, @RequestParam("userId") String userId){
        UsersEventsTemplate usersEventsTemplate = userEventsTemplateService.
                getUsersEventsTemplateByEventIdAndUserId(Long.valueOf(eventId), Long.valueOf(userId));


        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        Request request = new Request.Builder()
                .url("http://84.201.175.97:8080/api/templates/"+usersEventsTemplate.getTemplateId()+"/1")
                .method("GET", null)
                .addHeader("Content-Type", "application/json")
                .build();
        Response response = null;
        String attrId = null;
        try {
            response = client.newCall(request).execute();
            String bodyResponse = response.body().string();
            attrId = bodyResponse;
            int a = 1;
        } catch (IOException e) {
            e.printStackTrace();
        }

        request = new Request.Builder()
                .url("http://84.201.175.97:8080/api/messages?templateId="+usersEventsTemplate.getTemplateId()+"&attrId="+attrId)
                .method("GET", null)
                .addHeader("Content-Type", "application/json")
                .build();
        List<String> result = new ArrayList<>();
        try {
            response = client.newCall(request).execute();

            JsonParser parser = new JsonParser();
            JsonElement tradeElement = parser.parse(response.body().string());
            JsonArray trade = tradeElement.getAsJsonArray();
            for (int i = 0; i < trade.size(); i++) {
                JsonObject object = trade.get(i).getAsJsonObject();
                result.add(object.get("value").getAsString());
            }
            int a = 1;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/{id}")
    public void updateEvent(@PathVariable("id") Long id, @RequestBody EventRepresentation eventRepresentation){

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
