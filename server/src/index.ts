
// Node libraries
import path from 'path';
import { createServer } from 'http';
// Installed libraries
import 'dotenv/config'
import express, { RequestHandler } from 'express';
import {Server} from 'socket.io'
// Modules
import DB from './database/dbconnection'
import mainRouter from './routers/mainRoute';
import logInSignInRouter from './routers/logInSignInRouter'

// define consts
const app = express();
const httpServer = createServer(app)
const io = new Server(httpServer);
const PORT = process.env.PORT || 3000

//connect Database ] run server
DB.connect((err) => {
    if(err) console.log(err)
    else {
        console.log(`> DB connected`)
        httpServer.listen(PORT, () => console.log(`> Server listening on PORT: ${PORT}`))
    }
})

export = DB

// SocketIO 

io.on('connection',(socket) => {
    console.log(`user ${socket.id} has connected`)
    socket.on('disconnect', () => {
        console.log(`user ${socket.id} has disconnected`)
    })
})

// express - requests
app.use(express.urlencoded({extended:true}))
app.use(express.json())
app.use(express.static(path.join(__dirname,'../public')))

app.use('/',mainRouter)

app.use('/LogIn-SignUp',logInSignInRouter)