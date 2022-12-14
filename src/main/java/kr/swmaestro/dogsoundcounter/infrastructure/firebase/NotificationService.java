package kr.swmaestro.dogsoundcounter.infrastructure.firebase;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.*;
import kr.swmaestro.dogsoundcounter.infrastructure.jpa.entities.UserData;
import org.springframework.stereotype.Service;

import java.io.InputStream;

@Service
public class NotificationService {
    public NotificationService() throws Exception {
        InputStream credentials = NotificationService.class.getClassLoader().getResourceAsStream("dog.json");
        if (credentials == null) throw new RuntimeException("credentials not found");
        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(credentials))
                .build();

        FirebaseApp.initializeApp(options);
    }

    public String sendNotification(UserData target, String title, String description) {
        Message message = Message.builder()
                .setNotification(Notification.builder().setTitle(title).setBody(description).build())
                .setTopic(target.getUsername())
                .setApnsConfig(ApnsConfig.builder().setAps(Aps.builder().setSound("default").build()).build())
                .build();
        try {
            return FirebaseMessaging.getInstance().send(message);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
