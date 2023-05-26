package com.example.slotmachine;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Stage;

import java.util.Optional;

public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        // start balansz megadas
        TextInputDialog balanceDialog = new TextInputDialog();
        balanceDialog.setTitle("Starting Balance");
        balanceDialog.setHeaderText("Enter the starting balance:");
        Optional<String> balanceResult = balanceDialog.showAndWait();

        // start bet megadas
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Betting Amount");
        dialog.setHeaderText("Enter the betting amount:");
        Optional<String> result = dialog.showAndWait();



        // kezdoertek ellenorzes
        if (result.isPresent() && balanceResult.isPresent()) {
            int betAmount = Integer.parseInt(result.get());
            int startingBalance = Integer.parseInt(balanceResult.get());


            SlotMachineModel model = new SlotMachineModel(startingBalance, betAmount);


            SlotMachineView view = new SlotMachineView();


            SlotMachineController controller = new SlotMachineController(model, view);


            primaryStage.setTitle("Slot Machine");
            primaryStage.setScene(new Scene(view.createUI(), 410, 470));
            primaryStage.show();
        } else {
            // ha 0 a kezdoertek kilep
            System.exit(0);
        }
    }
}


