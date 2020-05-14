package com.solution.eventsmanager.controller.web;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.solution.eventsmanager.entity.Event;
import com.solution.eventsmanager.entity.EventView;
import com.solution.eventsmanager.entity.User;
import com.solution.eventsmanager.entity.UsersEventsTemplate;
import com.solution.eventsmanager.service.EventService;
import com.solution.eventsmanager.service.UserEventsTemplateService;
import com.solution.eventsmanager.service.UserService;
import com.solution.eventsmanager.utils.HttpRequestor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
public class MainWebController {

    @Autowired
    EventService eventService;

    @Autowired
    UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    UserEventsTemplateService userEventsTemplateService;

    @GetMapping("/")
    public String greeting(Model model) {
        List<Event> events = eventService.getAllEvents();
        List<EventView> eventViews = new ArrayList<>();

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = userService.getUserByLogin(auth.getName());

        for(Event event:events){
            if(event.getTitle().contains(currentUser.getLastName())){
                continue;
            }
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

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/profile")
    public String profile(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        User user = userService.getUserByLogin(username);
        model.addAttribute("user", user);
        return "profile";
    }

    @PostMapping("/profile")
    public String updatProfile(@ModelAttribute User user, Model model) {
        User userfromDB = userService.getUserByLogin(user.getLogin());

        userfromDB.setPassword(passwordEncoder.encode(user.getPassword()));
        userService.updateUser(userfromDB);
        model.addAttribute("user", user);
        return "profile";
    }


}