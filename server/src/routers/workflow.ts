import {Router} from "express";
import { followRequest,unFollowRequest } from "./functions/workFunctions";

const workRouter = Router()
export = workRouter

workRouter.post('/follow',followRequest)