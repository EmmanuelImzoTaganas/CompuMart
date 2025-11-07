package compumart.compumart.controller;

import compumart.compumart.model.Product;
import compumart.compumart.repositories.ProductRepository;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;

import java.util.List;
import java.util.stream.Collectors;

public class MainController extends BaseController {

    private final ProductRepository productRepository = new ProductRepository();

    @FXML private TilePane catalogTilePane;
    @FXML private ChoiceBox<String> categoryChoiceBox;
    @FXML private TextField searchBar;
    @FXML private Button cartButton;
    @FXML private Label cartItemCount;

    private ObservableList<Product> allProducts = FXCollections.observableArrayList();
    private ObservableList<Product> displayedProducts = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        loadProducts();
        setupCategoryFilter();
        setupSearchBar();
        renderCatalog();
    }

    private void loadProducts() {
        allProducts.setAll(productRepository.findAll()
                .stream()
                .filter(Product::isActive)
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
                .filter(p -> (selectedCategory.equals("All") || p.getType().equalsIgnoreCase(selectedCategory)))
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
        VBox card = new VBox(5);
        card.setStyle("-fx-background-color: white; -fx-padding: 10; -fx-border-radius: 5; -fx-background-radius: 5; " +
                "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 5, 0, 0, 0);");

        // Image from resources
        try {
            if (product.getImageFileName() != null && !product.getImageFileName().isEmpty()) {
                String path = String.format("/compumart/compumart/%s/%s", product.getCategory(), product.getImageFileName());
                ImageView imageView = new ImageView(new Image(getClass().getResourceAsStream(path), 150, 150, true, true));
                card.getChildren().add(imageView);
            }
        } catch (Exception e) {
            System.out.println("Failed to load image for product: " + product.getName());
        }

        Label nameLabel = new Label(product.getName());
        nameLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

        Label priceLabel = new Label("₱" + product.getPrice());
        priceLabel.setStyle("-fx-text-fill: #2ecc71; -fx-font-size: 12px;");

        Button addToCartButton = new Button("Add to Cart");
        addToCartButton.setOnMouseClicked((MouseEvent e) -> {
            int count = Integer.parseInt(cartItemCount.getText());
            cartItemCount.setText(String.valueOf(count + 1));
        });

        card.getChildren().addAll(nameLabel, priceLabel, addToCartButton);
        return card;
    }



}
