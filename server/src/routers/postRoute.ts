import express from "express";
import DB from "../database/dbconnection";
import { deleteUserPost, getPostImgFromId } from "./functions/postFunctions";
import { loadNewPost } from "./functions/postFunctions";

const postRouter = express.Router()
export = postRouter

type BodyUpdate = {id:Number,author:string,title:string,description:string}

postRouter.get('/:id',getPostImgFromId)

postRouter.post('/load',loadNewPost)

postRouter.post('/delete',deleteUserPost)

postRouter.post('/update',(req,res) => {
    const {id,author,title,description} : BodyUpdate = req.body

    let sql = /*sql*/ `UPDATE posts SET author = '${author}',title = '${title}', description = '${description}' WHERE id = ${id};`

    DB.query(sql,(err) => {
        if(err)res.status(500).json({'error':err.message})
        else res.status(200).json({'good':'ok'})
    })

})