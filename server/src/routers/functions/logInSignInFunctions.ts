import ExpressRouterCallback from "../../@types/expressCalback"
import DB from "../../database/dbconnection"
import { selectOneUsername, selectUserLogIn} from "../../database/sql/selectUser"
import User from "../../@types/user"
import { OkPacket } from "mysql"
import { insertNewUser } from "../../database/sql/insertUser"

export const userExist : ExpressRouterCallback = (req,res) => {
    let query: string = selectOneUsername(req.body.username)
    DB.query(query, (err,results) => {
        if(err) console.log(err)
        if(results.length === 0)res.json({'exist':'false'})
        else res.json({'exist':'true'})
    })
}

export const userSignUp : ExpressRouterCallback = (req,res) => {
    let obj: User = req.body as User
    let message = inputCheck(obj)
    if (message != 'good'){
        res.json({'error':message})
        return
    }

    //else
    let sql = insertNewUser(obj)
    DB.query(sql, (err,results) => {
        if(err) res.json({'error':'good','insertID':-1})
        else res.json({'error':'good','insertID':(results as OkPacket).insertId})
    })
}

export const userLogIn : ExpressRouterCallback = (req,res) => {
    let query: string = selectUserLogIn(req.body.username,req.body.pw)
    DB.query(query, (err,results) => {
        if(err) console.log(err)
        if(results.length === 1)res.json({'signIn':results[0].id})
        else res.json({'signIn':'failed'})
    })
}

const inputCheck = (u:User): string => {

    const usernameRegex = new RegExp(/^[a-zA-Z0-9](_(?!(\\.|_))|\\.(?!(_|\\.))|[a-zA-Z0-9]){6,18}[a-zA-Z0-9]$/)
    const emailRegex = new RegExp(/^[a-zA-Z0-9.!#$%&'*+\/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$/)
    const nameRegex = new RegExp(/^[a-zA-Z]+$/)

    if(!u.username || !u.name || !u.email || !u.hash_password || null == u.created_at || null == u.last_profile_update || null == u.last_post || null == u.last_music_sheet || null == u.last_instrument_offer) return "Missing fields"
    else if(!usernameRegex.test(u.username)) return "Error in the username sintax"
    else if (!emailRegex.test(u.email)) return "error in the email sintax"
    else if (!nameRegex.test(u.name)) return "error in the name, it mustn't contain number, spaces or symbols"
    else if (u.hash_password.length != 32) return "password must be the first 32 character of Hash256"
    else if (
        u.created_at < 0 ||
        u.last_profile_update < 0 ||
        u.last_post < 0 ||
        u.last_instrument_offer < 0 ||
        u.last_music_sheet < 0
    ) return "error in timestamps"
    else return "good"   
}