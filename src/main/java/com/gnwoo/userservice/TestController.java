package com.gnwoo.userservice;

import com.gnwoo.userservice.data.dto.*;
import com.gnwoo.userservice.data.request.*;
import com.gnwoo.userservice.data.repo.RelationRepo;
import com.gnwoo.userservice.data.repo.InviteRepo;
import com.gnwoo.userservice.data.repo.UserRepo;
import com.gnwoo.userservice.data.table.Relation;
import com.gnwoo.userservice.data.table.Invite;
import com.gnwoo.userservice.data.table.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class TestController {
    @Autowired
    private RelationRepo relationRepo;
    @Autowired
    private InviteRepo inviteRepo;

    // @TODO: rpc repo
    @Autowired
    private UserRepo userRepo;

    @GetMapping(path="/contacts")
    public ResponseEntity<List<ContactInUserView>> getContacts(@CookieValue Long uuid) {
        List<Object[]> relations = relationRepo.findRelationsOf(uuid);
        List<Long> uuids = new ArrayList<>();
        List<Long> rIDs = new ArrayList<>();
        relations.forEach(r -> {
            uuids.add((Long) r[1]);
            rIDs.add((Long) r[0]);
        });
        List<User> users = new ArrayList<>();

        // @TODO: rpc call, findAllByUUIDs(uuids: List<Long> (stream in rpc)) return User Stream
        userRepo.findAllById(uuids).iterator().forEachRemaining(users::add);

        List<ContactInUserView> rv = new LinkedList<>();
        for (int i = 0; i < uuids.size(); i++) {
            rv.add(new ContactInUserView(users.get(i), rIDs.get(i)));
        }
        return ResponseEntity.ok(rv);
    }

    @GetMapping(path="/contact-invites")
    public ResponseEntity<List<InviteInUserView>> contactInvites(@CookieValue Long uuid) {
        List<Object[]> requests = inviteRepo.findInviteFrom(uuid);
        List<Long> ids = new ArrayList<>();
        List<Long> uuids = new ArrayList<>();
        requests.forEach(r -> {
            ids.add((Long) r[0]);
            uuids.add((Long) r[1]);
        });
        List<User> users = new ArrayList<>();

        // @TODO: rpc call, findAllByUUIDs(uuids: List<Long> (stream in rpc)) return User Stream
        userRepo.findAllById(uuids).iterator().forEachRemaining(users::add);

        List<InviteInUserView> rv = new LinkedList<>();
        for (int i = 0; i < uuids.size(); i++) {
            rv.add(new InviteInUserView(ids.get(i), users.get(i)));
        }
        return ResponseEntity.ok(rv);
    }

    @GetMapping(path="/contact-search")
    public ResponseEntity<UserInSearchView> contactSearch (@RequestParam String username) {

        // @TODO: rpc call, findByUsername(username: string) return User
        List<User> users = userRepo.findByUsername(username);

        if (!users.isEmpty()) {
            return ResponseEntity.ok(new UserInSearchView(users.get(0)));
        }
        return ResponseEntity.ok().build();
    }

    @PostMapping(path="/contact-invite")
    public ResponseEntity<InviteInUserView> addFriend (@CookieValue Long uuid,
                                                       @RequestBody ContactInvitePostRequest req) {

        // @TODO: rpc call, findByUsername(username: string) return User
        List<User> users = userRepo.findByUsername(req.getUsername());

        if (!users.isEmpty()) {
            User user = users.get(0);
            Invite invite = inviteRepo.save(new Invite(uuid, user.getUuid()));
            return ResponseEntity.ok(new InviteInUserView(invite.getId(), user));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @PostMapping(path="/contact-invite-response")
    public ResponseEntity<ContactInUserView> contactInviteResponse (@CookieValue Long uuid,
                                                                    @RequestBody ContactInviteResponsePostRequest req) {
        Optional<Invite> inviteOptional = inviteRepo.findById(req.getId());
        if (inviteOptional.isPresent()) {
            if (req.getResponse()) {
                Invite invite = inviteOptional.get();

                // @TODO: rpc call, findByUUID(uuid: Long)) return User
                User userA = userRepo.findById(invite.getUuidA()).get();

                Relation newRelation = relationRepo.save(new Relation(userA.getUuid(), uuid));
                return ResponseEntity.ok(new ContactInUserView(userA, newRelation.getrID()));
            } else {
                return ResponseEntity.ok().build();
            }
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @PostMapping(path="/unfriend")
    public ResponseEntity unfriend (@RequestBody unfriendPostRequest req) {
        if (!relationRepo.findByrID(req.getrID()).isEmpty()) {
            relationRepo.deleteRelationByrID(req.getrID());
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    // mock auth sign up, ignore password
    @PostMapping(path="/sign-up")
    public ResponseEntity signUp (@RequestBody SignUpPostRequest req) {
        try {
            userRepo.save(new User(req.getUsername(), req.getDisplayName(), req.getEmail()));
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    // mock auth sign up, ignore password
    @PostMapping(path="/login")
    public ResponseEntity<UserInLoggedInView> login (@RequestBody LoginPostRequest req) {
        List<User> users = userRepo.findByUsername(req.getUsername());
        if (!users.isEmpty()) {
            User user = users.get(0);
            HttpHeaders headers = new HttpHeaders();
            headers.add("Set-Cookie", "JWT=MSFDC");
            headers.add("Set-Cookie", "uuid=" + user.getUuid());
            return ResponseEntity.ok().headers(headers).body(new UserInLoggedInView(user));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    // mock auth, no auth on JWT
    @GetMapping(path="/auth-status")
    public ResponseEntity<UserInLoggedInView> authStatus (@CookieValue Long uuid, @CookieValue String JWT) {
        Optional<User> users = userRepo.findById(uuid);
        if (users.isPresent()) {
            return ResponseEntity.ok(new UserInLoggedInView(users.get()));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
}
