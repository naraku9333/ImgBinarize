/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imgbinarize;

import cluster.Cluster;
import cluster.Euclidean;
import cluster.RetValue;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.imageio.ImageIO;

/**
 *
 * @author Sean Vogel
 */
public class ImgBinarize extends Application {
    private VBox vbox;
    private GridPane grid;    
    private ImageView iv = new ImageView();
    private Button btnSave; 
    private String filename;
    
    private BufferedImage loadImage(File f) throws FileNotFoundException, IOException {
        //Image im = new Image(new FileInputStream(f));
        BufferedImage bi = ImageIO.read(f);
        return bi;
    }
    
    private WritableImage binarize(BufferedImage im) throws Exception {
        int w = im.getWidth();
        int h = im.getHeight();
        int[] buf = new int[w * h];
        im.getData().getPixels(0, 0, im.getWidth(), im.getHeight(), buf);

        double[][] data = new double[buf.length][1];
        for(int i = 0; i < buf.length; ++i) {
            data[i][0] = buf[i];
        }
        
        RetValue ret = Cluster.kMeans(2, new Euclidean(), 5, data);

        BufferedImage bi = new BufferedImage(w, h, BufferedImage.TYPE_BYTE_BINARY);
            bi.getRaster().setPixels(0, 0, w, h, ret.clusters);
            
        return SwingFXUtils.toFXImage(bi, null);
    }
    
    @Override
    public void start(Stage primaryStage) {
        Button btn = new Button();
        btn.setText("Browse for image");
        btn.setOnAction(new EventHandler<ActionEvent>() {
            
            @Override
            public void handle(ActionEvent event) {
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Open Image File");
                fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
                File file = fileChooser.showOpenDialog(primaryStage);
                
                if(file != null) {
                    filename = file.getName();
                    try {
                        WritableImage wi = binarize(loadImage(file));
                        
                        iv.setImage(wi);
                        
                        primaryStage.setHeight(200 + wi.getHeight());
                        primaryStage.setWidth(100 + wi.getWidth());   
                        btnSave.setVisible(true);
                    } 
                    catch (FileNotFoundException ex) {
                        Logger.getLogger(ImgBinarize.class.getName()).log(Level.SEVERE, null, ex);
                    } 
                    catch (Exception ex) {
                        Logger.getLogger(ImgBinarize.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        });
        
        btnSave = new Button("Save image");
        btnSave.setVisible(false);
        btnSave.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Save Binarized Image");
                fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
                
                filename = filename.substring(0, filename.lastIndexOf('.'));
                filename = filename + "_bin.png";
                fileChooser.setInitialFileName(filename);
                
                File f = fileChooser.showSaveDialog(primaryStage);               
                if(f != null) {
                    try {
                        ImageIO.write(SwingFXUtils.fromFXImage(iv.getImage(), null), "png", f);
                    }
                    catch(Exception e) {
                        Logger.getLogger(ImgBinarize.class.getName()).log(Level.SEVERE, null, e);
                    }
                }
            }       
        });
        
        grid = new GridPane();
        //grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10, 25, 25, 25));
        
        grid.add(new Label("Select an image file to binarize\n*image must have bit depth of 8"), 0, 0);
        grid.add(btn, 1, 0);
        grid.add(btnSave, 1, 1);
        
        vbox = new VBox(20, grid, iv);
        vbox.setAlignment(Pos.CENTER);
        //root.add(vbox, 0, 2, 2, 2);
        
        Scene scene = new Scene(vbox, 350, 200);
        
        primaryStage.setTitle("ImgBinarize");
        primaryStage.setScene(scene);
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("icon.png")));
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }    
}
