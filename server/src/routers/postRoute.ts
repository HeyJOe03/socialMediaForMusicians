import express from "express";
import DB from "../database/dbconnection";
import { selectPostPicture } from "../database/sql/selectUser";
import { insertNewPost } from "../database/sql/insertUser";
import Post from "../@types/post";

const postRouter = express.Router()
export = postRouter

postRouter.get('/:id',(req,res) => {
    const id = parseInt(req.params.id)

    if(isNaN(id)) res.send('err')
    else{
        const sql = selectPostPicture(id)
        DB.query(sql,(err,result) => {
            if(err)res.send(err)
            else{
                res.writeHead(200, {
                    'Content-Type': 'image/png',
                    'Content-Length': (result[0].content as Buffer).length
                });
                res.end(result[0].content as Buffer)
            }
        })
    }
})

postRouter.post('/load',(req,res) => {
    const sql = insertNewPost(req.body as Post)
    console.log(sql)
    const b = Buffer.from((req.body as Post).content,'base64')
    DB.query(sql,b,(err) => {
        if(err)res.json({'error':err.message})
        else res.json({'ok':'ok'})
    })
})