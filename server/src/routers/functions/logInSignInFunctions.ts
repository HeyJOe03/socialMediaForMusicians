import ExpressRouterCallback from "../../@types/expressCalback"
import DB from "../../database/dbconnection"
import { selectOneUsernameQuery, selectUserLogInQuery, insertNewUserQuery} from "../../database/sql/userQueries"
import User from "../../@types/user"
import { OkPacket } from "mysql"

export const userExist : ExpressRouterCallback = (req,res) => {
    const sql = selectOneUsernameQuery(req.body.username)
    DB.query(sql, (err,results) => {
        if(err) console.log(err)
        if(results.length === 0)res.json({'exist':'false'})
        else res.json({'exist':'true'})
    })
}

export const userSignUp : ExpressRouterCallback = (req,res) => {
    let obj: User = req.body as User
    let message = inputCheck(obj)
    if (message != 'good'){
        res.status(500).send(message)
        return
    }

    //else
    const sql = insertNewUserQuery(obj)
    DB.query(sql, (err,results) => {
        if(err){
            res.status(500).send('impossible insert user')
            console.log(err)
        }
        else res.json({'insertID':(results as OkPacket).insertId})
    })
}

export const userLogIn : ExpressRouterCallback = (req,res) => {
    const sql = selectUserLogInQuery(req.body.username,req.body.pw)
    DB.query(sql, (err,results) => {
        if(err) console.log(err)
        if(results.length === 1)res.json({'signIn':results[0].id})
        else res.status(500).send('failed')
    })
}

const inputCheck = (u:User): string => {

    const usernameRegex = new RegExp(/^[a-zA-Z0-9](_(?!(\\.|_))|\\.(?!(_|\\.))|[a-zA-Z0-9]){6,18}[a-zA-Z0-9]$/)
    const emailRegex = new RegExp(/^[a-zA-Z0-9.!#$%&'*+\/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$/)
    const nameRegex = new RegExp(/^[a-zA-Z]+$/)

    if(!u.username || !u.name || !u.email || !u.hash_password) return "Missing fields"
    else if(!usernameRegex.test(u.username)) return "Error in the username sintax"
    else if (!emailRegex.test(u.email)) return "error in the email sintax"
    else if (!nameRegex.test(u.name)) return "error in the name, it mustn't contain number, spaces or symbols"
    else if (u.hash_password.length != 32) return "password must be the first 32 character of Hash256"
    else return "good"   
}