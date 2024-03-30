import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.InputMismatchException;
import java.util.Scanner;

public class PlaneManagement {

    //Assuming a plane with 4 rows and a maximum capacity of 14 seats in any row
    //The value 0 indicates an empty seat, and 1 indicates that the seat has been sold
    private static final int[][] SEAT_PLAN = {
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}
    };

    //Defining that 4 rows of seat labelled as A,B,C,D
    private static final String[] ROW_CHAR = {"A", "B", "C", "D"};
    //Defining array to store sold tickets information
    private static final Ticket[] SOLD_TICKETS = new Ticket[52];
    //Defining the file path to save the ticket informations
    public static final String FILE_PATH = "C:/PlaneManagement/";
    //Defining the file to save as text format
    public static final String FILE_FORMAT = ".txt";

    public static void main(String[] args) {
        System.out.println("Welcome to the Plane Management application");

        menu();
    }

    //Display the main menu options
    private static void menu() {
        System.out.println("************************************************");
        System.out.println("*                 MENU OPTIONS                 *");
        System.out.println("************************************************");
        System.out.println("\t1) Buy a seat");
        System.out.println("\t2) Cancel a seat");
        System.out.println("\t3) Find first available seat");
        System.out.println("\t4) Show seating plan");
        System.out.println("\t5) Print tickets information and total sales");
        System.out.println("\t6) Search ticket");
        System.out.println("\t0) Quit");
        System.out.println("************************************************");
        System.out.println("\n");

        //Read the user's option input
        Scanner sc = new Scanner(System.in);
        int menu;

        do {
            System.out.print("Please select an option: ");

            try {
                menu = sc.nextInt();

                if (menu >= 0 && menu <= 6) {
                    break;
                } else {
                    System.out.println("Invalid input. Please enter a valid menu option.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid menu option.");
                sc.nextLine();
            }
        } while (true);
        sc.nextLine();
        routeMenu(sc, menu);
    }

    //Defining route to different menu options based on user input
    private static void routeMenu(Scanner sc, int option) {
        switch (option) {
            case 1 -> buySeat(sc);
            case 2 -> cancelSeat(sc);
            case 3 -> findFirstAvailable();
            case 4 -> viewSeatPlan();
            case 5 -> printTicketsInfo();
            case 6 -> searchTicket(sc);
            case 0 -> {
                System.out.println("Thank you!");
                System.exit(0);
            }
            default -> throw new IllegalStateException("Unexpected value: " + option);
        }

        System.out.print("\nPress enter to continue...");
        sc.nextLine();
        menu();
    }


    //Function to handle buying the seat
    private static void buySeat(Scanner sc) {
        viewSeatPlan();
        while (true) {

            String input = enterSeatNumber(sc);

            if (input != null) {
                char rowChar = input.charAt(0);
                int row = getRow(rowChar);

                int seatNumber = Integer.parseInt(input.substring(1)) - 1;

                if (seatNumber >= 0 && seatNumber < SEAT_PLAN[row].length) {

                    if (SEAT_PLAN[row][seatNumber] == 0) {

                        Ticket ticket = setTicketDetails(sc, rowChar, seatNumber + 1);

                        SEAT_PLAN[row][seatNumber] = 1;
                        insertTicket(ticket);
                        try {
                            save(input, ticket);
                        } catch (IOException e) {
                            System.out.println("Failed to save file: " + e.getMessage());
                        }
                        System.out.println("Seat booked successfully!");
                        break;
                    } else {
                        System.out.println("Seat already booked.");
                    }
                } else {
                    System.out.println("Invalid Seat");
                }
            }
        }
    }

    //Prompt for User details to set ticket details and create a Person object
    private static Ticket setTicketDetails(Scanner sc, char rowChar, int seatNumber) {
        System.out.print("Enter your name: ");
        String name = sc.nextLine();

        System.out.print("Enter your surname: ");
        String surname = sc.nextLine();

        System.out.print("Enter your email address: ");
        String email = sc.nextLine();

        //Create a new Person object
        Person person = new Person(name, surname, email);

        return new Ticket(rowChar, seatNumber, getSeatPrice(seatNumber), person);
    }

    //Function to handle cancelling a seat
    private static void cancelSeat(Scanner sc) {
        while (true) {
            String input = enterSeatNumber(sc);

            if (input != null) {
                char rowChar = input.charAt(0);
                int row = getRow(rowChar);
                int seatNumber = Integer.parseInt(input.substring(1)) - 1;

                if (isValidSeat(row, seatNumber)) {
                    if (SEAT_PLAN[row][seatNumber] == 1) {
                        SEAT_PLAN[row][seatNumber] = 0;
                        removeTicket(rowChar, seatNumber + 1);
                        System.out.println("Seat canceled successfully!");
                        break;
                    } else {
                        System.out.println("Seat is not booked.");
                    }
                } else {
                    System.out.println("Invalid seat.");
                }
            }
        }
    }

    private static boolean isValidSeat(int row, int seatNumber) {
        return seatNumber >= 0 && seatNumber < SEAT_PLAN[row].length;
    }

    private static int getRow(char rowChar) {
        int row = -1;
        for (int i = 0; i < ROW_CHAR.length; i++) {
            if (ROW_CHAR[i].charAt(0) == rowChar) {
                row = i;
                break;
            }
        }
        return row;
    }

    //Function to find the first available seat
    public static void findFirstAvailable() {
        int availableSeat = -1;
        for (int row = 0; row < ROW_CHAR.length; row++) {
            for (int seat = 0; seat < SEAT_PLAN[row].length; seat++) {
                if (SEAT_PLAN[row][seat] == 0) {
                    availableSeat = seat + 1;
                    System.out.println("First Available Seat: " + ROW_CHAR[row] + availableSeat);
                    break;
                }
            }
            if (availableSeat != -1) break;
        }

        if (availableSeat == -1) {
            System.out.println("No Seats available!");
        }
    }

    //Function to display the seating plan
    private static void viewSeatPlan() {
        StringBuilder output = new StringBuilder();
        for (int[] ints : SEAT_PLAN) {
            for (int anInt : ints) {
                if (anInt == 1) {
                    output.append("X");
                } else {
                    output.append(anInt);
                }
            }
            output.append("\n");
        }
        System.out.println(output);

    }

    //Function to print tickets information and total sales
    private static void printTicketsInfo() {
        double totalPrice = 0.0;
        int ticket = 1;

        System.out.println("** Tickets **");
        for (Ticket soldTicket : SOLD_TICKETS) {
            if (soldTicket != null) {
                System.out.println("\nTicket " + ticket);
                System.out.println("----------");
                System.out.println(soldTicket);
                totalPrice += soldTicket.getPrice();
                ticket++;
            }
        }

        System.out.println("\nTotal Price: €" + totalPrice);
    }

    // Function to search for a ticket
    private static void searchTicket(Scanner sc) {
        while (true) {
            String input = enterSeatNumber(sc);

            if (input != null) {
                char rowChar = input.charAt(0);
                int seatNumber = Integer.parseInt(input.substring(1));
                boolean seatFound = false;
                for (Ticket soldTicket : SOLD_TICKETS) {
                    if (soldTicket != null && soldTicket.getRow().equals(rowChar) && soldTicket.getSeat() == seatNumber) {
                        System.out.println(soldTicket);
                        seatFound = true;
                        break;
                    }
                }

                if (!seatFound) System.out.println("This seat is available");
                break;
            }
        }
    }

    //Function to validate and enter a seat number
    private static String enterSeatNumber(Scanner sc) {
        System.out.print("Enter row letter (A-D) and seat number (1-14): ");
        String input = sc.nextLine().toUpperCase();
        if (input.matches("^[A-D](?:[1-9]|1[0-4])$")) {
            return input;
        } else {
            System.out.println("Invalid Seat");
            return null;
        }
    }

    //Function to get the price of a seat based on its number
    private static int getSeatPrice(int seatNumber) {
        if (seatNumber > 0 && seatNumber <= 5) {
            return 200;
        } else if (seatNumber > 5 && seatNumber <= 9) {
            return 150;
        } else {
            return 180;
        }
    }

    //Function to insert a ticket into the SOLD_TICKETS array
    private static void insertTicket(Ticket ticket) {
        for (int i = 0; i < SOLD_TICKETS.length; i++) {
            if (SOLD_TICKETS[i] == null) {
                SOLD_TICKETS[i] = ticket;
                break;
            }
        }
    }

    //Function to remove a ticket from the SOLD_TICKETS array
    private static void removeTicket(char row, int seat) {
        for (int i = 0; i < SOLD_TICKETS.length; i++) {
            if (SOLD_TICKETS[i] != null && SOLD_TICKETS[i].getRow().equals(row) && SOLD_TICKETS[i].getSeat() == seat) {
                SOLD_TICKETS[i] = null;
                break;
            }
        }
    }

    //Function to save ticket information to a file
    private static void save(String fileName, Ticket ticket) throws IOException {
        String filePath = FILE_PATH + fileName + FILE_FORMAT;

        Path path = Path.of(FILE_PATH);
        if (!Files.exists(path)) Files.createDirectory(path);

        FileWriter writer = new FileWriter(filePath);
        PrintWriter printWriter = new PrintWriter(writer);

        printWriter.println(ticket);

        printWriter.close();
    }
}
