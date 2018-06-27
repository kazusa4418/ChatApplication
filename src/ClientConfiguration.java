class ClientConfiguration {
    private String id;
    private String name;

    ClientConfiguration(String id, String name) {
        this.id = id;
        this.name = name;
    }

    String getId() {
        return id;
    }

    String getName() {
        return name;
    }
}
