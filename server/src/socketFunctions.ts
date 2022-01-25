import { Socket } from "socket.io"
import DB from "./database/dbconnection"
import { searchUserQuery } from "./database/sql/searchQuery"

let mSocket: Socket
let mId: Number

export const socketHandler = (socket: Socket) => {
    mSocket = socket
    console.log(`user ${mSocket.id} has connected`)

    socket.on('myID',(id: Number) => myIDEvent(id))

    socket.on('disconnect',() => disconnectEvent())
    socket.on('search-user',(search: string) => searchUser(search))
}

const myIDEvent = (id : Number) => {
    mId = id
}


const disconnectEvent = () => {
    console.log(`user ${mSocket.id} has disconnected`)

    DB.query(/*sql*/`UPDATE user SET last_time_online = CURRENT_TIMESTAMP WHERE id = ${mId}`, (err) => err ? console.log(err.message) : "" )
}

const searchUser = (search: string) => {
    if(search == '') return
    const sql = searchUserQuery(search)
    DB.query(sql,(err,result) => {
        if(err) mSocket.emit('search-error',err.message)
        else mSocket.emit('search-user',result)
    })
}