package ipca.pdm.a6160a9710.ui.clocks

import java.util.*

class Clock {
    var id: Int? = null
    var userID: Int? = null
    var userName: String? = null
    var direction: String? = null
    var datetime: Date? = null

    constructor() { }

    constructor(id: Int?, direction: String?, datetime: Date?) {
        this.id = id
        this.direction = direction
        this.datetime = datetime
    }

    constructor(id: Int?, userID: Int?, userName: String?, direction: String?, datetime: Date?) {
        this.id = id
        this.userID = userID
        this.userName = userName
        this.direction = direction
        this.datetime = datetime
    }
}