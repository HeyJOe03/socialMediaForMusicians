import { deleteUserShop, getShopImgFromId, updateShop,userShops,loadNewShop,infoShop } from "../functions/shopFunctions"
import {Router} from 'express'

const shopRouter = Router()
export = shopRouter

shopRouter.get('/:id',getShopImgFromId)

shopRouter.post('/load',loadNewShop)

shopRouter.post('/delete',deleteUserShop)

shopRouter.post('/update',updateShop)

shopRouter.post('/',userShops)

shopRouter.post('/info',infoShop)