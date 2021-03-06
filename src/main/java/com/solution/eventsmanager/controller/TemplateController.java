package com.solution.eventsmanager.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.solution.eventsmanager.entity.Event;
import com.solution.eventsmanager.entity.Template;
import com.solution.eventsmanager.entity.User;
import com.solution.eventsmanager.entity.UsersEventsTemplate;
import com.solution.eventsmanager.representation.EventRepresentation;
import com.solution.eventsmanager.representation.UsersEventsTemplateRepresentation;
import com.solution.eventsmanager.service.EventService;
import com.solution.eventsmanager.service.UserEventsTemplateService;
import com.solution.eventsmanager.utils.HttpRequestor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/templates")
public class TemplateController {
    Logger logger = LoggerFactory.getLogger(TemplateController.class);

    @Autowired
    UserEventsTemplateService userEventsTemplateService;

    @Autowired
    ObjectMapper objectMapper;


    @PostMapping("")
    public void createOrUpdateTemplate(@RequestBody String request){
        logger.info("createOrUpdateTemplate(): request = " + request);

        UsersEventsTemplate usersEventsTemplate = null;

        JsonObject jsonObject = new JsonParser().parse(request).getAsJsonObject();
        Long eventId = Long.valueOf(jsonObject.get("eventId").getAsString());
        Long userId = Long.valueOf(jsonObject.get("userId").getAsString());

        usersEventsTemplate = userEventsTemplateService.getUsersEventsTemplateByEventIdAndUserId(eventId, userId);
        if(usersEventsTemplate==null){
            usersEventsTemplate = new UsersEventsTemplate();
            usersEventsTemplate.setUserId(userId);
            usersEventsTemplate.setEventId(eventId);

            HttpRequestor httpRequestor = new HttpRequestor();

            MediaType mediaType = MediaType.parse("application/json");
            okhttp3.RequestBody body = okhttp3.RequestBody.create(mediaType, request);
            String responseBody = httpRequestor.sendRequest("/api/templates", body, "POST");

            usersEventsTemplate.setTemplateId(Long.valueOf(responseBody));

            userEventsTemplateService.createUserEventsTemplate(usersEventsTemplate);
        } else {
            HttpRequestor httpRequestor = new HttpRequestor();

            MediaType mediaType = MediaType.parse("application/json");
            okhttp3.RequestBody body = okhttp3.RequestBody.create(mediaType, request);
            String responseBody = httpRequestor.sendRequest("/api/templates/"+ usersEventsTemplate.getTemplateId(),
                    body, "PATCH");
        }
    }

    @GetMapping("/{eventId}/{userId}")
    public ResponseEntity<Template> getTemplate(@PathVariable String eventId, @PathVariable String userId){
        logger.info("getTemplate(): eventId = " + eventId);
        logger.info("getTemplate(): userId = " + userId);

        UsersEventsTemplate usersEventsTemplate = new UsersEventsTemplate();

        usersEventsTemplate = userEventsTemplateService
                .getUsersEventsTemplateByEventIdAndUserId(Long.valueOf(eventId), Long.valueOf(userId));

        if(usersEventsTemplate==null){
            return new ResponseEntity<>(null, HttpStatus.OK);
        }

        Long templateId = usersEventsTemplate.getTemplateId();

        HttpRequestor httpRequestor = new HttpRequestor();

        String responseBody = httpRequestor.sendRequest("/api/templates/"+templateId.toString(),
                null, "GET");

        Template template = new Template();

        JsonObject jsonObject = new JsonParser().parse(responseBody).getAsJsonObject();

        template.setRequestor(jsonObject.get("requestor").getAsString());
        template.setTemplate(jsonObject.get("template").getAsString());

        logger.info("getTemplate(): responseBody = " + responseBody);

        return new ResponseEntity<>(template, HttpStatus.OK);
    }
}
