import { deleteUserPost, getPostImgFromId, updatePost,userPosts,loadNewPost,postInfo } from "../functions/postFunctions"
import {Router} from 'express'

const postRouter = Router()
export = postRouter

postRouter.get('/:id',getPostImgFromId)

postRouter.post('/load',loadNewPost)

postRouter.post('/delete',deleteUserPost)

postRouter.post('/update',updatePost)

postRouter.post('/post',userPosts)

postRouter.post('/info',postInfo)