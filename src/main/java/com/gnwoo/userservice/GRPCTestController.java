package com.gnwoo.userservice;

import com.gnwoo.userservice.authRPC.AuthProto;
import com.gnwoo.userservice.authRPC.AuthServiceGrpc;
import com.gnwoo.userservice.data.repo.UserRepo;
import com.gnwoo.userservice.data.req.LoginRequest;
import com.gnwoo.userservice.data.req.SignUpRequest;
import com.gnwoo.userservice.data.table.User;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class GRPCTestController {
    @Autowired
    private UserRepo userRepository;

    private final AuthServiceGrpc.AuthServiceBlockingStub blockingStub;
    private final AuthServiceGrpc.AuthServiceStub asyncStub;

    public GRPCTestController() {
        ManagedChannel channel = ManagedChannelBuilder.forTarget("localhost:8081").usePlaintext().build();
        blockingStub = AuthServiceGrpc.newBlockingStub(channel);
        asyncStub = AuthServiceGrpc.newStub(channel);
    }

    @PostMapping(path="/signUp")
    @Transactional
    public ResponseEntity<String> signUp (@RequestBody SignUpRequest req) {
        try {
            User user = new User(req.getUsername(), req.getDisplayName(), req.getEmail());
            Long uuid = userRepository.save(user).getUuid();
            AuthProto.GeneralResponse res = blockingStub.withDeadlineAfter(5, TimeUnit.SECONDS)
                    .signUp(AuthProto.PasswordRequest.newBuilder()
                            .setUuid(uuid)
                            .setPassword(req.getPassword()).build());
            if (res != null) {
                if (res.getDecision()) {
                    return ResponseEntity.ok().build();
                }
            } else {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            }
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @PostMapping(path="/login")
    public ResponseEntity<User> login (@RequestBody LoginRequest req) {
        System.out.println("1");
        List<User> users = userRepository.findByUsername(req.getUsername());
        if (!users.isEmpty()) {
            User user = users.get(0);
            AuthProto.CredentialResponse res = blockingStub.withDeadlineAfter(5, TimeUnit.SECONDS).login(
                    AuthProto.PasswordRequest.newBuilder()
                            .setUuid(user.getUuid())
                            .setPassword(req.getPassword()).build());
            if (res != null && res.getDecision()) {
                HttpHeaders headers = new HttpHeaders();
                headers.set("Set-Cookie", "JWT=" + res.getJWT());
//                headers.set("Set-Cookie", "HttpOnly");
                return ResponseEntity.ok()
                        .headers(headers)
                        .body(user);
            }
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
}
