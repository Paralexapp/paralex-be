package com.paralex.erp.services;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.google.firebase.auth.UserRecord;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.io.IOException;
import java.util.Optional;

@Validated
@Service
@Log4j2
public class FirebaseService {
    @Value("${firebase.options.projectId}")
    private String projectId;

    @Value("${firebase.options.service-account-resource-path}")
    private Resource serviceAccountAsResource;

    public Optional<UserRecord> getUserFromFirebaseBy(@Nullable String email, @Nullable String phoneNumber) throws IOException, FirebaseAuthException {
        final var firebaseAuth = getAuth();
        UserRecord userRecord = null;

        if (phoneNumber != null)
            userRecord = firebaseAuth.getUserByPhoneNumber(phoneNumber);

        if (email != null)
            userRecord = firebaseAuth.getUserByEmail(email);

        return Optional.ofNullable(userRecord);
    }

    public UserRecord getUserRecord(@NotNull String userUid) throws IOException, FirebaseAuthException {
        return getAuth().getUser(userUid);
    }

    public UserRecord createUser(@NotNull UserRecord.CreateRequest request) throws IOException, FirebaseAuthException {
        return getAuth().createUser(request);
    }

    public FirebaseToken verifyIdToken(String token, boolean checkRevoked) throws IOException, FirebaseAuthException {
        return getAuth().verifyIdToken(token, checkRevoked);
    }

    @NotNull
    public String generatePasswordResetLink(String email) throws FirebaseAuthException, IOException {
        // INFO May not need actionCodeSettings
        return getAuth().generatePasswordResetLink(email);
    }

    @NotNull
    public String generateEmailVerificationLink(String email) throws FirebaseAuthException, IOException {
        // INFO May not need actionCodeSettings
        return getAuth().generateEmailVerificationLink(email);
    }

    public FirebaseAuth getAuth() throws IOException {
        return FirebaseAuth.getInstance(initializeApp());
    }

    public GoogleCredentials getCredentials() throws IOException {
        return GoogleCredentials.fromStream(serviceAccountAsResource
                .getInputStream());
    }

    public FirebaseApp initializeApp() throws IOException {
        return FirebaseApp.getApps().isEmpty() ?
                FirebaseApp.initializeApp(buildFirebaseOptions()) :
                FirebaseApp.getInstance();
    }

    public FirebaseOptions buildFirebaseOptions() throws IOException {
        return FirebaseOptions.builder()
                .setProjectId(projectId)
                .setCredentials(getCredentials())
                .build();
    }
}
