package com.example.registrationform;

import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testfx.api.FxAssert;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.matcher.base.NodeMatchers;

import static org.junit.jupiter.api.Assertions.assertFalse;

public class RegistrationAppTest extends ApplicationTest {

    @BeforeAll
    public static void setup() {
        // Delay between keystrokes in milliseconds
        System.setProperty("testfx.robot.write_sleep", "50");
        //System.setProperty("testfx.robot.write_sleep", "150");

        // Delay between other actions like clicking
        System.setProperty("testfx.robot.sleep", "250");
        //System.setProperty("testfx.robot.sleep", "750");
    }

    @Override
    public void start(Stage stage) {
        // Launches your application for the test
        new RegistrationApp().start(stage);
    }

    @Test
    public void testSuccessfulRegistration() {
        // 1. Robot API: Fill out the form
        clickOn("#firstName").write("Olamide");
        clickOn("#lastName").write("Aroso");
        clickOn("#email").write("olarockss@farmingdale.edu");
        clickOn("#dob").write("05/12/2002");
        clickOn("#zip").write("11520");

        // Click back on another field to lose focus on the zip field and trigger validation
        clickOn("#firstName");

        // 2. Matcher API: Verify the Add button is no longer disabled
        Button addButton = lookup("#addButton").queryButton();
        assertFalse(addButton.isDisabled(), "Add button should be enabled after valid input.");

        // Click the Add button
        clickOn("#addButton");

        // 3. Assert the success screen loaded correctly
        FxAssert.verifyThat("Registration Successful!", NodeMatchers.isVisible());
        FxAssert.verifyThat("Welcome, Olamide!", NodeMatchers.isVisible());

        sleep(5000);
    }

    @Test
    public void testInvalidEmailKeepsButtonDisabled() {
        // 1. Fills out the form with an INVALID email
        clickOn("#firstName").write("Olamide");
        clickOn("#lastName").write("Aroso");
        clickOn("#email").write("olarockss@gmail.com"); // Invalid domain
        clickOn("#dob").write("05/05/2002");
        clickOn("#zip").write("11735");

        // Trigger focus validation
        clickOn("#firstName");

        // 2. Assert the button remains disabled to protect the system
        Button addButton = lookup("#addButton").queryButton();

        // assertTrue used here instead of assertFalse
        org.junit.jupiter.api.Assertions.assertTrue(
                addButton.isDisabled(),
                "Security Check: Button should remain disabled due to invalid email domain."
        );

        sleep(3000); // Pauses so the audience can see the red error border on the email field
    }
}