import {Router} from "express"
import postRouter from './content/postRouter'
import sheetRouter from './content/sheetRouter'
import shopRouter from './content/shopRouter'

const dataRouter = Router()
export = dataRouter

dataRouter.use('/post',postRouter)
dataRouter.use('/sheet',sheetRouter)
dataRouter.use('/shop',shopRouter)