package com.example.slotmachine;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import java.util.Random;

public class SlotMachineController {
    private SlotMachineModel model;
    private SlotMachineView view;
    private ObjectMapper objectMapper;
    private Image[] symbols;

    private int spinClickCount = 0;

    public SlotMachineController(SlotMachineModel model, SlotMachineView view) {
        this.model = model;
        this.view = view;
        this.view.setSpinButtonAction(this::spinButtonClicked);
        this.objectMapper = new ObjectMapper();
        this.symbols = new Image[]{
                new Image(getClass().getResource("/image1.png").toExternalForm()),
                new Image(getClass().getResource("/image2.png").toExternalForm()),
                new Image(getClass().getResource("/image3.png").toExternalForm()),
                new Image(getClass().getResource("/image4.png").toExternalForm()),
                new Image(getClass().getResource("/image5.png").toExternalForm())
        };
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void spinButtonClicked() {
        // penz check
        int betAmount = model.getBetAmount();
        if (model.getBalance() < betAmount) {
            showAlert("Insufficient Balance", "You don't have enough funds to spin.");
            return;
        }

        // porgetes es randomizacio
        Image[] result = new Image[3];
        for (int i = 0; i < 3; i++) {
            result[i] = getRandomSymbol();
        }
        view.displayResult(result);

        spinClickCount++;

        // nyeres ellenorzes
        int winnings = calculateWinnings(result);
        int newBalance = model.getBalance() - betAmount + winnings;
        model.setBalance(newBalance);
        view.updateBalance(newBalance);

        // jsonbe mentes katt utan
        saveToJSON(newBalance, winnings, spinClickCount);
    }

        //randomizacio
    private Image getRandomSymbol() {
        Random random = new Random();
        int index = random.nextInt(symbols.length);
        return symbols[index];
    }

    private int calculateWinnings(Image[] result) {
        int winnings = 0;
        if (result[0].equals(result[1]) && result[0].equals(result[2])) {
            // 3 egyezes
            winnings = model.getBetAmount() * 5;
        } else if (result[0].equals(result[1]) || result[0].equals(result[2]) || result[1].equals(result[2])) {
            // 2 egyezes
            winnings = model.getBetAmount();
        }
        return winnings;
    }

    private void saveToJSON(int balance, int winnings, int spinClickCount) {

        ObjectMapper objectMapper = new ObjectMapper();

        // reprezentacio keszitese a json datahoz
        ObjectNode jsonNode = objectMapper.createObjectNode();

        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

        // folder krealas
        String folderName = "stat";
        File folder = new File(folderName);
        if (!folder.exists()) {
            folder.mkdirs();
        }


            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
            String filename = folderName + "/statisztika" + timestamp + ".json";
            File file = new File(filename);




        // array krealas
        ArrayNode gamesNode = (ArrayNode) jsonNode.get("games");
        if (gamesNode == null) {
            gamesNode = objectMapper.createArrayNode();
            jsonNode.put("games", gamesNode);
        }

        // gameNodeba a jsonbe iras
        ObjectNode gameNode = objectMapper.createObjectNode();
        gameNode.put("Current Balance", balance);
        gameNode.put("Current Win", winnings);
        gameNode.put("Current Bet", model.getBetAmount());
        gameNode.put("Game Count", spinClickCount);

        if (gamesNode.size() == 0) {
            String currentTimestamp = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            ObjectNode gameInstanceNode = objectMapper.createObjectNode();
            gameInstanceNode.put("timestamp", currentTimestamp);
            gameInstanceNode.set("game", gameNode);
            gamesNode.add(gameInstanceNode);
        }

        // jsonbe iras
        try {
            objectMapper.writeValue(file, jsonNode);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }





}
