import {Router} from "express";
import { profileFromID,profileImageFromID,profileEdit, profileFullFromID, editProfilePic } from "./functions/profileFunctions";

const profileRouter = Router()
export = profileRouter

profileRouter.post('/fromID',profileFromID)

profileRouter.get('/img/:id',profileImageFromID)

profileRouter.post('/edit',profileEdit)

profileRouter.post('/myProfile',profileFullFromID)

profileRouter.post('/editpic', editProfilePic)