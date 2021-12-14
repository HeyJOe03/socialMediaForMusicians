import {Router} from "express";
import { profileFromID,profileImageFromID } from "./functions/profileFunctions";

const profileRouter = Router()
export = profileRouter

profileRouter.post('/fromID',profileFromID)

profileRouter.get('/img/:id',profileImageFromID)