import {Router} from 'express'
import {userExist, userSignUp, userLogIn} from './functions/logInSignInFunctions' 

const logInSignInRouter = Router()
export = logInSignInRouter

// log in section
logInSignInRouter.post('/userLogIn',userLogIn)

// check if an user already exists
logInSignInRouter.post('/userExist',userExist)

// register a new user
logInSignInRouter.post('/userSignUp',userSignUp)