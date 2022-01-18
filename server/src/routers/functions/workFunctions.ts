import ExpressRouterCallback from "../../@types/expressCalback";
import DB from "../../database/dbconnection";

export const followRequest : ExpressRouterCallback = (req,res) => {
    //TODO: add check password

    const sql1: string =/*sql*/ `SELECT COUNT(*) FROM user WHERE id IN (${req.body.follower},${req.body.followed})`
    DB.query(sql1,(err,result) => {
        if(err) res.status(500).json({'error':err.message})
        else{
            if(result[0]['COUNT(*)'] !== 2) res.status(500).json({'error':'user doesn\'t exist'})
            else{
                const sql2: string = /*sql*/`SELECT id FROM follow WHERE follower = ${req.body.follower} AND followed =${req.body.followed}`
                DB.query(sql2,(err,result) => {
                    if(err)res.status(500).json({'error':err.message})
                    else if((result as any[]).length !== 0) res.status(500).json({'error':'already follow'})
                    else{
                        const sql3: string = /*sql*/`INSERT INTO follow (follower,followed) VALUES (${req.body.follower},${req.body.followed})`
                        DB.query(sql3,(err,result) => {
                            if(err)res.status(500).json({'error':err.message})
                            else res.status(200).json({'result':'successfull'})
                        })
                    }
                })
            }
        }
    })
}

export const unFollowRequest: ExpressRouterCallback = (req,res) => {
    const sql = /*sql*/`DELETE FROM follow WHERE followed = ${req.body.followed} AND follower = ${req.body.follower}`
    DB.query(sql,(err) => {
        if(err)res.status(500).json({'error':err.message})
        else res.status(200).json({'good':'deleted'})
    })
}

export const alreadyFollowRequest: ExpressRouterCallback = (req,res) => {
    const sql = /*sql*/`SELECT id FROM follow WHERE followed = ${req.body.followed} AND follower = ${req.body.follower}`
    DB.query(sql,(err,result) => {
        if(err)res.status(500).json({'error':err.message})
        else if((result as any[]).length !== 0) res.status(200).json({'alreadyFollow':true})
        else res.status(200).json({'alreadyFollow':false})
    })
}