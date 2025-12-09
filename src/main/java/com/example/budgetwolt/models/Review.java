package com.example.budgetwolt.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne
    private BasicUser commentOwner;
    @ManyToOne
    private BasicUser feedbackOwner;
    @ManyToOne
    private Chat chat;
    private int rate;
    private String text;

    public Review(String text, BasicUser commentOwner, Chat chat) {
        this.text = text;
        this.commentOwner = commentOwner;
        this.chat = chat;
    }
}
