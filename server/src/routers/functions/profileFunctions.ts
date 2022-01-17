import ExpressRouterCallback from "../../@types/expressCalback"
import DB from "../../database/dbconnection"
import { selectProfileQuery, selectProfilePictureQuery, selectProfileFullQuery, updateProfileQuery} from "../../database/sql/userQueries"
import Profile from "../../@types/profile"
import { OkPacket } from "mysql"

export const profileFromID:ExpressRouterCallback = (req,res) => {
    if(!req.body.id)res.status(500).send("missing ID")
    else{
        let id = -1
        if(typeof(req.body.id) != 'number'){
            try{
                id = parseInt(req.body.id)
            } catch(e){
                res.status(500).send((e as Error).message)
                return
            }
        } else id = req.body.id
        const sql = selectProfileQuery(id)
        
        DB.query(sql, (err,result) => {
            if(err){
                res.status(500).send(err.message)
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

    const sql = selectProfilePictureQuery(id)

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

export const profileEdit: ExpressRouterCallback = (req,res) => {
    const newProfile = req.body as Profile

    const inputsOK: string = inputCheck(newProfile)

    if(inputsOK != 'good') res.status(500).send(inputsOK)

    else{

        const sql = updateProfileQuery(newProfile)

        DB.query(sql,(err,result) => {
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
        || p.is_looking_someone_to_play_with == null
        || p.description == null 
        || p.instrument_interested_in == null //|| p.profile_pic == null
        ) return "Missing fields"
    else if(!usernameRegex.test(p.username)) return "Error in the username sintax"
    else if (!emailRegex.test(p.email)) return "error in the email sintax"
    else if (!nameRegex.test(p.name)) return "error in the name, it mustn't contain number, spaces or symbols"
    else if(p != null && (p.lat!! > 90 || p.lat!! < -90 || p.lon!! > 180 || p.lon!! < -180)) return "invalid coords"
    //else if (p.hash_password.length != 32) return "password must be the first 32 character of Hash256"
    else return "good"   
}

export const profileFullFromID:ExpressRouterCallback = (req,res) => {
    // TODO: add check on the hash_password
    
    const sql = selectProfileFullQuery(req.body.id) //select all but not the profile image
    //console.log(sql)
    DB.query(sql,(err,result) => {
        if(err) res.status(500).send(err.message)
        else{
            if(result.length === 0) res.status(500).send("wrong password or ID")
            else{
                const r = result[0] as Profile 
                const profile = {...r,
                    "lat":r.lat,
                    "lon":r.lon,
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
    //console.log(req.body)
    try {
        const b = Buffer.from(req.body.img as string,'base64')
        const sql = /*sql*/`UPDATE user SET profile_pic = ? WHERE id = ${req.body.id}`
        DB.query(sql,b,(err) => {
            if(err) res.status(500).json({'error':err.message})
            else res.status(200).json({'result':'image inserted'})
        })
    } catch (e: any) {
        res.status(500).json({'error':(e as Error).message})
    }
}