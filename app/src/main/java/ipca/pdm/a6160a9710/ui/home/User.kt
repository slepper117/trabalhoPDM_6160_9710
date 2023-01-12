package ipca.pdm.a6160a9710.ui.home

import ipca.pdm.a6160a9710.ui.areas.Area
import ipca.pdm.a6160a9710.ui.bookings.Booking
import ipca.pdm.a6160a9710.ui.clocks.Clock

class User {
    var name: String? = null
    var role: String? = null
    var userAreas: ArrayList<Area>? = null
    var userClocks: ArrayList<Clock>? = null
    var userBookings: ArrayList<Booking>? = null

    constructor() { }

    constructor( name: String?, role: String?) {
        this.name = name
        this.role = role
    }

    constructor(
        name: String?,
        role: String?,
        userAreas: ArrayList<Area>?,
        userClocks: ArrayList<Clock>?,
        userBookings: ArrayList<Booking>?
    ) {
        this.name = name
        this.role = role
        this.userAreas = userAreas
        this.userClocks = userClocks
        this.userBookings = userBookings
    }
}