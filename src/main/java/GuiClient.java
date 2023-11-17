import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.DecimalFormat;
import java.util.*;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.stream.Collectors;


import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Side;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.Duration;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

//import javafx.scene.web.WebEngine;
//import javafx.scene.web.WebView;

import javax.print.attribute.standard.Media;
import javax.swing.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
//import javafx.scene.media.MediaPlayer;
//import javafx.scene.media.MediaView;
import javafx.stage.Stage;
//import java.io.File;

import static java.awt.Color.BLACK;
import static java.awt.Color.blue;


public class GuiClient extends Application{
    Client clientConnection;
    private Consumer<Serializable> customerMessage, cookMessage, driverMessage;
    Database database = new Database();



    //---------------------------CUSTOMER APP STARTS HERE-------------------------------------

    // This array will hold all images to iterate through for slideshow
    private final Image[] images = {
            new Image("slide1.jpg"),
            new Image("slide2.jpg"),
            new Image("slide3.jpg"),
            new Image("slide4.jpg"),
            new Image("slide5.jpg"),
            new Image("slide6.jpg"),
            new Image("slide7.jpg")
    };

    private int currentIndex = 0;

    private void showNextImage(ImageView imageView) {
        imageView.setImage(images[currentIndex]);
        currentIndex = (currentIndex + 1) % images.length;
    }

    ImageView getImage(String imageName, int width, int height){

        Image image = new Image(imageName);
        ImageView imageView = new ImageView(image);
        imageView.setPreserveRatio(true);
        imageView.setFitWidth(width); // Set the desired width
        imageView.setFitHeight(height); // Set the desired height

        return imageView;
    }

    Button customerHome = new Button("Home");
    Button menu = new Button("MENU");
    Button contact = new Button("CONTACT");
    Button reserve = new Button("RESERVE");

    Button ravioliButton = new Button("Add to Cart");
    Button penneButton = new Button("Add to Cart");
    Button lasagneButton = new Button("Add to Cart");
    Button spaghettiButton =  new Button("Add to Cart");
    Button macaronesButton = new Button("Add to Cart");
    Button margaritaButton = new Button("Add to Cart");
    Button veggieButton = new Button("Add to Cart");
    Button canneloniButton = new Button("Add to Cart");
    Button tunaButton = new Button("Add to Cart");
    Button cheeseButton = new Button("Add to Cart");
    Button pepperoniButton = new Button("Add to Cart");
    Button salamiButton = new Button("Add to Cart");
    CalculatingTotal itemsInCart = new CalculatingTotal();
    Button viewCart = new Button("View Cart");
    Button checkoutButton = new Button("Proceed to checkout");
    Button emptyCart = new Button("Empty Cart");
    ListView<String> listView = new ListView<>();
    TextField customerNameTextField = new TextField();
    BillingInformation billing = new BillingInformation();

    ObservableList<String> items = FXCollections.observableArrayList();
//    ListView<String> updateList = new ListView<>(items);
    List<String> newMessages = new ArrayList<>();
    private ObservableList<String> updatesCustomer = FXCollections.observableArrayList();
    private ListView<String> updateList = new ListView<>(updatesCustomer);

    Set<String> displayedMessages = new HashSet<>();
    String driverIdForCustomer;
    Button tipButton = new Button("Tip the Driver");
    Button submitButton = new Button("Submit");

    List<List<String>> orderUpdatesForCustomer = new ArrayList<>();
    Boolean tipPage = false;

    VBox getLogo(){
        VBox logo = new VBox();
        Image image = new Image("logo.png");
        ImageView imageView1 = new ImageView(image);
        imageView1.setPreserveRatio(true);
        imageView1.setFitWidth(200); // Set the desired width
        imageView1.setFitHeight(150); // Set the desired height
        logo.getChildren().add(imageView1);

        return logo;
    }

    HBox getMenuButtons(){

        VBox logo = getLogo();

        HBox menuOptions = new HBox(10);
        menuOptions.getChildren().addAll(customerHome, menu, logo, contact, reserve);

        menuOptions.setAlignment(Pos.CENTER);
        menuOptions.getStyleClass().add("custom-button");

        customerHome.getStyleClass().add("menu-button");
        menu.getStyleClass().add("menu-button");
        contact.getStyleClass().add("menu-button");
        reserve.getStyleClass().add("menu-button");

        return menuOptions;
    }


