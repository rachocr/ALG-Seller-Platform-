package application.listguard;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.*;

import javafx.geometry.Pos;

public class DashboardController {

    // Labels
    @FXML private Label store_name;
    @FXML private Label store_email;
    @FXML private Label overviewPage;
    @FXML private Label productsPage;
    @FXML private Label ordersPage;
    @FXML private Label notificationsPage;
    @FXML private Label createProductPage;
    @FXML private Label editProductPage;
    @FXML private Label viewProductPage;

    @FXML private Label trackOrderPage;
    @FXML private Label orderIDLabel;
    @FXML private Label addressLabel;
    @FXML private Label statusLabel;

    // Profile Picture
    @FXML private Circle profile_picture;
    @FXML private Circle settings_profile;

    // Tabs
    @FXML private TabPane tabPane;
    @FXML private Tab overviewTab;
    @FXML private Tab productsTab;
    @FXML private Tab ordersTab;
    @FXML private Tab notificationsTab;
    @FXML private Tab helpcenterTab;
    @FXML private Tab settingsTab;
    @FXML private Tab createproductTab;
    @FXML private Tab editproductTab;
    @FXML private Tab viewProductTab;
    @FXML private Tab trackorderTab;

    // Combo Box
    @FXML private ComboBox<String> categoryComboBox;
    @FXML private ComboBox<String> categoryComboBoxEdit;
    @FXML private ComboBox<String> categoryComboBoxView;

    // Text Fields & Text Areas
    @FXML private TextField nameTF;
    @FXML private TextArea descriptionTA;
    @FXML private TextField stocksTF;
    @FXML private TextField basepriceTF;
    @FXML private TextField discountsTF;
    @FXML private TextField attributesTF;
    @FXML private TextField tagsTF;

    @FXML private TextField nameTFEdit;
    @FXML private TextArea descriptionTAEdit;
    @FXML private TextField stocksTFEdit;
    @FXML private TextField basepriceTFEdit;
    @FXML private TextField discountsTFEdit;
    @FXML private TextField attributesTFEdit;
    @FXML private TextField tagsTFEdit;

    @FXML private TextField nameTFView;
    @FXML private TextArea descriptionTAView;
    @FXML private TextField stocksTFView;
    @FXML private TextField basepriceTFView;
    @FXML private TextField discountsTFView;
    @FXML private TextField attributesTFView;
    @FXML private TextField tagsTFView;

    // Tables
    @FXML private TableView<Product> tableProducts;
    @FXML private TableColumn<Product, LocalDate> colListingDate;
    @FXML private TableColumn<Product, String> colProductName;
    @FXML private TableColumn<Product, String> colProductCategory;
    @FXML private TableColumn<Product, Double> colProductPrice;
    @FXML private TableColumn<Product, Integer> colProductQuantity;

    @FXML private TableView<Product> tableOverview;
    @FXML private TableColumn<Product, LocalDate> colListingDateOverview;
    @FXML private TableColumn<Product, String> colProductNameOverview;
    @FXML private TableColumn<Product, String> colProductCategoryOverview;
    @FXML private TableColumn<Product, Double> colProductPriceOverview;

    @FXML private TableView<Order> tableOrders;
    @FXML private TableColumn<Order, String> colOrderNo;
    @FXML private TableColumn<Order, String> colProductNameOrder;
    @FXML private TableColumn<Order, String> colProductCategoryOrder;
    @FXML private TableColumn<Order, LocalDate> colListingDateOrder;
    @FXML private TableColumn<Order, Integer> colProductQuantityOrder;
    @FXML private TableColumn<Order, Double> colProductPriceOrder;

    @FXML private TableView<Product> tableOrdered;
    @FXML private TableColumn<Product, String> colProductNameOrdered;
    @FXML private TableColumn<Product, String> colProductDescriptionOrdered;
    @FXML private TableColumn<Product, String> colProductCategoryOrdered;
    @FXML private TableColumn<Product, LocalDate> colListingDateOrdered;
    @FXML private TableColumn<Product, Double> colProductPriceOrdered;

    @FXML private Button updateButtonTrack;

    private Product selectedProduct;
    private Seller seller;
    private static final Logger logger = Logger.getLogger(DashboardController.class.getName());

    public void initialize(Seller seller) {
        if (seller == null) {
            logger.warning("Seller instance is null. Cannot proceed with initialization.");
            return;
        }

        this.seller = seller;
        setSellerData(seller);
        loadProfilePicture();
        populateCategoryComboBox();

        // Initialize TableView columns
        colListingDate.setCellValueFactory(new PropertyValueFactory<>("productListingDate"));
        colProductName.setCellValueFactory(new PropertyValueFactory<>("productName"));
        colProductCategory.setCellValueFactory(new PropertyValueFactory<>("productCategory"));
        colProductPrice.setCellValueFactory(new PropertyValueFactory<>("productPrice"));
        colProductQuantity.setCellValueFactory(new PropertyValueFactory<>("productQuantity"));

        colProductNameOverview.setCellValueFactory(new PropertyValueFactory<>("productName"));
        colProductCategoryOverview.setCellValueFactory(new PropertyValueFactory<>("productCategory"));
        colProductPriceOverview.setCellValueFactory(new PropertyValueFactory<>("productPrice"));
        colListingDateOverview.setCellValueFactory(new PropertyValueFactory<>("productListingDate"));

        colOrderNo.setCellValueFactory(new PropertyValueFactory<>("orderNo"));
        colProductNameOrder.setCellValueFactory(new PropertyValueFactory<>("productName"));
        colProductCategoryOrder.setCellValueFactory(new PropertyValueFactory<>("productCategory"));
        colListingDateOrder.setCellValueFactory(new PropertyValueFactory<>("orderDate"));
        colProductQuantityOrder.setCellValueFactory(new PropertyValueFactory<>("itemQuantity"));
        colProductPriceOrder.setCellValueFactory(new PropertyValueFactory<>("itemPrice"));

        colProductNameOrdered.setCellValueFactory(new PropertyValueFactory<>("productName"));
        colProductDescriptionOrdered.setCellValueFactory(new PropertyValueFactory<>("productDesc")); // Changed to productDesc
        colProductCategoryOrdered.setCellValueFactory(new PropertyValueFactory<>("productCategory"));
        colProductPriceOrdered.setCellValueFactory(new PropertyValueFactory<>("productPrice"));
        colListingDateOrdered.setCellValueFactory(new PropertyValueFactory<>("productListingDate"));

        fetchAndDisplayProducts();
        fetchAndDisplayOrders();
        addActionsColumn();
        addActionsColumnToOrders();
    }

