import javafx.scene.control.Label;

public class BillingInformation {


    void validateCreditCard(String creditCardNumber, Label validationLabel) {
        creditCardNumber = creditCardNumber.replaceAll("\\s", ""); // Remove spaces
        if (creditCardNumber.length() == 16 && creditCardNumber.matches("\\d{16}")) {
            validationLabel.setText("Valid credit card number.");
        } else {
            validationLabel.setText("Invalid credit card number.");
        }
    }

    void validateCVC(String cvc, Label cvcValidationLabel) {
        cvc = cvc.replaceAll("\\s", ""); // Remove spaces
        if (cvc.length() == 3 && cvc.matches("\\d{3}")) {
            cvcValidationLabel.setText("Valid CVC.");
        } else {
            cvcValidationLabel.setText("Invalid CVC.");
        }
    }

    void validateBillingName(String billingName, Label billingNameValidationLabel) {
        // Split the billing name into first and last names
        String[] names = billingName.trim().split("\\s+");
        if (names.length == 2 && !names[0].isEmpty() && !names[1].isEmpty()) {
            billingNameValidationLabel.setText("Valid billing name.");
        } else {
            billingNameValidationLabel.setText("Invalid billing name.");
        }
    }

    void validateStreetAddress(String streetAddress, Label streetAddressValidationLabel) {
        if (!streetAddress.trim().isEmpty()) {
            streetAddressValidationLabel.setText("Valid street address.");
        } else {
            streetAddressValidationLabel.setText("Invalid street address.");
        }
    }

    void validateCity(String city, Label cityValidationLabel) {
        if (!city.trim().isEmpty()) {
            cityValidationLabel.setText("Valid city.");
        } else {
            cityValidationLabel.setText("Invalid city.");
        }
    }

    void validateState(String state, Label stateValidationLabel) {
        if (!state.trim().isEmpty()) {
            stateValidationLabel.setText("Valid state.");
        } else {
            stateValidationLabel.setText("Invalid state.");
        }
    }

    void validateZipCode(String zipCode, Label zipCodeValidationLabel) {
        zipCode = zipCode.replaceAll("\\s", ""); // Remove spaces
        if (zipCode.length() == 5 && zipCode.matches("\\d{5}")) {
            zipCodeValidationLabel.setText("Valid ZIP code.");
        } else {
            zipCodeValidationLabel.setText("Invalid ZIP code.");
        }
    }
}
