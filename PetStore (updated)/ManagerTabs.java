import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;

public class ManagerTabs extends Application {
    
    
    @Override
    public void start(Stage primaryStage) {
        TabPane tabPane = new TabPane();

        PetManagement pm = new PetManagement();
        Tab pmTab = new Tab("Pet Management");
        pmTab.setContent(pm);
        pmTab.setClosable(false);
        
        ProductManagement productM = new ProductManagement();
        Tab productMTab = new Tab("Product Management");
        productMTab.setContent(productM);
        productMTab.setClosable(false);
        
        TransactionView tV = new TransactionView();
        Tab transactionTab = new Tab("Transaction View");
        transactionTab.setContent(tV);
        transactionTab.setClosable(false);
        
        tabPane.getTabs().addAll(pmTab, productMTab, transactionTab);

        Scene scene = new Scene(tabPane, 1280, 720);
        primaryStage.setTitle("Pet Store Management");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
