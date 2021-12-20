import express, {Request,Response} from "express"
const mainRouter = express.Router()
export = mainRouter

mainRouter.get('/', async (req: Request,res: Response) => {

        
    // const buf = Buffer.from(newIMG,'base64')

    // DB.query('SET FOREIGN_KEY_CHECKS=0')
    // DB.query(/*sql*/`INSERT INTO posts (description,content,posted_by,author,title) VALUE ("first sax post",?,42,42,"SexySax")`,buf)

    //DB.query(/*sql*/`INSERT INTO aboutme (id,description,profile_image,instrument_interested_in) VALUE(44,"test",?,"tutti")`,buf)

    res.send(`hello I'm the server`)
})