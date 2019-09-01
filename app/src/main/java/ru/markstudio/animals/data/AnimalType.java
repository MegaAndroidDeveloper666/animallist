package ru.markstudio.animals.data;

public enum AnimalType {
    CAT("cat", "Котейки"),
    DOG("dog", "Собаки");

    private final String query;
    private final String title;

    AnimalType(String query, String title) {
        this.query = query;
        this.title = title;
    }

    public static AnimalType getByQuery(String query) {
        for (AnimalType type: AnimalType.values()) {
            if (type.query.equals(query)) {
                return type;
            }
        }
        return null;
    }

    public String getQuery() {
        return query;
    }

    public String getTitle() {
        return title;
    }
}