    EventHandler<ActionEvent> tipOut(Stage primaryStage) {
        EventHandler<ActionEvent> tip = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                HBox menuBar = getMenuButtons();
                Label deliveryLabel = new Label("Your order was delivered");
                Label tipLabel = new Label("Add a Tip");

                tipLabel.getStyleClass().add("custom-label");
                deliveryLabel.getStyleClass().add("custom-label");

                // Create buttons
                Button tip5Button = new Button("$5");
                Button tip10Button = new Button("$10");
                Button tip15Button = new Button("$15");
                Button tip20Button = new Button("$20");
                Button noTipButton = new Button("No Tip");

                // Create VBox
                VBox vbox = new VBox(10); // 10 is the spacing between nodes
                vbox.setPadding(new Insets(20)); // Adds padding around the VBox
                vbox.getChildren().addAll(menuBar, deliveryLabel, tipLabel, tip5Button, tip10Button, tip15Button, tip20Button, noTipButton, submitButton);
                vbox.setStyle("-fx-background-color: #f6a304");
                vbox.setAlignment(Pos.CENTER);

                // Set action for the submit button (you can add your own action)

                submitButton.setDisable(true);

                tip5Button.setOnAction(event -> {
                    tip5Button.setDisable(true);
                    tip10Button.setDisable(true);
                    tip15Button.setDisable(true);
                    tip20Button.setDisable(true);
                    noTipButton.setDisable(true);
                    submitButton.setDisable(false);
                    clientConnection.send("Tip " + driverIdForCustomer + " " + 5.0);
                });
                tip10Button.setOnAction(event -> {
                    tip5Button.setDisable(true);
                    tip10Button.setDisable(true);
                    tip15Button.setDisable(true);
                    tip20Button.setDisable(true);
                    noTipButton.setDisable(true);
                    submitButton.setDisable(false);
                    clientConnection.send("Tip " + driverIdForCustomer + " " + 10.0);
                });
                tip15Button.setOnAction(event -> {
                    tip5Button.setDisable(true);
                    tip10Button.setDisable(true);
                    tip15Button.setDisable(true);
                    tip20Button.setDisable(true);
                    noTipButton.setDisable(true);
                    submitButton.setDisable(false);
                    clientConnection.send("Tip " + driverIdForCustomer + " " + 15.0);
                });
                tip20Button.setOnAction(event -> {
                    tip5Button.setDisable(true);
                    tip10Button.setDisable(true);
                    tip15Button.setDisable(true);
                    tip20Button.setDisable(true);
                    noTipButton.setDisable(true);
                    submitButton.setDisable(false);
                    clientConnection.send("Tip " + driverIdForCustomer + " " + 20.0);
                });
                noTipButton.setOnAction(event -> {
                    tip5Button.setDisable(true);
                    tip10Button.setDisable(true);
                    tip15Button.setDisable(true);
                    tip20Button.setDisable(true);
                    noTipButton.setDisable(true);
                    submitButton.setDisable(false);
                });

                submitButton.setOnAction(event -> {
                    System.out.println("submit check");
                    submitButton.setOnAction(startOrderApp(primaryStage));
                });

                tip5Button.getStyleClass().add("tip-amount");
                tip10Button.getStyleClass().add("tip-amount");
                tip15Button.getStyleClass().add("tip-amount");
                tip20Button.getStyleClass().add("tip-amount");
                noTipButton.getStyleClass().add("tip-amount");
                submitButton.getStyleClass().add("submit-button");

                // Create scene and set stage
                Scene scene = new Scene(vbox, 800, 800); // Set width and height accordingly
                scene.getStylesheets().add("styles.css");
                primaryStage.setTitle("Delivery Scene");
                primaryStage.setScene(scene);
                primaryStage.show();

            }
        };
        return tip;
    }


    EventHandler<ActionEvent> orderUpdates(Stage primaryStage){
        EventHandler<ActionEvent> updates = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {


                ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

                // Schedule a task to check for updates every second
                executorService.scheduleAtFixedRate(() -> {

                    // Simulate receiving new messages
                    List<String> newMessages = getNewMessages();

                    if (!newMessages.isEmpty()) {
                        // Filter out messages that have already been displayed
                        List<String> uniqueNewMessages = newMessages.stream()
                                .filter(message -> !displayedMessages.contains(message))
                                .collect(Collectors.toList()); // Collect filtered messages into a list

                        if (!uniqueNewMessages.isEmpty()) {
                            // Update the UI on the JavaFX application thread
                            Platform.runLater(() -> {
                                updatesCustomer.addAll(uniqueNewMessages);
                                displayedMessages.addAll(uniqueNewMessages);
                            });
                        }
                    }
                }, 0, 1, TimeUnit.SECONDS);


                HBox menuOptions =  getMenuButtons();
                VBox updateBox = new VBox(30, menuOptions, updateList, tipButton);
                updateBox.setStyle("-fx-background-color: #f6a304");

                tipButton.getStyleClass().add("tip-driver-button");
                tipButton.setOnAction(tipOut(primaryStage));

                updateList.setMaxWidth(350);
                updateList.getStyleClass().add("customer-listview");
                updateBox.setAlignment(Pos.CENTER);


                Scene updateScene = new Scene(updateBox, 800, 800);
                updateScene.getStylesheets().add("styles.css");

                primaryStage.setScene(updateScene);

            }
        };
        return updates;
    }

    EventHandler<ActionEvent> viewStatus(Stage primaryStage) {
        EventHandler<ActionEvent> checkStatus = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                String customerName = customerNameTextField.getText();
                ArrayList<String> foodOrderedList = new ArrayList<String>();
                foodOrderedList.add(0, customerName);
                foodOrderedList.addAll(itemsInCart.getFoodNames());

                // We send the list of food to the cook application
                // First Index consists of the customer name for cook application to know who the order belongs to
                clientConnection.send(foodOrderedList);

                HBox menuBar = getMenuButtons();
                Label orderPlaced = new Label("YOUR ORDER HAS BEEN PLACED, CLICK BELOW TO TRACK IT");
                Button statusOrder = new Button("View Order Status");

                VBox organize = new VBox(30, orderPlaced, statusOrder);
                organize.setAlignment(Pos.CENTER);

                VBox displayOrders = new VBox(50, menuBar, organize);
                displayOrders.setAlignment(Pos.CENTER);
                displayOrders.setStyle("-fx-background-color: #f6a304");

                Scene displayOrdersPage = new Scene(displayOrders, 800, 800);
                primaryStage.setScene(displayOrdersPage);
                displayOrdersPage.getStylesheets().add("styles.css");

                statusOrder.setOnAction(orderUpdates(primaryStage));
                statusOrder.getStyleClass().add("checkout-button");
            }
        };
        return checkStatus;
    }

    EventHandler<ActionEvent> placeOrder(Stage primaryStage) {
        EventHandler<ActionEvent> orderPlaced = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                HBox setHeader = getMenuButtons();

                TextField billingNameTextField = new TextField();
                billingNameTextField.setPromptText("Enter your billing name (First Last)");
                Label billingNameValidationLabel = new Label("Invalid billing name.");
                billingNameTextField.getStyleClass().add("billing-text");
                billingNameValidationLabel.getStyleClass().add("billing-label");
                billingNameTextField.setMaxWidth(250);

                TextField creditCardTextField = new TextField();
                creditCardTextField.setPromptText("Enter your 16-digit credit card number");
                Label validationLabel = new Label("Invalid credit card number."); // Initialize as invalid
                creditCardTextField.getStyleClass().add("billing-text");
                validationLabel.getStyleClass().add("billing-label");
                creditCardTextField.setMaxWidth(250);

                TextField cvcTextField = new TextField();
                cvcTextField.setPromptText("Enter your 3-digit CVC");
                Label cvcValidationLabel = new Label("Invalid CVC.");
                cvcTextField.getStyleClass().add("billing-text");
                cvcValidationLabel.getStyleClass().add("billing-label");
                cvcTextField.setMaxWidth(250);

                TextField streetAddressTextField = new TextField();
                streetAddressTextField.setPromptText("Enter your street address");
                Label streetAddressValidationLabel = new Label("Invalid street address.");
                streetAddressTextField.getStyleClass().add("billing-text");
                streetAddressValidationLabel.getStyleClass().add("billing-label");
                streetAddressTextField.setMaxWidth(250);

                TextField cityTextField = new TextField();
                cityTextField.setPromptText("Enter your city");
                Label cityValidationLabel = new Label("Invalid city.");
                cityTextField.getStyleClass().add("billing-text");
                cityValidationLabel.getStyleClass().add("billing-label");
                cityTextField.setMaxWidth(250);

                TextField stateTextField = new TextField();
                stateTextField.setPromptText("Enter your state");
                Label stateValidationLabel = new Label("Invalid state.");
                stateTextField.getStyleClass().add("billing-text");
                stateValidationLabel.getStyleClass().add("billing-label");
                stateTextField.setMaxWidth(250);

                TextField zipCodeTextField = new TextField();
                zipCodeTextField.setPromptText("Enter your ZIP code");
                Label zipCodeValidationLabel = new Label("Invalid ZIP code.");
                zipCodeTextField.getStyleClass().add("billing-text");
                zipCodeValidationLabel.getStyleClass().add("billing-label");
                zipCodeTextField.setMaxWidth(250);

                VBox billingInfo = new VBox(10);
                billingInfo.getChildren().addAll(
                        billingNameTextField, billingNameValidationLabel,
                        creditCardTextField, validationLabel,
                        cvcTextField, cvcValidationLabel,
                        streetAddressTextField, streetAddressValidationLabel,
                        cityTextField, cityValidationLabel,
                        stateTextField, stateValidationLabel,
                        zipCodeTextField, zipCodeValidationLabel
                );
                billingInfo.getStyleClass().add("billing-box");

                Button orderFood = new Button("Place Order");
                orderFood.getStyleClass().add("checkout-button");

                VBox holdAllInfo = new VBox(30, setHeader, billingInfo, orderFood);
                holdAllInfo.getStyleClass().add("billing-box");


                Scene order = new Scene(holdAllInfo, 800, 800);
                primaryStage.setScene(order);

                // Add event handler to validate the Billing Name
                billingNameTextField.textProperty().addListener((observable, oldValue, newValue) -> {
                    billing.validateBillingName(newValue, billingNameValidationLabel);
                });
                // Add event handler to validate the credit card number while typing
                creditCardTextField.textProperty().addListener((observable, oldValue, newValue) -> {
                    billing.validateCreditCard(newValue, validationLabel);
                });
                // Add event handler to validate the cvc card number while typing
                cvcTextField.textProperty().addListener((observable, oldValue, newValue) -> {
                    billing.validateCVC(newValue, cvcValidationLabel);
                });
                // Add event handler to validate the street address while typing
                streetAddressTextField.textProperty().addListener((observable, oldValue, newValue) -> {
                    billing.validateStreetAddress(newValue, streetAddressValidationLabel);
                });
                // Add event handler to validate the city while typing
                cityTextField.textProperty().addListener((observable, oldValue, newValue) -> {
                    billing.validateCity(newValue, cityValidationLabel);
                });
                // Add event handler to validate the state while typing
                stateTextField.textProperty().addListener((observable, oldValue, newValue) -> {
                    billing.validateState(newValue, stateValidationLabel);
                });
                // Add event handler to validate the zipcode while typing
                zipCodeTextField.textProperty().addListener((observable, oldValue, newValue) -> {
                    billing.validateZipCode(newValue, zipCodeValidationLabel);
                });

                order.getStylesheets().add("styles.css");

                orderFood.setOnAction(viewStatus(primaryStage));

                primaryStage.show();
            }
        };
        return orderPlaced;
    }

    // When user clicks "Empty Cart", this event handler is called
    // It clears all arrays, and variables up in caclulating total class
    // It also resets the listView items, and adds that no items are in cart
    // It also disables the button for proceesing to checkout
    EventHandler<ActionEvent> clearList(Stage primaryStage) {
        EventHandler<ActionEvent> listClear = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                itemsInCart.clearList();
                listView.getItems().clear();
                listView.getItems().add("\n");
                listView.getItems().add("NO ITEMS HAVE BEEN ADDED TO THE CART");
                checkoutButton.setDisable(true); // cannot checkout, no items in cart
                displayCart(primaryStage);
            }
        };
        return listClear;
    }

    // This Event Handler is called when user clicks on view Cart
    // View Cart will display a list of items in cart along witg tax, delivery fee, and total
    // If cart is empty it will display
    EventHandler<ActionEvent> displayCart(Stage primaryStage){
        EventHandler<ActionEvent> cart = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                HBox menuButtons = getMenuButtons();
                listView.getItems().clear();

                if(itemsInCart.getFoodNames().isEmpty()){
                    listView.getItems().add("\n");
                    listView.getItems().add("NO ITEMS HAVE BEEN ADDED TO THE CART");
                }
                else {

                    for (int i = 0; i < itemsInCart.getFoodNames().size(); i++) {
                        listView.getItems().add(itemsInCart.getFoodNames().get(i) + ": $" + itemsInCart.getFoodPrices().get(i));
                    }

                    listView.getItems().add("\n");
                    listView.getItems().add("Tax: $" + itemsInCart.getTax());
                    listView.getItems().add("Delivery Fee: $" + itemsInCart.getDeliveryFee());
                    listView.getItems().add("Total (" + itemsInCart.getFoodNames().size() + " items): $" + itemsInCart.getTotal());
                }

                listView.getStyleClass().add("food-list");
                VBox listAndClearButton = new VBox(listView, emptyCart);
                listAndClearButton.setMaxWidth(300);
                listAndClearButton.setAlignment(Pos.CENTER);

                VBox itemsList = new VBox(menuButtons, listAndClearButton, checkoutButton);
                itemsList.setAlignment(Pos.CENTER);
                itemsList.setStyle("-fx-background-color: #f6a304");
                checkoutButton.getStyleClass().add("checkout-button");

                // Create a scene and set it on the stage
                Scene cartPage = new Scene(itemsList, 800, 800);
                cartPage.getStylesheets().add("styles.css");
                primaryStage.setScene(cartPage);

                checkoutButton.setOnAction(placeOrder(primaryStage));
                emptyCart.setOnAction(clearList(primaryStage));
            }
        };
        return cart;
    }

    EventHandler<ActionEvent> addToCart(Stage primaryStage){
        EventHandler<ActionEvent> addItems = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                checkoutButton.setDisable(false); // enable the checkout button since there is atleast one item
                Button clickedButton = (Button) actionEvent.getSource();

                // We call the method calculate in CalculatingTotal which takes the price in string which is buttonID
                itemsInCart.calculate(clickedButton.getId());
            }
        };
        return addItems;
    }

    EventHandler<ActionEvent> displayMenu(Stage primaryStage){
        return new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                clientConnection.send("hello my friend");

                // all menu buttons and logos
                HBox menuButtons = getMenuButtons();


                // This VBox holds the image of menu
                VBox menuHolder = new VBox();
                menuHolder.getChildren().addAll(getImage("menu.jpg", 600, 800));
                menuHolder.setAlignment(Pos.CENTER);
                menuHolder.getStyleClass().add("menu-background");


                // Pastas below:
                // For each we creata a Vertical box that will have a label on top, picture of the item, and add to cart button
                VBox lasagne = new VBox();
                VBox ravioli = new VBox();
                VBox spaghetti = new VBox();
                VBox macrones = new VBox();
                VBox penne = new VBox();
                VBox canneloni = new VBox();

                lasagne.getChildren().add(new Label("Lasagne"));
                ravioli.getChildren().add(new Label("Ravioli"));
                spaghetti.getChildren().add(new Label("Spaghetti Bolonese"));
                macrones.getChildren().add(new Label("Macarones"));
                penne.getChildren().add(new Label("Penne"));
                canneloni.getChildren().add(new Label("Canneloni"));

                lasagne.getStyleClass().add("food-label");
                ravioli.getStyleClass().add("food-label");
                spaghetti.getStyleClass().add("food-label");
                macrones.getStyleClass().add("food-label");
                penne.getStyleClass().add("food-label");
                canneloni.getStyleClass().add("food-label");

                lasagne.getChildren().addAll(getImage("lasagne.jpeg", 200, 200), lasagneButton);
                ravioli.getChildren().addAll(getImage("ravioli.jpg", 200, 200), ravioliButton);
                spaghetti.getChildren().addAll(getImage("spaghetti.jpg", 200, 200), spaghettiButton);
                macrones.getChildren().addAll(getImage("macarones.jpg", 200, 200), macaronesButton);
                penne.getChildren().addAll(getImage("penne.jpg", 200, 200), penneButton);
                canneloni.getChildren().addAll(getImage("canneloni.jpg", 200, 200), canneloniButton);


                // Pizzas below:
                // For each we creata a Vertical box that will have a label on top, picture of the item, and add to cart button
                VBox margarita = new VBox();
                VBox cheese = new VBox();
                VBox pepperoni = new VBox();
                VBox tuna = new VBox();
                VBox veggie = new VBox();
                VBox salami = new VBox();

                margarita.getChildren().add(new Label("Margarita Pizza"));
                cheese.getChildren().add(new Label("4Cheese Pizza"));
                pepperoni.getChildren().add(new Label("Pepperoni Pizza"));
                tuna.getChildren().add(new Label("Tuna & Onion Pizza"));
                veggie.getChildren().add(new Label("Veggie Pizza"));
                salami.getChildren().add(new Label("Salami Pizza"));

                margarita.getStyleClass().add("food-label");
                cheese.getStyleClass().add("food-label");
                pepperoni.getStyleClass().add("food-label");
                tuna.getStyleClass().add("food-label");
                veggie.getStyleClass().add("food-label");
                salami.getStyleClass().add("food-label");

                margarita.getChildren().addAll(getImage("margarita.jpg", 200, 200), margaritaButton);
                cheese.getChildren().addAll(getImage("cheese.jpg", 200, 200), cheeseButton);
                pepperoni.getChildren().addAll(getImage("pepperoni.jpg", 200, 200), pepperoniButton);
                tuna.getChildren().addAll(getImage("tuna.jpg", 200, 200), tunaButton);
                veggie.getChildren().addAll(getImage("veggie.jpg", 200, 200), veggieButton);
                salami.getChildren().addAll(getImage("salami.jpg", 200, 200), salamiButton);


                // Combining 2 items by 2 items below to display it horizontally
                // So we take two items and put them in a separate horizontal box just for displaying purposes
                HBox lasagne_Margarita = new HBox(150, lasagne, margarita);
                lasagne_Margarita.getStyleClass().add("two-pics-hbox");
                HBox ravioli_cheese = new HBox(150, ravioli, cheese);
                ravioli_cheese.getStyleClass().add("two-pics-hbox");
                HBox spaghetti_pepperoni = new HBox(150, spaghetti, pepperoni);
                spaghetti_pepperoni.getStyleClass().add("two-pics-hbox");
                HBox macrones_tuna = new HBox(150, macrones, tuna);
                macrones_tuna.getStyleClass().add("two-pics-hbox");
                HBox penne_veggie = new HBox(150, penne, veggie);
                penne_veggie.getStyleClass().add("two-pics-hbox");
                HBox canneloni_salami = new HBox(150, canneloni, salami);
                canneloni_salami.getStyleClass().add("two-pics-hbox");


                // Now all Horizontal boxed of which each holds 2 menu items gets added to a Vertical Box
                VBox menuPicHolder = new VBox(30, lasagne_Margarita, ravioli_cheese, spaghetti_pepperoni, macrones_tuna, penne_veggie, canneloni_salami);
                menuPicHolder.setTranslateY(15);

                // The whole page consists of menubuttons on top, menu picture, and menu item pictures
                VBox page2 = new VBox(menuButtons, menuHolder, menuPicHolder, viewCart);

                viewCart.getStyleClass().add("cart-button");
                page2.setAlignment(Pos.CENTER);
                page2.setStyle("-fx-background-color: #f6a304");

                ScrollPane scrollPane = new ScrollPane(page2);
                scrollPane.setFitToWidth(true); // Allow the content to resize with the window width


                Scene menuScene = new Scene(scrollPane, 800, 800);
                menuScene.getStylesheets().add("styles.css");
                primaryStage.setScene(menuScene);


                // $9 menu items: Setting their ID's for price calculation and set on action for when they are clicked
                ravioliButton.setId("Ravioli-9");
                lasagneButton.setId("Lasagne-9");
                spaghettiButton.setId("Spaghetti Bolonese-9");
                macaronesButton.setId("Macarones-9");
                veggieButton.setId("Veggie Pizza-9");
                margaritaButton.setId("Margarita Pizza-9");

                ravioliButton.setOnAction(addToCart(primaryStage));
                lasagneButton.setOnAction(addToCart(primaryStage));
                spaghettiButton.setOnAction(addToCart(primaryStage));
                macaronesButton.setOnAction(addToCart(primaryStage));
                veggieButton.setOnAction(addToCart(primaryStage));
                margaritaButton.setOnAction(addToCart(primaryStage));

                // .getStyleClass().add("name-button");

                ravioliButton.getStyleClass().add("addcart-button");
                lasagneButton.getStyleClass().add("addcart-button");
                spaghettiButton.getStyleClass().add("addcart-button");
                macaronesButton.getStyleClass().add("addcart-button");
                veggieButton.getStyleClass().add("addcart-button");
                margaritaButton.getStyleClass().add("addcart-button");


                // $8 menu items: Setting their ID's for price calculation and set on action for when they are clicked
                penneButton.setId("Penne-8");
                canneloniButton.setId("Canneloni-8");
                tunaButton.setId("Tuna and Onion Pizza-8");

                penneButton.setOnAction(addToCart(primaryStage));
                canneloniButton.setOnAction(addToCart(primaryStage));
                tunaButton.setOnAction(addToCart(primaryStage));

                penneButton.getStyleClass().add("addcart-button");
                canneloniButton.getStyleClass().add("addcart-button");
                tunaButton.getStyleClass().add("addcart-button");


                // $10 menu items: Setting their ID's for price calculation and set on action for when they are clicked
                cheeseButton.setId("Cheese Pizza-10");
                pepperoniButton.setId("Pepperoni Pizza-10");
                salamiButton.setId("Salami Pizza-10");

                cheeseButton.setOnAction(addToCart(primaryStage));
                pepperoniButton.setOnAction(addToCart(primaryStage));
                salamiButton.setOnAction(addToCart(primaryStage));

                cheeseButton.getStyleClass().add("addcart-button");
                pepperoniButton.getStyleClass().add("addcart-button");
                salamiButton.getStyleClass().add("addcart-button");

                viewCart.setOnAction(displayCart(primaryStage));

            }
        };
    }

    EventHandler<ActionEvent> startOrderApp(Stage primaryStage) {
        EventHandler<ActionEvent> startCustomerUp = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {


                clientConnection = new Client(customerMessage);
                clientConnection.start();

                clientConnection.setClientType("customerName:");
                clientConnection.setCustomerName(customerNameTextField.getText());

                // We create this HBox to put all 4 menu buttons + the logo in between them
                HBox menuOptions = getMenuButtons();

                // We create this VBox to put all of our pictures inside for a slideshow
                VBox slideShow = new VBox();

                // The code below starts the slideshow pictures in a loop
                ImageView imageView = new ImageView();
                imageView.setPreserveRatio(true);
                imageView.setFitHeight(Double.MAX_VALUE);
                imageView.setFitWidth(800); // Set the desired image width
                imageView.setFitHeight(600); // Set the desired image height
                Timeline slideshow = new Timeline(
                        new KeyFrame(Duration.seconds(1.7), e -> showNextImage(imageView))
                );
                slideshow.setCycleCount(Timeline.INDEFINITE);
                slideshow.play();
                slideShow.getChildren().add(imageView);
                slideShow.setAlignment(Pos.CENTER);


                // This VBox is the whole page when game is loaded. It contains all menu buttons, logo, & slideshow
                VBox page1 = new VBox(menuOptions, slideShow);
                page1.setTranslateY(15);
//                page1.setStyle("-fx-background-color: rgba(0,0,0,0.29);");
                page1.setStyle("-fx-background-color: #f6a304");


                // Creating the scene and connecting the styles.css for the looks of the scene
                Scene scene = new Scene(page1, 800, 800);
                scene.getStylesheets().add("styles.css");

                // Set the scene for the stage
                primaryStage.setTitle("Order Food With Us");
                primaryStage.setScene(scene);


                menu.setOnAction(displayMenu(primaryStage));
                customerHome.setOnAction(startOrderApp(primaryStage));
            }
        };
        return startCustomerUp;
    }



    EventHandler<ActionEvent> customerOrderName(Stage primaryStage) {
        EventHandler<ActionEvent> name = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {


                StackPane stackPane = new StackPane();
                stackPane.setStyle("-fx-background-color: BLACK");

                // Load the image using the Image class
                Image image = new Image("login.jpg");

                // Create an ImageView to display the image
                ImageView imageView = new ImageView(image);
                imageView.setPreserveRatio(true);
                imageView.setFitHeight(800);
                imageView.setFitWidth(800);

                // Create text field and button
                customerNameTextField = new TextField();
                customerNameTextField.setPromptText("Enter your Name to Begin Placing order");
                customerNameTextField.getStyleClass().add("name-enter");
                customerNameTextField.setMaxWidth(300);

                customerNameTextField.setStyle(
                        "-fx-font-size: 16px;" + /* Font size */
                                "-fx-font-weight: bold;" + /* Font weight */
                                "-fx-prompt-text-fill: rgba(165,42,42,0.42);" + /* Placeholder text color */
                                "-fx-background-color: white;" + /* Background color */
                                "-fx-border-color: rgba(250,250,24,0.57);" + /* Border color */
                                "-fx-border-radius: 25px;" + /* Rounded corners */
                                "-fx-padding: 10px;" /* Padding */
                );

                Button startButton = new Button("Start");

                startButton.setStyle(
                        "-fx-background-color: #ffea00;" + /* Background color */
                                "-fx-text-fill: brown;" + /* Text color */
                                "-fx-font-size: 16px;" + /* Font size */
                                "-fx-font-weight: bold;" + /* Font weight */
                                "-fx-padding: 10px 20px;" + /* Padding (top, right, bottom, left) */
                                "-fx-background-radius: 5px;" + /* Rounded corners */
                                "-fx-cursor: hand;" /* Change cursor on hover for better user feedback */
                );

//                startButton.getStyleClass().add("name-button");

                // Create a VBox to hold the text field and button
                VBox starterPage = new VBox(20, customerNameTextField, startButton);
                starterPage.setAlignment(Pos.CENTER);

                // Add the ImageView and VBox to the StackPane
                stackPane.getChildren().addAll(imageView, starterPage);

                // Create a Scene and set it as the root of the stage
                Scene scene = new Scene(stackPane, 800, 800);
                primaryStage.setScene(scene);

                // Load the CSS styles
                scene.getStylesheets().add("styles.css");

                startButton.setOnAction(startOrderApp(primaryStage));

            }

        };
        return name;
    }

    //---------------------------CUSTOMER APP ENDS HERE-------------------------------------

    // ---------------------------------------------------------------------------------------------------------------


    //---------------------------COOK APP STARTS HERE-------------------------------------

    Button cookHome = new Button("Home");
    Button viewOrders = new Button("View Orders");
    Button reservations = new Button("Reservation Requests");
    Button cookMenu = new Button("Menu");
    List<List<String>> orders = new ArrayList<>();
    ListView<Order> viewList = new ListView<>();
    //Order orderClient;

    int idNumber = 0;
    //String firstWord;
    //String[] firstWord = new String[10];

    private static final String[] imagePaths = {
            "cook1.jpg",
            "cook2.jpg",
            "cook3.jpg",
            "cook5.jpg",
            "cook6.jpg",
            "cook7.jpg"
    };

    private int currentImageIndex = 0;
    private VBox cookBox;


    private void displayNextImage() {
        // Load the image
        Image image = new Image(getClass().getResourceAsStream(imagePaths[currentImageIndex]));
        ImageView imageView = new ImageView(image);

        imageView.setPreserveRatio(true);
        imageView.setFitWidth(800);
        imageView.setFitHeight(800);

        // Clear the VBox and add the new image
        cookBox.getChildren().clear();
        cookBox.getChildren().add(imageView);

        // Increment the image index or loop back to the first image
        currentImageIndex = (currentImageIndex + 1) % imagePaths.length;
    }


    HBox cookAppMenu(){

        VBox logo = getLogo();

        HBox menuOptions = new HBox(10);
        menuOptions.getChildren().addAll(cookHome, viewOrders, logo, reservations, cookMenu);


        cookHome.getStyleClass().add("menu-button");
        viewOrders.getStyleClass().add("menu-button");
        reservations.getStyleClass().add("menu-button");
        cookMenu.getStyleClass().add("menu-button");


        menuOptions.setAlignment(Pos.CENTER);
        menuOptions.getStyleClass().add("custom-button");

        return menuOptions;
    }

    private String extractFirstWord(String text) {
        if (text != null && text.matches("\\[\\w+.*")) {
            // Use regex to extract the first word
            String firstWord = text.replaceAll("^\\[(\\w+).*$", "$1");
            return firstWord;
        }
        return "";
    }


    void updateItemsInList() {
        ObservableList<Order> lastOrders = FXCollections.observableArrayList();

        // Populate the ListView with the last elements from the 2D ArrayList
        for (List<String> orderList : orders) {
            if (!orderList.isEmpty()) {
                String lastItem = orderList.get(orderList.size() - 1);

                // We store the name of the person who ordered so we can send them a message
                String firstWord = extractFirstWord(lastItem);

                // Remove brackets, first word, and first comma
                String orderId = lastItem.replaceFirst("^\\[\\w+,\\s*", "").replace("]", "");

                lastOrders.add(new Order(orderId, firstWord));
            }
        }

        viewList.setCellFactory(param -> new ListCell<Order>() {

            boolean setDisabled = false;

            private final Button button2 = new Button("Complete Order");

            {

                button2.setDisable(true); // leave complete order button disabled when created
                button2.setOnAction(event -> {
                    // Handle button click here
                    Order order = getItem();
                    clientConnection.send("Completed " + order.getFirstWord() + " " + order.getOrderId());

                    //System.out.println("Order ID: " + order.getOrderId());
                    //clientConnection.send("Placed " + order.getFirstWord());
//                    button2.setDisable(true);

                    setDisabled = true;

                    int index = getIndex();

                    if (index >= 0 && index < orders.size()) {
                        orders.remove(index); // Remove the item from driverOrders
                        lastOrders.remove(index); // Remove the item from drivermessages
                    }


//                    String selectedItem = order.getOrderId();
//                    System.out.println("Processing: " + selectedItem);
                });
            }

            private final Button button = new Button("Start Order");

            {

                button.getStyleClass().add("start-orderButton");

//                button.setDisable(false);

                button.setOnAction(event -> {

//                    button.setDisable(true); // start order becomes disabled once order is started


                    // Handle button click here
                    Order order = getItem();
                    System.out.println("Order ID: " + order.getOrderId());
                    clientConnection.send("Placed " + order.getFirstWord());
                    button2.setDisable(false); // We enable the complete order button once order is started

                    String selectedItem = order.getOrderId();
                    System.out.println("Processing: " + selectedItem);
                });
            }


            @Override
            protected void updateItem(Order order, boolean empty) {
                super.updateItem(order, empty);
                if (empty || order == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    HBox buttonBox = new HBox();
                    Label label = new Label(order.getOrderId());

                    buttonBox.setSpacing(25);
                    buttonBox.setAlignment(Pos.CENTER);
                    buttonBox.getChildren().addAll(button, label, button2);
                    setGraphic(buttonBox);
                }
            }
        });

        viewList.setItems(lastOrders);
    }


    EventHandler<ActionEvent> orderList(Stage primaryStage) {
        EventHandler<ActionEvent> viewOrders = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                HBox topMenu = cookAppMenu();

//                viewList.setStyle("-fx-background-color: lightblue;");

                // Create a VBox layout to display the ListView
                //VBox checkOrders = new VBox(topMenu);
                VBox checkOrders = new VBox(80, topMenu, viewList);
                Scene checkOrdersPage = new Scene(checkOrders, 800, 800);
                primaryStage.setScene(checkOrdersPage);

                viewList.getStyleClass().add("cook-listview");

//                viewList.getStyleClass().add("cook-listview");
                viewList.setMaxWidth(500);
                checkOrders.setAlignment(Pos.CENTER);
                checkOrders.setStyle("-fx-background-color: rgba(154,41,41,0.58)");

                checkOrdersPage.getStylesheets().add("styles.css");

            }
        };
        return viewOrders;
    }


    EventHandler<ActionEvent> startCooking(Stage primaryStage) {
        EventHandler<ActionEvent> cookingStartsNow = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {


                clientConnection = new Client(cookMessage);
                clientConnection.start();

                clientConnection.setClientType("cook:");
                clientConnection.setCustomerName("Restaurant");

                HBox startMenu = cookAppMenu();

                cookBox = new VBox();
                displayNextImage();

                VBox cook = new VBox(30, startMenu, cookBox);
//                cook.setStyle("-fx-background-color: rgba(28,65,211,0.41)");
                cook.setStyle("-fx-background-color: rgba(154,41,41,0.58)");

                cook.setAlignment(Pos.CENTER);
                cookBox.setAlignment(Pos.CENTER);

                Scene scene = new Scene(cook, 800, 800);
                primaryStage.setScene(scene);
                //primaryStage.setTitle("Image Slideshow");
                primaryStage.show();

                viewOrders.setOnAction(orderList(primaryStage));

                scene.getStylesheets().add("styles.css");

                // Set up a timeline to switch images every 3 seconds
                Duration duration = Duration.seconds(2);
                KeyFrame keyFrame = new KeyFrame(duration, event -> displayNextImage());
                Timeline timeline = new Timeline(keyFrame);
                timeline.setCycleCount(Timeline.INDEFINITE);
                timeline.play();




                cookHome.setOnAction(startCooking(primaryStage));

            }
        };
        return cookingStartsNow;
    }




    //---------------------------COOK APP ENDS HERE---------------------------------------



    // ---------------------------------------------------------------------------------------------------------------



    //---------------------------DRIVER APP STARTS HERE-------------------------------------
    Button driverHome = new Button("Home");
    Button pickUpOrders = new Button("Pickup Orders");
    Button earnings = new Button("Earnings");
    Button account = new Button("Account");
    TextField fullNameField, usernameField, emailField, ssnField, loginUsernameField;
    PasswordField passwordField, confirmPasswordField, loginPasswordField;
    String fullName, username, password, confirmPassword, email, ssn;
    ListView<String> messageListView = new ListView<>();
    List<String> driverOrders = new ArrayList<>();
    String customerNameForDriver;
    Double driverEarnings = 0.0;
    Integer questCount = 0;




    HBox driverAppMenu(){

        VBox logo = getLogo();

        HBox menuOptions = new HBox(10);
        menuOptions.getChildren().addAll(driverHome, pickUpOrders, logo, earnings, account);

        menuOptions.setAlignment(Pos.CENTER);
        menuOptions.getStyleClass().add("custom-button");

//        menuOptions.setStyle("-fx-background-color: rgba(190,113,85,0.93);");

        driverHome.getStyleClass().add("menu-button");
        pickUpOrders.getStyleClass().add("menu-button");
        earnings.getStyleClass().add("menu-button");
        account.getStyleClass().add("menu-button");

        return menuOptions;
    }

    private VBox createRequirementBox(String smallText, String bigText) {
        VBox box = new VBox();
        Label smallLabel = new Label(smallText);
        smallLabel.setFont(Font.font("Arial", 12));
//        smallLabel.setStyle("-fx-background-color: rgba(0,128,0,0.49)");

        Label bigLabel = new Label(bigText);
        bigLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));

        box.setAlignment(Pos.CENTER);
        box.getChildren().addAll(smallLabel, bigLabel);
        return box;
    }

    private VBox createCoverageBox(String title, String description) {
        Label titleLabel = new Label(title);
        titleLabel.setStyle("-fx-font-size: 16; -fx-font-weight: bold; -fx-text-fill: black;");

        Label descriptionLabel = new Label(description);
        descriptionLabel.setStyle("-fx-font-size: 14; -fx-text-fill: black;");

        VBox coverageBox = new VBox();
        coverageBox.setStyle("-fx-background-color: rgba(128,128,128,0.35); -fx-padding: 10;");
        coverageBox.getChildren().addAll(titleLabel, descriptionLabel);

        return coverageBox;
    }

    private HBox createProfilePhotoBox() {
        HBox box = new HBox();
        box.setAlignment(Pos.CENTER);

        // Create a circular clip for the image
        Circle clip = new Circle(600);
//        clip.setFill(Color.WHITE);

        // Load the profile photo image (replace with your image file)
        Image profilePhoto = new Image("driver.jpg");

        // Create an ImageView for the profile photo
        ImageView imageView = new ImageView(profilePhoto);
        imageView.setPreserveRatio(true);
        imageView.setFitWidth(50);
        imageView.setFitHeight(50);
        imageView.setClip(clip);

        box.getChildren().add(imageView);
        return box;
    }


    EventHandler<ActionEvent> insurance(Stage primaryStage) {
        EventHandler<ActionEvent> carInsurance = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                HBox menuButtons = driverAppMenu();
////            menuButtons.setPadding(new Insets(50, 0, 10, 0));

                Image image = new Image("insurance.png"); // Replace with the actual path to your image
                ImageView imageView = new ImageView(image);
                imageView.setFitWidth(600); // Adjust the width as needed
                imageView.setFitHeight(200); // Adjust the height as needed
                imageView.setPreserveRatio(true);

                // Create a Label for the insurance message
                Label insuranceLabel = new Label("Insurance Hub");
                insuranceLabel.setStyle("-fx-font-size: 30; -fx-font-weight: bold; -fx-text-fill: Black");

                // Create a Label for the additional message
                Label messageLabel = new Label("Every trip using Pastabilities is insured");
                messageLabel.setStyle("-fx-font-size: 20; -fx-text-fill: Black");

                // Create VBoxes for coverages with white background and black text
                VBox liabilityCoverageBox = createCoverageBox("Liability Coverages", "Insured for injuries.");
                VBox physicalDamageCoverageBox = createCoverageBox("Contingent Physical Damage Coverage", "Damage protection to your vehicle");
                VBox optionalInjuryProtectionBox = createCoverageBox("Optional Injury Protection", "Optional injury and income protection for you");

                // Create a Label for the long message
                Label longMessageLabel = new Label(
                        "Contingent comprehensive and collision coverage only applies to a covered accident if you maintain this coverage\non your own personal auto insurance. "
                                + "Before this coverage applies, you must first pay a $2,500 deductible.\nApproved Vehicle Marketplace drivers are responsible for a $1,000 deductible.");
                longMessageLabel.setStyle("-fx-font-size: 14; -fx-text-fill: black;");

                // Create a VBox to arrange the components vertically with spacing
                VBox vbox = new VBox();
                vbox.setStyle("-fx-background-color: white; -fx-padding: 20;");
                vbox.setSpacing(10); // Set spacing between the VBoxes
                vbox.getChildren().addAll(menuButtons, imageView, insuranceLabel, messageLabel, liabilityCoverageBox,
                        physicalDamageCoverageBox, optionalInjuryProtectionBox, longMessageLabel);
                vbox.setAlignment(Pos.CENTER);
//                longMessageLabel.setAlignment(Pos.CENTER);

                // Create a Scene and set it on the Stage
                Scene scene = new Scene(vbox, 800, 800); // Adjust the width and height as needed
                scene.getStylesheets().add("styles.css");
                primaryStage.setScene(scene);

                // Set the stage properties
                primaryStage.setTitle("Insurance Hub App");
                primaryStage.show();

            }
        };
        return carInsurance;
    }


    EventHandler<ActionEvent> manageAccount(Stage primaryStage) {
        EventHandler<ActionEvent> account = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                primaryStage.setTitle("Account Info");

                HBox menuButtons = driverAppMenu();
//                menuButtons.setPadding(new Insets(50, 0, 10, 0));

                // Create the main VBox
                VBox driverRequirementsVBox = new VBox();
                driverRequirementsVBox.setAlignment(Pos.CENTER);

                driverRequirementsVBox.getChildren().add(menuButtons);

                // Label for "Driver Requirements"
                Label titleLabel = new Label("Account Info");
                titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 22));
                titleLabel.setPadding(new Insets(50, 0, 20, 0));
                driverRequirementsVBox.getChildren().add(titleLabel);


                HBox profilePhotoBox = createProfilePhotoBox();
                driverRequirementsVBox.getChildren().add(profilePhotoBox);
                profilePhotoBox.setPadding(new Insets(15, 0, 40, 0));

                Label basicInfoLabel = new Label("Basic Info");
                basicInfoLabel.setFont(Font.font("Arial", FontWeight.BOLD, 22));
                basicInfoLabel.setPadding(new Insets(0, 0, 15, 0));
                driverRequirementsVBox.getChildren().add(basicInfoLabel);


                VBox vehicleInsurance = createRequirementBox("Name", "Muhammad Muzzammil ✓");
                VBox vehicleRegistration = createRequirementBox("Pronouns", "Add Your Pronouns >");
                VBox vehicleInspection = createRequirementBox("Phone Number", "+1 (312) 889-4687 ✓");
                VBox email = createRequirementBox("Email", "mmuzzammil99@gmail.com ✓");

                vehicleInsurance.setPadding(new Insets(10, 0, 10, 0));
                vehicleRegistration.setPadding(new Insets(10, 0, 10, 0));
                vehicleInspection.setPadding(new Insets(10, 0, 10, 0));
                email.setPadding(new Insets(10, 0, 10, 0));

                driverRequirementsVBox.getChildren().addAll(vehicleInsurance, vehicleRegistration, vehicleInspection, email);
                driverRequirementsVBox.setStyle("-fx-background-color: rgba(128,128,128,0.35)");


                Scene scene = new Scene(driverRequirementsVBox, 800, 800);
                scene.getStylesheets().add("styles.css");
                primaryStage.setScene(scene);

            }
        };
        return account;
    }


    EventHandler<ActionEvent> payment(Stage primaryStage) {
        EventHandler<ActionEvent> driverPayment = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                primaryStage.setTitle("Payment");

                HBox menuButtons = driverAppMenu();
//                menuButtons.setPadding(new Insets(50, 0, 10, 0));

                // Create the main VBox
                VBox driverRequirementsVBox = new VBox();
                driverRequirementsVBox.setAlignment(Pos.CENTER);

                driverRequirementsVBox.getChildren().add(menuButtons);

                // Label for "Driver Requirements"
                Label titleLabel = new Label("Payout");
                titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 22));
                titleLabel.setPadding(new Insets(50, 0, 20, 0));
                driverRequirementsVBox.getChildren().add(titleLabel);

                // Create the boxes with labels
                VBox backgroundCheckBox = createRequirementBox("Earnings paid out weekly", "Bank Account - XXXXXXX72");

                backgroundCheckBox.setPadding(new Insets(10, 0, 10, 0));

                Label description = new Label("A week runs from Monday at 4:00 a.m to the following\n" +
                        "Monday at 3:59 a.m. All rides during that time period\n" +
                        "will count towards that week's pay period. Learn more");

                description.setPadding(new Insets(0, 0, 10, 0));

                driverRequirementsVBox.getChildren().addAll(backgroundCheckBox, description);



                Label titleLabel2 = new Label("Linked payment methods");
                titleLabel2.setFont(Font.font("Arial", FontWeight.BOLD, 22));
                titleLabel2.setPadding(new Insets(50, 0, 20, 0));

                driverRequirementsVBox.getChildren().add(titleLabel2);

                VBox vehicleInsurance = createRequirementBox("Get access to earnings after every trip", "Uber Pro Card");
                VBox vehicleRegistration = createRequirementBox("Instant Payments", "Bank Account - XXXXXXX72");
                VBox vehicleInspection = createRequirementBox("Transfer Money Instantly - $0.85", "Add Debit Card");

                vehicleInsurance.setPadding(new Insets(10, 0, 10, 0));
                vehicleRegistration.setPadding(new Insets(10, 0, 10, 0));
                vehicleInspection.setPadding(new Insets(10, 0, 10, 0));

                driverRequirementsVBox.getChildren().addAll(vehicleInsurance, vehicleRegistration, vehicleInspection);