    @FXML
    private void updateShipmentStatus() {
        String orderIDLabelText = orderIDLabel.getText();
        int orderID = extractOrderID(orderIDLabelText);

        try {
            // Step 1: Retrieve current order status from order_item table
            String currentStatus = getCurrentOrderStatus(orderID);

            // Step 2: Determine new order status
            String newStatus = determineNewOrderStatus(currentStatus);

            updateOrderStatusInOrderItemTable(orderID, newStatus);

            System.out.println("Order status updated successfully to " + newStatus);
            fetchAndDisplayOrderedProduct(orderIDLabelText);
            switchToOrders();

            if(newStatus.equals("Packed")){
                showStatusUpdatedInformation(newStatus);
            }else if(newStatus.equals("Shipped")) {
                showStatusUpdatedInformation(newStatus);
            }else if(newStatus.equals("In Transit")){
                showStatusUpdatedInformation(newStatus);
            }else if(newStatus.equals("Delivered")){
                showStatusUpdatedInformation(newStatus);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            // Handle SQLException appropriately, e.g., show error message
            showAlert("Failed to update order status: " + e.getMessage());
        }
    }

    // Helper method to retrieve current order status from order_item table
    private String getCurrentOrderStatus(int orderID) throws SQLException {
        String currentStatus = null;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT order_status FROM order_item WHERE order_itemID = ?")) {

            // Bind the orderID parameter
            stmt.setInt(1, orderID);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                currentStatus = rs.getString("order_status");
            } else {
                // Handle case where no order status is found (optional)
                System.out.println("No order status found for orderID: " + orderID);
            }
        }

