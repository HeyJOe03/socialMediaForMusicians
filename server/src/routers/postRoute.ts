import express from "express";
import DB from "../database/dbconnection";
import { selectPostPicture } from "../database/sql/selectUser";
import { insertNewPost } from "../database/sql/insertUser";
import Post from "../@types/post";
import { getPostImgFromId } from "./functions/postFunctions";
import { loadNewPost } from "./functions/postFunctions";

import { deletePost } from "../database/sql/delete";

const postRouter = express.Router()
export = postRouter

postRouter.get('/:id',getPostImgFromId)

postRouter.post('/load',loadNewPost)

postRouter.post('/delete',(req,res) => {
    const id = parseInt(req.body.id)
    if(isNaN(id)) res.status(500).send({'error':'missing id'})

    else{
        DB.query(deletePost(id),(err) => {
            if(err) res.status(500).json({'error':'error'})
            else res.status(200).json(id)
        })
    }
})