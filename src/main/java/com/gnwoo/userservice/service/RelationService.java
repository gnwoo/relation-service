package com.gnwoo.userservice.service;

import com.gnwoo.userservice.data.database.repo.GroupRepo;
import com.gnwoo.userservice.data.database.repo.InviteRepo;
import com.gnwoo.userservice.data.database.repo.RelationRepo;
import com.gnwoo.userservice.rpc.database.repo.UserRepo;
import com.gnwoo.userservice.data.database.table.Invite;
import com.gnwoo.userservice.data.database.table.Relation;
import com.gnwoo.userservice.data.database.table.entity.GroupEntity;
import com.gnwoo.userservice.data.database.table.entity.UserEntity;
import com.gnwoo.userservice.rpc.database.table.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RelationService {
    @Autowired
    private RelationRepo relationRepo;
    @Autowired
    private InviteRepo inviteRepo;
    @Autowired
    private GroupRepo groupRepo;

    // TODO: a rpc repo
    @Autowired
    private UserRepo userRepo;

    public List<Object[]> getUserContacts(String _uuid) {
        List<Object[]> userRelations = relationRepo.findUserRelationsOf(_uuid);
        List<Object[]> rv = new ArrayList<>();
        for (Object[] objects : userRelations) {
            String rID = (String) objects[0];
            String uuid = (String) objects[1];

            // TODO: rpc call, findUsersByUUIDs(uuids: List<String> (string stream)) return List<User> (User stream)
            userRepo.findById(idStr2Long(uuid)).ifPresent(user -> rv.add(new Object[]{ new UserEntity(user), rID }));
        }
        return rv;
    }

    public List<Object[]> getGroupContacts(String _uuid) {
        List<Object[]> groupRelations = relationRepo.findGroupRelationsOf(_uuid);
        List<Object[]> rv = new ArrayList<>();
        for (Object[] objects : groupRelations) {
            String rID = (String) objects[0];
            String groupID = (String) objects[1];
            groupRepo.findById(idStr2Long(groupID)).ifPresent(groupEntity -> rv.add(new Object[]{ groupEntity, rID }));
        }
        return rv;
    }

    public UserEntity userSearch(String username) {
        // TODO: rpc call, findByUsername(username: String) return User
        return userRepo.findByUsername(username).map(UserEntity::new).orElse(null);
    }

    public GroupEntity groupSearch(String groupID) {
        return groupRepo.findByEntityID(idStr2Long(groupID)).orElse(null);
    }

    public boolean postUserInvite(String uuid, String username) {
        // TODO: rpc call, findByUsername(username: String) return User
        Optional<User> userOp = userRepo.findByUsername(username);
        if (userOp.isPresent()) {
            User user = userOp.get();
            if (inviteRepo.findUserInviteFromTo(uuid, user.getUuid()).isEmpty()) {
                try {
                    inviteRepo.save(new Invite("u", uuid, user.getUuid()));
                    return true;
                } catch (Exception ignored) { }
            }
        }
        return false;
    }

    public boolean postGroupInvite(String uuid, String groupID) {
        Optional<GroupEntity> groupEntityOp = groupRepo.findByEntityID(idStr2Long(groupID));
        if (groupEntityOp.isPresent()) {
            GroupEntity groupEntity = groupEntityOp.get();
            if (inviteRepo.findGroupInviteFromTo(uuid, groupEntity.getGroupID()).isEmpty()) {
                try {
                    inviteRepo.save(new Invite("g", uuid, groupEntity.getGroupID()));
                    return true;
                } catch (Exception ignored) { }
            }
        }
        return false;
    }

    public List<Object[]> userInvites(String _uuid) {
        List<Object[]> userInvites = inviteRepo.findUserInviteFrom(_uuid);
        List<Object[]> rv = new ArrayList<>();
        for (Object[] objects : userInvites) {
            String inviteID = (String) objects[0];
            String uuid = (String) objects[1];

            // TODO: rpc call, findUsersByUUIDs(uuids: List<String> (string stream)) return List<User> (User stream)
            userRepo.findById(idStr2Long(uuid))
                    .ifPresent(user -> rv.add(new Object[]{ new UserEntity(user), inviteID }));
        }
        return rv;
    }

    public List<Object[]> groupInvites(String _groupID) {
        List<Object[]> groupInvites = inviteRepo.findGroupInviteFrom(_groupID);
        List<Object[]> rv = new ArrayList<>();
        for (Object[] objects : groupInvites) {
            String inviteID = (String) objects[0];
            String groupID = (String) objects[1];
            groupRepo.findByEntityID(idStr2Long(groupID))
                    .ifPresent(groupEntity -> rv.add(new Object[]{ groupEntity, inviteID }));
        }
        return rv;
    }

    @Transactional
    public boolean userInviteResponse(String uuid, Long inviteID, Boolean decision) {
        boolean rv = handleInviteResponse(uuid, inviteID, decision);
        return rv;
    }

    @Transactional
    public boolean groupInviteResponse(String uuid, Long inviteID, Boolean decision) {
        boolean rv = handleInviteResponse(uuid, inviteID, decision);
        return rv;
    }

    private boolean handleInviteResponse(String uuid, Long inviteID, Boolean decision) {
        Optional<Invite> inviteOp = inviteRepo.findById(inviteID);
        if (inviteOp.isPresent()) {
            Invite invite = inviteOp.get();
            if (invite.getEntityIDB().equals(uuid)) {
                if (decision) {
                    try {
                        relationRepo.save(new Relation(invite));
                        inviteRepo.deleteById(invite.getInviteID());
                    } catch (Exception e) {
                        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                        return false;
                    }
                } else {
                    inviteRepo.deleteById(invite.getInviteID());
                }
                return true;
            }
        }
        return false;
    }

    private Long idStr2Long(String idStr) {
        return Long.parseLong(idStr.substring(2));
    }

    public boolean deleteUserContact(String uuid, String rID) {
        boolean rv = handleDeleteContact(rID, rID);
        return rv;
    }

    public boolean deleteGroupContact(String uuid, String rID) {
        boolean rv = handleDeleteContact(rID, rID);
        return rv;
    }

    private boolean handleDeleteContact(String uuid, String rID) {
        Relation relation = relationRepo.findByrID(rID).orElse(null);
        if (relation != null) {
            if (relation.getEntityIDA().equals(uuid) || relation.getEntityIDB().equals(uuid)) {
                relationRepo.deleteById(rID);
                return true;
            }
        }
        return false;
    }
}
