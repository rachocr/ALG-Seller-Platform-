package application.listguard;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.layout.HBox;
import javafx.util.Callback;

import java.sql.*;
import java.time.LocalDate;

public class CustomerController {

    @FXML
    private TableView<Product> customerTable;

    @FXML
    private TableColumn<Product, String> colProductName;

    @FXML
    private TableColumn<Product, String> colProductCategory;

    @FXML
    private TableColumn<Product, Double> colProductPrice;

    @FXML
    private TableColumn<Product, Integer> colProductQuantity;

    @FXML
    private TableColumn<Product, String> colShopName;

    @FXML
    private TableColumn<Product, Void> colActions;

    private final ObservableList<Product> productList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Initialize columns
        colProductName.setCellValueFactory(new PropertyValueFactory<>("productName"));
        colProductCategory.setCellValueFactory(new PropertyValueFactory<>("productCategory"));
        colProductPrice.setCellValueFactory(new PropertyValueFactory<>("productPrice"));
        colProductQuantity.setCellValueFactory(new PropertyValueFactory<>("productQuantity"));
        colShopName.setCellValueFactory(new PropertyValueFactory<>("shopName")); // Bind shopName column

        // Add actions column
        addActionsColumn();

        // Fetch and display all products from database
        fetchAndDisplayProducts();
    }

    @FXML
    private void fetchAndDisplayProducts() {
        // Clear existing products from the list
        productList.clear();

        // SQL query to retrieve all products with quantity greater than 0
        String query = "SELECT p.productID, p.product_listing_date, p.product_name, p.product_category, p.product_price, p.product_quantity, s.shop_name " +
                "FROM product p " +
                "JOIN seller s ON p.sellerID = s.sellerID " +
                "WHERE p.product_quantity > 0";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            // Process each row in the result set
            while (resultSet.next()) {
                int productId = resultSet.getInt("productID");
                LocalDate listingDate = resultSet.getDate("product_listing_date").toLocalDate();
                String productName = resultSet.getString("product_name");
                String productCategory = resultSet.getString("product_category");
                double productPrice = resultSet.getDouble("product_price");
                int productQuantity = resultSet.getInt("product_quantity");
                String shopName = resultSet.getString("shop_name");

                // Filter out products with quantity 0 or less
                if (productQuantity > 0) {
                    // Create Product object with Seller information
                    Product product = new Product(productId, listingDate, productName, productCategory, productPrice, productQuantity, "","","",0.0, shopName);
                    productList.add(product);
                }
            }

            // Set items to TableView
            customerTable.setItems(productList);

        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exceptions appropriately (show error message, log, etc.)
        }
    }

    private void addActionsColumn() {
        TableColumn<Product, Void> colActions = new TableColumn<>("Actions");

        Callback<TableColumn<Product, Void>, TableCell<Product, Void>> cellFactory = new Callback<TableColumn<Product, Void>, TableCell<Product, Void>>() {
            @Override
            public TableCell<Product, Void> call(TableColumn<Product, Void> param) {
                return new ActionTableCell();
            }
        };

        colActions.setCellFactory(cellFactory);
        customerTable.getColumns().add(colActions);
    }

    private class ActionTableCell extends TableCell<Product, Void> {
        private final Button btnOrder = new Button("Order");

        private final HBox hbox = new HBox(btnOrder);

        public ActionTableCell() {
            btnOrder.getStyleClass().add("order-button");

            btnOrder.setOnAction(event -> {
                Product product = getTableView().getItems().get(getIndex());
                showConfirmationDialog(product);
            });

            hbox.setSpacing(5);
            hbox.setAlignment(Pos.CENTER);
        }

        @Override
        protected void updateItem(Void item, boolean empty) {
            super.updateItem(item, empty);
            if (empty) {
                setGraphic(null);
            } else {
                setGraphic(hbox);
            }
        }

        private void showConfirmationDialog(Product product) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Amazon List Guard");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want to order this product?");

            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.getStylesheets().add(getClass().getResource("/application/listguard/application.css").toExternalForm());

            alert.showAndWait()
                    .filter(response -> response == ButtonType.OK)
                    .ifPresent(response -> processOrder(product));
        }

        private void processOrder(Product product) {
            int newQuantity = product.getProductQuantity() - 1;
            updateProductQuantity(product.getProductId(), newQuantity);
            insertOrder(product);
        }

        private void updateProductQuantity(int productId, int newQuantity) {
            String query = "UPDATE product SET product_quantity = ? WHERE productID = ?";
            try (Connection connection = DatabaseConnection.getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement(query)) {

                preparedStatement.setInt(1, newQuantity);
                preparedStatement.setInt(2, productId);
                preparedStatement.executeUpdate();

                // Update the local product list and table view
                Product product = getTableView().getItems().get(getIndex());
                product.setProductQuantity(newQuantity);
                customerTable.refresh(); // Refresh the table view after update

            } catch (SQLException e) {
                e.printStackTrace();
                // Handle database update exception appropriately
            }
        }

        private void insertOrder(Product product) {
            String insertOrderItemQuery = "INSERT INTO order_item (productID, item_quantity, order_date, order_status, item_price) VALUES (?, ?, ?, ?, ?)";
            String insertOrderQuery = "INSERT INTO orders (order_itemID, sellerID, buyerID) VALUES (?, ?, ?)";

            LocalDate currentDate = LocalDate.now();
            int sellerID = getSellerID(product.getProductId());
            int buyerID = 1; // Default buyer ID as 1
            int productQuantity = 1; // Assuming the order is for 1 item
            String orderStatus = "Processing";
            double itemPrice = product.getProductPrice();

            try (Connection connection = DatabaseConnection.getConnection()) {
                // Insert into order_item table and get the generated order_itemID
                int orderItemID;
                try (PreparedStatement orderItemStatement = connection.prepareStatement(insertOrderItemQuery, Statement.RETURN_GENERATED_KEYS)) {
                    orderItemStatement.setInt(1, product.getProductId());
                    orderItemStatement.setInt(2, productQuantity);
                    orderItemStatement.setDate(3, java.sql.Date.valueOf(currentDate));
                    orderItemStatement.setString(4, orderStatus);
                    orderItemStatement.setDouble(5, itemPrice);

                    int affectedRows = orderItemStatement.executeUpdate();
                    if (affectedRows == 0) {
                        throw new SQLException("Creating order item failed, no rows affected.");
                    }

                    try (ResultSet generatedKeys = orderItemStatement.getGeneratedKeys()) {
                        if (generatedKeys.next()) {
                            orderItemID = generatedKeys.getInt(1);
                        } else {
                            throw new SQLException("Creating order item failed, no ID obtained.");
                        }
                    }
                }

                // Insert into orders table using the generated order_itemID
                try (PreparedStatement orderStatement = connection.prepareStatement(insertOrderQuery)) {
                    orderStatement.setInt(1, orderItemID);
                    orderStatement.setInt(2, sellerID);
                    orderStatement.setInt(3, buyerID);

                    orderStatement.executeUpdate();
                }

                Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                successAlert.setTitle("Amazon List Guard");
                successAlert.setHeaderText(null);
                successAlert.setContentText("Order placed successfully!");
                successAlert.showAndWait();

            } catch (SQLException e) {
                e.printStackTrace();
                // Handle database insertion exception appropriately
            }
        }

        private int getSellerID(int productId) {
            String query = "SELECT sellerID FROM product WHERE productID = ?";
            try (Connection connection = DatabaseConnection.getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement(query)) {

                preparedStatement.setInt(1, productId);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        return resultSet.getInt("sellerID");
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
                // Handle exception
            }
            return -1;
        }
    }
}
