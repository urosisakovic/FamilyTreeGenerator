package familytree;

import java.io.File;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Group;
import java.util.ArrayList;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.transform.Transform;
import javax.imageio.ImageIO;

public class Main extends Application {

    private final String PATH = "C:\\Users\\urosi\\src\\FamilyTreeGenerator\\ft.xml";
    private final int BOX_WIDTH = 70;
    private final int BOX_HEIGHT = 20;
    private final int BOX_PADDING_HOR = 50;
    private final int BOX_PADDING_VER = 10;
    
    @Override
    public void start(Stage stage) throws Exception {
        FamilyTree ft = new FamilyTree();
        ft.build(PATH);
        ft.print();
        
        ft.computeDrawPositions(BOX_WIDTH, BOX_HEIGHT, BOX_PADDING_HOR, BOX_PADDING_VER);        
        ArrayList<DrawNode> drawArray = ft.getDrawArray();
        
        Group root = new Group();
        Scene scene = new Scene(root, 1000, 1500);
        
        for (int i = 0; i < drawArray.size(); i++) {
            Group newBox = new Group();
            
            Rectangle newRect = new Rectangle(
                            0, 
                            0,
                            BOX_WIDTH,
                            BOX_HEIGHT
                    );
            newRect.setFill(Color.TRANSPARENT);
            newRect.setStroke(Color.BLACK);
            
            Text txt = new Text(drawArray.get(i).name);
            txt.setStroke(Color.BLACK);
            txt.setTranslateY(3 * BOX_HEIGHT / 4);
            txt.setTranslateX(BOX_WIDTH / 10);
            
            newBox.getChildren().addAll(txt, newRect);
            newBox.setTranslateX(drawArray.get(i).posX);
            newBox.setTranslateY(drawArray.get(i).posY);
            
            root.getChildren().add(newBox);
        }
        
        ArrayList<FamilyLine> linesArray = ft.getDrawLines(BOX_HEIGHT, BOX_WIDTH);
        for (int i = 0; i < linesArray.size(); i++) {
            Line newLine = new Line(
                    linesArray.get(i).startX,
                    linesArray.get(i).startY,
                    linesArray.get(i).endX,
                    linesArray.get(i).endY
            );
            root.getChildren().add(newLine);
        }
        
        root.setTranslateY(2 * BOX_HEIGHT);
        stage.setScene(scene);
        stage.setTitle("Isakovici");
        stage.show();
        
        WritableImage wim = new WritableImage(3000, 4500);
        SnapshotParameters spa = new SnapshotParameters();

        spa.setTransform(Transform.scale(3, 3));
        root.snapshot(spa, wim);

        File file = new File("FamilyTreeImage.png");

        try {
            ImageIO.write(SwingFXUtils.fromFXImage(wim, null), "png", file);
        } catch (Exception s) {
        }
        
    }
    
    public static void main(String[] args) {
        launch(args);
    }

    
    
}
