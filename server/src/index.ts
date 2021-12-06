
import 'dotenv/config'
import express from 'express';
import {Server} from 'socket.io'
import mysql from 'mysql'
import options from './dbconnection'
import mainRouter from './routers/mainRoute';
import path from 'path';
import { createServer } from 'http';

const app = express();
const httpServer = createServer(app)
const io = new Server(httpServer);
const PORT = process.env.PORT || 3000

const DB = mysql.createConnection(process.env.DB_URI || options).connect((err) => {
    if(err) console.log(err)
    else {
        console.log(`> DB connected`)
        httpServer.listen(PORT, () => console.log(`> Server listening on PORT: ${PORT}`))
    }
})

io.on('connection',(socket) => {
    console.log(`user ${socket.id} has connected`)
    socket.on('disconnect', () => {
        console.log(`user ${socket.id} has disconnected`)
    })
})

app.use(express.urlencoded({extended:true}))
app.use(express.json())
app.use(express.static(path.join(__dirname,'../test')))
app.use('/',mainRouter)