//                driverRequirementsVBox.setStyle("-fx-background-color: rgba(0,0,255,0.47)");
                driverRequirementsVBox.setStyle("-fx-background-color: rgba(128,128,128,0.35)");


                Scene scene = new Scene(driverRequirementsVBox, 800, 800);
                scene.getStylesheets().add("styles.css");
                primaryStage.setScene(scene);

            }
        };
        return driverPayment;
    }

    EventHandler<ActionEvent> documents(Stage primaryStage) {
        EventHandler<ActionEvent> driverDocs = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                primaryStage.setTitle("Driver Requirements");

                HBox menuButtons = driverAppMenu();
//                menuButtons.setPadding(new Insets(50, 0, 10, 0));

                // Create the main VBox
                VBox driverRequirementsVBox = new VBox();
                driverRequirementsVBox.setAlignment(Pos.CENTER);

                driverRequirementsVBox.getChildren().add(menuButtons);

                // Label for "Driver Requirements"
                Label titleLabel = new Label("Driver Requirements");
                titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 22));
                titleLabel.setPadding(new Insets(50, 0, 20, 0));
                driverRequirementsVBox.getChildren().add(titleLabel);

                // Create the boxes with labels
                VBox backgroundCheckBox = createRequirementBox("completed ✓", "Background Check");
                VBox driversLicenseBox = createRequirementBox("completed ✓", "Driver's License");
                VBox profilePhotoBox = createRequirementBox("completed ✓", "Profile Photo");

                backgroundCheckBox.setStyle("-fx-background-color: #68c568");
                driversLicenseBox.setStyle("-fx-background-color: rgba(104,197,104,0.65)");
                profilePhotoBox.setStyle("-fx-background-color: #68c568");

                backgroundCheckBox.setMaxWidth(200);
                driversLicenseBox.setMaxWidth(200);
                profilePhotoBox.setMaxWidth(200);

                backgroundCheckBox.setPadding(new Insets(10, 0, 10, 0));
                driversLicenseBox.setPadding(new Insets(10, 0, 10, 0));
                profilePhotoBox.setPadding(new Insets(10, 0, 10, 0));

                driverRequirementsVBox.getChildren().addAll(backgroundCheckBox, driversLicenseBox, profilePhotoBox);



                Label titleLabel2 = new Label("Toyota Camry AC80928");
                titleLabel2.setFont(Font.font("Arial", FontWeight.BOLD, 22));
                titleLabel2.setPadding(new Insets(50, 0, 20, 0));

                driverRequirementsVBox.getChildren().add(titleLabel2);

                VBox vehicleInsurance = createRequirementBox("completed ✓", "Vehicle Insurance");
                VBox vehicleRegistration = createRequirementBox("completed ✓", "Vehicle Registration");
                VBox vehicleInspection = createRequirementBox("completed ✓", "Vehicle Inspection");

                vehicleInsurance.setStyle("-fx-background-color: #68c568");
                vehicleRegistration.setStyle("-fx-background-color: rgba(104,197,104,0.65)");
                vehicleInspection.setStyle("-fx-background-color: #68c568");

                vehicleInsurance.setMaxWidth(200);
                vehicleRegistration.setMaxWidth(200);
                vehicleInspection.setMaxWidth(200);

                vehicleInsurance.setPadding(new Insets(10, 0, 10, 0));
                vehicleRegistration.setPadding(new Insets(10, 0, 10, 0));
                vehicleInspection.setPadding(new Insets(10, 0, 10, 0));

                driverRequirementsVBox.getChildren().addAll(vehicleInsurance, vehicleRegistration, vehicleInspection);
                driverRequirementsVBox.setStyle("-fx-background-color: rgba(128,128,128,0.35)");


                Scene scene = new Scene(driverRequirementsVBox, 800, 800);
                scene.getStylesheets().add("styles.css");
                primaryStage.setScene(scene);

            }
        };
        return driverDocs;
    }

    EventHandler<ActionEvent> dropOffFinished(Stage primaryStage) {
        EventHandler<ActionEvent> dropped = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                clientConnection.send("droppedOff " + customerNameForDriver);

                questCount++;

                HBox menuStart = driverAppMenu();

                Label orderLabel = new Label("Order was successfully completed");
                orderLabel.setStyle("-fx-font-size: 20;");

                Label earningsLabel = new Label("You earned " + driverEarnings + " for this delivery");
                earningsLabel.setStyle("-fx-font-size: 20;");

                // Create an HBox for the "Continue searching for deliveries" label and button
                HBox continueSearchBox = new HBox(10); // 10 is the spacing between nodes

                Label continueSearchLabel = new Label("Continue searching for deliveries");
                continueSearchLabel.setStyle("-fx-font-size: 20;");

                Button continueSearchButton = new Button(">");
                continueSearchButton.setOnAction(orderListForDriver(primaryStage));
                continueSearchBox.getChildren().addAll(continueSearchLabel, continueSearchButton);

                // Create a VBox to arrange the labels and the HBox vertically
                VBox vbox = new VBox(20); // 20 is the spacing between nodes
                vbox.setPadding(new Insets(20));
                vbox.getChildren().addAll(menuStart, orderLabel, earningsLabel, continueSearchBox);
                vbox.setAlignment(Pos.CENTER);

                vbox.setStyle("-fx-background-color: rgba(128,128,128,0.35)");

                // Set the VBox as the root of the scene
                Scene scene = new Scene(vbox, 800, 800);
                scene.getStylesheets().add("styles.css");

                // Set the scene and show the stage
                primaryStage.setScene(scene);
                primaryStage.setTitle("Order Completed Scene");
                primaryStage.show();

            }
        };
        return dropped;
    }


    EventHandler<ActionEvent> dropOffGoogleMaps(Stage primaryStage, String startLocation, String destination) {
        EventHandler<ActionEvent> dropOff = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                HBox menuStart = driverAppMenu();

                clientConnection.send("pickedUp " + customerNameForDriver);

                WebView webView = new WebView();

                // Construct the Google Maps URL with directions
                String mapsURL = "https://www.google.com/maps/dir/" + startLocation + "/" + destination;

                webView.getEngine().load(mapsURL);

                Button finishDropOff = new Button("Finish DropOff");
                finishDropOff.getStyleClass().add("map-button");

                VBox container = new VBox(menuStart, webView, finishDropOff);

                Scene scene = new Scene(container, 800, 800);
                scene.getStylesheets().add("styles.css");

                primaryStage.setTitle("Google Maps Directions in JavaFX with Button");
                primaryStage.setScene(scene);

                finishDropOff.setOnAction(dropOffFinished(primaryStage));

            }
        };
        return dropOff;
    }


    void googleMaps(Stage primaryStage, String startLocation, String destination){

        HBox menuStart = driverAppMenu();

        WebView webView = new WebView();

        // Construct the Google Maps URL with directions
        String mapsURL = "https://www.google.com/maps/dir/" + startLocation + "/" + destination;

        webView.getEngine().load(mapsURL);

        Button finishPickup = new Button("Finish Pickup");
        finishPickup.getStyleClass().add("map-button");

        VBox container = new VBox(menuStart, webView, finishPickup);

        Scene scene = new Scene(container, 800, 800);
        scene.getStylesheets().add("styles.css");

        primaryStage.setTitle("Google Maps Directions in JavaFX with Button");
        primaryStage.setScene(scene);


        String startLocation2 = "Your+Starting+Location";
        String destination2 = "6623+North+Damen+Ave,+Chicago,+IL";
        finishPickup.setOnAction(dropOffGoogleMaps(primaryStage,startLocation2,destination2));


    }


    void updatingDriverList(Stage primaryStage) {

        ObservableList<String> drivermessages = FXCollections.observableArrayList();
        String lastItem = new String();


        // The order name and person
        for (int i = 0; i < driverOrders.size(); i++) {
            lastItem = driverOrders.get(i);
            drivermessages.add(lastItem);
        }


        messageListView.setCellFactory(param -> new ListCell<String>() {

            private final Button button2 = new Button("Accept");

            {

                button2.getStyleClass().add("accept-Button");

                button2.setOnAction(event -> {

                    clientConnection.send("Accepted " + customerNameForDriver);
                    String startLocation = "Your+Starting+Location";
                    String destination = "6400+North+Ridge+Blvd,+Chicago,+IL";

                    int index = getIndex();

                    if (index >= 0 && index < driverOrders.size()) {
                        driverOrders.remove(index); // Remove the item from driverOrders
                        drivermessages.remove(index); // Remove the item from drivermessages
                    }

                    googleMaps(primaryStage, startLocation, destination);

                }); // Accept button on action ends here
            }

            private final Button button = new Button("Reject");

            {

                button.getStyleClass().add("reject-Button");

                button.setOnAction(event -> {

                    int index = getIndex();

                    if (index >= 0 && index < driverOrders.size()) {
                        driverOrders.remove(index); // Remove the item from driverOrders
                        drivermessages.remove(index); // Remove the item from drivermessages
                    }

                }); // Reject button on action ends here


            }


            @Override
            protected void updateItem(String order, boolean empty) {
                super.updateItem(order, empty);
                if (empty || order == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    HBox buttonBox = new HBox();
                    Label label = new Label(order);

                    buttonBox.setSpacing(25);
                    buttonBox.setAlignment(Pos.CENTER);
                    buttonBox.getChildren().addAll(button, label, button2);
                    setGraphic(buttonBox);
                }
            }
        });

        messageListView.setItems(drivermessages);
    }

    // List function ends here

    EventHandler<ActionEvent> orderListForDriver(Stage primaryStage) {
        EventHandler<ActionEvent> orderStart = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                System.out.println("im here 1");


                HBox menuStart = driverAppMenu();

                primaryStage.setTitle("Message List");


                messageListView.setMaxWidth(350);
                messageListView.getStyleClass().add("driver-listview");


                VBox root = new VBox(50, menuStart, messageListView);
                root.setAlignment(Pos.CENTER);
//                root.setStyle("-fx-background-color: rgba(0,0,255,0.47)");
                root.setStyle("-fx-background-color: rgba(128,128,128,0.35)");

                Scene scene = new Scene(root, 800, 800);
                primaryStage.setScene(scene);

                scene.getStylesheets().add("styles.css");

                primaryStage.show();

            }
        };
        return orderStart;
    }


    EventHandler<ActionEvent> showEarnings(Stage primaryStage) {
        EventHandler<ActionEvent> earnings = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                HBox menuStart = driverAppMenu();
                menuStart.setPadding(new Insets(0, 0, 0, 0));

                Label earningLabel = new Label("Total Earnings: $" + driverEarnings);
                earningLabel.setStyle("-fx-font-size: 30;");
                earningLabel.setPadding(new Insets(100, 0, 50, 0));

                Label questLabel = new Label("Quest Progress: " + questCount + "/10");
                questLabel.setStyle("-fx-font-size: 30;");


                VBox earningBox = new VBox(menuStart, earningLabel, questLabel);
                earningBox.setAlignment(Pos.CENTER);
                earningBox.setStyle("-fx-background-color: rgba(128,128,128,0.35)");

                Scene scene = new Scene(earningBox, 800, 800);
                scene.getStylesheets().add("styles.css");

                primaryStage.setScene(scene);
                primaryStage.show();

            }
        };
        return earnings;
    }

    private VBox createSideVBox(int spacingBetweenLabels, String... labels) {
        VBox sideVBox = new VBox(30); // Vertical VBox for each side

        for (String label : labels) {
            Label currentLabel = new Label(label);
            currentLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 12pt; -fx-text-fill: rgba(0,0,0,0.58);"); // Set style for bold text, 14pt size, and blue color
            sideVBox.getChildren().add(currentLabel);
        }

        sideVBox.setAlignment(Pos.CENTER);

        return sideVBox;
    }

    EventHandler<ActionEvent> settingsForApp(Stage primaryStage) {
        EventHandler<ActionEvent> settings = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                HBox menuStart = driverAppMenu();

                int spacingBetweenHBoxes = 40; // Adjust the spacing between HBoxes
                int spacingBetweenLabels = 5; // Adjust the spacing between labels

                // Create VBoxes for left and right sides
                VBox leftVBox = createSideVBox(spacingBetweenLabels, "Sounds & Voices", "Navigation", "Accessibility", "Communication", "Night Mode", "Follow My Ride", "Emergency Contacts", "Speed Limit", "911 Data Sharing", "Ride Check", "Video Recording");
                VBox rightVBox = createSideVBox(spacingBetweenLabels, "Off >", "Google Maps >", "Vibration >", "Preference >", "Automatic (time of day) >", "Set Up >", "Choose >", "Set Restriction >", "Allowed >", "On >", "Off >");

                // Combine left and right VBoxes into an HBox
                HBox mainHBox = new HBox(spacingBetweenHBoxes);
                mainHBox.getChildren().addAll(leftVBox, rightVBox);
                mainHBox.setAlignment(Pos.CENTER);

                VBox finalPage = new VBox(20, menuStart, mainHBox);
                finalPage.setAlignment(Pos.CENTER);
                finalPage.setStyle("-fx-background-color: rgba(128,128,128,0.35)");


                // Create the scene
                Scene scene = new Scene(finalPage, 800, 800); // Set width and height accordingly
                scene.getStylesheets().add("styles.css");

                // Set the scene and show the stage
                primaryStage.setTitle("Settings Scene Example");
                primaryStage.setScene(scene);
                primaryStage.show();
            }
        };
        return settings;
    }

    EventHandler<ActionEvent> aboutSettings(Stage primaryStage) {
        EventHandler<ActionEvent> about = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                HBox menuStart = driverAppMenu();

                VBox mainVBox = new VBox(20); // Vertical gap of 20 between buttons
                mainVBox.setAlignment(Pos.TOP_CENTER); // Align to the top of the VBox

                // Create buttons
                Button versionButton = new Button("Version 4.425.1000");
                Button acknowledgementsButton = new Button("Acknowledgements");

                // Add elements to the VBox
                mainVBox.getChildren().addAll(menuStart, versionButton, acknowledgementsButton);

                mainVBox.setStyle("-fx-background-color: rgba(128,128,128,0.35)");

                // Set event handlers if needed

                // Create the scene
                Scene scene = new Scene(mainVBox, 800, 800);
                scene.getStylesheets().add("styles.css");

                // Set the scene and show the stage
                primaryStage.setTitle("Version Scene Example");
                primaryStage.setScene(scene);
                primaryStage.show();

            }
        };
        return about;
    }


    void startDelivery(Stage primaryStage) {

        HBox menuStart = driverAppMenu();

        // This VBox holds the image of menu
        VBox imageHolder = new VBox();
        imageHolder.getChildren().addAll(getImage("delivery.jpg", 800, 800));
        imageHolder.setAlignment(Pos.CENTER);

        VBox driver = new VBox(menuStart, imageHolder);
        Scene driverPage = new Scene(driver, 800, 800);
        primaryStage.setScene(driverPage);

//        driver.setStyle("-fx-background-color: rgba(0,0,255,0.47)");
        driver.setStyle("-fx-background-color: rgba(128,128,128,0.35)");

        driverPage.getStylesheets().add("styles.css");

        pickUpOrders.setOnAction(orderListForDriver(primaryStage));
        driverHome.setOnAction(event -> {
            startDelivery(primaryStage);
        });
        earnings.setOnAction(showEarnings(primaryStage));


        // Account settings below: We will create a drop down menu

        ContextMenu driverContextMenu = new ContextMenu();

        MenuItem option1 = new MenuItem("Documents");
        MenuItem option2 = new MenuItem("Payments");
        MenuItem option3 = new MenuItem("Manage Account");
        MenuItem option4 = new MenuItem("Insurance");
        MenuItem option5 = new MenuItem("Security and Privacy");
        MenuItem option6 = new MenuItem("App Settings");
        MenuItem option7 = new MenuItem("About");
        MenuItem option8 = new MenuItem("Sign Out");

        option1.getStyleClass().add("dropDown-button");
        option2.getStyleClass().add("dropDown-button");
        option3.getStyleClass().add("dropDown-button");
        option4.getStyleClass().add("dropDown-button");
        option5.getStyleClass().add("dropDown-button");
        option6.getStyleClass().add("dropDown-button");
        option7.getStyleClass().add("dropDown-button");
        option8.getStyleClass().add("dropDown-button");
        driverContextMenu.setStyle("-fx-background-color: rgb(211, 211, 211)");

        driverContextMenu.getItems().addAll(option1, option2, option3, option4, option5, option6, option7, option8);


        account.setOnAction(event -> {
            driverContextMenu.show(account, Side.BOTTOM, 0, 0); // Show below the button
        });


        option1.setOnAction(documents(primaryStage));
        option2.setOnAction(payment(primaryStage));
        option3.setOnAction(manageAccount(primaryStage));
        option4.setOnAction(insurance(primaryStage));
        option6.setOnAction(settingsForApp(primaryStage));
        option7.setOnAction(aboutSettings(primaryStage));
        option8.setOnAction(signIn(primaryStage));


    }


    void errorLoginPage(Stage primaryStage) {

        primaryStage.setTitle("Login Information Not Found");

        // Create a label for the message with custom font style
        Label messageLabel = new Label("Login Information Not Found. Please Try Again Or Sign Up To Join");
        messageLabel.setFont(Font.font("Arial", 20)); // Set a bold 20-point font
        messageLabel.setAlignment(Pos.CENTER);

        // Create a button to retry login
        Button retryButton = new Button("Retry Login");
        retryButton.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-font-weight: bold;");
        retryButton.setMinWidth(150); // Set a minimum width for the button


        retryButton.setOnAction(signIn(primaryStage));
        // Create a layout for the page
        VBox layout = new VBox(20);
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-background-color: rgba(0,0,255,0.47)"); // Set background color
        layout.getChildren().addAll(messageLabel, retryButton);

        Scene scene = new Scene(layout, 800, 800);
        primaryStage.setScene(scene);

        primaryStage.show();


    }


    EventHandler<ActionEvent> signUpConfirmationPage(Stage primaryStage) {
        EventHandler<ActionEvent> confirm = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                primaryStage.setTitle("Thank You Page");

                // Save all information
                fullName = fullNameField.getText();
                username = usernameField.getText();
                password = passwordField.getText();
                confirmPassword = confirmPasswordField.getText();
                email = emailField.getText();
                ssn = ssnField.getText();

                database.insertData(usernameField.getText(), passwordField.getText());


                // Create a label for the thank you message
                Label thankYouLabel = new Label("\t    Thank you for Signing up!\n\nYou are registered and may drive now!");
                thankYouLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));

                // Create a button to go to the login page
                Button loginButton = new Button("Login Page");
                loginButton.setOnAction(signIn(primaryStage));
                loginButton.getStyleClass().add("cart-button");

                // Create a layout for the thank you page and set background color to gray
                VBox layout = new VBox(20);
                layout.setAlignment(Pos.CENTER);
                layout.setStyle("-fx-background-color: rgba(0,0,255,0.47)");
                layout.getChildren().addAll(thankYouLabel, loginButton);

                Scene scene = new Scene(layout, 800, 800);
                scene.getStylesheets().add("styles.css");
                primaryStage.setScene(scene);

                primaryStage.show();

            }
        };
        return confirm;
    }


    EventHandler<ActionEvent> driverSigningUp(Stage primaryStage) {
        EventHandler<ActionEvent> signedUp = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                primaryStage.setTitle("Signup Page");

                // Create text fields for user information
                fullNameField = new TextField();
                fullNameField.setPromptText("Full Name");
                fullNameField.setMaxWidth(170);

                usernameField = new TextField();
                usernameField.setPromptText("Username");
                usernameField.setMaxWidth(170);

                passwordField = new PasswordField();
                passwordField.setPromptText("Password");
                passwordField.setMaxWidth(170);

                confirmPasswordField = new PasswordField();
                confirmPasswordField.setPromptText("Confirm Password");
                confirmPasswordField.setMaxWidth(170);

                emailField = new TextField();
                emailField.setPromptText("Email");
                emailField.setMaxWidth(170);

                ssnField = new TextField();
                ssnField.setPromptText("Social Security");
                ssnField.setMaxWidth(170);

                // Create a signup button
                Button signupButton = new Button("Sign Up");

                signupButton.setOnAction(signUpConfirmationPage(primaryStage));

                // Create a layout for the signup page
                VBox layout = new VBox(10);
                layout.setAlignment(Pos.CENTER);
                layout.getChildren().addAll(
                        fullNameField, usernameField, passwordField, confirmPasswordField, emailField, ssnField, signupButton
                );

                layout.setStyle("-fx-background-color: GRAY");


                Scene scene = new Scene(layout, 800, 800);
                primaryStage.setScene(scene);

                primaryStage.show();

            }
        };
        return signedUp;
    }

    EventHandler<ActionEvent> loginLoadingPage(Stage primaryStage) {
        EventHandler<ActionEvent> confirmingLogin = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {


                boolean exists = database.retrieveData(loginUsernameField.getText(), loginPasswordField.getText());


                // If the user exists only then send to the next page
                if(exists){

                    exists = false;

                    clientConnection = new Client(driverMessage);
                    clientConnection.start();
                    clientConnection.setClientType("driverName:");
                    clientConnection.setCustomerName(loginUsernameField.getText());

                    startDelivery(primaryStage);

                }
                // User login failed
                else{
                    errorLoginPage(primaryStage);
                }

            }
        };
        return confirmingLogin;
    }


    EventHandler<ActionEvent> signIn(Stage primaryStage) {
        EventHandler<ActionEvent> signInForDriver = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                primaryStage.setTitle("Login Page");

                // Load the image
                Image backgroundImage = new Image("customerLogin.jpg");
                ImageView backgroundImageView = new ImageView(backgroundImage);

                backgroundImageView.setFitWidth(800);
                backgroundImageView.setFitHeight(800);
                backgroundImageView.setPreserveRatio(true);

                // Create text fields for username and password
                loginUsernameField = new TextField();
                loginUsernameField.setPromptText("Username");
                loginPasswordField = new PasswordField();
                loginPasswordField.setPromptText("Password");

                loginUsernameField.setMaxWidth(150);
                loginPasswordField.setMaxWidth(150);

                // Create login and signup buttons
                Button loginButton = new Button("Login");
                Button signupButton = new Button("Sign Up");

                loginButton.getStyleClass().add("cart-button");
                signupButton.getStyleClass().add("cart-button");
                loginUsernameField.getStyleClass().add("login-textField");
                loginPasswordField.getStyleClass().add("login-textField");

                // Create a layout for the login page
                VBox layout = new VBox(10, loginUsernameField, loginPasswordField, loginButton, signupButton);
                layout.setAlignment(Pos.CENTER);

                // Create a StackPane to layer the image and the layout
                StackPane root = new StackPane();
                root.getChildren().addAll(backgroundImageView, layout);
                root.setStyle("-fx-background-color: rgba(0,0,255,0.47)");

                Scene scene = new Scene(root, 800, 800);
                primaryStage.setScene(scene);

                scene.getStylesheets().add("styles.css");

                primaryStage.show();


                loginButton.setOnAction(loginLoadingPage(primaryStage));
                signupButton.setOnAction(driverSigningUp(primaryStage));

            }
        };
        return signInForDriver;
    }

    private static double roundToTwoDecimalPlaces(double value) {
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        String formattedValue = decimalFormat.format(value);
        return Double.parseDouble(formattedValue);
    }


    //---------------------------DRIVER APP ENDS HERE-------------------------------------




    // ---------------------------------------------------------------------------------------------------------------



    public static void main(String[] args) {
        // TODO Auto-generated method stub
        launch(args);
    }

    private List<String> getNewMessages() {
        return newMessages;
    }


    @Override
    public void start(Stage primaryStage) throws Exception {
        // TODO Auto-generated method stub



        //------------------------- COMMUNICATION STARTS HERE -------------------------

        customerMessage = message-> { // catch messages coming from server to customer app

            System.out.println("Message for Customer: " + message.toString());

//            System.out.println("Message for customer: " + message.toString());
            if(message.toString().contains("prepared")){
//                items.add(message.toString());
//                updateList.setItems(items);
                System.out.println("Cook App Says: " + message);
                newMessages.add(message.toString());

            }
            else if(message.toString().contains("Completed")){
//                items.add(message.toString());
//                updateList.setItems(items);
                System.out.println("Cook App Says: " + message);
                newMessages.add(message.toString());

            }
            else if(message.toString().contains("Picked Up")){
                newMessages.add(message.toString());
            }
            else if(message.toString().contains("assigned")){
                newMessages.add(message.toString());

                String[] word =  message.toString().split(" ");
                driverIdForCustomer = word[8];

                clientConnection.send("DriverMoney " + driverIdForCustomer + " " + itemsInCart.getTotal());
            }
            else if(message.toString().contains("Delivered")){

                newMessages.add("Your order was delivered! Enjoy!");

            }
        };

        cookMessage = message-> { // catch messages coming from server to cook app

            System.out.println("Message for Cook: " + message.toString());

            if(message instanceof ArrayList){
                System.out.println("order is here: " + message);

                List<String> order = new ArrayList<>(); // Create an array list
                order.add(message.toString()); // Store it

                orders.add(order); // Then store it so cook app can access it
                updateItemsInList();

            }
            //System.out.println("Message for cook: " + message.toString());
        };

        driverMessage = message-> { // catch messages coming from server to driver app

            System.out.println("Message for Driver: " + message.toString());

            if(message.toString().contains("NewOrder")){

//                messages.add(message.toString());

                String order = message.toString();

                String[] parts = order.split("\\s+");  // Split the string by whitespace
                customerNameForDriver = parts.length > 1 ? parts[1] : "";
                System.out.println("Name for driver is: " + customerNameForDriver + "is");

                driverOrders.add(order);
                updatingDriverList(primaryStage);

            }
            if(message.toString().contains("Earning")){

//                String  money = message.toString();
//                String[] parts = money.split("\\s+");
//                driverEarnings += Double.parseDouble(parts[1]);
//                System.out.println("Driver money = " + driverEarnings);

                String money = message.toString();
                String[] parts = money.split("\\s+");
                driverEarnings += Double.parseDouble(parts[1]);

                // Round the driverEarnings to two decimal places
                driverEarnings = roundToTwoDecimalPlaces(driverEarnings);

                System.out.println("Driver money = " + driverEarnings);

            }

        };

        //---------------------- COMMUNICATION ENDS HERE -------------------------



        //startOrderApp(primaryStage);

        primaryStage.show();

        Button customer = new Button("CUSTOMER APP");
        Button cook = new Button("COOK APP");
        Button driver = new Button("DRIVER APP");

        customer.getStyleClass().add("start-buttons");
        cook.getStyleClass().add("start-buttons");
        driver.getStyleClass().add("start-buttons");

//        VBox startApplication = new VBox(75, customer, cook, driver);
//        startApplication.getStyleClass().add("start-page");


        Image backgroundImage = new Image("logo.png"); // Specify the image file path
        ImageView backgroundView = new ImageView(backgroundImage);
        backgroundView.setPreserveRatio(true);
        backgroundView.setFitHeight(800);
        backgroundView.setFitWidth(800);

        Region background = new Region();
        background.setStyle("-fx-background-color: rgba(7,7,7,0.99);");
        background.setMinSize(800, 800);

        // Apply animations to the background
        TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(6), backgroundView);
        translateTransition.setFromX(0);
        translateTransition.setToX(200);
        translateTransition.setAutoReverse(true);
        translateTransition.setCycleCount(TranslateTransition.INDEFINITE);

        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(2), background);
        fadeTransition.setFromValue(1.0);
        fadeTransition.setToValue(0.2);
        fadeTransition.setAutoReverse(true);
        fadeTransition.setCycleCount(FadeTransition.INDEFINITE);

        // Play the animations
        translateTransition.play();
        fadeTransition.play();

        // Create an HBox to space out the buttons horizontally
        VBox buttonContainer = new VBox(175); // 20 is the spacing between buttons
        buttonContainer.getChildren().addAll(cook, customer, driver);
        buttonContainer.setAlignment(Pos.CENTER);

        // Create a StackPane and add nodes
        StackPane app = new StackPane();
        app.getChildren().addAll(backgroundView, background, buttonContainer);

        // Center the buttons vertically
        StackPane.setAlignment(buttonContainer, Pos.CENTER);


        Scene startScene = new Scene(app, 800, 800);
        startScene.getStylesheets().add("styles.css");
        primaryStage.setTitle("DRIVER, CUSTOMER, COOK");
        primaryStage.setScene(startScene);

        customer.setOnAction(customerOrderName(primaryStage));




//        cook.setOnAction(beginCook(primaryStage));

        cook.setOnAction(startCooking(primaryStage));





//        driver.setOnAction(driverOrderName(primaryStage));

        driver.setOnAction(signIn(primaryStage));



        //--------------------------------------------------
        //startGame(primaryStage); // program starts
        //--------------------------------------------------


        // This is when they exit from the screen
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent t) {
                Platform.exit();
                System.exit(0);
            }
        });


    }


} // end of class











