package com.example.registrationform;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class RegistrationApp extends Application {

    // Regular expressions for validation
    private static final String NAME_REGEX = "^[a-zA-Z\\s-]{2,25}$";
    private static final String DOB_REGEX = "^(0[1-9]|1[0-2])/(0[1-9]|[12][0-9]|3[01])/\\d{4}$";
    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@farmingdale\\.edu$";
    private static final String ZIP_REGEX = "^\\d{5}$";

    // Boolean flags to track validity
    private boolean isFirstNameValid = false;
    private boolean isLastNameValid = false;
    private boolean isEmailValid = false;
    private boolean isDobValid = false;
    private boolean isZipValid = false;

    // Primary "Add" button
    private Button addButton;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Student Registration Form");

        // Layout setup
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(15);
        grid.setPadding(new Insets(25, 25, 25, 25));

        // Form title
        Label sceneTitle = new Label("Register New Student");
        sceneTitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(sceneTitle, 0, 0, 2, 1);

        // Initializing field

        // First Name
        TextField firstNameField = new TextField();
        firstNameField.setPromptText("John");
        firstNameField.setId("firstName");
        Label firstNameLabel = new Label("First Name:");
        Label firstNameError = createErrorLabel("2-25 characters.");
        grid.add(firstNameLabel, 0, 1);
        grid.add(firstNameField, 1, 1);
        grid.add(firstNameError, 2, 1);

        // Last Name
        TextField lastNameField = new TextField();
        lastNameField.setPromptText("Doe");
        lastNameField.setId("lastName");
        Label lastNameLabel = new Label("Last Name:");
        Label lastNameError = createErrorLabel("2-25 characters.");
        grid.add(lastNameLabel, 0, 2);
        grid.add(lastNameField, 1, 2);
        grid.add(lastNameError, 2, 2);

        // Email
        TextField emailField = new TextField();
        emailField.setPromptText("johndoe@farmingdale.edu");
        emailField.setId("email");
        Label emailLabel = new Label("Email:");
        Label emailError = createErrorLabel("Must end in @farmingdale.edu");
        grid.add(emailLabel, 0, 3);
        grid.add(emailField, 1, 3);
        grid.add(emailError, 2, 3);

        // Date of Birth
        TextField dobField = new TextField();
        dobField.setPromptText("MM/DD/YYYY");
        dobField.setId("dob");
        Label dobLabel = new Label("Date of Birth:");
        Label dobError = createErrorLabel("Format: MM/DD/YYYY");
        grid.add(dobLabel, 0, 4);
        grid.add(dobField, 1, 4);
        grid.add(dobError, 2, 4);

        // Zip Code
        TextField zipField = new TextField();
        zipField.setPromptText("11735");
        zipField.setId("zip");
        Label zipLabel = new Label("Zip Code:");
        Label zipError = createErrorLabel("5-digit number.");
        grid.add(zipLabel, 0, 5);
        grid.add(zipField, 1, 5);
        grid.add(zipError, 2, 5);

        // "Add" button
        addButton = new Button("Add");
        addButton.setId("addButton");
        addButton.setDisable(true);
        grid.add(addButton, 1, 6);

        // Listeners for Validation

        // Attached focus listeners to validate fields when the user clicks away
        addValidationListener(firstNameField, NAME_REGEX, firstNameError, 1);
        addValidationListener(lastNameField, NAME_REGEX, lastNameError, 2);
        addValidationListener(emailField, EMAIL_REGEX, emailError, 3);
        addValidationListener(dobField, DOB_REGEX, dobError, 4);
        addValidationListener(zipField, ZIP_REGEX, zipError, 5);

        // "Add" button action
        addButton.setOnAction(e -> {
            showSuccessUI(primaryStage, firstNameField.getText());
        });

        // Setup scene and show
        Scene scene = new Scene(grid, 550, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Helper method that creates hidden, red error labels.
     */
    private Label createErrorLabel(String message) {
        Label label = new Label(message);
        label.setStyle("-fx-text-fill: red; -fx-font-size: 10px;");
        label.setVisible(false);
        return label;
    }

    /**
     * Adds a focus listener to a TextField to validate its contents using regex when it loses focus.
     * * @param field The TextField to listen to.
     * @param regex The regular expression to validate against.
     * @param errorLabel The label to display if validation fails.
     * @param fieldId An integer identifier to track which specific field is being validated.
     */
    private void addValidationListener(TextField field, String regex, Label errorLabel, int fieldId) {
        field.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                try {
                    boolean isValid = validateInput(field.getText(), regex);
                    if (isValid) {
                        field.setStyle("-fx-border-color: green; -fx-border-width: 2px; -fx-border-radius: 3px;");
                        errorLabel.setVisible(false);
                        updateFieldStatus(fieldId, true);
                    } else {
                        field.setStyle("-fx-border-color: red; -fx-border-width: 2px; -fx-border-radius: 3px;");
                        errorLabel.setVisible(true);
                        updateFieldStatus(fieldId, false);
                    }
                } catch (IllegalArgumentException e) {
                    System.out.println("Validation Error: " + e.getMessage());
                }
            }
        });
    }

    /**
     * Validates a given input string against a specified regular expression.
     *
     * @param input The text input from the user to be validated.
     * @param regex The regular expression pattern to match against.
     * @return true if the input matches the regex and is not empty, false otherwise.
     * @throws IllegalArgumentException if the input or regex is null.
     */
    public boolean validateInput(String input, String regex) throws IllegalArgumentException {
        if (input == null || regex == null) {
            throw new IllegalArgumentException("Input string and regex pattern cannot be null.");
        }
        return !input.trim().isEmpty() && input.matches(regex);
    }

    /**
     * Updates the boolean flags for field validity and checks if the "Add" button should be enabled.
     */
    private void updateFieldStatus(int fieldId, boolean status) {
        switch (fieldId) {
            case 1: isFirstNameValid = status; break;
            case 2: isLastNameValid = status; break;
            case 3: isEmailValid = status; break;
            case 4: isDobValid = status; break;
            case 5: isZipValid = status; break;
        }

        // Enable "Add" button only if ALL fields are valid
        if (isFirstNameValid && isLastNameValid && isEmailValid && isDobValid && isZipValid) {
            addButton.setDisable(false);
        } else {
            addButton.setDisable(true);
        }
    }

    /**
     * Creates and switches to a new Success UI.
     */
    private void showSuccessUI(Stage stage, String firstName) {
        VBox layout = new VBox(20);
        layout.setAlignment(Pos.CENTER);

        Label successMessage = new Label("Registration Successful!");
        successMessage.setFont(Font.font("Tahoma", FontWeight.BOLD, 24));
        successMessage.setStyle("-fx-text-fill: green;");

        Label welcomeMessage = new Label("Welcome, " + firstName + "!");
        welcomeMessage.setFont(Font.font("Tahoma", FontWeight.NORMAL, 18));

        Button exitButton = new Button("Exit");
        exitButton.setOnAction(e -> stage.close());

        layout.getChildren().addAll(successMessage, welcomeMessage, exitButton);

        Scene successScene = new Scene(layout, 550, 400);
        stage.setScene(successScene);
    }

    public static void main(String[] args) {
        launch(args);
    }
}