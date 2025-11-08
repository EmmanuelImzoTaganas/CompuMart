package compumart.compumart.controller;

import compumart.compumart.model.Cart;
import compumart.compumart.model.Product;
import compumart.compumart.model.User;
import compumart.compumart.repositories.CartRepository;
import compumart.compumart.repositories.ProductRepository;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Alert;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.Map;

public class CartController extends BaseController {

    private final CartRepository cartRepository = new CartRepository();
    private final ProductRepository productRepository = new ProductRepository();

    @FXML private VBox cartItemsContainer;
    @FXML private Label subtotalLabel;
    @FXML private Label taxLabel;
    @FXML private Label totalLabel;

    @FXML
    public void initialize() {}

    @Override
    public void onSceneShown() {
        loadCartItems();
    }


    @FXML
    public void toStore() {
        app.switchTo("main");
    }

    private void loadCartItems() {
        cartItemsContainer.getChildren().clear();

        User currentUser = getCurrentUser();
        if (currentUser == null) {
            showAlert(Alert.AlertType.WARNING, "Not Logged In", "Please log in to view your cart.");
            app.switchTo("login");  // or whichever scene name is your login screen
            return;
        }


        // assign cart once, never reassign this variable
        final Cart cart = cartRepository.findByEmail(currentUser.getEmail()) == null
                ? createNewCartFor(currentUser)
                : cartRepository.findByEmail(currentUser.getEmail());
        currentUser.setCart(cart);

        if (cart.getCart().isEmpty()) {
            cartItemsContainer.getChildren().add(new Label("Your cart is empty."));
            updateSummary(0.0);
            return;
        }

        double subtotal = 0.0;

        for (Map.Entry<String, Integer> entry : cart.getCart().entrySet()) {
            final String productId = entry.getKey();
            final int quantity = entry.getValue();

            Product product = productRepository.findById(productId);
            if (product == null) {
                continue;
            }

            double itemTotalPrice = product.getPrice() * quantity;
            subtotal += itemTotalPrice;

            HBox itemRow = new HBox(10);
            itemRow.setAlignment(Pos.CENTER_LEFT);

            Label nameLabel = new Label(product.getName());
            nameLabel.setPrefWidth(180);

            Label qtyLabel = new Label(String.valueOf(quantity));
            qtyLabel.setPrefWidth(30);

            Button minusBtn = new Button("-");
            minusBtn.setOnAction(new javafx.event.EventHandler<javafx.event.ActionEvent>() {
                @Override
                public void handle(javafx.event.ActionEvent e) {
                    Integer currentQty = cart.getCart().get(productId);
                    if (currentQty != null) {
                        int newQty = currentQty - 1;
                        if (newQty <= 0) {
                            cart.getCart().remove(productId);
                        } else {
                            cart.getCart().put(productId, newQty);
                        }
                        cartRepository.saveOrUpdate(cart);
                        System.out.println("Cart updated after minus");
                        loadCartItems();
                    }
                }
            });

            Button plusBtn = new Button("+");
            plusBtn.setOnAction(new javafx.event.EventHandler<javafx.event.ActionEvent>() {
                @Override
                public void handle(javafx.event.ActionEvent e) {
                    Integer currentQty = cart.getCart().get(productId);
                    if (currentQty != null) {
                        cart.getCart().put(productId, currentQty + 1);
                        cartRepository.saveOrUpdate(cart);
                        System.out.println("Cart updated after plus");
                        loadCartItems();
                    }
                }
            });

            Button removeBtn = new Button("Remove");
            removeBtn.setOnAction(new javafx.event.EventHandler<javafx.event.ActionEvent>() {
                @Override
                public void handle(javafx.event.ActionEvent e) {
                    cart.getCart().remove(productId);
                    cartRepository.saveOrUpdate(cart);
                    System.out.println("Item removed");
                    loadCartItems();
                }
            });

            Label priceLabel = new Label("₱" + String.format("%.2f", itemTotalPrice));

            itemRow.getChildren().addAll(nameLabel, minusBtn, qtyLabel, plusBtn, priceLabel, removeBtn);
            cartItemsContainer.getChildren().add(itemRow);
        }

        updateSummary(subtotal);
    }

    private Cart createNewCartFor(User user) {
        Cart newCart = new Cart(user.getEmail(), user.getfName(), null);
        cartRepository.saveOrUpdate(newCart);
        System.out.println("Created new cart for email: " + user.getEmail());
        return newCart;
    }

    private void updateSummary(double subtotal) {
        double tax = subtotal * 0.08;
        double total = subtotal + tax;
        subtotalLabel.setText("₱" + String.format("%.2f", subtotal));
        taxLabel.setText("₱" + String.format("%.2f", tax));
        totalLabel.setText("₱" + String.format("%.2f", total));
    }
}
