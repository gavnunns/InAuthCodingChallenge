package inauthcodingchallange

class City {
    String name
    String country
    double latitude
    double longitude

    static constraints = {
        latitude(nullable: false)
        longitude(nullable: false)
        name(nullable: false)
        country(nullable: false)
    }
}
