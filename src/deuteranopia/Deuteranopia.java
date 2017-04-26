/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package deuteranopia;

import java.awt.image.BufferedImage;
import javafx.scene.control.ScrollPane;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.imageio.ImageIO;

/**
 *
 * @author emmab
 */
public class Deuteranopia extends Application {

    BufferedImage inputImage;
    BufferedImage outputImage;
    int called = 0;

    public Deuteranopia() {
        this.inputImage = new BufferedImage(200, 200, BufferedImage.TYPE_INT_RGB);
        this.outputImage = new BufferedImage(200, 200, BufferedImage.TYPE_INT_RGB);
    }

    @Override
    public void start(Stage primaryStage) {

        Button btn = new Button();
        FileChooser fileChooser = new FileChooser();
        btn.setText("Select Photo");
        btn.setOnAction((ActionEvent event) -> {
            if (event.getSource() == btn) {
                try {
                    File fileRet = fileChooser.showOpenDialog(null);

                    inputImage = ImageIO.read(fileRet);

                    outputImage = new BufferedImage(inputImage.getWidth(), inputImage.getHeight(), BufferedImage.TYPE_INT_RGB);

                    for (int y = 0; y < inputImage.getHeight(); y++) {
                        for (int x = 0; x < inputImage.getWidth(); x++) {
                            int pixel = inputImage.getRGB(x, y);
                            int shiftedPixel = pixel;

                            if (x == 0 && y == 0) {
                                System.out.println(pixel);
                            }

                            int blue = pixel & 255; //0xff

                            shiftedPixel = shiftedPixel >> 8; //shiftedPixel >>=8
                            int green = shiftedPixel & 255;

                            shiftedPixel = shiftedPixel >> 8; //shiftedPixel >>=8
                            int red = shiftedPixel & 255;

                            int gray = (red + green + blue) / 3;

                            int finalPixel = (gray << 16) | (gray << 8) | blue;
                            /*
                            int iRed = 255 - red;
                            int iGreen = 255 - green;
                            int iBlue = 255 - blue;
                            
                            int finalPixel = (iRed << 16) + (iGreen << 8) + (iBlue);
                             */
                            if (x == 0 && y == 0) {
                                System.out.println(red + "-" + green + "-" + blue);
                            }

                            outputImage.setRGB(x, y, finalPixel);
                            called++;

                        }
                    }
                    if (called > 0) {
                        PhotoViewer pv = new PhotoViewer(inputImage, outputImage);
                    }
                    try {
                        ImageIO.write(outputImage, "PNG", new File("C:\\Users\\emmab\\Desktop\\newPhoto2.png"));
                    } catch (IOException ex) {
                        Logger.getLogger(Deuteranopia.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } catch (IOException ex) {
                    Logger.getLogger(Deuteranopia.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        StackPane root;
        root = new StackPane();
        root.getChildren().add(btn);

        Scene scene = new Scene(root, 150, 100);

        primaryStage.setTitle("Deuteranopia Conversion");
        primaryStage.setScene(scene);
        primaryStage.show();

    }

    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws IOException {
        launch(args);
    }
}

class PhotoViewer {

    PhotoViewer(BufferedImage inputImage, BufferedImage outputImage) {
        Stage photos = new Stage();
        photos.setTitle("Deuteranopia");
        FlowPane photoPane = new FlowPane();
        
        Image inputImageJfx = SwingFXUtils.toFXImage(inputImage, null);
        Image outputImageJfx = SwingFXUtils.toFXImage(outputImage, null);
        ImageView inputImageView = new ImageView(inputImageJfx);
        ImageView outputImageView = new ImageView(outputImageJfx);
        
        photoPane.getChildren().add(inputImageView);
        photoPane.getChildren().add(outputImageView);
        photoPane.setPadding(new Insets(20, 20, 20, 20));
       
        ScrollPane scroll = new ScrollPane();
        scroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);  
        scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);  
        scroll.setContent(photoPane);
        
        Scene scene = new Scene(scroll, inputImageJfx.getWidth()+15, inputImageJfx.getHeight());
        
        photos.setScene(scene);
        photos.show();
    }
}
