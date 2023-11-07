public enum Message {
    TYPES("\"What type of sort would you want to be excuted? Type:\n\tsingle\t\tfor a single threaded " +
        "quicksort\n\tmulti\t\tfor multithreaded quicksort\n\tboth\t\tfor execution of both + comparison in " +
        "time\n\tquit\t\tto exit the application\n"),
    COUNT("Enter count of elements: "), DATA("Element"), SINGLE("single"), MULTI("multi"), BOTH("both");

    String msg;

    Message(String msg) {
        this.msg = msg;
    }

    void print() {
        System.out.print(msg);
    }
}
