package com.aurionpro.OOAD.Guitar.Version5.test;

import java.util.List;
import java.util.Scanner;

import com.aurionpro.OOAD.Guitar.Version5.model.Builder;
import com.aurionpro.OOAD.Guitar.Version5.model.Instrument;
import com.aurionpro.OOAD.Guitar.Version5.model.InstrumentSpec;
import com.aurionpro.OOAD.Guitar.Version5.model.Inventory;
import com.aurionpro.OOAD.Guitar.Version5.model.MandolinSpec;
import com.aurionpro.OOAD.Guitar.Version5.model.Style;
import com.aurionpro.OOAD.Guitar.Version5.model.Type;
import com.aurionpro.OOAD.Guitar.Version5.model.Wood;

public class InstrumentTest {
    private static final Scanner scanner = new Scanner(System.in);
    private static final Inventory inventory = new Inventory();

    public static void main(String[] args) {
        initializeInventory();

        while (true) {
            System.out.println("\nInstrument Inventory Menu:");
            System.out.println("1. Add Instrument");
            System.out.println("2. View All Instruments");
            System.out.println("3. Search Instruments");
            System.out.println("4. Delete Instrument");
            System.out.println("5. Exit");
            System.out.print("Choose an option: ");
            String input = scanner.nextLine();

            switch (input) {
                case "1":
                    addInstrumentMenu();
                    break;
                case "2":
                    viewAllInstruments();
                    break;
                case "3":
                    searchInstruments();
                    break;
                case "4":
                    deleteInstrument();
                    break;
                case "5":
                    System.out.println("Goodbye!");
                    return;
                default:
                    System.out.println("Invalid option. Try again.");
            }
        }
    }

    private static void addInstrumentMenu() {
        try {
            System.out.print("Enter Serial Number: ");
            String serial = scanner.nextLine();

            System.out.print("Enter Price: ");
            double price = Double.parseDouble(scanner.nextLine());

            Builder builder = readBuilder();
            System.out.print("Enter Model: ");
            String model = scanner.nextLine();
            Type type = readType();
            Wood backWood = readWood("Back");
            Wood topWood = readWood("Top");

            System.out.print("Is this a Mandolin? (yes/no): ");
            String isMandolin = scanner.nextLine().trim().toLowerCase();

            if (isMandolin.equals("yes")) {
                Style style = readStyle();
                MandolinSpec spec = new MandolinSpec(builder, model, type, backWood, topWood, style);
                inventory.addInstrument(serial, price, spec);
            } else {
                InstrumentSpec spec = new InstrumentSpec(builder, model, type, backWood, topWood);
                inventory.addInstrument(serial, price, spec);
            }

            System.out.println("Instrument added successfully!");
        } catch (Exception e) {
            System.out.println("Failed to add instrument. Error: " + e.getMessage());
        }
    }

    private static void viewAllInstruments() {
        List<Instrument> instruments = inventory.getAllInstruments();
        if (instruments.isEmpty()) {
            System.out.println("Inventory is empty.");
            return;
        }

        for (Instrument instrument : instruments) {
            displayInstrument(instrument);
        }
    }

    private static void deleteInstrument() {
        System.out.print("Enter Serial Number to delete: ");
        String serial = scanner.nextLine().trim();

        boolean removed = inventory.removeInstrument(serial);
        if (removed) {
            System.out.println("Instrument with serial " + serial + " deleted.");
        } else {
            System.out.println("No instrument found with serial " + serial + ".");
        }
    }

