package com.gnwoo.userservice;

import com.gnwoo.userservice.data.dto.UserDTO;
import com.gnwoo.userservice.data.repo.ContactRepo;
import com.gnwoo.userservice.data.repo.FriendRequestRepo;
import com.gnwoo.userservice.data.repo.UserRepo;
import com.gnwoo.userservice.data.req.LoginRequest;
import com.gnwoo.userservice.data.req.SignUpRequest;
import com.gnwoo.userservice.data.table.Contact;
import com.gnwoo.userservice.data.table.FriendRequest;
import com.gnwoo.userservice.data.table.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedList;
import java.util.List;

//@RestController
//@CrossOrigin(origins = "*")
public class TestController {
    @Autowired
    private UserRepo userRepository;
    @Autowired
    private ContactRepo contactRepo;
    @Autowired
    private FriendRequestRepo friendRequestRepo;

    @PostMapping(path="/getContacts")
    public @ResponseBody List<UserDTO> getContacts(@RequestParam Long uuid) {
        List<Long> relations = contactRepo.findRelationsFor(uuid);
        List<UserDTO> rv = new LinkedList<>();
        userRepository.findAllById(relations).forEach(e -> {
            rv.add(new UserDTO(e));
        });
        return rv;
    }

    @PostMapping(path="/addFriend")
    public @ResponseBody String addFriend (@RequestParam Long uuid,
                                           @RequestParam Long friendUUID) {
        if (!uuid.equals(friendUUID) && friendRequestRepo.findRequestFromTo(uuid, friendUUID).isEmpty()) {
            friendRequestRepo.save(new FriendRequest(uuid, friendUUID));
            return "TRUE";
        }
        return "FALSE";
    }

    @PostMapping(path="/getRequestsToMe")
    public @ResponseBody List<UserDTO> getRequestsToMe (@RequestParam Long uuid) {
        List<Long> requests = friendRequestRepo.findRequestTo(uuid);
        List<UserDTO> rv = new LinkedList<>();
        userRepository.findAllById(requests).forEach(e -> {
            rv.add(new UserDTO(e));
        });
        return rv;
    }

    @PostMapping(path="/getRequestsFromMe")
    public @ResponseBody List<UserDTO> getRequestsFromMe (@RequestParam Long uuid) {
        List<Long> requests = friendRequestRepo.findRequestFrom(uuid);
        List<UserDTO> rv = new LinkedList<>();
        userRepository.findAllById(requests).forEach(e -> {
            rv.add(new UserDTO(e));
        });
        return rv;
    }

    // example for auth right here
    @PostMapping(path="/acceptRequest")
    public @ResponseBody String getRequestsFromMe (@RequestParam Long uuid,
                                                   @RequestParam Long requesterUUID) {
        List<Long> requests = friendRequestRepo.findRequestFromTo(requesterUUID, uuid);
        if (!requests.isEmpty()) {
            friendRequestRepo.deleteById(requests.get(0));
            contactRepo.save(new Contact(requesterUUID, uuid));
            return "TRUE";
        }
        return "no such request";
    }

    @PostMapping(path="/unfriend")
    public @ResponseBody String unfriend (@RequestParam Long uuid,
                                          @RequestParam Long friendUUID) {
        List<Long> relations = contactRepo.findRelationBetween(uuid, friendUUID);
        if (!relations.isEmpty()) {
            contactRepo.deleteById(relations.get(0));
            return "TRUE";
        }
        return "FALSE";
    }

//    @PostMapping(path="/signUp")
    public ResponseEntity<UserDTO> signUp (@RequestBody SignUpRequest req) {
        try {

            User user = new User(req.getUsername(), req.getDisplayName(), req.getEmail());
            userRepository.save(user);
            return new ResponseEntity<>(new UserDTO(user), HttpStatus.ACCEPTED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    // no auth
//    @PostMapping(path="/login")
    public ResponseEntity<UserDTO> login (@RequestBody LoginRequest req) {
        List<User> user = userRepository.findByUsername(req.getUsername());
        if (!user.isEmpty()) {
            return new ResponseEntity<>(new UserDTO(user.get(0)), HttpStatus.ACCEPTED);
        } else {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }
}
