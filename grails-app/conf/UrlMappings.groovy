class UrlMappings {



	static mappings = {
        "/$controller/$action?/$id?(.$format)?"{
            constraints {
                // apply constraints here
            }
        }

        "/"(view:"/index")
        "500"(view:'/error')

//        "/getAllDataSets"(resources:"location", excludes:['delete', 'update', 'show'])
        "/getAllDataSets"(controller: "location", action: "listAll")
        "/getData/$latitude/$longitude"(controller: "location", action: "showLoc")
        "/addData"(controller: "location", parseRequest: true) {
            action = [POST: "addData"]
        }

	}
}
