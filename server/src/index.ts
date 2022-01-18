
// Node libraries
import path from 'path';
import { createServer } from 'http';

// Installed libraries
import 'dotenv/config'
import express from 'express';
import {Server, Socket} from 'socket.io'

// Modules
import DB from './database/dbconnection'

// Routes
import mainRouter from './routers/mainRoute';
import logInSignUpRouter from './routers/logInSignUpRouter'
import profileRouter from './routers/profileRoute';
import dataRouter from './routers/dataRoute'
import { socketHandler } from './socketFunctions';
import workRouter from './routers/workflow';

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

// SocketIO 

io.on('connection',socketHandler)

// express - requests

app.use(express.urlencoded({extended: true,limit: '5mb'}))
app.use(express.json({limit: '5mb'}))
app.use(express.static(path.join(__dirname,'../public')))

app.use('/',mainRouter)

app.use('/LogIn-SignUp',logInSignUpRouter)
app.use('/profile', profileRouter)
app.use('/data',dataRouter)
app.use('/request',workRouter)