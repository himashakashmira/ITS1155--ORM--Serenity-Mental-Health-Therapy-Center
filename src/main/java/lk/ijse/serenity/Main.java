package lk.ijse.serenity;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lk.ijse.serenity.entity.TherapyProgram;
import lk.ijse.serenity.entity.User;
import lk.ijse.serenity.util.PasswordUtil;
import lk.ijse.serenity.util.SessionFactoryConfig;
import org.hibernate.Session;

import org.hibernate.Transaction;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(this.getClass().getResource("/view/LoginView.fxml"));
        primaryStage.setTitle("Serenity Therapy Center");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();

        Session session = SessionFactoryConfig.getInstance().getSession();
        Transaction transaction = session.beginTransaction();

        transaction.commit();
        session.close();

    }

    public static void main(String[] args) {
        launch(args);
    }

}
