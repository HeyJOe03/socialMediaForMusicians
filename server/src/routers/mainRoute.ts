import express, {Request,Response} from "express";
import DB from '../database/dbconnection'
import User from "../@types/user";

import {selectUserLogIn,selectOneUsername} from '../database/sql/selectUser'
import {insertNewUser} from '../database/sql/insertUser'
import { OkPacket} from "mysql";

const mainRouter = express.Router()
export = mainRouter

mainRouter.get('/', (req: Request,res: Response) => {

    res.send(`hello I'm the server`)
    
})