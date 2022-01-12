import { deleteUserPost, getPostImgFromId, updatePost,userPosts,loadNewPost,postInfo } from "./functions/shopFunctions"
import {Router} from 'express'

const shopRouter = Router()
export = shopRouter

shopRouter.get('/:id',getPostImgFromId)

shopRouter.post('/load',loadNewPost)

shopRouter.post('/delete',deleteUserPost)

shopRouter.post('/update',updatePost)

shopRouter.post('/post',userPosts)

shopRouter.post('/info',postInfo)