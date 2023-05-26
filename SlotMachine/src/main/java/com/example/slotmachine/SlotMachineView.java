package com.example.slotmachine;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


public class SlotMachineView {
    private Button spinButton;
    private ImageView[] symbolViews;
    private Label balanceLabel;

    public VBox createUI() {
        // spin gomb
        spinButton = new Button("Spin");
        spinButton.setOnAction(event -> {
            if (spinButtonAction != null) {
                spinButtonAction.run();
            }
        });

        // symbolok krealasa
        symbolViews = new ImageView[3];
        for (int i = 0; i < symbolViews.length; i++) {
            symbolViews[i] = new ImageView();

        }

        // balance labelezes
        balanceLabel = new Label();
        balanceLabel.setFont(new Font("Arial",30));

        // jatekter layout
        HBox symbolBox = new HBox(10);
        symbolBox.getChildren().addAll(symbolViews);
        symbolBox.setAlignment(Pos.CENTER);


        VBox root = new VBox(10);


        root.setStyle("-fx-background-image: url('/bg.png');" + "-fx-background-size: cover;");
        root.setPadding(new Insets(10));
        root.setAlignment(Pos.CENTER);
        root.getChildren().addAll(symbolBox, spinButton, balanceLabel);

        return root;
    }

    public void displayResult(Image[] symbols) {
        Platform.runLater(() -> {
            for (int i = 0; i < symbolViews.length; i++) {
                symbolViews[i].setImage(symbols[i]);
            }
        });
    }

    public void updateBalance(int balance) {
        Platform.runLater(() -> balanceLabel.setText("Balance: " + balance));
    }

    private Runnable spinButtonAction;

    public void setSpinButtonAction(Runnable action) {
        spinButtonAction = action;
    }
}