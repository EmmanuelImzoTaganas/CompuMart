package compumart.compumart.controller;

import compumart.compumart.model.Product;
import compumart.compumart.repositories.ProductRepository;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductController extends BaseController {

    private final ProductRepository productRepository = new ProductRepository();

    // Tables
    @FXML private TableView<Product> tableKeyboard;
    @FXML private TableColumn<Product, Integer> stockKeyboardColumn;
    @FXML private TableColumn<Product, String> brandKeyboardColumn;
    @FXML private TableColumn<Product, String> modelKeyboardColumn;
    @FXML private TableColumn<Product, Double> priceKeyboardColumn;
    @FXML private TableColumn<Product, String> imageKeyboardColumn;

    @FXML private TableView<Product> tableMouse;
    @FXML private TableColumn<Product, Integer> stockMouseColumn;
    @FXML private TableColumn<Product, String> brandMouseColumn;
    @FXML private TableColumn<Product, String> modelMouseColumn;
    @FXML private TableColumn<Product, Double> priceMouseColumn;
    @FXML private TableColumn<Product, String> imageMouseColumn;

    @FXML private TableView<Product> tableStorage;
    @FXML private TableColumn<Product, Integer> stockStorageColumn;
    @FXML private TableColumn<Product, String> brandStorageColumn;
    @FXML private TableColumn<Product, String> modelStorageColumn;
    @FXML private TableColumn<Product, Double> priceStorageColumn;
    @FXML private TableColumn<Product, String> imageStorageColumn;

    @FXML private TableView<Product> tableMemory;
    @FXML private TableColumn<Product, Integer> stockMemoryColumn;
    @FXML private TableColumn<Product, String> brandMemoryColumn;
    @FXML private TableColumn<Product, String> modelMemoryColumn;
    @FXML private TableColumn<Product, Double> priceMemoryColumn;
    @FXML private TableColumn<Product, String> imageMemoryColumn;

    // Input fields
    @FXML private TextField stockKeyboard, brandKeyboard, modelKeyboard, priceKeyboard, imageKeyboard;
    @FXML private TextField stockMouse, brandMouse, modelMouse, priceMouse, imageMouse;
    @FXML private TextField stockStorage, brandStorage, modelStorage, priceStorage, imageStorage;
    @FXML private TextField stockMemory, brandMemory, modelMemory, priceMemory, imageMemory;

    // Maps
    private final Map<String, TableView<Product>> tables = new HashMap<>();
    private final Map<String, ObservableList<Product>> lists = new HashMap<>();
    private final Map<String, TextField[]> inputs = new HashMap<>();

    @FXML
    public void initialize() {
        // Initialize lists
        lists.put("keyboard", FXCollections.observableArrayList());
        lists.put("mouse", FXCollections.observableArrayList());
        lists.put("storage", FXCollections.observableArrayList());
        lists.put("memory", FXCollections.observableArrayList());

        // Link tables
        tables.put("keyboard", tableKeyboard);
        tables.put("mouse", tableMouse);
        tables.put("storage", tableStorage);
        tables.put("memory", tableMemory);

        // Link input fields
        inputs.put("keyboard", new TextField[]{stockKeyboard, brandKeyboard, modelKeyboard, priceKeyboard, imageKeyboard});
        inputs.put("mouse", new TextField[]{stockMouse, brandMouse, modelMouse, priceMouse, imageMouse});
        inputs.put("storage", new TextField[]{stockStorage, brandStorage, modelStorage, priceStorage, imageStorage});
        inputs.put("memory", new TextField[]{stockMemory, brandMemory, modelMemory, priceMemory, imageMemory});

        // Bind table columns
        bindColumns();

        // Attach ObservableLists
        tableKeyboard.setItems(lists.get("keyboard"));
        tableMouse.setItems(lists.get("mouse"));
        tableStorage.setItems(lists.get("storage"));
        tableMemory.setItems(lists.get("memory"));

        // Load products
        loadAllProducts();

        // Attach selection listeners for tables
        setupSelectionListeners();
    }

    private void bindColumns() {
        // Keyboard
        stockKeyboardColumn.setCellValueFactory(c -> new SimpleIntegerProperty(c.getValue().getStock()).asObject());
        brandKeyboardColumn.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getName()));
        modelKeyboardColumn.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getModel()));
        priceKeyboardColumn.setCellValueFactory(c -> new SimpleDoubleProperty(c.getValue().getPrice()).asObject());
        imageKeyboardColumn.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getImage()));

        // Mouse
        stockMouseColumn.setCellValueFactory(c -> new SimpleIntegerProperty(c.getValue().getStock()).asObject());
        brandMouseColumn.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getName()));
        modelMouseColumn.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getModel()));
        priceMouseColumn.setCellValueFactory(c -> new SimpleDoubleProperty(c.getValue().getPrice()).asObject());
        imageMouseColumn.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getImage()));

        // Storage
        stockStorageColumn.setCellValueFactory(c -> new SimpleIntegerProperty(c.getValue().getStock()).asObject());
        brandStorageColumn.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getName()));
        modelStorageColumn.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getModel()));
        priceStorageColumn.setCellValueFactory(c -> new SimpleDoubleProperty(c.getValue().getPrice()).asObject());
        imageStorageColumn.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getImage()));

        // Memory
        stockMemoryColumn.setCellValueFactory(c -> new SimpleIntegerProperty(c.getValue().getStock()).asObject());
        brandMemoryColumn.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getName()));
        modelMemoryColumn.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getModel()));
        priceMemoryColumn.setCellValueFactory(c -> new SimpleDoubleProperty(c.getValue().getPrice()).asObject());
        imageMemoryColumn.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getImage()));
    }

    private void loadAllProducts() {
        lists.values().forEach(ObservableList::clear);

        List<Product> allProducts = productRepository.findAll();
        for (Product product : allProducts) {
            if (!product.isActive()) continue;
            String type = product.getType().toLowerCase();
            if (lists.containsKey(type)) {
                lists.get(type).add(product);
            }
        }
    }

    @FXML
    private void addProduct(javafx.event.ActionEvent event) {
        Button btn = (Button) event.getSource();
        String type = getTypeFromButton(btn);
        if (type == null) return;

        TextField[] fields = inputs.get(type);
        ObservableList<Product> list = lists.get(type);

        try {
            String brand = fields[1].getText().trim();
            String model = fields[2].getText().trim();
            String sku = brand + "-" + model;

            if (productRepository.findBySKU(sku) != null) {
                showAlert(Alert.AlertType.WARNING, "Duplicate SKU", "A product with this SKU already exists.");
                return;
            }

            Product product = new Product();
            product.setSku(sku);
            product.setName(brand);
            product.setType(type);
            product.setModel(model);
            product.setStock(Integer.parseInt(fields[0].getText().trim()));
            product.setPrice(Double.parseDouble(fields[3].getText().trim()));
            product.setImage(fields[4].getText().trim());

            productRepository.insert(product);
            list.add(product);
            clearFields(fields);
            showAlert(Alert.AlertType.INFORMATION, "Success", "Product added successfully.");
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Invalid number input for stock or price.");
        }
    }

    @FXML
    private void editProduct(javafx.event.ActionEvent event) {
        Button btn = (Button) event.getSource();
        String type = getTypeFromButton(btn);
        if (type == null) return;

        TableView<Product> table = tables.get(type);
        Product selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(Alert.AlertType.ERROR, "Error", "Select a product to edit.");
            return;
        }

        TextField[] fields = inputs.get(type);
        try {
            selected.setName(fields[1].getText().trim());
            selected.setModel(fields[2].getText().trim());
            selected.setStock(Integer.parseInt(fields[0].getText().trim()));
            selected.setPrice(Double.parseDouble(fields[3].getText().trim()));
            selected.setImage(fields[4].getText().trim());

            productRepository.update(selected.getId(), selected);
            table.refresh();
            showAlert(Alert.AlertType.INFORMATION, "Success", "Product updated successfully.");
            clearFields(fields);
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Invalid number input for stock or price.");
        }
    }

    @FXML
    private void deleteProduct(javafx.event.ActionEvent event) {
        Button btn = (Button) event.getSource();
        String type = getTypeFromButton(btn);
        if (type == null) return;

        TableView<Product> table = tables.get(type);
        Product selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(Alert.AlertType.ERROR, "Error", "Select a product to delete.");
            return;
        }

        productRepository.deactivateProductBySKU(selected.getSku());
        lists.get(type).remove(selected);
        showAlert(Alert.AlertType.INFORMATION, "Success", "Product deactivated successfully.");
    }

    private void clearFields(TextField[] fields) {
        for (TextField field : fields) field.clear();
    }

    private String getTypeFromButton(Button btn) {
        String id = btn.getId();
        if (id == null) return null;
        if (id.contains("Keyboard")) return "keyboard";
        if (id.contains("Mouse")) return "mouse";
        if (id.contains("Storage")) return "storage";
        if (id.contains("Memory")) return "memory";
        return null;
    }

    private void setupSelectionListeners() {
        for (Map.Entry<String, TableView<Product>> entry : tables.entrySet()) {
            String type = entry.getKey();
            TableView<Product> table = entry.getValue();
            TextField[] fields = inputs.get(type);

            table.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
                if (newSelection != null) {
                    fields[0].setText(String.valueOf(newSelection.getStock()));
                    fields[1].setText(newSelection.getName());
                    fields[2].setText(newSelection.getModel());
                    fields[3].setText(String.valueOf(newSelection.getPrice()));
                    fields[4].setText(newSelection.getImage());
                }
            });
        }
    }

    @FXML
    public void toDashboard() {
        app.switchTo("admin");
    }

    @FXML
    public void onLogOut() {
        app.switchTo("login");
    }
}
