package com.tristanbrewee.javafx.controllers;

import com.tristanbrewee.model.CurrentLocation;
import com.tristanbrewee.model.ToneHeight;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;

public class DrawWindowController {

    @FXML
    Canvas cnvCanvas;
    GraphicsContext graphicsContext;

    @FXML
    StackPane stcStackPane;

    @FXML
    Button btnDraw;

    @FXML
    Button btnSave;

    private HashSet<ToneHeight> toneHeights;

    public void initialize(){
        toneHeights = new HashSet<>();
        graphicsContext = cnvCanvas.getGraphicsContext2D();
    }

    public void btnDrawClicked(){
        for (int i = 0; i < 5000; i++){
            int tone = (int)(Math.random() * 25) + 1;
            boolean toneExists = false;
            for (ToneHeight toneHeight: toneHeights) {
                if (toneHeight.getTone() == tone){
                    toneExists = true;
                    break;
                }
            }
            if (!toneExists){
                toneHeights.add(getRandomToneHeight(tone));
            }
            for (ToneHeight toneHeight: toneHeights) {
                if (toneHeight.getTone() == tone){
                    draw(toneHeight);
                    break;
                }
            }
        }
    }

    private ToneHeight getRandomToneHeight(int tone){
        int randomHeight = (int)(Math.random() * cnvCanvas.getHeight() + 1);
        int randomWidth = (int)(Math.random() * cnvCanvas.getWidth() + 1);
        CurrentLocation currentLocation = new CurrentLocation(randomWidth, randomHeight);
        int randomRed = (int)(Math.random() * 255 + 1);
        int randomGreen = (int)(Math.random() * 255 + 1);
        int randomBlue = (int)(Math.random() * 255 + 1);
        Color randomColor = Color.rgb(randomRed, randomGreen, randomBlue);
        ToneHeight toneHeight = new ToneHeight(tone, randomColor, currentLocation);
        return toneHeight;
    }

    private void draw(ToneHeight toneHeight){
        graphicsContext.setStroke(toneHeight.getColor());
        graphicsContext.setLineWidth(2);
        int newX = toneHeight.getCurrentLocation().getX() + (int)(Math.random() * 11 - 5);
        boolean otherSideFlag = false;
        if (newX > cnvCanvas.getWidth()) {
            newX -= cnvCanvas.getWidth();
            otherSideFlag = true;
        }
        if (newX < 0) {
            newX += cnvCanvas.getWidth();
            otherSideFlag = true;
        }
        int newY = toneHeight.getCurrentLocation().getY() + (int)(Math.random() * 11 - 5);
        if (newY > cnvCanvas.getHeight()) {
            newY -= cnvCanvas.getHeight();
            otherSideFlag = true;
        }
        if (newY < 0) {
            newY += cnvCanvas.getHeight();
            otherSideFlag = true;
        }
        if (!otherSideFlag)
            graphicsContext.strokeLine(toneHeight.getCurrentLocation().getX(), toneHeight.getCurrentLocation().getY(), newX, newY);
        toneHeight.setCurrentLocation(new CurrentLocation(newX, newY));
    }

    public void btnSaveClicked(){
        FileChooser fc = new FileChooser();
        fc.setInitialDirectory(new File("src/com/tristanbrewee/output"));
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("PNG","*.png"));
        fc.setTitle("Save Map");
        File file = fc.showSaveDialog(null);
        if(file != null){
            WritableImage wi = new WritableImage((int)cnvCanvas.getWidth(),(int)cnvCanvas.getHeight());
            try {
                ImageIO.write(SwingFXUtils.fromFXImage(cnvCanvas.snapshot(null,wi),null),"png",file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
