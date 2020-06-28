package com.gnwoo.userservice;

import com.gnwoo.userservice.data.dto.request.*;
import com.gnwoo.userservice.data.dto.contact.GroupContact;
import com.gnwoo.userservice.data.dto.contact.UserContact;
import com.gnwoo.userservice.data.dto.invite.GroupInvite;
import com.gnwoo.userservice.data.dto.invite.UserInvite;
import com.gnwoo.userservice.data.dto.searchresult.GroupSearchResult;
import com.gnwoo.userservice.data.dto.searchresult.UserSearchResult;
import com.gnwoo.userservice.data.database.table.entity.GroupEntity;
import com.gnwoo.userservice.data.database.table.entity.UserEntity;
import com.gnwoo.userservice.rpc.dto.UserInfo;
import com.gnwoo.userservice.rpc.database.repo.UserRepo;
import com.gnwoo.userservice.rpc.dto.request.LoginRequest;
import com.gnwoo.userservice.rpc.dto.request.SignUpRequest;
import com.gnwoo.userservice.rpc.database.table.User;
import com.gnwoo.userservice.service.RelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class TestController {
    @Autowired
    RelationService relationService;
    @Autowired
    private UserRepo userRepo; // user service mock

    @GetMapping(path="/user-contacts")
    public ResponseEntity<List<UserContact>> getUserContacts(@CookieValue String uuid) {
        List<UserContact> userContacts = new ArrayList<>();
        relationService.getUserContacts(uuid).forEach(objects -> {
            UserEntity userContact = (UserEntity) objects[0];
            String rID = (String) objects[1];
            userContacts.add(new UserContact(userContact, rID));
        });
        return ResponseEntity.ok(userContacts);
    }

    @GetMapping(path="/group-contacts")
    public ResponseEntity<List<GroupContact>> getGroupContacts(@CookieValue String uuid) {
        List<GroupContact> groupContacts = new ArrayList<>();
        relationService.getGroupContacts(uuid).forEach(objects -> {
            GroupEntity groupEntity = (GroupEntity) objects[0];
            String rID = (String) objects[1];
            groupContacts.add(new GroupContact(groupEntity, rID));
        });
        return ResponseEntity.ok(groupContacts);
    }

    @GetMapping(path="/user-search")
    public ResponseEntity<UserSearchResult> userSearch (@RequestParam String username) {
        UserEntity userEntity = relationService.userSearch(username);
        if (userEntity != null) {
            return ResponseEntity.ok(new UserSearchResult(userEntity));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @GetMapping(path="/group-search")
    public ResponseEntity<GroupSearchResult> groupSearch (@RequestParam String groupID) {
        GroupEntity groupEntity = relationService.groupSearch(groupID);
        if (groupEntity != null) {
            return ResponseEntity.ok(new GroupSearchResult(groupEntity));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @PostMapping(path="/user-invite")
    public ResponseEntity<Void> postUserInvite (@CookieValue String uuid, @RequestBody UserInviteRequest req) {
        if (relationService.postUserInvite(uuid, req.getUsername())) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @PostMapping(path="/group-invite")
    public ResponseEntity<Void> postGroupInvite (@CookieValue String uuid, @RequestBody GroupInviteRequest req) {
        if (relationService.postGroupInvite(uuid, req.getGroupID())) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @GetMapping(path="/user-invites")
    public ResponseEntity<List<UserInvite>> userInvites(@CookieValue String uuid) {
        List<UserInvite> userInvites = new ArrayList<>();
        relationService.userInvites(uuid).forEach(objects -> {
            UserEntity userEntity = (UserEntity) objects[0];
            Long inviteID = (Long) objects[1];
            userInvites.add(new UserInvite(userEntity, inviteID));
        });
        return ResponseEntity.ok(userInvites);
    }

    @GetMapping(path="/group-invites")
    public ResponseEntity<List<GroupInvite>> groupInvites(@CookieValue String uuid) {
        List<GroupInvite> groupInvites = new ArrayList<>();
        relationService.groupInvites(uuid).forEach(objects -> {
            GroupEntity groupEntity = (GroupEntity) objects[0];
            Long inviteID = (Long) objects[1];
            groupInvites.add(new GroupInvite(groupEntity, inviteID));
        });
        return ResponseEntity.ok(groupInvites);
    }

    @PostMapping(path="/user-invite-response")
    public ResponseEntity<Void> userInviteResponse (@CookieValue String uuid, @RequestBody InviteResponseRequest req) {
        if (relationService.userInviteResponse(uuid, req.getInviteID(), req.getDecision())) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @PostMapping(path="/group-invite-response")
    public ResponseEntity<Void> groupInviteResponse (@CookieValue String uuid, @RequestBody InviteResponseRequest req) {
        if (relationService.groupInviteResponse(uuid, req.getInviteID(), req.getDecision())) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @DeleteMapping(path="/user-contact")
    public ResponseEntity<Void> deleteUserContact (@CookieValue String uuid, @RequestBody DeleteContactRequest req) {
        if (relationService.deleteUserContact(uuid, req.getrID())) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @DeleteMapping(path="/group-contact")
    public ResponseEntity<String> deleteGroupContact (@CookieValue String uuid, @RequestBody DeleteContactRequest req) {
        if (relationService.deleteGroupContact(uuid, req.getrID())) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    // mock auth sign up, ignore password
    @PostMapping(path="/sign-up")
    public ResponseEntity<Void> signUp (@RequestBody SignUpRequest req) {
        try {
            userRepo.save(new User(req.getUsername(), req.getDisplayName(), req.getEmail()));
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    // mock auth sign up, ignore password
    @PostMapping(path="/login")
    public ResponseEntity<UserInfo> login (@RequestBody LoginRequest req) {
        Optional<User> userOp = userRepo.findByUsername(req.getUsername());
        if (userOp.isPresent()) {
            User user = userOp.get();
            HttpHeaders headers = new HttpHeaders();
            headers.add("Set-Cookie", "JWT=MSFDC");
            headers.add("Set-Cookie", "uuid=" + user.getUuid());
            return ResponseEntity.ok().headers(headers).body(new UserInfo(user));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    // mock auth, no auth on JWT
    @GetMapping(path="/authentication-status")
    public ResponseEntity<UserInfo> authenticationStatus (@CookieValue String uuid, @CookieValue String JWT) {
        return userRepo.findById( Long.parseLong(uuid.substring(2)) )
                .map(user -> ResponseEntity.ok(new UserInfo(user)))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.BAD_REQUEST).build());
    }
}
