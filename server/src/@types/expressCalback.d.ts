import { Request,Response } from "express";

type ExpressRouterCallback = (req:Request,res:Response) => void

export = ExpressRouterCallback