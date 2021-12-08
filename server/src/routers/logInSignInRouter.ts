import {Router} from 'express'
import {userExist, userSignIn, userLogIn} from './functions/logInSignInFunctions' 

const logInSignInRouter = Router()
export = logInSignInRouter

// log in section
logInSignInRouter.post('/userLogin',userLogIn)

// check if an user already exists
logInSignInRouter.post('/userExist',userExist)

// register a new user
logInSignInRouter.post('/userSignIn',userSignIn)