        // Return the currentStatus, or handle null appropriately
        return currentStatus;
    }

    // Helper method to update order status in order_item table
    private void updateOrderStatusInOrderItemTable(int orderID, String newStatus) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement updateStmt = conn.prepareStatement(
                     "UPDATE order_item SET order_status = ? WHERE order_itemID = ?")) {

            updateStmt.setString(1, newStatus);
            updateStmt.setInt(2, orderID);

            int rowsAffected = updateStmt.executeUpdate();
            if (rowsAffected > 0) {
                // Update successful
                System.out.println("Order status updated in order_item table to " + newStatus);
            } else {
                // Update failed
                System.out.println("Failed to update order status in order_item table.");
                // Handle failure appropriately, e.g., show error message
                showAlert("Failed to update order status in order_item table.");
            }
        }
    }

    private String determineNewOrderStatus(String currentStatus) {
        // Replace with your logic to determine new status
        switch (currentStatus) {
            case "Shipped":
                return "In Transit";
            case "In Transit":
                return "Delivered";
            case "Delivered":
                return "Delivered"; // No further action needed if already delivered
            default:
                return "Shipped"; // Default case or initial status
        }
    }


    private void updateOrderStatus(String orderNo, String newStatus) {
        int orderID = extractOrderID(orderNo);
        if (orderID == -1) {
            System.out.println("Invalid order number: " + orderNo);
            return;
        }

        int orderItemID = getOrderItemIDByOrderID(orderID);
        if (orderItemID == -1) {
            System.out.println("No order item found for order ID: " + orderID);
            return;
        }

        try (Connection conn = DatabaseConnection.getConnection()) {
            // Get the current status
            String currentStatus = getCurrentOrderStatus(conn, orderID);

            // Check if we can update to the new status
            if (!canUpdateToStatus(currentStatus, newStatus)) {
                System.out.println("Cannot update from current status: " + currentStatus + " to " + newStatus);
                return;
            }

            // Perform the update
            try (PreparedStatement stmt = conn.prepareStatement(
                    "UPDATE order_item SET order_status = ? WHERE order_itemID = ?")) {
                stmt.setString(1, newStatus);
                stmt.setInt(2, orderItemID);
                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("Order status updated to " + newStatus + " successfully.");
                } else {
                    System.out.println("Failed to update order status.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private int getOrderItemIDByOrderID(int orderID) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT order_itemID FROM orders WHERE orderID = ?")) {
            stmt.setInt(1, orderID);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("order_itemID");
                } else {
                    System.out.println("No order item found for order ID: " + orderID);
                    return -1;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    private String getCurrentOrderStatus(Connection conn, int orderID) throws SQLException {
        String currentStatus = null;
        try (PreparedStatement stmt = conn.prepareStatement(
                "SELECT order_status FROM order_item WHERE order_itemID = (SELECT order_itemID FROM orders WHERE orderID = ?)")) {
            stmt.setInt(1, orderID);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    currentStatus = rs.getString("order_status");
                }
            }
        }
        return currentStatus;
    }

    private boolean canUpdateToStatus(String currentStatus, String targetStatus) {
        if (currentStatus == null) {
            // No current status found, assume it cannot update
            return false;
        }

        switch (currentStatus) {
            case "Processing":
                return targetStatus.equals("Packed");
            case "Packed":
                return targetStatus.equals("Shipped");
            case "Shipped":
                return false; // Already shipped, cannot update further
            default:
                return false; // Unknown or unexpected status
        }
    }

    private void showStatusUpdatedInformation(String newStatus) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Amazon List Guard");
        alert.setHeaderText(null);
        alert.setContentText("Order status has been updated to: " + newStatus + ".");

        alert.showAndWait();
    }

    private void showShippedInformationDialog(String status) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Amazon List Guard");
        alert.setHeaderText(null);
        alert.setContentText("Order status has been updated to: " + status + ".");

        alert.showAndWait();
    }

    private void addActionsColumn() {
        TableColumn<Product, Void> colActions = new TableColumn<>("Action");
        Callback<TableColumn<Product, Void>, TableCell<Product, Void>> cellFactory = new Callback<>() {
            @Override
            public TableCell<Product, Void> call(final TableColumn<Product, Void> param) {
                final TableCell<Product, Void> cell = new TableCell<>() {
                    private final Button btnEdit = new Button("Edit");
                    private final Button btnView = new Button("View");
                    private final Button btnRemove = new Button("Remove");
                    private final HBox hbox = new HBox(btnEdit, btnView, btnRemove);

                    {
                        // Styling for buttons
                        btnEdit.getStyleClass().add("edit-button");
                        btnView.getStyleClass().add("view-button");
                        btnRemove.getStyleClass().add("remove-button");

                        // Set spacing between buttons
                        hbox.setSpacing(5); // Adjust spacing as needed

                        // Action handlers
                        btnEdit.setOnAction((event) -> {
                            Product product = getTableView().getItems().get(getIndex());
                            switchToEditProduct(product);
                        });

                        btnView.setOnAction((event) -> {
                            Product product = getTableView().getItems().get(getIndex());
                            switchToViewProduct(product);
                        });

                        btnRemove.setOnAction((event) -> {
                            Product product = getTableView().getItems().get(getIndex());
                            removeProduct(product);
                        });

                        hbox.setAlignment(Pos.CENTER); // Align buttons as needed
                        hbox.setSpacing(5);
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(hbox);
                        }
                    }
                };
                return cell;
            }
        };

        colActions.setCellFactory(cellFactory);
        tableProducts.getColumns().add(colActions);
    }

    private void addActionsColumnToOrders() {
        TableColumn<Order, Void> colActions = new TableColumn<>("Action");
        Callback<TableColumn<Order, Void>, TableCell<Order, Void>> cellFactory = new Callback<>() {
            @Override
            public TableCell<Order, Void> call(final TableColumn<Order, Void> param) {
                final TableCell<Order, Void> cell = new TableCell<>() {
                    private final Button btnTrack = new Button("Track");
                    private final Button btnUpdate = new Button("Update");
                    private final HBox hbox = new HBox(btnTrack,btnUpdate);

                    {
                        // Styling for buttons
                        btnTrack.getStyleClass().add("track-button");
                        btnUpdate.getStyleClass().add("update-button");

                        // Set spacing between buttons
                        hbox.setSpacing(5); // Adjust spacing as needed

                        // Action handlers
                        btnTrack.setOnAction((event) -> {
                            Order order = getTableView().getItems().get(getIndex());
                            System.out.println(order);
                            switchToTrackProduct(order);
                            System.out.println("Tracking order: " + order.getOrderNo());

                            // Fetch and display products for the selected order
                            fetchAndDisplayOrderedProduct(order.getOrderNo());
                        });

                        btnUpdate.setOnAction((event) -> {
                            Order order = getTableView().getItems().get(getIndex());
                            String currentStatus = order.getOrderStatus();
                            String targetStatus = "";

                            // Determine target status based on current status
                            if (currentStatus.equals("Processing")) {
                                targetStatus = "Packed";
                                showShippedInformationDialog("Packed");
                            } else if (currentStatus.equals("Packed")) {
                                targetStatus = "Shipped";
                                showShippedInformationDialog("Shipped");
                            } else if (currentStatus.equals("Shipped")) {
                                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                alert.setTitle("Amazon List Guard");
                                alert.setHeaderText(null);
                                alert.setContentText("This order has already been shipped. Proceed to tracking the order.");

                                alert.showAndWait();
                            } else {
                                System.out.println("Cannot update from current status: " + currentStatus);
                                return;
                            }

                            // Update the order status in the database
                            updateOrderStatus(order.getOrderNo(), targetStatus);

                            // Fetch and display updated order and shipping status
                            fetchAndDisplayOrderedProduct(order.getOrderNo());
                        });

                        hbox.setAlignment(Pos.CENTER); // Align buttons as needed
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(hbox);
                        }
                    }
                };
                return cell;
            }
        };

        colActions.setCellFactory(cellFactory);
        tableOrders.getColumns().add(colActions);
    }

    private void fetchAndDisplayProducts() {
        int sellerId = getSellerId(seller.getEmail());
        if (sellerId != -1) {
            ObservableList<Product> products = getProductsBySellerId(sellerId);

            // Filter out products with quantity 0 or less
            ObservableList<Product> filteredProducts = FXCollections.observableArrayList();
            for (Product product : products) {
                if (product.getProductQuantity() > 0) {
                    filteredProducts.add(product);
                }
            }

            tableProducts.setItems(filteredProducts); // Set items to TableView
            tableOverview.setItems(filteredProducts);
        } else {
            showAlert("Seller ID not found!");
            logger.warning("Seller ID not found for email: " + seller.getEmail());
        }
    }

    private void updateOrderDetailsLabels(Order order) {
        trackOrderPage.setText(seller.getShopName() + " > Orders > Track Order > " + order.getOrderNo());
        orderIDLabel.setText(order.getOrderNo());
        addressLabel.setText(order.getAddressBuyer());
        statusLabel.setText(order.getOrderStatus());
    }

    private void fetchAndDisplayOrderedProduct(String orderNo) {
        try {
            int orderID = extractOrderID(orderNo);

            // Fetch ordered products
            ObservableList<Product> orderedProducts = getOrderedProductsByOrderID(orderID);
            if (!orderedProducts.isEmpty()) {
                Platform.runLater(() -> tableOrdered.setItems(orderedProducts));
            } else {
                showAlert("No ordered products found for order No: " + orderNo);
                logger.warning("No ordered products found for order No: " + orderNo);
            }

            // Fetch orders made to the seller (assuming getOrdersBySellerId returns ObservableList<Order>)
            ObservableList<Order> sellerOrders = getOrdersBySellerId(getSellerIdByOrderId(orderID));

            // Find the specific order and update labels
            for (Order order : sellerOrders) {
                if (order.getOrderNo().equals(orderNo)) {
                    updateOrderDetailsLabels(order);
                    return;
                }
            }

            // If order with orderNo was not found in sellerOrders
            showAlert("Order details not found for order No: " + orderNo);
            logger.warning("Order details not found for order No: " + orderNo);

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error fetching ordered products for order No: " + orderNo, e);
            showAlert("Error fetching ordered products. Please try again.");
        } catch (NumberFormatException e) {
            logger.log(Level.SEVERE, "Invalid order number format: " + orderNo, e);
            showAlert("Invalid order number format. Please check and try again.");
        }
    }

    private ObservableList<Product> getOrderedProductsByOrderID(int orderID) throws SQLException {
        ObservableList<Product> orderedProducts = FXCollections.observableArrayList();

        // Retrieve the seller ID for the given order ID
        int sellerId = getSellerIdByOrderId(orderID);

        // Fetch products listed by the seller
        ObservableList<Product> sellerProducts = getProductsBySellerId(sellerId);

        // Fetch orders made to the seller
        ObservableList<Order> sellerOrders = getOrdersBySellerId(sellerId);

        // Filter products ordered within the specific order ID
        for (Order order : sellerOrders) {
            if (order.getOrderNo().equals("ORD " + orderID)) {
                // Find corresponding product in seller's products
                for (Product product : sellerProducts) {
                    if (order.getProductName().equals(product.getProductName())) {
                        orderedProducts.add(product);
                        break; // Found the product, so break the inner loop
                    }
                }
            }
        }

        return orderedProducts;
    }

    private int getSellerIdByOrderId(int orderID) throws SQLException {
        int sellerId = -1;
        String query = "SELECT sellerID FROM orders WHERE orderID = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, orderID);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                sellerId = resultSet.getInt("sellerID");
            }
        }
        return sellerId;
    }

    private int extractOrderID(String orderNo) {
        return Integer.parseInt(orderNo.replace("ORD ", ""));
    }

    private int getSellerId(String sellerEmail) {
        int sellerId = -1;
        String query = "SELECT sellerID FROM seller WHERE email_seller = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, sellerEmail);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                sellerId = resultSet.getInt("sellerID");
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error fetching seller ID", e);
        }
        return sellerId;
    }

    private ObservableList<Product> getProductsBySellerId(int sellerId) {
        ObservableList<Product> products = FXCollections.observableArrayList();
        String query = "SELECT productID, product_listing_date, product_name, product_category, product_price, product_quantity, product_tags, product_desc, product_attributes, product_discount FROM product WHERE sellerID = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, sellerId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int productId = resultSet.getInt("productID");
                Date date = resultSet.getDate("product_listing_date");
                LocalDate listingDate = (date != null) ? date.toLocalDate() : null; // Check for null
                String productName = resultSet.getString("product_name");
                String productCategory = resultSet.getString("product_category");
                double productPrice = resultSet.getDouble("product_price");
                int productQuantity = resultSet.getInt("product_quantity");
                String productTags = resultSet.getString("product_tags");
                String productDesc = resultSet.getString("product_desc");
                String productAttributes = resultSet.getString("product_attributes");
                double productDiscount = resultSet.getDouble("product_discount");
                Product product = new Product(productId, listingDate, productName, productCategory, productPrice, productQuantity, productTags, productDesc, productAttributes, productDiscount, seller.getShopName());
                products.add(product);
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error fetching products for seller ID: " + sellerId, e);
        }
        return products;
    }

    private void fetchAndDisplayOrders() {
        int sellerId = getSellerId(seller.getEmail());
        if (sellerId != -1) {
            ObservableList<Order> orders = getOrdersBySellerId(sellerId);
            if (!orders.isEmpty()) {
                tableOrders.setItems(orders); // Set items to TableView
            }
        } else {
            showAlert("Seller ID not found!");
            logger.warning("Seller ID not found for email: " + seller.getEmail());
        }
    }

    private ObservableList<Order> getOrdersBySellerId(int sellerId) {
        ObservableList<Order> orders = FXCollections.observableArrayList();
        String query = "SELECT o.orderID, o.buyerID, oi.order_date, oi.order_itemID, " +
                "p.product_name, p.product_category, p.product_price, oi.item_quantity, " +
                "b.name_buyer, b.email_buyer, b.address_buyer, b.contactinfo_buyer, " +
                "oi.order_status " +
                "FROM orders o " +
                "JOIN order_item oi ON o.order_itemID = oi.order_itemID " +
                "JOIN product p ON oi.productID = p.productID " +
                "JOIN buyer b ON o.buyerID = b.buyerID " +
                "WHERE o.sellerID = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, sellerId);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int orderId = resultSet.getInt("orderID");
                LocalDate orderDate = resultSet.getDate("order_date").toLocalDate();
                int orderItemId = resultSet.getInt("order_itemID");
                String productName = resultSet.getString("product_name");
                String productCategory = resultSet.getString("product_category");
                double itemPrice = resultSet.getDouble("product_price");
                int itemQuantity = resultSet.getInt("item_quantity");

                // Buyer details
                String nameBuyer = resultSet.getString("name_buyer");
                String emailBuyer = resultSet.getString("email_buyer");
                String addressBuyer = resultSet.getString("address_buyer");
                String contactInfoBuyer = resultSet.getString("contactinfo_buyer");

                // Order status from order_item table
                String orderStatus = resultSet.getString("order_status");

                // Assuming your Order class constructor includes these details
                String orderNo = "ORD " + orderId; // Assuming you construct orderNo as "ORD" + orderId

                Order order = new Order(orderNo, sellerId, 1, orderDate, orderItemId, productName,
                        productCategory, itemPrice, itemQuantity, nameBuyer,
                        emailBuyer, addressBuyer, contactInfoBuyer, orderStatus);
                orders.add(order);
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error fetching orders for seller ID: " + sellerId, e);
        }

        return orders;
    }

    @FXML
    private void createProduct() {
        String name = nameTF.getText();
        String description = descriptionTA.getText();
        String category = categoryComboBox.getValue();
        String stocksText = stocksTF.getText();
        String basePriceText = basepriceTF.getText();
        String discountText = discountsTF.getText();
        String attributes = attributesTF.getText();
        String tags = tagsTF.getText();

        // Validate numeric fields
        if (!validateNumericInput(stocksText, basePriceText, discountText)) {
            showAlert("Invalid numeric input. Please enter valid numbers for Stocks, Base Price, and Discount.");
            return;
        }

        int stocks = Integer.parseInt(stocksText);
        double basePrice = Double.parseDouble(basePriceText);
        double discount = Double.parseDouble(discountText);

        if (name.isEmpty() || description.isEmpty() || category.isEmpty() || stocks <= 0 || basePrice <= 0) {
            showAlert("Please fill in all required fields with valid values.");
            return;
        }

        // Check for duplicate product
        if (isDuplicateProduct(name, description, category)) {
            showAlert("A product with a similar name and description already exists.");
            return;
        }

        int sellerId = getSellerId(seller.getEmail());
        if (sellerId == -1) {
            showAlert("Seller ID not found.");
            return;
        }

        // Set the current date as the listing date
        LocalDate listingDate = LocalDate.now();

        String insertQuery = "INSERT INTO product (product_name, product_desc, product_category, product_price, product_attributes, " +
                "product_tags, product_discount, sellerID, product_listing_date, product_quantity) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, description);
            preparedStatement.setString(3, category);
            preparedStatement.setDouble(4, basePrice - (basePrice * discount / 100));
            preparedStatement.setString(5, attributes);
            preparedStatement.setString(6, tags);
            preparedStatement.setDouble(7, discount);
            preparedStatement.setInt(8, sellerId);
            preparedStatement.setObject(9, listingDate); // Set listing date
            preparedStatement.setInt(10, stocks); // Set product_quantity
            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                // Retrieve auto-generated product_id
                ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int productId = generatedKeys.getInt(1);
                    showAlert("Product added successfully with ID: " + productId);
                    fetchAndDisplayProducts();
                    clearProductFields();
                    switchToProducts();
                } else {
                    showAlert("Failed to retrieve product ID after insertion.");
                }
            } else {
                showAlert("Failed to add product.");
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error adding product", e);
            showAlert("An error occurred while adding the product. Please try again.");
        }
    }

    private boolean isDuplicateProduct(String productName, String productDescription, String productCategory) {
        int sellerId = getSellerId(seller.getEmail());
        if (sellerId == -1) {
            logger.warning("Seller ID not found.");
            return true; // Consider as duplicate to prevent invalid insertion
        }

        // First, check if there's an exact match for the product name, description, and category
        String queryExactCheck = "SELECT COUNT(*) AS count FROM product WHERE sellerID = ? AND product_name = ? AND product_desc = ? AND product_category = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(queryExactCheck)) {
            preparedStatement.setInt(1, sellerId);
            preparedStatement.setString(2, productName);
            preparedStatement.setString(3, productDescription);
            preparedStatement.setString(4, productCategory);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next() && resultSet.getInt("count") > 0) {
                return true; // Found an exact match for product name, description, and category, consider it a duplicate
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error checking for exact duplicate product", e);
            return true; // Consider error as duplicate to prevent invalid insertion
        }

        // If no exact match, check for similar product descriptions and names
        String query = "SELECT product_name, product_desc, product_category FROM product WHERE sellerID = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, sellerId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String existingProductName = resultSet.getString("product_name");
                String existingProductDescription = resultSet.getString("product_desc");
                String existingProductCategory = resultSet.getString("product_category");

                // Check if product names are similar and descriptions are similar and categories match
                if (isSimilar(existingProductName, productName) && isSimilar(existingProductDescription, productDescription) && existingProductCategory.equalsIgnoreCase(productCategory)) {
                    return true; // Found a similar product, consider it a duplicate
                }

                // Check if product names are similar and descriptions are similar and categories does not match
                if (isSimilar(existingProductName, productName) && isSimilar(existingProductDescription, productDescription) && !existingProductCategory.equalsIgnoreCase(productCategory)) {
                    return true; // Found a similar product, consider it a duplicate
                }

                // Check if product names are different but descriptions are similar and categories match
                if (!existingProductName.equalsIgnoreCase(productName) && isSimilar(existingProductDescription, productDescription) && existingProductCategory.equalsIgnoreCase(productCategory)) {
                    return true; // Found a product with a different name but similar description, consider it a duplicate
                }

                // Check if product names are different but descriptions are similar and categories does not match
                if (!existingProductName.equalsIgnoreCase(productName) && isSimilar(existingProductDescription, productDescription) && !existingProductCategory.equalsIgnoreCase(productCategory)) {
                    return true; // Found a product with a different name but similar description, consider it a duplicate
                }

                // Check if product names are similar but descriptions are different and categories match
                if (isSimilar(existingProductName, productName) && !existingProductDescription.equalsIgnoreCase(productDescription) && existingProductCategory.equalsIgnoreCase(productCategory)) {
                    return true; // Found a product with a different description but similar name, consider it a duplicate
                }

                // Check if product names are similar but descriptions are different and categories does not match
                if (isSimilar(existingProductName, productName) && !existingProductDescription.equalsIgnoreCase(productDescription) && !existingProductCategory.equalsIgnoreCase(productCategory)) {
                    return true; // Found a product with a different description but similar name, consider it a duplicate
                }

            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error checking for duplicate product by description", e);
            return true; // Consider error as duplicate to prevent invalid insertion
        }
        return false; // No duplicate found
    }

    private boolean isSimilar(String text1, String text2) {
        // Normalize and simplify the texts
        String normalizedText1 = normalizeText(text1);
        String normalizedText2 = normalizeText(text2);

        // Extract model numbers
        String model1 = extractModelNumber(normalizedText1);
        String model2 = extractModelNumber(normalizedText2);

        // Check if model numbers are the same
        if (!model1.equals(model2)) {
            return false; // Model numbers must match for descriptions to be considered similar
        }

        // Check if the essential parts of the text are similar
        return hasSignificantOverlap(normalizedText1, normalizedText2, 0.8); // Using 0.8 as the threshold
    }

    private String normalizeText(String text) {
        // Normalize text by converting to lowercase and removing non-alphanumeric characters
        return text.toLowerCase().replaceAll("[^a-z0-9\\s]", "").trim();
    }

    private String extractModelNumber(String text) {
        // Extract the model number from the text
        Pattern pattern = Pattern.compile("[a-z0-9]+");
        Matcher matcher = pattern.matcher(text);
        if (matcher.find()) {
            return matcher.group();
        }
        return "";
    }

    private boolean hasSignificantOverlap(String text1, String text2, double threshold) {
        // Split texts into words
        Set<String> words1 = new HashSet<>(Arrays.asList(text1.split("\\s+")));
        Set<String> words2 = new HashSet<>(Arrays.asList(text2.split("\\s+")));

        // Calculate intersection
        Set<String> intersection = new HashSet<>(words1);
        intersection.retainAll(words2);

        // Calculate overlap ratio
        double overlapRatio = (double) intersection.size() / Math.min(words1.size(), words2.size());

        return overlapRatio >= threshold;
    }

    private boolean validateNumericInput(String... inputs) {
        try {
            for (String input : inputs) {
                Double.parseDouble(input); // Try parsing as double
            }
            return true; // All inputs are valid numbers
        } catch (NumberFormatException e) {
            return false; // At least one input is not a valid number
        }
    }

    @FXML
    private void updateProduct() {
        // Gather updated values from UI
        String name = nameTFEdit.getText();
        String description = descriptionTAEdit.getText();
        String category = categoryComboBoxEdit.getValue();
        String stocksText = stocksTFEdit.getText();
        String basePriceText = basepriceTFEdit.getText();
        String discountText = discountsTFEdit.getText();
        String attributes = attributesTFEdit.getText();
        String tags = tagsTFEdit.getText();

        // Validate numeric fields
        if (!validateNumericInput(stocksText, basePriceText, discountText)) {
            showAlert("Invalid numeric input. Please enter valid numbers for Stocks, Base Price, and Discount.");
            return;
        }

        int stocks = Integer.parseInt(stocksText);
        double basePrice = Double.parseDouble(basePriceText);
        double discount = Double.parseDouble(discountText);

        // Validate non-numeric fields
        if (name.isEmpty() || description.isEmpty() || category.isEmpty() || stocks <= 0 || basePrice <= 0) {
            showAlert("Please fill in all required fields with valid values.");
            return;
        }

        // Check for duplicate product
        if (isDuplicateProduct(name, description, category)) {
            showAlert("A product with a similar name and description already exists.");
            return;
        }

        int sellerId = getSellerId(seller.getEmail());
        if (sellerId == -1) {
            showAlert("Seller ID not found.");
            return;
        }

        // Update the selected product
        try {
            // Update query with product_discount included
            String updateQuery = "UPDATE product SET product_name = ?, product_desc = ?, product_category = ?, product_price = ?, product_attributes = ?, product_tags = ?, product_quantity = ?, product_discount = ? WHERE productID = ?";
            try (Connection connection = DatabaseConnection.getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {
                preparedStatement.setString(1, name);
                preparedStatement.setString(2, description);
                preparedStatement.setString(3, category);
                preparedStatement.setDouble(4, basePrice - (basePrice * discount / 100));
                preparedStatement.setString(5, attributes);
                preparedStatement.setString(6, tags);
                preparedStatement.setInt(7, stocks);
                preparedStatement.setDouble(8, discount);
                preparedStatement.setInt(9, selectedProduct.getProductId());

                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected > 0) {
                    showAlert("Product updated successfully!");
                    fetchAndDisplayProducts(); // Refresh table view
                    switchToProducts(); // Switch back to products tab or update view
                } else {
                    showAlert("Failed to update product.");
                }
            } catch (SQLException e) {
                logger.log(Level.SEVERE, "Error updating product", e);
                showAlert("An error occurred while updating the product. Please try again.");
            }
        } catch (NumberFormatException e) {
            showAlert("Invalid numeric input. Please enter valid numbers for Stocks, Base Price, and Discount.");
        }
    }

    private void removeProduct(Product product) {
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Amazon List Guard");
        confirmation.setHeaderText(null);
        confirmation.setContentText("Are you sure you want to remove this product?");
        confirmation.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                int productId = product.getProductId();
                System.out.print(productId);
                String deleteQuery = "DELETE FROM product WHERE productID = ?";
                try (Connection connection = DatabaseConnection.getConnection();
                     PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery)) {
                    preparedStatement.setInt(1, productId);
                    int rowsAffected = preparedStatement.executeUpdate();
                    if (rowsAffected > 0) {
                        showAlert("Product removed successfully!");
                        // Refresh table view to reflect changes
                        fetchAndDisplayProducts(); // This method should update the table
                    } else {
                        showAlert("Failed to remove product.");
                    }
                } catch (SQLException e) {
                    logger.log(Level.SEVERE, "SQL Exception occurred while deleting product", e);
                    showAlert("An error occurred while removing the product. Please try again.");
                }
            }
        });
    }

    @FXML
    private void switchToViewProduct(Product product) {
        tabPane.getSelectionModel().select(viewProductTab);

        fetchAndDisplayProducts();
        fetchAndDisplayOrders();
        // Populate the fields with the product details
        if (product != null) {

            double originalPrice = product.getProductPrice();
            double discountPercentage = product.getProductDiscount();

            double basePrice = originalPrice / (1 - (discountPercentage / 100.0));

            double roundedBasePrice = Math.round(basePrice * 100.0) / 100.0;

            String formattedBasePrice = String.format("%.2f", roundedBasePrice);

            nameTFView.setText(product.getProductName());
            descriptionTAView.setText(product.getProductDesc());
            stocksTFView.setText(String.valueOf(product.getProductQuantity()));
            basepriceTFView.setText(formattedBasePrice);
            discountsTFView.setText(String.valueOf(discountPercentage));
            tagsTFView.setText(product.getProductTags());
            attributesTFView.setText(product.getProductAttributes());

            // Set the category ComboBox
            categoryComboBoxView.getSelectionModel().select(product.getProductCategory());

            // Disable editing of fields if necessary
            nameTFView.setEditable(false);
            descriptionTAView.setEditable(false);
            stocksTFView.setEditable(false);
            basepriceTFView.setEditable(false);
            discountsTFView.setEditable(false);
            tagsTFView.setEditable(false);
            attributesTFView.setEditable(false);
            categoryComboBoxView.setDisable(true);

            // Additional logic or UI updates as needed for view mode
        } else {
            showAlert("Product details are null or invalid.");
        }
    }

    @FXML
    private void switchToEditProduct(Product product) {

        tabPane.getSelectionModel().select(editproductTab);
        fetchAndDisplayProducts();
        fetchAndDisplayOrders();

        selectedProduct = product;

        // Populate the fields with the product details
        if (product != null) {

            double originalPrice = product.getProductPrice();
            double discountPercentage = product.getProductDiscount();

            double basePrice = originalPrice / (1 - (discountPercentage / 100.0));

            double roundedBasePrice = Math.round(basePrice * 100.0) / 100.0;

            String formattedBasePrice = String.format("%.2f", roundedBasePrice);

            nameTFEdit.setText(product.getProductName());
            descriptionTAEdit.setText(product.getProductDesc());
            stocksTFEdit.setText(String.valueOf(product.getProductQuantity()));
            basepriceTFEdit.setText(formattedBasePrice);
            discountsTFEdit.setText(String.valueOf(discountPercentage));
            tagsTFEdit.setText(product.getProductTags());
            attributesTFEdit.setText(product.getProductAttributes());

            // Set the category ComboBox
            categoryComboBoxEdit.getSelectionModel().select(product.getProductCategory());

            // Populate other fields as needed
        } else {
            showAlert("Product details are null or invalid.");
        }
    }

    private void setSellerData(Seller seller) {
        store_name.setText(seller.getName());
        store_email.setText(seller.getEmail());
        overviewPage.setText(seller.getShopName() + " > Overview");
        productsPage.setText(seller.getShopName() + " > Products");
        ordersPage.setText(seller.getShopName() + " > Orders");
        notificationsPage.setText(seller.getShopName() + " > Notifications");
        createProductPage.setText(seller.getShopName() + " > Products > Create Product");
        editProductPage.setText(seller.getShopName() + " > Products > Edit Product");
        viewProductPage.setText(seller.getShopName() + " > Products > View Product");
    }

    private void loadProfilePicture() {
        Image image = new Image(getClass().getResourceAsStream("/assets/profilepicture.jpg"));
        if (profile_picture != null) {
            profile_picture.setFill(new ImagePattern(image));
            settings_profile.setFill(new ImagePattern(image));
        } else {
            logger.warning("Profile picture is null");
        }
    }

    private void populateCategoryComboBox() {
        ObservableList<String> categories = FXCollections.observableArrayList(
                "Arts & Crafts", "Automotive", "Baby", "Beauty & Personal Care",
                "Books", "Boys' Fashion", "Computers", "Deals", "Digital Music", "Electronics",
                "Girls' Fashion", "Health & Household", "Home & Kitchen", "Industrial & Scientific",
                "Kindle Store", "Luggage", "Men's Fashion", "Movies & TV", "Music, CDs & Vinyl",
                "Pet Supplies", "Prime Video", "Software", "Sports & Outdoors", "Tools & Home Improvement",
                "Toys & Games", "Video Games", "Women's Fashion");
        categoryComboBox.setItems(categories);
        categoryComboBox.setValue("Arts & Crafts"); // Set default value

        categoryComboBoxEdit.setItems(categories);
        categoryComboBoxEdit.setValue("Arts & Crafts"); // Set default value
    }

    private void clearProductFields() {
        nameTF.clear();
        descriptionTA.clear();
        stocksTF.clear();
        basepriceTF.clear();
        discountsTF.clear();
        attributesTF.clear();
        tagsTF.clear();
        categoryComboBox.setValue("Arts & Crafts");

        nameTFEdit.clear();
        descriptionTAEdit.clear();
        stocksTFEdit.clear();
        basepriceTFEdit.clear();
        discountsTFEdit.clear();
        attributesTFEdit.clear();
        tagsTFEdit.clear();
        categoryComboBoxEdit.setValue("Arts & Crafts");
    }

    @FXML
    private void logout() {

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Amazon List Guard");
        alert.setHeaderText(null);
        alert.setContentText("Successfully logged out!");
        alert.showAndWait();

        clearUserData();
        navigateToLoginScreen();
    }

    private void clearUserData() {
        store_name.setText("");
        store_email.setText("");
        profile_picture.setFill(null); // Clear profile picture if needed
    }

    private void navigateToLoginScreen() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("login-view.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) tabPane.getScene().getWindow(); // Get current stage
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            logger.log(Level.SEVERE, "IO Exception occurred while navigating to login screen", e);
        }
    }

    @FXML
    private void switchToOverview() {
        tabPane.getSelectionModel().select(overviewTab);
        fetchAndDisplayProducts();
        fetchAndDisplayOrders();
    }

    @FXML
    private void switchToProducts() {
        tabPane.getSelectionModel().select(productsTab);
        fetchAndDisplayProducts();
        fetchAndDisplayOrders();
    }

    @FXML
    private void switchToOrders() {
        tabPane.getSelectionModel().select(ordersTab);
        fetchAndDisplayProducts();
        fetchAndDisplayOrders();
    }

    @FXML
    private void switchToNotifications() {
        tabPane.getSelectionModel().select(notificationsTab);
        fetchAndDisplayProducts();
        fetchAndDisplayOrders();
    }

    @FXML
    private void switchToHelpCenter() {
        tabPane.getSelectionModel().select(helpcenterTab);
        fetchAndDisplayProducts();
        fetchAndDisplayOrders();
    }

    @FXML
    private void switchToSettings() {
        tabPane.getSelectionModel().select(settingsTab);
        fetchAndDisplayProducts();
        fetchAndDisplayOrders();
    }

    @FXML
    private void switchToCreateProduct() {
        tabPane.getSelectionModel().select(createproductTab);
        fetchAndDisplayProducts();
        fetchAndDisplayOrders();
    }

    @FXML
    private void switchToTrackProduct(Order order) {
        tabPane.getSelectionModel().select(trackorderTab);
        fetchAndDisplayProducts();
        fetchAndDisplayOrders();

        try {
            // Step 1: Retrieve current order status from order_item table
            String currentStatus = getCurrentOrderStatus(extractOrderID(order.getOrderNo()));

            // Debug statement to check currentStatus
            System.out.println("Current Status: " + currentStatus);

            // Step 2: Determine new order status
            String newStatus = determineNewOrderStatus(currentStatus);

            // Debug statement to check newStatus
            System.out.println("New Status: " + newStatus);

            // Step 3: Determine button state based on newStatus
            if (currentStatus.equals("Processing") || currentStatus.equals("Packed") || currentStatus.equals("Delivered")) {
                updateButtonTrack.setDisable(true);
                System.out.println("Button disabled");
            } else {
                updateButtonTrack.setDisable(false);
                System.out.println("Button enabled");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            // Handle SQLException appropriately, e.g., show error message
            showAlert("Failed to update order status: " + e.getMessage());
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Amazon List Guard");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void showUnderDevelopmentDialog() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Amazon List Guard");
        alert.setHeaderText(null);
        alert.setContentText("Please contact the administrator before proceeding.");
        alert.showAndWait();
    }
}