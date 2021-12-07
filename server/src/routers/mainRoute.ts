import express, {Request,Response} from "express";
import DB from '../database/dbconnection'

import {selectUserLogIn,selectOneUsername} from '../database/sql/selectUser'
import {insertNewUser} from '../database/sql/insertUser'   

const mainRouter = express.Router()

mainRouter.get('/', (req: Request,res: Response) => {

    let obj = {
        username : 'HeyJOe_5',
        name: 'giovanni',
        email : 'jocarmi03@gmail.com',
        hash_password : '78329',
        is_looking_someone_to_play_with: true,
        created_at: 0,
        last_profile_update: 0,
        last_post: 0,
        last_instrument_offer: 0,
        last_music_sheet: 0
    }

    insertNewUser(obj)

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
    let query: string = selectOneUsername(req.body.username)
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