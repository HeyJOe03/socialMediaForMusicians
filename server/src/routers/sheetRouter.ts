import { deleteUserPost, getPostImgFromId, updatePost,userPosts,loadNewPost,postInfo } from "./functions/sheetFunctions"
import {Router} from 'express'

const sheetRouter = Router()
export = sheetRouter

sheetRouter.get('/:id',getPostImgFromId)

sheetRouter.post('/load',loadNewPost)

sheetRouter.post('/delete',deleteUserPost)

sheetRouter.post('/update',updatePost)

sheetRouter.post('/post',userPosts)

sheetRouter.post('/info',postInfo)