    private static void searchInstruments() {
        System.out.println("\nChoose Search Mode:");
        System.out.println("1. Search by Model");
        System.out.println("2. Search by Builder");
        System.out.println("3. Search by Type");
        System.out.println("4. Search by Back Wood");
        System.out.println("5. Search by Top Wood");
        System.out.println("6. Search by Any Combination");
        System.out.print("Your choice: ");
        String choice = scanner.nextLine().trim();

        Builder builder = null;
        String model = "";
        Type type = null;
        Wood backWood = null;
        Wood topWood = null;
        Style style = null;

        try {
            switch (choice) {
                case "1":
                    System.out.print("Enter Model (partial allowed): ");
                    model = scanner.nextLine().trim();
                    break;
                case "2":
                    builder = askOptionalBuilder();
                    break;
                case "3":
                    type = askOptionalType();
                    break;
                case "4":
                    backWood = askOptionalWood("Back");
                    break;
                case "5":
                    topWood = askOptionalWood("Top");
                    break;
                case "6":
                    System.out.print("Enter Model (optional): ");
                    model = scanner.nextLine().trim();
                    builder = askOptionalBuilder();
                    type = askOptionalType();
                    backWood = askOptionalWood("Back");
                    topWood = askOptionalWood("Top");

                    System.out.print("Enter Style (A/F) for Mandolin (optional): ");
                    String styleInput = scanner.nextLine().trim();
                    if (!styleInput.isEmpty()) {
                        style = Style.valueOf(styleInput.toUpperCase());
                    }
                    break;
                default:
                    System.out.println("Invalid choice.");
                    return;
            }

            InstrumentSpec spec = (style != null)
                ? new MandolinSpec(builder, model, type, backWood, topWood, style)
                : new InstrumentSpec(builder, model, type, backWood, topWood);

            List<Instrument> results = inventory.search(spec);

            if (results.isEmpty()) {
                System.out.println("No matching instruments found.");
            } else {
                System.out.println("Found " + results.size() + " instrument(s):");
                for (Instrument instrument : results) {
                    displayInstrument(instrument);
                }
            }
        } catch (Exception e) {
            System.out.println("Error during search: " + e.getMessage());
        }
    }

    // ---------------- Helper Methods ----------------

    private static Builder readBuilder() {
        try {
            System.out.print("Enter Builder (FENDER, MARTIN, GIBSON, etc.): ");
            return Builder.valueOf(scanner.nextLine().trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid builder.");
            return readBuilder();
        }
    }

    private static Type readType() {
        try {
            System.out.print("Enter Type (ACOUSTIC, ELECTRIC): ");
            return Type.valueOf(scanner.nextLine().trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid type.");
            return readType();
        }
    }

    private static Wood readWood(String label) {
        try {
            System.out.print("Enter " + label + " Wood: ");
            return Wood.valueOf(scanner.nextLine().trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid wood.");
            return readWood(label);
        }
    }

    private static Style readStyle() {
        try {
            System.out.print("Enter Style (A or F): ");
            return Style.valueOf(scanner.nextLine().trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid style.");
            return readStyle();
        }
    }

    private static Builder askOptionalBuilder() {
        System.out.print("Enter Builder (or leave blank): ");
        String input = scanner.nextLine().trim();
        try {
            return input.isEmpty() ? null : Builder.valueOf(input.toUpperCase());
        } catch (Exception e) {
            System.out.println("Invalid builder. Skipped.");
            return null;
        }
    }

    private static Type askOptionalType() {
        System.out.print("Enter Type (or leave blank): ");
        String input = scanner.nextLine().trim();
        try {
            return input.isEmpty() ? null : Type.valueOf(input.toUpperCase());
        } catch (Exception e) {
            System.out.println("Invalid type. Skipped.");
            return null;
        }
    }

    private static Wood askOptionalWood(String label) {
        System.out.print("Enter " + label + " Wood (or leave blank): ");
        String input = scanner.nextLine().trim();
        try {
            return input.isEmpty() ? null : Wood.valueOf(input.toUpperCase());
        } catch (Exception e) {
            System.out.println("Invalid wood. Skipped.");
            return null;
        }
    }

    private static void displayInstrument(Instrument instrument) {
        InstrumentSpec spec = instrument.getSpec();
        System.out.println("\n Instrument Found:");
        System.out.println("  Serial #: " + instrument.getSerialNumber());
        System.out.println("  Price: $" + instrument.getPrice());
        System.out.println("  Builder: " + spec.getBuilder());
        System.out.println("  Model: " + spec.getModel());
        System.out.println("  Type: " + spec.getType());
        System.out.println("  Back Wood: " + spec.getBackWood());
        System.out.println("  Top Wood: " + spec.getTopWood());
        if (spec instanceof MandolinSpec) {
            System.out.println("  Style: " + ((MandolinSpec) spec).getStyle());
        }
        System.out.println("--------------------------------------------------");
    }

    private static void initializeInventory() {
        inventory.addInstrument("V95693", 1499.95,
            new InstrumentSpec(Builder.FENDER, "Stratocastor", Type.ELECTRIC,
                               Wood.ALDER, Wood.ALDER));

        inventory.addInstrument("11277", 3999.95,
            new InstrumentSpec(Builder.COLLINGS, "CJ", Type.ACOUSTIC,
                               Wood.INDIAN_ROSEWOOD, Wood.SITKA));

        inventory.addInstrument("M12345", 1199.00,
            new MandolinSpec(Builder.MARTIN, "F5", Type.ACOUSTIC,
                             Wood.MAPLE, Wood.MAPLE, Style.F));
    }
}
