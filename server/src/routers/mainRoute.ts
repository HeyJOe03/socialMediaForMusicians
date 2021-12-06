import express, {Request,Response} from "express";
import DB from '../database/dbconnection'

import selectUserLogIn from '../database/sql/selectUser'

const mainRouter = express.Router()

mainRouter.get('/', (req: Request,res: Response) => {
    res.send(`hello I'm the server`)
})

mainRouter.post('/login',(req:Request,res:Response) => {

    let query: string = selectUserLogIn(req.body.username,req.body.pw)

    DB.query(query, (err,results) => {
        if(err) console.log(err)
        if(results.length === 1)res.json({'signIn':results[0].id})
        else res.json({'signIn':'failed'})
    })
    
})

mainRouter.post('/user/exist',(req,res) => {
    let query: string = /*sql*/`SELECT username from user WHERE username='${req.body.username}'`
    DB.query(query, (err,results) => {
        if(err) console.log(err)
        if(results.length === 0)res.json({'exist':'false'})
        else res.json({'exist':'true'})
    })
})

mainRouter.post('/user/insert',(req,res) => {
    console.log(req.body)
    
    //let query: string = /*sql*/`SELECT username from user WHERE username='${req.body.username}'`
    /*DB.query(query, (err,results) => {
        if(err) console.log(err)
        if(results.length === 0)res.json({'exist':'false'})
        else res.json({'exist':'true'})
    })*/

    res.json({'inserted?':true})

})

export default mainRouter