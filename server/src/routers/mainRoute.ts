import express, {Request,Response} from "express";

const mainRouter = express.Router()
export = mainRouter

mainRouter.get('/', (req: Request,res: Response) => {

    res.send(`hello I'm the server`)
    
})