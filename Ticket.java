public class Ticket {
    //Variables to store ticket information
    private Character row;
    private Integer seat;
    private Integer price;
    private Person person;

    //Constructor to initialize Ticket object
    public Ticket(Character row, Integer seat, Integer price, Person person) {
        this.row = row;
        this.seat = seat;
        this.price = price;
        this.person = person;
    }

    //Getter method for row
    public Character getRow() {
        return row;
    }

    //Setter method for row
    public void setRow(Character row) {
        this.row = row;
    }

    //Getter method for seat
    public Integer getSeat() {
        return seat;
    }

    //Getter method for seat
    public void setSeat(Integer seat) {
        this.seat = seat;
    }

    //Getter method for price
    public Integer getPrice() {
        return price;
    }

    //Setter method for price
    public void setPrice(Integer price) {
        this.price = price;
    }

    //Getter method for person
    public Person getPerson() {
        return person;
    }

    //Setter method for person
    public void setPerson(Person person) {
        this.person = person;
    }

    // Override toString method to provide a formatted string representation of Ticket
    @Override
    public String toString() {
        return "Row: " + row + "\n" +
                "Seat: " + seat + "\n" +
                "Price: €" + price + "\n" +
                "- " + person;
    }
}
