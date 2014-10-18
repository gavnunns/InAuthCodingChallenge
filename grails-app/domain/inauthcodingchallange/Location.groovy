package inauthcodingchallange

class Location {

    double latitude;
    double longitude;

    static constraints = {
        latitude(nullable: false)
        longitude(nullable: false)
    }
}
