package com.example.budgetwolt.fxControllers;

import com.example.budgetwolt.hibernateControl.CustomHibernate;
import com.example.budgetwolt.models.*;
import jakarta.persistence.EntityManagerFactory;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;

public class ChatController {

    public ListView<Review> messageListView;
    public Button sendButton;
    public TextArea messageField;

    private EntityManagerFactory entityManagerFactory;
    private CustomHibernate customHibernate;

    private User currentUser;
    private FoodOrder currentFoodOrder;

    public void setData(EntityManagerFactory entityManagerFactory, User currentUser, FoodOrder currentFoodOrder) {
        this.entityManagerFactory = entityManagerFactory;
        customHibernate = new CustomHibernate(this.entityManagerFactory);
        this.currentUser = currentUser;
        this.currentFoodOrder = currentFoodOrder;
        loadChat();
    }

    public void loadChat() {
        if (currentFoodOrder.getChat() != null) {
            messageListView.setItems(FXCollections.observableList(currentFoodOrder.getChat().getMessages()));
        }
    }

    public void sendMessage(ActionEvent actionEvent) {
        if(currentFoodOrder.getChat() == null) {
            Chat chat = new Chat("Chat no " + currentFoodOrder.getTitle(), currentFoodOrder);
            customHibernate.create(chat);
        }




        loadChat();
    }
}
