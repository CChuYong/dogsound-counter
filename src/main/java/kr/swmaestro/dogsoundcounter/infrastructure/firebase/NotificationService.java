package kr.swmaestro.dogsoundcounter.infrastructure.firebase;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import kr.swmaestro.dogsoundcounter.infrastructure.jpa.entities.UserData;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {
    public NotificationService() throws Exception{
        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(NotificationService.class.getClassLoader().getResourceAsStream("dog.json")))
                .build();

        FirebaseApp.initializeApp(options);
    }
    public void sendNotification(UserData target, String title, String description){
        Message message = Message.builder()
                .setNotification(Notification.builder().setTitle(title).setBody(description).build())
                .setTopic(target.getUsername())
                .build();
        try{
            String res = FirebaseMessaging.getInstance().send(message);
            System.out.println(res +" to send to " + target.getUsername());
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }
}
