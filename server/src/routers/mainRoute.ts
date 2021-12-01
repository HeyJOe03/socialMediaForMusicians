import express, {Request,Response, RequestHandler} from "express";

const mainRouter = express.Router()

mainRouter.get('/', (req: Request,res: Response) => {
    res.send(`hello I'm the server`)
})

export default mainRouter