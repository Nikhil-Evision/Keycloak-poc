package com.evision.keycloaktesting.service;

import com.evision.keycloaktesting.Credentials;
import com.evision.keycloaktesting.DTO.UserDTO;
import com.evision.keycloaktesting.KeycloakConfig;
import lombok.AllArgsConstructor;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.RoleResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@AllArgsConstructor
@Service
public class KeyCloakService {

    public void addUser(UserDTO userDTO){
        CredentialRepresentation credential = Credentials
                .createPasswordCredentials(userDTO.getPassword());
        UserRepresentation user = new UserRepresentation();
        user.setUsername(userDTO.getUserName());
        user.setFirstName(userDTO.getFirstname());
        user.setLastName(userDTO.getLastName());
        user.setEmail(userDTO.getEmailId());
        user.setCredentials(Collections.singletonList(credential));
        user.setEnabled(true);

        UsersResource instance = getInstance();
        instance.create(user);
    }

    public void assignRoleToUserByUsername(String username, String rolename) {
        RealmResource realmResource = KeycloakConfig.getInstance().realm(KeycloakConfig.realm);
        UsersResource usersResource = realmResource.users();

        // Retrieve the user by username
        List<UserRepresentation> users = usersResource.search(username, true);
        if (users.isEmpty()) {
            throw new IllegalArgumentException("User not found.");
        }

        // Retrieve the role by rolename
        RoleResource roleResource = realmResource.roles().get(rolename);
        if (roleResource == null) {
            throw new IllegalArgumentException("Role not found.");
        }

        // Assign the role to the user
        UserResource userResource = usersResource.get(users.get(0).getId());
        userResource.roles().realmLevel().add(Arrays.asList(roleResource.toRepresentation()));
    }

    public void assignRoleToUser(String userId, String roleId) {
        RealmResource realmResource = KeycloakConfig.getInstance().realm(KeycloakConfig.realm);
        UsersResource usersResource = realmResource.users();

        // Retrieve the user by ID
        UserResource userResource = usersResource.get(userId);

        // Retrieve the role by ID
        RoleResource roleResource = realmResource.roles().get(roleId);

        // Assign the role to the user
        userResource.roles().realmLevel().add(Arrays.asList(roleResource.toRepresentation()));
    }


    public List<UserRepresentation> getUser(String userName){
        UsersResource usersResource = getInstance();
        List<UserRepresentation> user = usersResource.search(userName, true);
        return user;

    }

    public void updateUser(String userId, UserDTO userDTO){
        CredentialRepresentation credential = Credentials
                .createPasswordCredentials(userDTO.getPassword());
        UserRepresentation user = new UserRepresentation();
        user.setUsername(userDTO.getUserName());
        user.setFirstName(userDTO.getFirstname());
        user.setLastName(userDTO.getLastName());
        user.setEmail(userDTO.getEmailId());
        user.setCredentials(Collections.singletonList(credential));

        UsersResource usersResource = getInstance();
        usersResource.get(userId).update(user);
    }
    public void deleteUser(String userId){
        UsersResource usersResource = getInstance();
        usersResource.get(userId)
                .remove();
    }


    public void sendVerificationLink(String userId){
        UsersResource usersResource = getInstance();
        usersResource.get(userId)
                .sendVerifyEmail();
    }

    public void sendResetPassword(String userId){
        UsersResource usersResource = getInstance();

        usersResource.get(userId)
                .executeActionsEmail(Arrays.asList("UPDATE_PASSWORD"));
    }

    public UsersResource getInstance(){
        return KeycloakConfig.getInstance().realm(KeycloakConfig.realm).users();
    }


}
