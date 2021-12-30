import ExpressRouterCallback from "../../@types/expressCalback"
import DB from "../../database/dbconnection"
import { selectProfile, selectProfilePicture, selectPostsInfo} from "../../database/sql/selectUser"

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
        console.log(sql)
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