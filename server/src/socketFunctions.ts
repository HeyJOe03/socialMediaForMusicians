import { Socket } from "socket.io"
import DB from "./database/dbconnection"
import { searchUserQuery } from "./database/sql/searchQuery"

let mSocket: Socket

export const socketHandler = (socket: Socket) => {
    mSocket = socket
    console.log(`user ${mSocket.id} has connected`)


    socket.on('disconnect',() => disconnectEvent())
    socket.on('search-user',(search: string) => searchUser(search))
}


const disconnectEvent = () => {
    console.log(`user ${mSocket.id} has disconnected`)
}

const searchUser = (search: string) => {
    console.log(search)

    if(search == '') return
    const sql = searchUserQuery(search)
    DB.query(sql,(err,result) => {
        if(err) mSocket.emit('search-error',err.message)
        else mSocket.emit('search-user',result)
    })
}