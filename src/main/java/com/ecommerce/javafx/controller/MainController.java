package com.ecommerce.javafx.controller;

import com.ecommerce.javafx.service.ApiService;
import com.ecommerce.model.dto.response.ProductResponse;
import com.ecommerce.model.dto.response.CartResponse;
import com.ecommerce.model.dto.response.OrderResponse;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.List;
import java.util.Optional;

public class MainController {

    @FXML private VBox contentArea;
    @FXML private Button productsBtn, cartBtn, ordersBtn, profileBtn;

    private final ApiService apiService = new ApiService();
    private final ObservableList<ProductResponse> productList = FXCollections.observableArrayList();
    private final ObservableList<CartResponse> cartList = FXCollections.observableArrayList();
    private final ObservableList<OrderResponse> orderList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        showProducts();
    }

    @FXML
    private void showProducts() {
        setActiveButton(productsBtn);
        Platform.runLater(() -> {
            try {
                List<ProductResponse> products = apiService.getProducts();
                productList.setAll(products);
                showProductTable();
            } catch (Exception e) {
                showError("Failed to load products", e.getMessage());
            }
        });
    }

    @FXML
    private void showCart() {
        setActiveButton(cartBtn);
        Platform.runLater(() -> {
            try {
                CartResponse cart = apiService.getCart();
                showCartView(cart);
            } catch (Exception e) {
                showError("Failed to load cart", e.getMessage());
            }
        });
    }

    @FXML
    private void showOrders() {
        setActiveButton(ordersBtn);
        Platform.runLater(() -> {
            try {
                List<OrderResponse> orders = apiService.getOrders();
                orderList.setAll(orders);
                showOrderTable();
            } catch (Exception e) {
                showError("Failed to load orders", e.getMessage());
            }
        });
    }

    @FXML
    private void showProfile() {
        setActiveButton(profileBtn);
        showProfileView();
    }

    private void showProductTable() {
        TableView<ProductResponse> table = new TableView<>(productList);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<ProductResponse, Long> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(data -> new javafx.beans.property.SimpleObjectProperty<>(data.getValue().getId()));

        TableColumn<ProductResponse, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getName()));

        TableColumn<ProductResponse, Double> priceCol = new TableColumn<>("Price");
        priceCol.setCellValueFactory(data -> new javafx.beans.property.SimpleObjectProperty<>(data.getValue().getPrice()));

        TableColumn<ProductResponse, Integer> stockCol = new TableColumn<>("Stock");
        stockCol.setCellValueFactory(data -> new javafx.beans.property.SimpleObjectProperty<>(data.getValue().getStockQuantity()));

        TableColumn<ProductResponse, Void> actionCol = new TableColumn<>("Action");
        actionCol.setCellFactory(col -> new TableCell<>() {
            private final Button addBtn = new Button("Add to Cart");
            {
                addBtn.setOnAction(e -> {
                    ProductResponse product = getTableView().getItems().get(getIndex());
                    addToCart(product);
                });
            }
            @Override protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : addBtn);
            }
        });

        table.getColumns().addAll(idCol, nameCol, priceCol, stockCol, actionCol);
        contentArea.getChildren().setAll(table);
    }

    private void addToCart(ProductResponse product) {
        TextInputDialog dialog = new TextInputDialog("1");
        dialog.setTitle("Add to Cart");
        dialog.setHeaderText("Add " + product.getName() + " to cart");
        dialog.setContentText("Quantity:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(qty -> {
            try {
                int quantity = Integer.parseInt(qty);
                apiService.addToCart(product.getId(), quantity);
                showInfo("Success", "Added " + quantity + " x " + product.getName() + " to cart");
            } catch (Exception e) {
                showError("Failed to add to cart", e.getMessage());
            }
        });
    }

    private void showCartView(CartResponse cart) {
        VBox cartBox = new VBox(15);
        if (cart.getItems() == null || cart.getItems().isEmpty()) {
            cartBox.getChildren().add(new Label("Your cart is empty"));
        } else {
            for (var item : cart.getItems()) {
                HBox itemBox = new HBox(15);
                itemBox.setStyle("-fx-padding: 10; -fx-background-color: #f8f9fa; -fx-background-radius: 5;");
                itemBox.getChildren().addAll(
                    new Label(item.getProductName() + " x " + item.getQuantity()),
                    new Label("$" + String.format("%.2f", item.getPrice() * item.getQuantity())),
                    new Button("Remove")
                );
                cartBox.getChildren().add(itemBox);
            }
            Button checkoutBtn = new Button("Proceed to Checkout");
            checkoutBtn.setOnAction(e -> handleCheckout());
            cartBox.getChildren().add(checkoutBtn);
        }
        contentArea.getChildren().setAll(cartBox);
    }

    private void showOrderTable() {
        TableView<OrderResponse> table = new TableView<>(orderList);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<OrderResponse, Long> idCol = new TableColumn<>("Order ID");
        idCol.setCellValueFactory(data -> new javafx.beans.property.SimpleObjectProperty<>(data.getValue().getId()));

        TableColumn<OrderResponse, Double> totalCol = new TableColumn<>("Total");
        totalCol.setCellValueFactory(data -> new javafx.beans.property.SimpleObjectProperty<>(data.getValue().getTotalAmount()));

        TableColumn<OrderResponse, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getStatus()));

        TableColumn<OrderResponse, String> dateCol = new TableColumn<>("Date");
        dateCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(
            data.getValue().getCreatedAt() != null ? data.getValue().getCreatedAt().toString() : ""
        ));

        table.getColumns().addAll(idCol, totalCol, statusCol, dateCol);
        contentArea.getChildren().setAll(table);
    }

    private void showProfileView() {
        VBox profileBox = new VBox(10);
        profileBox.getChildren().addAll(
            new Label("Profile Information"),
            new Separator(),
            new Label("Email: user@example.com"),
            new Label("Name: John Doe"),
            new Label("Role: CUSTOMER")
        );
        contentArea.getChildren().setAll(profileBox);
    }

    private void handleCheckout() {
        showInfo("Checkout", "Checkout process would redirect to payment flow");
    }

    private void handleExit() {
        Platform.exit();
    }

    private void setActiveButton(Button active) {
        productsBtn.getStyleClass().remove("nav-active");
        cartBtn.getStyleClass().remove("nav-active");
        ordersBtn.getStyleClass().remove("nav-active");
        profileBtn.getStyleClass().remove("nav-active");
        active.getStyleClass().add("nav-active");
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showInfo(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}