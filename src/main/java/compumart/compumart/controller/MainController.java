package compumart.compumart.controller;

import javafx.animation.PauseTransition;
import javafx.util.Duration;
import compumart.compumart.model.Product;
import compumart.compumart.repositories.CartRepository;
import compumart.compumart.repositories.ProductRepository;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;

import java.util.List;
import java.util.stream.Collectors;

public class MainController extends BaseController {

    private final ProductRepository productRepository = new ProductRepository();

    @FXML private TilePane catalogTilePane;
    @FXML private ChoiceBox<String> categoryChoiceBox;
    @FXML private TextField searchBar;
    @FXML private Label cartItemCount;

    private ObservableList<Product> allProducts = FXCollections.observableArrayList();
    private ObservableList<Product> displayedProducts = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        catalogTilePane.setHgap(15);
        catalogTilePane.setVgap(15);
        catalogTilePane.setPrefColumns(3);
        catalogTilePane.setTileAlignment(javafx.geometry.Pos.TOP_LEFT);

        loadProducts();
        setupCategoryFilter();
        setupSearchBar();
        renderCatalog();
        updateCartCount();

        PauseTransition pause = new PauseTransition(Duration.millis(300));
        searchBar.textProperty().addListener((obs, oldText, newText) -> {
            pause.setOnFinished(e -> applyFilters());
            pause.playFromStart();
        });
    }

    // Refresh cart count whenever scene is shown
    @Override
    public void onSceneShown() {
        updateCartCount();
    }

    private void updateCartCount() {
        if (getCurrentUser() != null && getCurrentUser().getCart() != null) {
            int totalItems = getCurrentUser().getCart().getCart().values().stream().mapToInt(Integer::intValue).sum();
            cartItemCount.setText(String.valueOf(totalItems));
        } else {
            cartItemCount.setText("0");
        }
    }

    private void loadProducts() {
        allProducts.setAll(productRepository.findAll()
                .stream()
                .filter(Product::isActive)
                .filter(p -> !p.getType().equalsIgnoreCase("monitor"))
                .collect(Collectors.toList()));
        displayedProducts.setAll(allProducts);
    }

    private void setupCategoryFilter() {
        List<String> categories = allProducts.stream()
                .map(p -> p.getType().toLowerCase())
                .distinct()
                .sorted()
                .toList();

        categoryChoiceBox.setItems(FXCollections.observableArrayList(categories));
        categoryChoiceBox.getItems().add(0, "All");
        categoryChoiceBox.setValue("All");
        categoryChoiceBox.setOnAction(e -> applyFilters());
    }

    private void setupSearchBar() {
        searchBar.textProperty().addListener((obs, oldText, newText) -> applyFilters());
    }

    private void applyFilters() {
        String selectedCategory = categoryChoiceBox.getValue();
        String searchTerm = searchBar.getText().toLowerCase().trim();

        displayedProducts.setAll(allProducts.stream()
                .filter(p -> selectedCategory.equals("All") || p.getType().equalsIgnoreCase(selectedCategory))
                .filter(p -> p.getName().toLowerCase().contains(searchTerm))
                .collect(Collectors.toList()));

        renderCatalog();
    }

    private void renderCatalog() {
        catalogTilePane.getChildren().clear();
        for (Product product : displayedProducts) {
            VBox productCard = createProductCard(product);
            catalogTilePane.getChildren().add(productCard);
        }
    }

    private VBox createProductCard(Product product) {
        VBox card = new VBox(8);
        card.setStyle("-fx-background-color: white; -fx-padding: 10; -fx-border-radius: 5; -fx-background-radius: 5;" +
                "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 5, 0, 0, 0);");
        card.setPrefWidth(180);

        // Image
        if (product.getImage() != null && !product.getImage().isEmpty()) {
            String[] extensions = { "", ".jpg", ".jpeg", ".png", ".webp" };
            Image image = null;

            for (String ext : extensions) {
                try {
                    image = new Image(getClass().getResourceAsStream(
                            String.format("/compumart/compumart/%s/%s%s", product.getType(), product.getImage(), ext)));
                    if (!image.isError()) break;
                } catch (Exception ignored) {}
            }

            if (image != null && !image.isError()) {
                ImageView imageView = new ImageView(image);
                imageView.setPreserveRatio(true);
                imageView.setFitWidth(150);
                imageView.setFitHeight(150);
                card.getChildren().add(imageView);
            }
        }

        Label nameLabel = new Label(product.getName());
        nameLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px; -fx-wrap-text: true;");

        Label priceLabel = new Label("₱" + product.getPrice());
        priceLabel.setStyle("-fx-text-fill: #2ecc71; -fx-font-size: 12px;");

        Label stockLabel = new Label("Stock: " + product.getStock());
        stockLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #555;");

        if (product.getModel() != null && !product.getModel().isEmpty()) {
            Label modelLabel = new Label("Model: " + product.getModel());
            modelLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #555;");
            card.getChildren().add(modelLabel);
        }

        Button addToCartButton = new Button("Add to Cart");
        addToCartButton.setMaxWidth(Double.MAX_VALUE);
        addToCartButton.setOnMouseClicked((MouseEvent e) -> {
            if (getCurrentUser() == null) {
                showAlert(Alert.AlertType.WARNING, "Not Logged In", "Please log in to add items to the cart.");
                return;
            }
            getCurrentUser().getCart().addToCart(String.valueOf(product.getId()), 1);
            new CartRepository().saveOrUpdate(getCurrentUser().getCart());
            updateCartCount();
        });

        card.getChildren().addAll(nameLabel, priceLabel, stockLabel, addToCartButton);
        return card;
    }

    @FXML
    public void toCart() {
        if (getCurrentUser() == null) {
            showAlert(Alert.AlertType.WARNING, "Not Logged In", "Please log in to view the cart.");
            return;
        }
        app.switchTo("cart");
    }

}

