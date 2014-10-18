import generator.LocationGenerator
import grails.converters.JSON
import grails.util.Environment
import inauthcodingchallange.City
import inauthcodingchallange.Location

class BootStrap {

    def init = { servletContext ->
        def currentEnv = Environment.current

        JSON.registerObjectMarshaller( Location ) { Location location ->
            return [
                    id: location.id,
                    latitude: location.latitude,
                    longitude: location.longitude
            ]
        }

        if (currentEnv == Environment.DEVELOPMENT) {
            LocationGenerator generator = new LocationGenerator()
            //generating 10k random longitude and latitude values and saving them in the database
            for (i in 1..10000) {
                Location loc = new Location()
                loc.latitude = generator.generateLatitude(5)
                loc.longitude = generator.generateLongitude(5)
                if (!loc.save(flush: true)) {
                    loc.errors.each {
                        println it
                    }
                }
            }

            City city = new City(name: "Tokyo", country: "Japan", latitude: 35.689487, longitude: 139.691706)
            if (!city.save(flush: true)) {
                city.errors.each {
                    println it
                }
            }
            city = new City(name: "Sydney", country: "Australia", latitude: -33.867487, longitude: 151.206990)
            if (!city.save(flush: true)) {
                city.errors.each {
                    println it
                }
            }
            city = new City(name: "Riyadh", country: "Saudi Arabia", latitude: 24.633333, longitude: 46.716667)
            if (!city.save(flush: true)) {
                city.errors.each {
                    println it
                }
            }
            city = new City(name: "Zurich", country: "Switzerland", latitude: 47.368650, longitude: 8.539183)
            if (!city.save(flush: true)) {
                city.errors.each {
                    println it
                }
            }
            city = new City(name: "Reykjavik", country: "Iceland", latitude: 64.133333, longitude: -21.933333)
            if (!city.save(flush: true)) {
                city.errors.each {
                    println it
                }
            }
            city = new City(name: "Mexico City", country: "Mexico", latitude: 19.432608, longitude: -99.133208)
            if (!city.save(flush: true)) {
                city.errors.each {
                    println it
                }
            }
            city = new City(name: "Lima", country: "Peru", latitude: -12.046374, longitude: -77.042793)
            if (!city.save(flush: true)) {
                city.errors.each {
                    println it
                }
            }


        } else if (currentEnv == Environment.TEST) {
            // do custom init for test here

        } else if (currentEnv == Environment.PRODUCTION) {
            // do custom init for prod here
        }
    }
    def destroy = {
    }
}
