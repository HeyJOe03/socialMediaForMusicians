
import 'dotenv/config'
import bodyParser from 'body-parser'
import express from 'express';
import socketio from 'socket.io'
import mysql from 'mysql'
import options from './dbconnection'
import mainRouter from './routers/mainRoute';

const app = express();
const http = require('http').Server(app);
const io = new socketio.Server(http);
const PORT = process.env.PORT || 3000

const DB = mysql.createConnection(process.env.DB_URI || options).connect((err) => {
    if(err) console.log(err)
    else {
        console.log(`> DB connected`)
        app.listen(PORT, () => console.log(`> Server listening on PORT: ${PORT}`))
    }
})

io.on('connection',(socket) => {
    console.log(`user ${socket.id} has connected`)
    socket.on('disconnect', () => {
        console.log(`user ${socket.id} has disconnected`)
    })
})

app.use(bodyParser.urlencoded({extended:true}))
app.use(bodyParser.json())
app.use('/',mainRouter)