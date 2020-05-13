package com.solution.eventsmanager.controller.web;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.solution.eventsmanager.entity.Event;
import com.solution.eventsmanager.entity.EventView;
import com.solution.eventsmanager.entity.User;
import com.solution.eventsmanager.entity.UsersEventsTemplate;
import com.solution.eventsmanager.service.EventService;
import com.solution.eventsmanager.service.UserEventsTemplateService;
import com.solution.eventsmanager.service.UserService;
import com.solution.eventsmanager.utils.HttpRequestor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
public class MainWebController {

    @Autowired
    EventService eventService;

    @Autowired
    UserService userService;

    @Autowired
    UserEventsTemplateService userEventsTemplateService;

    @GetMapping("/")
    public String greeting(Model model) {
        List<Event> events = eventService.getAllEvents();
        List<EventView> eventViews = new ArrayList<>();

        for(Event event:events){
            EventView eventView = new EventView();
            eventView.setId(event.getId().toString());
            eventView.setTitle(event.getTitle());
            eventView.setDate(event.getDate());
            User user = userService.getUSerById(event.getAssignee());
            if(user!=null){
                eventView.setAssignee(user.getFirstName() + " " + user.getLastName());
            }

            UsersEventsTemplate usersEventsTemplate = userEventsTemplateService.
                    getUsersEventsTemplateByEventIdAndUserId(event.getId(), event.getAssignee());

            if(usersEventsTemplate!=null){
                HttpRequestor httpRequestor = new HttpRequestor();

                String responseBody = httpRequestor.sendRequest("/api/templates/"+usersEventsTemplate.getTemplateId()+"/1",
                        null, "GET");
                String attrId = responseBody;

                responseBody = httpRequestor.sendRequest("/api/messages?templateId="+usersEventsTemplate.getTemplateId()+"&attrId="+attrId,
                        null, "GET");

                List<String> lastNames = new ArrayList<>();

                JsonParser parser = new JsonParser();
                JsonElement tradeElement = parser.parse(responseBody);
                JsonArray trade = tradeElement.getAsJsonArray();
                for (int i = 0; i < trade.size(); i++) {
                    lastNames.add(trade.get(i).getAsString());
                }

                eventView.setUsers(lastNames.toString().replace("[", "").replace("]", "").replaceAll("%20", " "));
                eventView.setSum(Integer.toString(lastNames.size()*300));
            }

            eventViews.add(eventView);
        }
        model.addAttribute("events", eventViews);
        return "index";
    }

}