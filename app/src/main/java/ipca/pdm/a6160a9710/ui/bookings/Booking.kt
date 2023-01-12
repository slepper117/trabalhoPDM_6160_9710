package ipca.pdm.a6160a9710.ui.bookings

import java.util.*

class Booking {
    var id: Int? = null
    var userID: Int? = null
    var userName: String? = null
    var roomID: Int? = null
    var roomName: String? = null
    var startDate : Date? = null
    var finalDate : Date? = null
    var description : String? = null
    var validated : Boolean? = null

    constructor(id: Int?, startDate: Date?, description: String?) {
        this.id = id
        this.startDate = startDate
        this.description = description
    }

    constructor(id: Int?, startDate: Date?, finalDate: Date?, description: String?, validated: Boolean?) {
        this.id = id
        this.startDate = startDate
        this.finalDate = finalDate
        this.description = description
        this.validated = validated
    }

    constructor(id: Int?,
                userID: Int?,
                userName: String?,
                roomID: Int?,
                roomName: String?,
                startDate: Date?,
                finalDate: Date?,
                description: String?,
                validated: Boolean?
    ) {
        this.id = id
        this.userID = userID
        this.userName = userName
        this.roomID = roomID
        this.roomName = roomName
        this.startDate = startDate
        this.finalDate = finalDate
        this.description = description
        this.validated = validated
    }
}

