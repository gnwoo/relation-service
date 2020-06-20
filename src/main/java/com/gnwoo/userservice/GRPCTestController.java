package com.gnwoo.userservice;

import com.gnwoo.userservice.authRPC.AuthProto;
import com.gnwoo.userservice.authRPC.AuthServiceGrpc;
import com.gnwoo.userservice.data.dto.UserDTO;
import com.gnwoo.userservice.data.repo.UserRepo;
import com.gnwoo.userservice.data.req.SignUpRequest;
import com.gnwoo.userservice.data.table.User;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*")
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

//    @PostMapping(path="/signUp")
    public ResponseEntity<UserDTO> signUp (@RequestBody SignUpRequest req) {
        System.out.println("收到！");
        try {
            User user = new User(req.getUsername(), req.getDisplayName(), req.getEmail());
            System.out.println("准备杀死auth");
            AuthProto.GeneralResponse response = blockingStub.signUp(AuthProto.PasswordRequest
                    .newBuilder()
                    .setUuid(userRepository.save(user).getUuid().toString())
                    .setPassword(req.getPassword())
                    .build());
            System.out.println(response.getFeedback());
            if (response.getDecision()) {
                return new ResponseEntity<>(new UserDTO(user), HttpStatus.ACCEPTED);
            } else {
                return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }
}
