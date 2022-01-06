import ExpressRouterCallback from "../../@types/expressCalback"
import DB from "../../database/dbconnection"
import { selectProfile, selectProfilePicture, selectPostsInfo, selectProfileFull} from "../../database/sql/selectUser"
import Profile from "../../@types/profile"
import { updateProfile } from "../../database/sql/update"
import { OkPacket } from "mysql"

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

    //console.log({...req.body,'profile_pic':'base64'})

    if(inputsOK != 'good') res.status(500).json({'err':inputsOK})

    else{

        const sql = updateProfile(newProfile)

        DB.query(sql,newProfile.profile_pic? Buffer.from(newProfile.profile_pic,'base64') : null,(err,result) => {
            if (err) res.status(500).send({'err':err.message})
            else{
                if((result as OkPacket).affectedRows == 1)res.status(200).json({"query":"good"})
                else res.status(500).send({"result":result})
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

export const profileFullFromID:ExpressRouterCallback = (req,res) => {
    // TODO: add check on the hash_password
    const sql = selectProfileFull(req.body.id) //select all but not the profile image
    DB.query(sql,(err,result) => {
        if(err) res.status(500).json({"error":err.message})
        else{
            if(result.length === 0) res.status(500).json({"err":"wrong password or ID"})
            else{
                const r = result[0] as Profile 
                const profile = {...r,
                    "profile_pic":(result[0].profile_pic as Buffer).toString('base64'),
                    "lat":r.lat.toString(),
                    "lon":r.lon.toString(),
                    'is_online': intToBool(result[0].is_online),
                    'is_blocked': intToBool(result[0].is_blocked),
                    "is_looking_someone_to_play_with": intToBool(result[0].is_looking_someone_to_play_with),
                }
                res.status(200).json(profile)
            }
        }
    })
}

const intToBool = (n : Number) : boolean => {
    return n == 1 ? true : false
}

export const editProfilePic : ExpressRouterCallback = (req,res) => {
    try {
        const b = Buffer.from(req.body.img as string,'base64')
        let sql = /*sql*/`UPDATE user SET profile_pic = ?`
        DB.query(sql,b,(err,result) => {
            if(err) res.status(500)//.json({"err":err.message})
            else res.status(200)
        })
    } catch (error) {
        res.status(500)
    }
}