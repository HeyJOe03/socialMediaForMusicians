import express from "express";
import { deleteUserPost, getPostImgFromId, updatePost,userPosts } from "./functions/postFunctions";
import { loadNewPost } from "./functions/postFunctions";

const postRouter = express.Router()
export = postRouter

postRouter.get('/:id',getPostImgFromId)

postRouter.post('/load',loadNewPost)

postRouter.post('/delete',deleteUserPost)

postRouter.post('/update',updatePost)

postRouter.post('/post',userPosts)