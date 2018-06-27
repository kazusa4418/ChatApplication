class Checker {
    static boolean idFormatCheck(String id) {
        return id.matches("[0-9a-zA-Z_\\-]{4,32}");
    }

    static boolean pwFormatCheck(String password) {
        return password.matches("[0-9a-zA-Z]{4,32}");
    }
}