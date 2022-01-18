import {Router} from "express";
import { followRequest,unFollowRequest,alreadyFollowRequest, countFollowerAndFollowed } from "./functions/workFunctions";

const workRouter = Router()
export = workRouter

workRouter.post('/follow',followRequest)
workRouter.post('/unfollow',unFollowRequest)
workRouter.post('/alreadyFollow',alreadyFollowRequest)
workRouter.post('/countFollow',countFollowerAndFollowed)