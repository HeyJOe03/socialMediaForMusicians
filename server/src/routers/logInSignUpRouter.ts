import {Router} from 'express'
import {userExist, userSignUp, userLogIn} from './functions/logInSignInFunctions' 

const logInSignUpRouter = Router()
export = logInSignUpRouter

// log in section
logInSignUpRouter.post('/userLogIn',userLogIn)

// check if an user already exists
logInSignUpRouter.post('/userExist',userExist)

// register a new user
logInSignUpRouter.post('/userSignUp',userSignUp)