import { deleteUserSheet, getSheetImgFromId, updateSheet,userSheets,loadNewSheet,infoSheet } from "../functions/sheetFunctions"
import {Router} from 'express'

const sheetRouter = Router()
export = sheetRouter

sheetRouter.get('/:id',getSheetImgFromId)

sheetRouter.post('/load',loadNewSheet)

sheetRouter.post('/delete',deleteUserSheet)

sheetRouter.post('/update',updateSheet)

sheetRouter.post('/',userSheets)

sheetRouter.post('/info',infoSheet)