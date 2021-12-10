import ExpressRouterCallback from "../../@types/expressCalback"
import DB from "../../database/dbconnection"
import { selectProfile} from "../../database/sql/selectUser"

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
            if(err) res.status(500)
            else{
                // console.log(result[0].profile_image)
                //const image = Buffer.from(result[0].profile_image).toString("base64")
                //res.json({...result[0],profile_image:image})

                const image = Buffer.from((result[0].profile_image as Buffer).toJSON().data).toString('base64')
                res.json({...result[0], profile_image: 'data:image/jpeg;base64,' + image})
            }
        })
    }
}