import {Router} from "express"
import postRouter from './postRouter'
import sheetRouter from './sheetRouter'
import shopRouter from './shopRouter'

const dataRouter = Router()
export = dataRouter

dataRouter.use('/post',postRouter)
dataRouter.use('/sheet',sheetRouter)
dataRouter.use('/shop',shopRouter)