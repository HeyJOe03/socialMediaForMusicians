import ExpressRouterCallback from "../../@types/expressCalback"
import DB from "../../database/dbconnection"
import { selectProfile, selectProfilePicture, selectPostsInfo, selectOneUsername} from "../../database/sql/selectUser"
import Profile from "../../@types/profile"
import { Response } from "express"
import { updateProfile } from "../../database/sql/update"
import { OkPacket, queryCallback } from "mysql"

export const profileFromID:ExpressRouterCallback = (req,res) => {
    if(!req.body.id)res.status(500)
    else{
        let id = -1
        if(typeof(req.body.id) != 'number'){
            try{
                id = parseInt(req.body.id)
            } catch(e){
                res.status(500)
                return
            }
        } else id = req.body.id
        let sql = selectProfile(id)
        
        DB.query(sql, (err,result) => {
            if(err){
                res.status(500)
                console.log(err)
            } 
            else if( result[0] == undefined) res.status(500).send("no result")
            else res.json(result[0])
            /*{
               // const b64 = Buffer.from(result[0].profile_pic).toString('base64')
                //res.json({...result[0],'profile_pic':b64})
                res.json(result[0])
            }*/
        })
    }
}

export const profileImageFromID:ExpressRouterCallback =(req,res) => {
    
    const id = parseInt(req.params.id)
    // console.log(id)
    if(isNaN(id)) {
        res.status(500).send("id error")
        return
    }

    //console.log(id)

    const sql = selectProfilePicture(id)

    DB.query(sql, (err,result) => {

        if (err) res.status(500).send("Db error")
        else if( result[0] == undefined) res.status(500).send("no result")
        else{
            // console.log(result)
            res.writeHead(200, {
                'Content-Type': 'image/png',
                'Content-Length': (result[0].profile_pic as Buffer).length
            });
            res.end(result[0].profile_pic as Buffer)
        }
    })

}

export const userPosts: ExpressRouterCallback = (req,res) => {

    if(req.body.id == undefined || isNaN(parseInt(req.body.id))) res.status(500).json({'error':'id not provided'})

    else{
        let id = parseInt(req.body.id)
        let sql = selectPostsInfo(id)
        DB.query(sql,(err,result) => {
            if(err) res.status(500).json({'error':"query error"})
            else res.json({"result":result})   
        })
    }

}

export const profileEdit: ExpressRouterCallback = (req,res) => {
    const newProfile = req.body as Profile

    const inputsOK: string = inputCheck(newProfile)

    if(inputsOK != 'good') res.status(500).json({'err':inputsOK})

    else{

        const sql = updateProfile(newProfile)
        DB.query(sql,newProfile.profile_pic? Buffer.from(newProfile.profile_pic,'base64') : null,(err,result) => {
            if (err) res.status(500).json({'err':err.message})
            else{
                if((result as OkPacket).affectedRows == 1)res.status(200).json({"query":"good"})
                else res.status(500).json({"result":result})
            }
        })


    }

}

const inputCheck = (p:Profile): string => {

    const usernameRegex = new RegExp(/^[a-zA-Z0-9](_(?!(\\.|_))|\\.(?!(_|\\.))|[a-zA-Z0-9]){6,18}[a-zA-Z0-9]$/)
    const emailRegex = new RegExp(/^[a-zA-Z0-9.!#$%&'*+\/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$/)
    const nameRegex = new RegExp(/^[a-zA-Z]+$/)

    if(!p.username || !p.name || !p.email //|| !p.hash_password 
        || p.is_looking_someone_to_play_with == null || p.lat == null 
        || p.lon == null || p.description == null 
        || p.instrument_interested_in == null //|| p.profile_pic == null
        ) return "Missing fields"
    else if(!usernameRegex.test(p.username)) return "Error in the username sintax"
    else if (!emailRegex.test(p.email)) return "error in the email sintax"
    else if (!nameRegex.test(p.name)) return "error in the name, it mustn't contain number, spaces or symbols"
    //else if (p.hash_password.length != 32) return "password must be the first 32 character of Hash256"
    else return "good"   
}

export const postMyProfile:ExpressRouterCallback = (req,res) => {
    // TODO: add check on the hash_password
    const sql = /*sql*/`SELECT * FROM user WHERE id = ${req.body.id}`
    DB.query(sql,(err,result) => {
        if(err) res.status(500).json({"error":err.message})
        else{
            if(result.length === 0) res.status(500).json({"err":"wrong password or ID"})
            else{
                const profile = {...result[0],"profile_pic":(result[0].profile_pic as Buffer).toString('base64'),"lat":result[0].lat.toString(),"lon":result[0].lon.toString()}
                res.status(200).json(profile)
            }
        }
    })
}