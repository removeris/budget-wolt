package com.example.budgetwolt.fxControllers;

import com.example.budgetwolt.hibernateControl.CustomHibernate;
import com.example.budgetwolt.models.*;
import jakarta.persistence.EntityManagerFactory;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
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

        messageListView.setCellFactory(lv -> new ListCell<Review>() {
            @Override
            protected void updateItem(Review item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    String sender = item.getCommentOwner().getUsername();
                    setText(sender + ": " + item.getText());
                }
            }
        });

        this.entityManagerFactory = entityManagerFactory;
        customHibernate = new CustomHibernate(this.entityManagerFactory);
        this.currentUser = currentUser;
        this.currentFoodOrder = currentFoodOrder;
        loadChat();
    }

    public void loadChat() {
        if (currentFoodOrder.getChat() != null) {
            Chat freshChat = customHibernate.getChatWithMessages(currentFoodOrder.getChat().getId());

            if (freshChat != null) {
                messageListView.getItems().setAll(freshChat.getMessages());
                messageListView.scrollTo(messageListView.getItems().size() - 1);
            }
        }
    }

    public void sendMessage(ActionEvent actionEvent) {
        String messageText = messageField.getText();
        if (messageText == null || messageText.trim().isEmpty()) return;

        Chat chat = currentFoodOrder.getChat();

        if (chat == null) {
            chat = new Chat("Order Chat #" + currentFoodOrder.getId(), currentFoodOrder);
            customHibernate.create(chat);

            currentFoodOrder.setChat(chat);
            customHibernate.update(currentFoodOrder);
        }

        Review chatMessage = new Review(messageText, (BasicUser) currentUser, chat);
        customHibernate.create(chatMessage);

        messageField.clear();

        loadChat();
    }
}
