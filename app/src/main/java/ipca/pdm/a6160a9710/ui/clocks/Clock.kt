package ipca.pdm.a6160a9710.ui.clocks

import java.util.*

class Clock {
    var id: Int? = null
    var userName: String? = null
    var direction: String? = null
    var datetime: Date? = null

    constructor(id: Int?, direction: String?, datetime: Date?) {
        this.id = id
        this.direction = direction
        this.datetime = datetime
    }
}