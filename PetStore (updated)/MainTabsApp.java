import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;

public class MainTabsApp extends Application {
    
    private int userId;
    
    public MainTabsApp(int userId) {
        this.userId = userId;
    }
    
    @Override
    public void start(Stage primaryStage) {
        TabPane tabPane = new TabPane();

        UserInfoApp userInfoApp = new UserInfoApp(userId);
        Tab userInfoTab = new Tab("User Info and Cart");
        userInfoTab.setContent(userInfoApp);
        userInfoTab.setClosable(false);

        Tab petAdoptionTab = new Tab("Pet Adoption");
        petAdoptionTab.setContent(new PetAdoptionApp(userId));
        petAdoptionTab.setClosable(false);

        Tab productStoreTab = new Tab("Product Store");
        productStoreTab.setContent(new ProductStoreApp(userId, userInfoApp));
        productStoreTab.setClosable(false);

        tabPane.getTabs().addAll(petAdoptionTab, productStoreTab, userInfoTab);

        Scene scene = new Scene(tabPane, 1280, 720);
        primaryStage.setTitle("Pet Store Adoption App");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
