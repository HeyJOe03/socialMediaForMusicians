import express from "express"
import { deleteUserPost, getPostImgFromId, updatePost,userPosts,loadNewPost,postInfo } from "./functions/postFunctions"

const postRouter = express.Router()
export = postRouter

postRouter.get('/post/:id',getPostImgFromId)

postRouter.post('/load',loadNewPost)

postRouter.post('/delete',deleteUserPost)

postRouter.post('/update',updatePost)

postRouter.post('/post',userPosts)

postRouter.post('/post/info',postInfo)