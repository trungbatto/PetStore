import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

import java.util.List;

public class TransactionView extends VBox {

    private TableView<Transaction> tableView;

    public TransactionView() {
        tableView = new TableView<>();

        // Define columns
        TableColumn<Transaction, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("transactionId"));

        TableColumn<Transaction, Integer> userIdCol = new TableColumn<>("User ID");
        userIdCol.setCellValueFactory(new PropertyValueFactory<>("userId"));

        TableColumn<Transaction, String> typeCol = new TableColumn<>("Type");
        typeCol.setCellValueFactory(new PropertyValueFactory<>("transactionType"));

        TableColumn<Transaction, String> dateCol = new TableColumn<>("Date");
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date")); // toString() assumed

        TableColumn<Transaction, Double> priceCol = new TableColumn<>("Price");
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        TableColumn<Transaction, Integer> amountCol = new TableColumn<>("Amount");
        amountCol.setCellValueFactory(new PropertyValueFactory<>("amount"));

        tableView.getColumns().addAll(idCol, userIdCol, typeCol, dateCol, priceCol, amountCol);
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        loadTransactions();

        this.getChildren().add(tableView);
        this.setSpacing(10);
        this.setPadding(new javafx.geometry.Insets(10));
    }

    private void loadTransactions() {
        DAOTransaction dao = new DAOTransaction();
        List<Transaction> transactionList = dao.getAllTransactions();

        tableView.getItems().clear();
        tableView.getItems().addAll(transactionList);
    }
}
