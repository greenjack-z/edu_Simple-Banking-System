package banking;

public class Menu {
    private final Main main;

    public Menu(Main main) {
        this.main = main;
    }

    enum Item {
        BALANCE("Balance"),
        CREATE("Create an account"),
        DELETE("Close account"),
        EXIT("Exit"),
        INCOME("Add income"),
        LOGIN("Log into account"),
        LOGOUT("Log out"),
        TRANSFER("Do transfer");

        final String title;

        Item(String title) {
            this.title = title;
        }

        @Override
        public String toString() {
            return title;
        }
    }

    enum Page {
        WELCOME (new Item[]{Item.EXIT, Item.CREATE, Item.LOGIN}),
        ACCOUNT (new Item[]{Item.EXIT, Item.BALANCE, Item.INCOME, Item.TRANSFER, Item.DELETE, Item.LOGOUT});

        final Item[] items;
        Page(Item[] items) {
            this.items = items;
        }
    }

    Page currentPage = Page.WELCOME;

    void print() {
        System.out.println();
        for (int i = 1; i < currentPage.items.length; i++) {
            System.out.printf("%d. %s%n", i, currentPage.items[i]);
        }
        System.out.printf("0. %s%n%n", currentPage.items[0]);
    }

    void choose(int choice) {
        switch (currentPage.items[choice]) {
            case EXIT -> main.exit();
            case LOGIN -> main.login();
            case LOGOUT -> main.logout();
            case CREATE -> main.createAccount();
            case BALANCE -> main.balance();
            case INCOME -> main.income();
            case TRANSFER -> main.transfer();
            case DELETE -> main.delete();
        }
    }
}
